package org.tuiasi.engine.renderer.material;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joml.Vector3f;

@Data @AllArgsConstructor @NoArgsConstructor
public class Material implements IMaterial{
    private Vector3f ambient;
    private Vector3f diffuse;
    private Vector3f specular;
    private float shininess;
}
