package org.tuiasi.engine.renderer.light;

import org.tuiasi.engine.global.nodes.spatial.Spatial3D;

// Uses rotation from the transform to determine the direction of the light
public class DirectionalLight extends LightSource{

    public DirectionalLight() {
        super();
    }

    public DirectionalLight(Spatial3D transform, LightData lightData) {
        super(transform, lightData);
    }

}
