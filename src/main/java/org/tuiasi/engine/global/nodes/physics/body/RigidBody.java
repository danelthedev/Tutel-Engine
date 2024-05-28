package org.tuiasi.engine.global.nodes.physics.body;

import org.joml.Vector3f;
import org.tuiasi.engine.global.nodes.spatial.Spatial3D;
import org.tuiasi.engine.logic.PhysicsProperties;

public class RigidBody extends Spatial3D implements IBody{
    @Override
    public void physRun() {
        translate(new Vector3f(0, PhysicsProperties.getGravity(), 0));
    }
}
