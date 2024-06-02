package org.tuiasi.engine.global.nodes.physics.body;

import org.tuiasi.engine.global.nodes.physics.collider.Collider3D;

public interface IBody {

    void setCollider(Collider3D collider);
    Collider3D getCollider();
    void physRun();
}
