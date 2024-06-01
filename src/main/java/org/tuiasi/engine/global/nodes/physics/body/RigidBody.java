package org.tuiasi.engine.global.nodes.physics.body;

import lombok.Data;
import org.joml.Vector3f;
import org.tuiasi.engine.global.nodes.physics.collider.Collider3D;
import org.tuiasi.engine.global.nodes.spatial.Spatial3D;
import org.tuiasi.engine.logic.PhysicsProperties;

@Data
public class RigidBody extends Spatial3D implements IBody{

    private Collider3D collider;

    @Override
    public void physRun() {
        translate(new Vector3f(0, PhysicsProperties.getGravity(), 0));
    }
}
