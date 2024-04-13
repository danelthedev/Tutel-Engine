package org.tuiasi.engine.renderer.light;

import lombok.Data;
import org.joml.Vector3f;
import org.tuiasi.engine.global.nodes.spatial.Spatial3D;

@Data
public class LightSource {

    private Spatial3D transform;
    private LightData lightData;

    private Vector3f color;

    public LightSource(Spatial3D transform, LightData lightData, Vector3f color){
        this.transform = transform;
        this.lightData = lightData;
        this.color = color;
    }

}
