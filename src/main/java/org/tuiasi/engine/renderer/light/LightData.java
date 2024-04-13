package org.tuiasi.engine.renderer.light;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.joml.Vector3f;

@Data @AllArgsConstructor
public class LightData {
    private float ambient;
    private float diffuse;
    private float specular;
}
