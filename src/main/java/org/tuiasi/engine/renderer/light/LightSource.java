package org.tuiasi.engine.renderer.light;

import lombok.Data;
import org.tuiasi.engine.global.nodes.spatial.Spatial3D;

@Data
public class LightSource {

    private Spatial3D transform;
    private LightData lightData;


    public LightSource(Spatial3D transform, LightData lightData){
        this.transform = transform;
        this.lightData = lightData;
    }

}
