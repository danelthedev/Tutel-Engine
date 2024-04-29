package org.tuiasi.engine.renderer.light;

import org.tuiasi.engine.global.nodes.spatial.Spatial3D;

public class DirectionalLight extends LightSource{

    public DirectionalLight(Spatial3D transform, LightData lightData) {
        super(transform, lightData);
    }
}
