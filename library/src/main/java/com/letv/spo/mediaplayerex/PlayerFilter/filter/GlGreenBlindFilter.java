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


public class GlGreenBlindFilter extends GlColorMatrixFilter {
    /*
    * For Deuteranopia:
    * 1.Convert to LMS
    * 2.Calculate image as seen by deuteranopia
    * 3.Calculate new LMS value to YUV
    * 4.Calculate error between normal YUV image and deuteranopia YUV image
    * 5.Daltonize:spread error to Y and U channel
    * 6.Convert back to RGB
    * 7.Save the result
    *
    * For Deuteranomaly:
    * 1.Convert to LMS
    * 2.Calculate image as seen by deuteranomaly
    * 3.Calculate new LMS value back to RGB
    * 4.Calculate error between normal image and deuteranomaly image
    * 5.Daltonize:spread error to R and B channel
    * 6.Save the result
    * */

    public GlGreenBlindFilter() {
        this(0);
    }

    public GlGreenBlindFilter(final int intensity) {
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
                1.0989f, -0.1350f, 0.0362f, 0.0f,
                0.0000f, 1.0000f, 0.0000f, 0.0f,
                -0.0312f, 0.0354f, 0.9958f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        //2
        matrixs.add(new float[]{
                1.1759f, -0.2416f, 0.0657f, 0.0f,
                0.0000f, 1.0000f, 0.0000f, 0.0f,
                -0.0574f, 0.0642f, 0.9932f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        //3
        matrixs.add(new float[]{
                1.2369f, -0.3273f, 0.0904f, 0.0f,
                0.0000f, 1.0000f, 0.0000f, 0.0f,
                -0.0798f, 0.0880f, 0.9918f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        //4
        matrixs.add(new float[]{
                1.2858f, -0.3972f, 0.1114f, 0.0f,
                0.0000f, 1.0000f, 0.0000f, 0.0f,
                -0.0993f, 0.1082f, 0.9912f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        //5
        matrixs.add(new float[]{
                1.3253f, -0.4550f, 0.1297f, 0.0f,
                0.0000f, 1.0000f, 0.0000f, 0.0f,
                -0.1168f, 0.1255f, 0.9913f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        //6
        matrixs.add(new float[]{
                1.4900f, -0.6574f, 0.1674f, 0.0f,
                0.2526f, 0.6514f, 0.0960f, 0.0f,
                -0.3541f, 0.4465f, 0.9076f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        //7
        matrixs.add(new float[]{
                1.5314f, -0.7144f, 0.1831f, 0.0f,
                0.2729f, 0.6217f, 0.1054f, 0.0f,
                -0.3851f, 0.4843f, 0.9008f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        //8
        matrixs.add(new float[]{
                1.5669f, -0.7639f, 0.1970f, 0.0f,
                0.2901f, 0.5961f, 0.1139f, 0.0f,
                -0.4119f, 0.5168f, 0.8952f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        //9
        matrixs.add(new float[]{
                1.5669f, -0.7639f, 0.1970f, 0.0f,
                0.2901f, 0.5961f, 0.1139f, 0.0f,
                -0.4119f, 0.5168f, 0.8952f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        //10
        matrixs.add(new float[]{
                1.4027f, -0.8266f, 0.4240f, 0.0f,
                0.1740f, 0.6429f, 0.1832f, 0.0f,
                -0.2818f, 0.5784f, 0.7032f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        return matrixs;
    }
}
