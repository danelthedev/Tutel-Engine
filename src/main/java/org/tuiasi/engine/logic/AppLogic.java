package org.tuiasi.engine.logic;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.tuiasi.engine.global.WindowVariables;
import org.tuiasi.engine.global.nodes.INodeValue;
import org.tuiasi.engine.global.nodes.physics.body.IBody;
import org.tuiasi.engine.global.nodes.physics.body.KinematicBody;
import org.tuiasi.engine.global.nodes.physics.collider.Collider3D;
import org.tuiasi.engine.global.nodes.reflective.ReflectiveObjectManager;
import org.tuiasi.engine.logic.IO.MouseHandler;
import org.tuiasi.engine.global.nodes.Node;
import org.tuiasi.engine.global.nodes.spatial.Spatial3D;
import org.tuiasi.engine.logic.codeProcessor.UserScript;
import org.tuiasi.engine.logic.logger.Log;
import org.tuiasi.engine.misc.json.Vector3fSerializer;
import org.tuiasi.engine.renderer.Renderer;
import org.tuiasi.engine.renderer.camera.Camera;
import org.tuiasi.engine.renderer.camera.MainCamera;
import org.tuiasi.engine.ui.DefaultEngineEditorUI;
import org.tuiasi.engine.ui.uiWindows.prefabs.UINodeInspectorWindow;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AppLogic {

    @Getter
    static String appVersion = "0.1.0 Alpha";

    @Getter @Setter
    static EngineState engineState = EngineState.EDITOR;

    @Getter @Setter
    static Node<?> root;

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
        root.saveState();
        selectedNode = root;

        nodesWithScripts = new ArrayList<>();
        physicsNodes = new ArrayList<>();

        cameras = new ArrayList<>();

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

        if(node.getValue() instanceof INodeValue)
            node.saveState();
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
        objectMapper.registerModule(new SimpleModule().addSerializer(Vector3f.class, new Vector3fSerializer()));
        // configure mapper to ignore all fields except those market as JsonProperty
        objectMapper.setVisibility(objectMapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.NONE)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE)
                .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE));
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        try {
            String projectConfig =  objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
            PrintWriter writer = new PrintWriter(projectFile);
            writer.print(projectConfig);
            writer.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Node<?> createNodeFromJson(JsonNode jsonNode) throws ClassNotFoundException, IllegalAccessException, InstantiationException, JsonProcessingException {
        String className = jsonNode.get("className").asText();
        String scriptPath = jsonNode.get("script").asText();
        Class<?> clazz = Class.forName(className);
        ObjectMapper objectMapper = new ObjectMapper();
        Object value = objectMapper.treeToValue(jsonNode.get("value"), clazz);

        Node<?> node = new Node<>(null, jsonNode.get("name").asText(), value);

        // handle script objects
        if(!scriptPath.equals("null")){
            node.setScript(scriptPath);
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            int result = compiler.run(null, null, null, node.getScript());

            try {
                String classPath = node.getScript().substring(0, node.getScript().lastIndexOf("\\"));
                String cName = node.getScript().substring(node.getScript().lastIndexOf("\\") + 1, node.getScript().lastIndexOf("."));

                URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{new File(classPath).toURI().toURL()});
                Class<?> script = Class.forName(cName, true, classLoader);
                UserScript scriptInstance = (UserScript) script.getDeclaredConstructor().newInstance();
                node.setScriptObj(scriptInstance);
                node.getScriptObj().setRoot(AppLogic.getRoot());
                scriptInstance.setAttachedNode(node);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            node.setScript("");
        }

        JsonNode childrenNode = jsonNode.get("children");
        for (JsonNode childNode : childrenNode) {
            Node<?> child = createNodeFromJson(childNode);
            node.addChild(child);
            child.setParent(node);
        }

        return node;
    }

    public static void loadProject(){
        // read the project file and parse it to a node tree
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            cleanNodeQueue();
            Renderer.removeAllRenderables();
            Renderer.removeAllLightSources();
            JsonNode newRoot = objectMapper.readTree(projectFile);
            root = createNodeFromJson(newRoot);
            initializeNodes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
