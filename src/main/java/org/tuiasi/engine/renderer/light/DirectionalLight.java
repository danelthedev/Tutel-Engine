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

    public DirectionalLight(LightSource lightSource) {
        super(lightSource, lightSource.getLightData());
    }

    @Override
    public Object saveState(){
        return new DirectionalLight((LightSource) super.saveState());
    }

    @Override
    public void loadState(Object state){
        super.loadState(state);
    }

}
