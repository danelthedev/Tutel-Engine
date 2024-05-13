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
}
