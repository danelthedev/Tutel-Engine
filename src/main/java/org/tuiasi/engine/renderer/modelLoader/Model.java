package org.tuiasi.engine.renderer.modelLoader;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class Model {
    private float[] vertices;
    private int[] indices;
    String textureName;

    public Model(float[] vertices, int[] indices, String textureName) {
        this.vertices = vertices;
        this.indices = indices;
        this.textureName = textureName;
    }
}
