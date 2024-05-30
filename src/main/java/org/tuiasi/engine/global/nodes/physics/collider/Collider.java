package org.tuiasi.engine.global.nodes.physics.collider;

import java.util.List;

public interface Collider {
    boolean isColliding(Collider other);

    List<Collider> getColliding();

    boolean isOnFloor();
    boolean isOnWall();
    boolean isOnCeiling();
}
