package org.tuiasi.engine.renderer.light;

import org.joml.Vector3f;
import org.tuiasi.engine.global.nodes.spatial.Spatial3D;

public class PointLight extends LightSource{

    public PointLight(Spatial3D transform, LightData lightData, Vector3f color) {
        super(transform, lightData, color);
    }
}
