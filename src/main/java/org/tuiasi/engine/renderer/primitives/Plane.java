package org.tuiasi.engine.renderer.primitives;

public class Plane implements Primitive {

    public static float[] vertexData = new float[]{
            -25f, 0f, -25f,          0.0f, 1.0f, 0.0f,      0.0f, 1.0f,
            25f, 0f, -25f,           0.0f, 1.0f, 0.0f,      1.0f, 1.0f,
            25f, 0f, 25f,            0.0f, 1.0f, 0.0f,      1.0f, 0.0f,
            25f, 0f, 25f,            0.0f, 1.0f, 0.0f,      1.0f, 0.0f,
            -25f, 0f, 25f,           0.0f, 1.0f, 0.0f,      0.0f, 0.0f,
            -25f, 0f, -25f,          0.0f, 1.0f, 0.0f,      0.0f, 1.0f,
    };

    public static int[] indexData = new int[]{
            0, 1, 2,
            3, 4, 5
    };

}
