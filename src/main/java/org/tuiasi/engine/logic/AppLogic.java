package org.tuiasi.engine.logic;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;
import org.tuiasi.engine.global.nodes.Node;
import org.tuiasi.engine.global.nodes.spatial.Spatial3D;

public class AppLogic {

    @Getter @Setter
    static Node<Spatial3D> root;

    @Getter @Setter
    static Node<?> selectedNode;

    public static void init(){
        root = new Node<>(null, "Root", new Spatial3D(
                new Vector3f(0,0,0),
                new Vector3f(0,0,0),
                new Vector3f(1,1,1)
        ));

        Node<Spatial3D> child1 = new Node<>(root, "Child1", new Spatial3D(
                new Vector3f(1,0,0),
                new Vector3f(0,0,0),
                new Vector3f(1,1,1)
        ));

        Node<String> child2 = new Node<>(root, "Child2", "Banana");
    }

}
