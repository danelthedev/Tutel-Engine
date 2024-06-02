package org.tuiasi.engine.global.nodes.physics.body;

import lombok.Data;
import org.joml.Vector3f;
import org.tuiasi.engine.global.nodes.physics.collider.Collider3D;
import org.tuiasi.engine.global.nodes.spatial.Spatial3D;
import org.tuiasi.engine.logic.PhysicsProperties;

@Data
public class RigidBody extends Spatial3D implements IBody{

    private Collider3D collider;

    public RigidBody(){
        super();
    }

    public RigidBody(Vector3f position, Vector3f rotation, Vector3f scale) {
        super(position, rotation, scale);
    }

    public RigidBody(Vector3f position, Vector3f rotation, Vector3f scale, Collider3D collider) {
        super(position, rotation, scale);
        this.collider = collider;
    }

    @Override
    public void physRun() {
        translate(new Vector3f(0, PhysicsProperties.getGravity(), 0));
    }

    @Override
    public Object saveState(){
        return new RigidBody(getPosition(), getRotation(), getScale(), collider);
    }

    @Override
    public void loadState(Object state){
        RigidBody newState = (RigidBody) state;
        setPosition(newState.getPosition());
        setRotation(newState.getRotation());
        setScale(newState.getScale());
        setCollider(newState.getCollider());
    }
}
