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

import java.util.ArrayList;
import java.util.List;


public class GlBlueBlindFilter extends GlColorMatrixFilter {
    /*
    * For Tritanopia and Tritanomaly:
    * 1.Convert to LMS
    * 2.Calculate image as seen by tritanopia and tritanomaly
    * 3.Calculate error between normal LMS image and tritanopia & tritanomaly LMS image
    * 4.Daltonize:spread error to L and M channel
    * 5.Convert back to RGB
    * 6.Save the result
    * */

    public GlBlueBlindFilter() {
        this(0);
    }

    public GlBlueBlindFilter(final int intensity) {
        super();
        setColorMatrixList(createMatrixList());
        setIntensity(intensity);
    }

    public static List<float[]> createMatrixList() {
        List<float[]> matrixs = new ArrayList<float[]>();
        //0
        matrixs.add(new float[]{
                1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        //1
        matrixs.add(new float[]{
                1.0f, -0.0806f, 0.0806f, 0.0f,
                0.0f, 0.9379f, 0.0621f, 0.0f,
                0.0f, 0.0105f, 0.9895f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        //2
        matrixs.add(new float[]{
                1.0f, -0.1611f, 0.1611f, 0.0f,
                0.0f, 0.8758f, 0.1242f, 0.0f,
                0.0f, 0.0210f, 0.9790f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        //3
        matrixs.add(new float[]{
                1.0f, -0.2417f, 0.2417f, 0.0f,
                0.0f, 0.8137f, 0.1863f, 0.0f,
                0.0f, 0.0314f, 0.9686f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        //4
        matrixs.add(new float[]{
                1.0f, -0.3223f, 0.3223f, 0.0f,
                0.0f, 0.7515f, 0.2485f, 0.0f,
                0.0f, 0.0419f, 0.9581f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        //5
        matrixs.add(new float[]{
                1.0f, -0.4029f, 0.4029f, 0.0f,
                0.0f, 0.6894f, 0.3106f, 0.0f,
                0.0f, 0.0524f, 0.9476f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        //6
        matrixs.add(new float[]{
                1.0f, -0.4834f, 0.4834f, 0.0f,
                0.0f, 0.6273f, 0.3727f, 0.0f,
                0.0f, 0.0629f, 0.9371f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        //7
        matrixs.add(new float[]{
                1.0f, -0.5640f, 0.5640f, 0.0f,
                0.0f, 0.5652f, 0.4348f, 0.0f,
                0.0f, 0.0734f, 0.9266f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        //8
        matrixs.add(new float[]{
                1.0f, -0.6446f, 0.6446f, 0.0f,
                0.0f, 0.5031f, 0.4969f, 0.0f,
                0.0f, 0.0839f, 0.9161f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        //9
        matrixs.add(new float[]{
                1.0f, -0.7251f, 0.7251f, 0.0f,
                0.0f, 0.4410f, 0.5590f, 0.0f,
                0.0f, 0.0943f, 0.9057f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        //10
        matrixs.add(new float[]{
                1.0f, -0.805712f, 0.805712f, 0.0f,
                0.0f, 0.378838f, 0.621162f, 0.0f,
                0.0f, 0.104823f, 0.895177f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        return matrixs;
    }
}
