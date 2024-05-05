package org.tuiasi.engine.renderer.modelLoader;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class Model {
    private float[] vertices;
    private int[] indices;

    public Model(float[] vertices, int[] indices) {
        this.vertices = vertices;
        this.indices = indices;
    }
}
