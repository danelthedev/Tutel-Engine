package org.tuiasi.engine.renderer.modelLoader;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.tuiasi.engine.renderer.mesh.Mesh;

@Data @NoArgsConstructor
public class Model {
    private Mesh mesh;

    String path;
    String meshName;
    String textureName;

    public Model(String path, float[] vertices, int[] indices, String textureName) {
        mesh = new Mesh(path, vertices, indices);
        this.path = path.substring(0, path.lastIndexOf("\\"));
        this.meshName = path.substring(path.lastIndexOf("\\") + 1);
        this.textureName = textureName;
    }
}
