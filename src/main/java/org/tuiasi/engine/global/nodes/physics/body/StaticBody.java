package org.tuiasi.engine.global.nodes.physics.body;

import lombok.Data;
import org.tuiasi.engine.global.nodes.physics.collider.Collider3D;
import org.tuiasi.engine.global.nodes.spatial.Spatial3D;

@Data
public class StaticBody extends Spatial3D implements IBody{

    private Collider3D collider;

    @Override
    public void physRun() {

    }
}
