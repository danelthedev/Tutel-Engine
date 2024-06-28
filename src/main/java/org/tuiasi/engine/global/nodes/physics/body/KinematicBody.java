package org.tuiasi.engine.global.nodes.physics.body;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;
import org.tuiasi.engine.global.nodes.EditorVisible;
import org.tuiasi.engine.global.nodes.physics.collider.Collider3D;
import org.tuiasi.engine.global.nodes.spatial.Spatial3D;
import org.tuiasi.engine.logic.AppLogic;
import org.tuiasi.engine.logic.EngineState;
import org.tuiasi.engine.renderer.light.DirectionalLight;
import org.tuiasi.engine.renderer.light.LightSource;

@Data @Getter @Setter
public class KinematicBody extends Spatial3D implements IBody{

    private Collider3D collider;

    @EditorVisible
    Vector3f velocity = new Vector3f(0,0,0);
    @EditorVisible
    Vector3f acceleration = new Vector3f(0,0,0);
    @EditorVisible
    Vector3f friction = new Vector3f(0,0,0);

    public KinematicBody(Vector3f position, Vector3f rotation, Vector3f scale) {
        super(position, rotation, scale);
    }

    public KinematicBody(Vector3f position, Vector3f rotation, Vector3f scale, Collider3D collider) {
        super(position, rotation, scale);
        this.collider = collider;
    }

    public KinematicBody(Vector3f position, Vector3f rotation, Vector3f scale, Collider3D collider, Vector3f velocity, Vector3f acceleration, Vector3f friction) {
        super(position, rotation, scale);
        this.collider = collider;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.friction = friction;
    }

    public KinematicBody(){
        super();
    }

    @Override
    public void physRun() {
        velocity.x -= Math.signum(velocity.x) * friction.x;
        velocity.y -= Math.signum(velocity.y) * friction.y;
        velocity.z -= Math.signum(velocity.z) * friction.z;

        velocity.add(acceleration);
        Vector3f newPosition = new Vector3f(getPosition()).add(velocity);
        if(collider != null){
            Collider3D possibleCollider = collider.isColliding(newPosition);
            if(possibleCollider != null){
                velocity.x -= Math.signum(velocity.x) * 0.01f;
                velocity.y -= Math.signum(velocity.y) * 0.01f;
                velocity.z -= Math.signum(velocity.z) * 0.01f;
            }
            else{
                setPosition(newPosition);
            }
        }
    }

    @Override
    public Object saveState(){
        return new KinematicBody(new Vector3f(getPosition()), new Vector3f(getRotation()), new Vector3f(getScale()), collider, new Vector3f(velocity), new Vector3f(acceleration), new Vector3f(friction));
    }

    @Override
    public void loadState(Object state){
        super.loadState(state);
        if(state == null)
            return;

        KinematicBody kinematicBody = (KinematicBody) state;

        this.collider = kinematicBody.collider;
        this.velocity = kinematicBody.velocity;
        this.acceleration = kinematicBody.acceleration;
        this.friction = kinematicBody.friction;
    }


}
