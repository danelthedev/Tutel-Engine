package org.tuiasi.engine.renderer.modelLoader;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.tuiasi.engine.renderer.mesh.Mesh;

@Data @NoArgsConstructor
public class Model {
    private String path;
    private Mesh mesh;
    private float[] vertices;
    private int[] indices;
    String textureName;

    public Model(float[] vertices, int[] indices, String textureName) {
        mesh = new Mesh(path, vertices, indices);

        this.vertices = vertices;
        this.indices = indices;
        this.textureName = textureName;
    }
}
