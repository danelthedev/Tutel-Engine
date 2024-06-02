package org.tuiasi.engine.global.nodes.physics.body;

import lombok.Data;
import org.tuiasi.engine.global.nodes.physics.collider.Collider3D;
import org.tuiasi.engine.global.nodes.spatial.Spatial3D;

@Data
public class StaticBody extends Spatial3D implements IBody{

    private Collider3D collider;

    public StaticBody(){
        super();
    }

    public StaticBody(Collider3D collider){
        super();
        this.collider = collider;
    }

    @Override
    public void physRun() {

    }

    @Override
    public Object saveState(){
        return new StaticBody(collider);
    }

    @Override
    public void loadState(Object state){
        StaticBody newState = (StaticBody) state;
        setCollider(newState.getCollider());
    }

}
