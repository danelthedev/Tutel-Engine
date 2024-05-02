package org.tuiasi.engine.renderer.primitives;

public class Plane {

    public static float[] vertexData = new float[]{
            -50f, 0f, -50f,   1.0f, 1.0f, 1.0f,       0.0f, -1.0f, 0.0f,      0.0f, 1.0f,
            50f, 0f, -50f,    1.0f, 1.0f, 1.0f,       0.0f, -1.0f, 0.0f,      1.0f, 1.0f,
            50f, 0f, 50f,     1.0f, 1.0f, 1.0f,       0.0f, -1.0f, 0.0f,      1.0f, 0.0f,
            50f, 0f, 50f,     1.0f, 1.0f, 1.0f,       0.0f, -1.0f, 0.0f,      1.0f, 0.0f,
            -50f, 0f, 50f,    1.0f, 1.0f, 1.0f,       0.0f, -1.0f, 0.0f,      0.0f, 0.0f,
            -50f, 0f, -50f,   1.0f, 1.0f, 1.0f,       0.0f, -1.0f, 0.0f,      0.0f, 1.0f,
    };

    public static int[] indexData = new int[]{
            0, 1, 2,
            3, 4, 5
    };
}
