package org.tuiasi.engine.renderer.light;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.joml.Vector3f;

@Data @AllArgsConstructor
public class LightData {
    private Vector3f ambient;
    private Vector3f diffuse;
    private Vector3f specular;
}
