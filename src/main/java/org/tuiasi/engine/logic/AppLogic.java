package org.tuiasi.engine.logic;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.tuiasi.engine.logic.IO.MouseHandler;
import org.tuiasi.engine.global.nodes.Node;
import org.tuiasi.engine.global.nodes.spatial.Spatial3D;
import org.tuiasi.engine.ui.DefaultEngineEditorUI;
import org.tuiasi.engine.ui.uiWindows.prefabs.UINodeInspectorWindow;

import java.util.ArrayList;
import java.util.List;

public class AppLogic {

    @Getter @Setter
    static EngineState engineState = EngineState.EDITOR;

    @Getter @Setter
    static Node<Spatial3D> root;

    @Getter @Setter
    static Node<?> selectedNode;

    static List<Node<?>> nodesWithScripts;

    public static void init(){
        root = new Node<>(null, "Root", new Spatial3D(
                new Vector3f(0,0,0),
                new Vector3f(0,0,0),
                new Vector3f(1,1,1)
        ));
        selectedNode = root;

        nodesWithScripts = new ArrayList<>();
    }

    public static void initializeNodes(){
        // iterate through the tree recursively and initialize them if they have an attached script obj
        initializeNode(root);
    }

    public static void initializeNode(Node<?> node){
        if(node.getScriptObj() != null){
            nodesWithScripts.add(node);
            node.getScriptObj().init();
        }

        for(Node<?> child : node.getChildren()){
            initializeNode(child);
        }
    }

    public static void run(){
        if(engineState == EngineState.EDITOR) {
            if (MouseHandler.isButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
                Node<?> node = root.findNode(node1 -> {
                    if (node1.getValue() instanceof Spatial3D) {
                        Spatial3D spatial3D = (Spatial3D) node1.getValue();
                        return spatial3D.isMouseHovered();
                    }

                    return false;
                });
                if (node != null) {
                    selectedNode = node;
                    UINodeInspectorWindow nodeInspectorWindow = ((UINodeInspectorWindow) DefaultEngineEditorUI.getWindow("Node Inspector"));
                    if (nodeInspectorWindow != null) {
                        nodeInspectorWindow.refresh();
                    }
                }
            }
        }
        else{
            for(Node<?> node : nodesWithScripts){
                node.getScriptObj().run();
            }
        }
    }

    public static void cleanNodeQueue(){
        nodesWithScripts.clear();
    }

}
