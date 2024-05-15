package org.tuiasi.engine.renderer.material;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tuiasi.engine.global.nodes.EditorVisible;
import org.tuiasi.engine.renderer.texture.Texture;

@Data @NoArgsConstructor @AllArgsConstructor
public class Material implements IMaterial{
    @EditorVisible
    private Texture diffuse;
    @EditorVisible
    private Texture specular;
    @EditorVisible
    private float shininess;

}
