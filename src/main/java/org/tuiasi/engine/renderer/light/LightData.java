package org.tuiasi.engine.renderer.light;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.joml.Vector3f;
import org.tuiasi.engine.global.nodes.EditorVisible;

@Data @AllArgsConstructor
public class LightData {
    @EditorVisible
    private Vector3f ambient;
    @EditorVisible
    private Vector3f diffuse;
    @EditorVisible
    private Vector3f specular;

    public LightData(){
        ambient = new Vector3f(0.2f, 0.2f, 0.2f);
        diffuse = new Vector3f(0.5f, 0.5f, 0.5f);
        specular = new Vector3f(1.0f, 1.0f, 1.0f);
    }

}
