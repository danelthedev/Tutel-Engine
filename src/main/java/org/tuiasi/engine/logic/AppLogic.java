package org.tuiasi.engine.logic;

import lombok.Getter;
import lombok.Setter;
import org.tuiasi.engine.global.nodes.Node;

public class AppLogic {

    @Getter @Setter
    static Node root;

    public static void init(){
        root = new Node("Root");
    }

}
