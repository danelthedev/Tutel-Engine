package org.tuiasi.engine.renderer.material;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joml.Vector3f;
import org.tuiasi.engine.renderer.texture.Texture;

@Data @AllArgsConstructor @NoArgsConstructor
public class Material implements IMaterial{
    private Texture diffuse;
    private Texture specular;
    private float shininess;
}
