package org.tuiasi.engine.global.nodes.physics.body;

import org.tuiasi.engine.global.nodes.physics.collider.Collider3D;

public interface IBody {

    public void setCollider(Collider3D collider);
    public Collider3D getCollider();
    public void physRun();
}
