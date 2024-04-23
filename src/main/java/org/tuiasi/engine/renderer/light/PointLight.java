package org.tuiasi.engine.renderer.light;

import org.joml.Vector3f;
import org.tuiasi.engine.global.nodes.spatial.Spatial3D;

public class PointLight extends LightSource{

    public PointLight(Spatial3D transform, LightData lightData) {
        super(transform, lightData);
    }
}
