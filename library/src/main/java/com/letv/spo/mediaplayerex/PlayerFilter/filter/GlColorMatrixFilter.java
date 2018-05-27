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
package com.letv.spo.mediaplayerex.PlayerFilter.filter;

import android.opengl.GLES20;
import android.util.Log;

import java.util.List;


public class GlColorMatrixFilter extends GlFilter {

    public static final String COLOR_MATRIX_FRAGMENT_SHADER = "" +
            "varying highp vec2 vTextureCoord;\n" +
            "\n" +
            "uniform sampler2D sTexture;\n" +
            "\n" +
            "uniform lowp mat4 colorMatrix;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    lowp vec4 textureColor = texture2D(sTexture, vTextureCoord);\n" +
            "    lowp vec4 outputColor = textureColor * colorMatrix;\n" +
            "    \n" +
            "    gl_FragColor = outputColor;\n" +
            "}";
    private static final String TAG = GlColorMatrixFilter.class.getSimpleName();
    private List<float[]> mColorMatrixList;

    public GlColorMatrixFilter() {
        super(DEFAULT_VERTEX_SHADER, COLOR_MATRIX_FRAGMENT_SHADER);
    }

    public List<float[]> getColorMatrixList() {
        return mColorMatrixList;
    }

    public void setColorMatrixList(List<float[]> matrixList) {
        mColorMatrixList = matrixList;
    }

    public void setIntensity(final int intensity) {
        if (mColorMatrixList != null) {
            if (intensity >= 0 && intensity <= 10) {
                runOnDraw(new Runnable() {
                    @Override
                    public void run() {
                        GLES20.glUniformMatrix4fv(getHandle("colorMatrix"), 1, false, mColorMatrixList.get(intensity), 0);
                    }
                });
            } else {
                Log.e(TAG, "Unsupported intensity!");
            }
        } else {
            Log.e(TAG, "ColorMatrixList is null!");
        }
    }

}
