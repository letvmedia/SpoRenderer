/*
 * Copyright (C) 2018 Letv
 * Copyright (C) 2018 Zhang Hui <zhanghuicuc@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.letv.spo.mediaplayerex.PlayerFilter;

import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import android.view.Surface;

import com.letv.spo.mediaplayerex.PlayerFilter.filter.GlFilter;
import com.letv.spo.mediaplayerex.PlayerFilter.filter.GlPreviewFilter;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_FRAMEBUFFER;
import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_MAX_TEXTURE_SIZE;
import static android.opengl.GLES20.GL_NEAREST;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glViewport;

public class SpoRenderer implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {

    private static final String TAG = SpoRenderer.class.getSimpleName();
    private final GLSurfaceView glPreview;
    private SurfaceTexture previewTexture;
    private boolean updateSurface = false;
    private int texName;
    //see https://blog.piasy.com/2016/06/07/Open-gl-es-android-2-part-1/
    //mvpmatrix = mmatrix * vmatrix * projmatrix
    private float[] MVPMatrix = new float[16];
    private float[] ProjMatrix = new float[16];
    //model matrix:
    //Local space：我们为每个物体建好模型的时候，它们的坐标就是 Local space 坐标；
    //World space：当我们要绘制多个物体时，如果直接使用 Local space 的坐标（把所有物体的原点放在一起），
    //那它们很可能会发生重叠，因此我们需要把它们进行合理的移动、排布，最终各自的坐标就是 World space 的坐标了；
    //Model matrix：把 Local space 坐标转换到 World space 坐标所使用的变换矩阵，它是针对每个物体做不同的变换；
    private float[] MMatrix = new float[16];
    //View space：通常也叫 Camera space 或者 Eye space，是从观察者（也就是我们自己）所在的位置出发，所看到的空间；
    //View matrix：把 World space 坐标转换到 View space 坐标所使用的变换矩阵，它相当于是在移动相机位置，实际上是反方向移动整个场景（所有物体）；
    private float[] VMatrix = new float[16];
    //transform matrix
    private float[] STMatrix = new float[16];
    private FramebufferObject filterFramebufferObject;
    private GlPreviewFilter previewFilter;
    private GlFilter glFilter;
    private boolean isNewFilter;
    private float aspectRatio = 1f;

    private MediaPlayer simplePlayer = null;
    private Surface mSurface = null;

    /**
     * 初始化SpoRenderer.
     *
     * @param glPreview GLSurfaceView实例
     */
    public SpoRenderer(GLSurfaceView glPreview) {
        super();
        Matrix.setIdentityM(STMatrix, 0);
        this.glPreview = glPreview;
    }

    /**
     * 设置Filter
     * 在启动渲染线程之后调用
     *
     * @param filter GlFilter实例
     */
    public void setGlFilter(final GlFilter filter) {
        glPreview.queueEvent(new Runnable() {
            @Override
            public void run() {
                if (glFilter != null) {
                    glFilter.release();
                    glFilter = null;
                }
                glFilter = filter;
                isNewFilter = true;
                glPreview.requestRender();
            }
        });
    }

    /**
     * 设置Filter的强度
     * 在设置Filter之后调用。
     * <p>
     * 目前只可以对色盲优化滤镜设置强度，强度范围0-10，数字越大强度越高
     * 0代表无优化，1-9代表针对不同程度色弱的优化滤镜， 10代表针对色盲的优化滤镜
     *
     * @param intensity Filter强度
     */
    public void setGlFilterIntensity(int intensity) {
        if (glFilter != null) {
            glFilter.setIntensity(intensity);
            glPreview.requestRender();
        }
    }

    /**
     * 释放资源
     * 在surface destroyed之后调用
     */
    public void release() {
        if (simplePlayer != null) {
            Log.i(TAG, "release player");
            simplePlayer.release();
            simplePlayer = null;
        }
        glPreview.queueEvent(new Runnable() {
            @Override
            public void run() {
                if (glFilter != null) {
                    glFilter.release();
                    glFilter = null;
                }
                if (previewFilter != null) {
                    previewFilter.release();
                    previewFilter = null;
                }
                if (filterFramebufferObject != null) {
                    filterFramebufferObject.release();
                    filterFramebufferObject = null;
                }
            }
        });
    }

    @Override
    public void onSurfaceCreated(final GL10 gl, final EGLConfig config) {
        Log.i(TAG, "onSurfaceCreated");

        // enable face culling feature
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        // specify which faces to not draw
        GLES20.glCullFace(GLES20.GL_FRONT);

        //glClearColor函数是设置清屏的颜色，参数分别对应RGBA, 这里0 0 0 1就是透明的意思
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        final int[] args = new int[1];

        GLES20.glGenTextures(args.length, args, 0);
        texName = args[0];
        previewTexture = new SurfaceTexture(texName);
        previewTexture.setOnFrameAvailableListener(this);

        GLES20.glBindTexture(GlPreviewFilter.GL_TEXTURE_EXTERNAL_OES, texName);
        // GL_TEXTURE_EXTERNAL_OES
        EglUtil.setupSampler(GlPreviewFilter.GL_TEXTURE_EXTERNAL_OES, GL_LINEAR, GL_NEAREST);
        GLES20.glBindTexture(GL_TEXTURE_2D, 0);

        filterFramebufferObject = new FramebufferObject();
        // GL_TEXTURE_EXTERNAL_OES
        previewFilter = new GlPreviewFilter(GlPreviewFilter.GL_TEXTURE_EXTERNAL_OES);
        previewFilter.setup();

        Matrix.setLookAtM(VMatrix, 0,
                0.0f, 0.0f, 5.0f,
                0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f
        );

        synchronized (this) {
            updateSurface = false;
        }

        if (glFilter != null) {
            isNewFilter = true;
        }

        GLES20.glGetIntegerv(GL_MAX_TEXTURE_SIZE, args, 0);

    }

    @Override
    public void onSurfaceChanged(final GL10 gl, final int width, final int height) {
        Log.i(TAG, "onSurfaceChanged width = " + width + "  height = " + height);
        filterFramebufferObject.setup(width, height);
        previewFilter.setFrameSize(width, height);
        if (glFilter != null) {
            glFilter.setFrameSize(width, height);
        }

        aspectRatio = (float) width / height;
        Matrix.frustumM(ProjMatrix, 0, -aspectRatio, aspectRatio, -1, 1, 5, 7);
        Matrix.setIdentityM(MMatrix, 0);
    }

    @Override
    public void onDrawFrame(final GL10 gl) {

        synchronized (this) {
            if (updateSurface) {
                previewTexture.updateTexImage();
                previewTexture.getTransformMatrix(STMatrix);
                updateSurface = false;
            }
        }

        if (isNewFilter) {
            if (glFilter != null) {
                glFilter.setup();
                glFilter.setFrameSize(filterFramebufferObject.getWidth(), filterFramebufferObject.getHeight());
            }
            isNewFilter = false;
        }

        if (glFilter != null) {
            filterFramebufferObject.enable();
            //设置视角窗口大小glViewport，其实就是决定绘制的矩形区域的大小
            glViewport(0, 0, filterFramebufferObject.getWidth(), filterFramebufferObject.getHeight());
        }

        //使用glClearColor函数所设置的颜色进行清屏
        GLES20.glClear(GL_COLOR_BUFFER_BIT);

        Matrix.multiplyMM(MVPMatrix, 0, VMatrix, 0, MMatrix, 0);
        Matrix.multiplyMM(MVPMatrix, 0, ProjMatrix, 0, MVPMatrix, 0);

        previewFilter.draw(texName, MVPMatrix, STMatrix, aspectRatio);

        if (glFilter != null) {
            GLES20.glBindFramebuffer(GL_FRAMEBUFFER, 0);
            GLES20.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glFilter.draw(filterFramebufferObject.getTexName());
        }
    }

    @Override
    public synchronized void onFrameAvailable(final SurfaceTexture previewTexture) {
        //每来一帧都会调用
        updateSurface = true;
        glPreview.requestRender();
    }

    /**
     * 设置播放器
     * 在surface created之后调用
     *
     * @param player 播放器实例
     */
    public void setPlayer(MediaPlayer player) {
        Log.i(TAG, "setPlayer");
        if (this.simplePlayer != null) {
            Log.i(TAG, "release previous player");
            this.simplePlayer.release();
            this.simplePlayer = null;
        }
        this.simplePlayer = player;

        if (mSurface == null) {
            mSurface = new Surface(previewTexture);
        }
        //zhanghui9:这里是关键
        this.simplePlayer.setSurface(mSurface);
    }
}
