package org.tuiasi.engine.renderer.light;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.tuiasi.engine.global.nodes.EditorVisible;
import org.tuiasi.engine.global.nodes.INodeValue;
import org.tuiasi.engine.global.nodes.spatial.Spatial3D;

@Getter @Setter
public class PointLight extends LightSource {

    @EditorVisible
    @JsonProperty
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

    public PointLight(LightSource lightSource, float constant, float linear, float quadratic) {
        super(lightSource, lightSource.getLightData());

        this.constant = constant;
        this.linear = linear;
        this.quadratic = quadratic;
    }

    @Override
    public Object saveState(){
        return new PointLight((LightSource) super.saveState(), constant, linear, quadratic);
    }

    @Override
    public void loadState(Object state){
        super.loadState(state);
        PointLight pointLight = (PointLight) state;
        this.constant = pointLight.constant;
        this.linear = pointLight.linear;
        this.quadratic = pointLight.quadratic;
    }

}
