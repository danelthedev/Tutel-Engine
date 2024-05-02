package org.tuiasi.engine.renderer.light;

import lombok.Data;
import org.tuiasi.engine.global.nodes.spatial.Spatial3D;

// Uses rotation from the transform to determine the direction of the light
public class DirectionalLight extends LightSource{

    public DirectionalLight(Spatial3D transform, LightData lightData) {
        super(transform, lightData);
        setType(1);
    }
}
