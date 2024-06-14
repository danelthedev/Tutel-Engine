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
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public class AppLogic {

    @Getter
    static String appVersion = "0.1.1 Alpha";

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
        if(node.getScript() != null && !node.getScript().isEmpty() && !node.getScript().equals("null")){
            nodesWithScripts.add(node);

            String pathToScript = workingDirectory + "\\assets\\" + node.getScript();
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            int result = compiler.run(null, null, null, pathToScript);

            try {
                String classPath = pathToScript.substring(0, pathToScript.lastIndexOf("\\"));
                String cName = pathToScript.substring(pathToScript.lastIndexOf("\\") + 1, pathToScript.lastIndexOf("."));

                URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{new File(classPath).toURI().toURL()});
                Class<?> script = Class.forName(cName, true, classLoader);
                UserScript scriptInstance = (UserScript) script.getDeclaredConstructor().newInstance();
                node.setScriptObj(scriptInstance);
                node.getScriptObj().setRoot(AppLogic.getRoot());
                scriptInstance.setAttachedNode(node);
            }catch (Exception e) {
                e.printStackTrace();
            }

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

    public static Node<?> createNodeFromJson(JsonNode jsonNode) throws ClassNotFoundException, JsonProcessingException {
        String className = jsonNode.get("className").asText();
        String scriptPath = jsonNode.get("script").asText();
        System.out.println(scriptPath);
        Class<?> clazz = Class.forName(className);
        ObjectMapper objectMapper = new ObjectMapper();
        Object value = objectMapper.treeToValue(jsonNode.get("value"), clazz);

        Node<?> node = new Node<>(null, jsonNode.get("name").asText(), value);

        // handle script objects
        if(scriptPath != null && !scriptPath.isEmpty() && !scriptPath.equals("null")){
            node.setScript(scriptPath);
            String pathToScript = workingDirectory + "\\assets\\" + node.getScript();
            System.out.println(pathToScript);
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            int result = compiler.run(null, null, null, pathToScript);

            try {
                String classPath = pathToScript.substring(0, pathToScript.lastIndexOf("\\"));
                String cName = pathToScript.substring(pathToScript.lastIndexOf("\\") + 1, pathToScript.lastIndexOf("."));

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

    public static void loadProject() {
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

    public static void copyJarFile(String destinationPath) throws IOException {
        // Get the path of the currently running JAR file
        String jarPath = new File(AppLogic.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getAbsolutePath();
        System.out.println(jarPath);
        // Copy the JAR file to the specified destination
        File sourceJar = new File(jarPath);
        File destinationJar = new File(destinationPath);

        // Copy the jar
        Files.copy(sourceJar.toPath(), destinationJar.toPath(), StandardCopyOption.REPLACE_EXISTING);
        updateManifestMainClass(destinationPath, destinationPath);
        // Copy the .tutel file to the export path
        Files.copy(projectFile.toPath(), new File(destinationJar.getParentFile(), projectFile.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
        // copy the assets folder to the export path
        File assetsFolder = new File(workingDirectory + "\\assets");
        File exportAssetsFolder = new File(destinationJar.getParentFile(), "assets");
        copyFolder(assetsFolder, exportAssetsFolder);

    }

    private static void copyFolder(File startingFolder, File destinationFolder){
        if(!destinationFolder.exists()){
            destinationFolder.mkdirs();
        }
        for(File file : startingFolder.listFiles()){
            if(file.isDirectory()){
                copyFolder(file, new File(destinationFolder, file.getName()));
            }else{
                try {
                    Files.copy(file.toPath(), new File(destinationFolder, file.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void updateManifestMainClass(String jarFile, String newJarFile) throws IOException {
        Path tempDir = Files.createTempDirectory("jar");

        try (JarFile jar = new JarFile(jarFile)) {
            // Extract jar contents
            jar.stream().forEach(entry -> {
                File file = new File(tempDir.toFile(), entry.getName());
                if (entry.isDirectory()) {
                    file.mkdirs();
                } else {
                    try (InputStream is = jar.getInputStream(entry);
                         OutputStream os = new FileOutputStream(file)) {
                        is.transferTo(os);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            // Edit the MANIFEST.MF file
            File manifestFile = new File(tempDir.toFile(), "META-INF/MANIFEST.MF");
            if (manifestFile.exists()) {
                StringBuilder content = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new FileReader(manifestFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("Main-Class: org.tuiasi.TutelEngine")) {
                            line = "Main-Class: org.tuiasi.Game";
                        }
                        content.append(line).append("\n");
                    }
                }
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(manifestFile))) {
                    writer.write(content.toString());
                }
            }

            // Create a new JAR file with the updated manifest
            try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(newJarFile))) {
                Files.walk(tempDir).forEach(path -> {
                    if (Files.isDirectory(path)) return;
                    String name = tempDir.relativize(path).toString().replace("\\", "/");
                    try {
                        jos.putNextEntry(new JarEntry(name));
                        Files.copy(path, jos);
                        jos.closeEntry();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } finally {
            // Clean up temporary files
            Files.walk(tempDir).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
        }

        System.out.println("Updated JAR file created: " + newJarFile);
    }

}
