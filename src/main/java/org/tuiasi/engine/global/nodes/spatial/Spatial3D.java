package org.tuiasi.engine.global.nodes.spatial;

import lombok.Data;
import org.joml.Vector3f;

@Data
public class Spatial3D extends Spatial{

    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;

    public Spatial3D(Vector3f position, Vector3f rotation, Vector3f scale){
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Spatial3D(){
        this.position = new Vector3f(0,0,0);
        this.rotation = new Vector3f(0,0,0);
        this.scale = new Vector3f(1,1,1);
    }

    public void translate(Vector3f translation){
        this.position.add(translation);
    }

    public void rotate(Vector3f rotation){
        this.rotation.add(rotation);
    }

    public void scale(Vector3f scale){
        this.scale.add(scale);
    }
}
