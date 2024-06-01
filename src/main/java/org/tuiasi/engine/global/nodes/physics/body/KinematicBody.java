package org.tuiasi.engine.global.nodes.physics.body;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;
import org.tuiasi.engine.global.nodes.EditorVisible;
import org.tuiasi.engine.global.nodes.physics.collider.Collider3D;
import org.tuiasi.engine.global.nodes.spatial.Spatial3D;

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

    @Override
    public void physRun() {
        // apply friction on each coordinate of the velocity, going towards 0
        velocity.x -= Math.signum(velocity.x) * friction.x;
        velocity.y -= Math.signum(velocity.y) * friction.y;
        velocity.z -= Math.signum(velocity.z) * friction.z;

        velocity.add(acceleration);
        Vector3f newPosition = new Vector3f(getPosition()).add(velocity);
        if(collider != null){
            if(collider.isColliding(newPosition)){
                velocity = new Vector3f(0,0,0);
            }
            else{
                setPosition(newPosition);
            }
        }
    }


}
