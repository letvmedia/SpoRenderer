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


public class GlRedBlindFilter extends GlColorMatrixFilter {
    /*
    * For Protanope:
    * 1.Convert to LMS
    * 2.Calculate image as seen by protanope
    * 3.Calculate new LMS value back to RGB
    * 4.Calculate error between normal image and protanope image
    * 5.Daltonize:spread error to G and B channel
    * 6.Save the result
    *
    * For Protanomaly:
    * 1.Convert to LMS
    * 2.Calculate image as seen by protanomaly
    * 3.Calculate new LMS value to YUV
    * 4.Calculate error between normal YUV image and protanomaly YUV image
    * 5.Daltonize:spread error to Y and U channel
    * 6.Convert back to RGB
    * 7.Save the result
    * */

    public GlRedBlindFilter() {
        this(0);
    }

    public GlRedBlindFilter(final int intensity) {
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
                1.0849f, -0.1048f, 0.0200f, 0.0f,
                0.0692f, 0.9158f, 0.0149f, 0.0f,
                0.1658f, -0.2117f, 1.0459f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        //2
        matrixs.add(new float[]{
                1.1571f, -0.1933f, 0.0361f, 0.0f,
                0.1288f, 0.8442f, 0.0270f, 0.0f,
                0.3035f, -0.3869f, 1.0834f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        //3
        matrixs.add(new float[]{
                1.2199f, -0.2693f, 0.0495f, 0.0f,
                0.1812f, 0.7819f, 0.0369f, 0.0f,
                0.4200f, -0.5344f, 1.1144f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        //4
        matrixs.add(new float[]{
                1.2752f, -0.3358f, 0.0606f, 0.0f,
                0.2279f, 0.7270f, 0.0452f, 0.0f,
                0.5201f, -0.6605f, 1.1404f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        //5
        matrixs.add(new float[]{
                1.3247f, -0.3947f, 0.0700f, 0.0f,
                0.2701f, 0.6777f, 0.0521f, 0.0f,
                0.6073f, -0.7698f, 1.1625f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        //6
        matrixs.add(new float[]{
                1.3696f, -0.4476f, 0.0780f, 0.0f,
                0.3088f, 0.6332f, 0.0580f, 0.0f,
                0.6839f, -0.8654f, 1.1814f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        //7
        matrixs.add(new float[]{
                1.4106f, -0.4955f, 0.0849f, 0.0f,
                0.3446f, 0.5923f, 0.0631f, 0.0f,
                0.7521f, -0.9499f, 1.1978f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        //8
        matrixs.add(new float[]{
                1.4484f, -0.5393f, 0.0909f, 0.0f,
                0.3779f, 0.5546f, 0.0675f, 0.0f,
                0.8131f, -1.0252f, 1.2120f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        //9
        matrixs.add(new float[]{
                1.4836f, -0.5798f, 0.0962f, 0.0f,
                0.4092f, 0.5194f, 0.0714f, 0.0f,
                0.8683f, -1.0928f, 1.2245f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        //10
        matrixs.add(new float[]{
                1.0f, 0.0f, 0.0f, 0.0f,
                0.5836f, 0.4146f, 0.0f, 0.0f,
                0.6384f, -0.6384f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        return matrixs;
    }
}
