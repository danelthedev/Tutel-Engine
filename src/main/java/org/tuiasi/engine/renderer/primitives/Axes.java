package org.tuiasi.engine.renderer.primitives;

public class Axes {
    public static float[] vertexData = new float[]{
            -100.0f, 0.0f, 0.0f, 0, 0, 0, 0, 0,
            100.0f, 0.0f, 0.0f, 0, 0, 0, 0, 0,

            0.0f, -100.0f, 0.0f, 0, 0, 0, 0, 0,
            0.0f, 100.0f, 0.0f, 0, 0, 0, 0, 0,

            0.0f, 0.0f, -100.0f, 0, 0, 0, 0, 0,
            0.0f, 0.0f, 100.0f, 0, 0, 0, 0, 0,
    };

    public static int[] indexData = new int[]{
            0, 1,
            2, 3,
            4, 5
    };
}
