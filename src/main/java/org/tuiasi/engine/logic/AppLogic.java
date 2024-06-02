package org.tuiasi.engine.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.tuiasi.engine.global.WindowVariables;
import org.tuiasi.engine.global.nodes.physics.body.IBody;
import org.tuiasi.engine.global.nodes.physics.body.KinematicBody;
import org.tuiasi.engine.global.nodes.physics.collider.Collider3D;
import org.tuiasi.engine.logic.IO.MouseHandler;
import org.tuiasi.engine.global.nodes.Node;
import org.tuiasi.engine.global.nodes.spatial.Spatial3D;
import org.tuiasi.engine.logic.logger.Log;
import org.tuiasi.engine.renderer.camera.Camera;
import org.tuiasi.engine.renderer.camera.MainCamera;
import org.tuiasi.engine.ui.DefaultEngineEditorUI;
import org.tuiasi.engine.ui.uiWindows.prefabs.UINodeInspectorWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AppLogic {

    @Getter @Setter
    static EngineState engineState = EngineState.EDITOR;

    @Getter @Setter
    static Node<Spatial3D> root;

    @Getter @Setter
    static Node<?> selectedNode;

    @Getter
    static List<Node<?>> nodesWithScripts;

    @Getter
    static List<Node<?>> physicsNodes;

    @Getter
    static Camera editorCamera;

    @Getter
    static List<Camera> cameras;

    private static boolean addedTestNodes = false;

    @Getter @Setter
    private static String workingDirectory = ".";
    @Getter @Setter
    private static File projectFile;

    public static void init(){
        WindowVariables windowVariables = WindowVariables.getInstance();
        editorCamera = new Camera((float) Math.toRadians(45.0f), (float) windowVariables.getWidth() / windowVariables.getHeight(), 0.1f, 1000.0f);

        MainCamera.setInstance(editorCamera);

        root = new Node<>(null, "Root", new Spatial3D(
                new Vector3f(0,0,0),
                new Vector3f(0,0,0),
                new Vector3f(1,1,1)
        ));
        selectedNode = root;

        nodesWithScripts = new ArrayList<>();
        physicsNodes = new ArrayList<>();

        cameras = new ArrayList<>();

    }

    public static void addTestNodes(){
        // add a kinematic body with a collider child 2
        Node<KinematicBody> kinematicBody = new Node<>(root, "Obj", new KinematicBody(
                new Vector3f(0,0,0),
                new Vector3f(0,0,0),
                new Vector3f(1,1,1)
        ));
        Node<Collider3D> collider = new Node<>(kinematicBody, "Collider", new Collider3D(
                new Vector3f(0,0,0),
                new Vector3f(0,0,0),
                new Vector3f(1,1,1)));

        Node<KinematicBody> kinematicBody2 = new Node<>(root, "Wall", new KinematicBody(
                new Vector3f(0,0,0),
                new Vector3f(0,0,0),
                new Vector3f(1,1,1)
        ));
        Node<Collider3D> collider2 = new Node<>(kinematicBody2, "Collider 2", new Collider3D(
                new Vector3f(0,0,0),
                new Vector3f(0,0,0),
                new Vector3f(3,3,3)
        ));


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

        if(node.getValue() instanceof IBody){
            physicsNodes.add(node);

            // set the collider variable of the body if it has one
            for(Node<?> child : node.getChildren()){
                if(child.getValue() instanceof Collider3D){
                    ((IBody)node.getValue()).setCollider((Collider3D) child.getValue());
                }
            }

        }

        if(node.getValue() instanceof Camera){
            cameras.add((Camera) node.getValue());
            if(((Camera) node.getValue()).getIsMainCamera())
                MainCamera.setInstance((Camera) node.getValue());
        }

        for(Node<?> child : node.getChildren()){
            initializeNode(child);
        }

        node.saveState();
    }

    public static void run(){
        if(!addedTestNodes){
            addTestNodes();
            addedTestNodes = true;
        }

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
            for(Node<?> node: physicsNodes){
                ((IBody)node.getValue()).physRun();
            }

        }
    }

    public static void cleanNodeQueue(){
        nodesWithScripts.clear();
        physicsNodes.clear();
        cameras.clear();
        resetNodes();
    }

    public static void resetNodes(){
        resetNode(root);
    }

    public static void resetNode(Node<?> node){
        node.loadState();
        for(Node<?> child : node.getChildren()){
            resetNode(child);
        }
    }

    public static void saveProject(){
        // iterate over all nodes, use jackson to convert them to string and print them
        ObjectMapper objectMapper = new ObjectMapper();

    }

    public static void loadProject(){

    }

}
