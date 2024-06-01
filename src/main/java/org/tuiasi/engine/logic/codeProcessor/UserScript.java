package org.tuiasi.engine.logic.codeProcessor;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;
import org.tuiasi.engine.global.nodes.Node;
import org.tuiasi.engine.global.nodes.spatial.Spatial3D;
import org.tuiasi.engine.logic.AppLogic;

@Getter
public class UserScript implements IScript{

    @Setter
    protected Node<?> attachedNode;
    @Setter
    protected Node<?> root;


    public UserScript(){
    }

    @Override
    public void init() {

    }

    @Override
    public void run() {

    }

    @Override
    public void clean() {

    }
}
