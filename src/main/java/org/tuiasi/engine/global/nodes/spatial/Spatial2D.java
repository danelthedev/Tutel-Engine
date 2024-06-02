package org.tuiasi.engine.global.nodes.spatial;

import lombok.Data;
import org.joml.Vector2f;

@Data
public class Spatial2D extends Spatial{

    private Vector2f position;
    private Vector2f rotation;
    private Vector2f scale;

    public Spatial2D(Vector2f position, Vector2f rotation, Vector2f scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Spatial2D(){
        this.position = new Vector2f(0,0);
        this.rotation = new Vector2f(0,0);
        this.scale = new Vector2f(1,1);
    }

    public void translate(Vector2f translation){
        this.position.add(translation);
    }

    public void rotate(Vector2f rotation){
        this.rotation.add(rotation);
    }

    public void scale(Vector2f scale){
        this.scale.add(scale);
    }

    @Override
    public Object saveState() {
        return new Spatial2D(position, rotation, scale);
    }

    @Override
    public void loadState(Object state) {
        Spatial2D newState = (Spatial2D) state;
        this.position = newState.getPosition();
        this.rotation = newState.getRotation();
        this.scale = newState.getScale();
    }

}