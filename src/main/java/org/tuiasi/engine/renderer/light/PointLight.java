package org.tuiasi.engine.renderer.light;

import lombok.Getter;
import lombok.Setter;
import org.tuiasi.engine.global.nodes.EditorVisible;
import org.tuiasi.engine.global.nodes.spatial.Spatial3D;

@Getter @Setter
public class PointLight extends LightSource{

    @EditorVisible
    private float constant, linear, quadratic;

    public PointLight(){
        super();
    }

    public PointLight(Spatial3D transform, LightData lightData, float constant, float linear, float quadratic) {
        super(transform, lightData);

        this.constant = constant;
        this.linear = linear;
        this.quadratic = quadratic;
    }

}
