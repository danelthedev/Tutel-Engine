package org.tuiasi.engine.renderer.light;

import org.tuiasi.engine.global.nodes.spatial.Spatial3D;

public class SpotLight extends LightSource{

    public SpotLight(Spatial3D transform, LightData lightData) {
        super(transform, lightData);
    }
}
