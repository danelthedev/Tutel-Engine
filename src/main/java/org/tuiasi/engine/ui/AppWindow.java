package org.tuiasi.engine.ui;

import imgui.*;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static java.lang.Math.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.tuiasi.engine.global.IO.KeyboardHandler;
import org.tuiasi.engine.global.IO.MouseHandler;
import org.tuiasi.engine.global.WindowVariables;
import org.tuiasi.engine.global.nodes.spatial.Spatial3D;
import org.tuiasi.engine.renderer.camera.Camera;
import org.tuiasi.engine.renderer.camera.MainCamera;
import org.tuiasi.engine.renderer.light.LightData;
import org.tuiasi.engine.renderer.light.LightSource;
import org.tuiasi.engine.renderer.material.Material;
import org.tuiasi.engine.renderer.renderable.Renderable3D;
import org.tuiasi.engine.renderer.shader.DrawMode;
import org.tuiasi.engine.renderer.shader.Shader;
import org.tuiasi.engine.renderer.shader.ShaderProgram;
import org.tuiasi.engine.renderer.shader.Uniform;
import org.tuiasi.engine.renderer.texture.Texture;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryUtil.NULL;

@Getter @Setter
@NoArgsConstructor
public class AppWindow {

    private int width, height;
    private boolean resized;
    private String title;
    private Vector4f clearColor;

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    private String glslVersion = null;
    private long windowID;
    private DefaultEngineEditorUI defaultEngineEditorUI;

    WindowVariables windowVariables;

    public static ImFont appFont;

    List<Renderable3D> objects;
    Renderable3D testObject, axisObject;
    LightSource lightSource;

    public AppWindow(int width, int height, String title, Vector4f clearColor, DefaultEngineEditorUI defaultEngineEditorUI){
        //Init class variables
        this.width = width;
        this.height = height;
        this.title = title;
        this.clearColor = clearColor;
        this.resized = false;
        this.defaultEngineEditorUI = defaultEngineEditorUI;
    }

    public void init() {
        initWindow();
        initImGui();
        imGuiGlfw.init(windowID, true);
        imGuiGl3.init(glslVersion);


        lightSource = new LightSource(  new Spatial3D(  new Vector3f(10, 10, 10),
                                                        new Vector3f(0, 0, 0),
                                                        new Vector3f(1, 1, 1)),
                                        new LightData(  new Vector3f(.2f, .2f, .2f),
                                                        new Vector3f(1.0f, 1.0f, 1.0f),
                                                        new Vector3f(1.0f, 1.0f, 1.0f)));

        objects = new ArrayList<>();

        testObject = new Renderable3D(
                new float[]{
                        -0.5f, -0.5f, -0.5f,    1.0f, 1.0f,1.0f,         0.0f,  0.0f, -1.0f,  0.0f, 0.0f,
                        0.5f, -0.5f, -0.5f,    1.0f, 1.0f,1.0f,         0.0f,  0.0f, -1.0f,  1.0f, 0.0f,
                        0.5f,  0.5f, -0.5f,    1.0f, 1.0f,1.0f,         0.0f,  0.0f, -1.0f,  1.0f, 1.0f,
                        0.5f,  0.5f, -0.5f,    1.0f, 1.0f,1.0f,         0.0f,  0.0f, -1.0f,  1.0f, 1.0f,
                        -0.5f,  0.5f, -0.5f,    1.0f, 1.0f,1.0f,         0.0f,  0.0f, -1.0f,  0.0f, 1.0f,
                        -0.5f, -0.5f, -0.5f,    1.0f, 1.0f,1.0f,         0.0f,  0.0f, -1.0f,  0.0f, 0.0f,

                        -0.5f, -0.5f,  0.5f,    1.0f, 1.0f,1.0f,         0.0f,  0.0f, 1.0f,   0.0f, 0.0f,
                        0.5f, -0.5f,  0.5f,    1.0f, 1.0f,1.0f,         0.0f,  0.0f, 1.0f,   1.0f, 0.0f,
                        0.5f,  0.5f,  0.5f,    1.0f, 1.0f,1.0f,         0.0f,  0.0f, 1.0f,   1.0f, 1.0f,
                        0.5f,  0.5f,  0.5f,    1.0f, 1.0f,1.0f,         0.0f,  0.0f, 1.0f,   1.0f, 1.0f,
                        -0.5f,  0.5f,  0.5f,    1.0f, 1.0f,1.0f,         0.0f,  0.0f, 1.0f,   0.0f, 1.0f,
                        -0.5f, -0.5f,  0.5f,    1.0f, 1.0f,1.0f,         0.0f,  0.0f, 1.0f,   0.0f, 0.0f,

                        -0.5f,  0.5f,  0.5f,    1.0f, 1.0f,1.0f,         -1.0f,  0.0f,  0.0f,  1.0f, 0.0f,
                        -0.5f,  0.5f, -0.5f,    1.0f, 1.0f,1.0f,         -1.0f,  0.0f,  0.0f,  1.0f, 1.0f,
                        -0.5f, -0.5f, -0.5f,    1.0f, 1.0f,1.0f,         -1.0f,  0.0f,  0.0f,  0.0f, 1.0f,
                        -0.5f, -0.5f, -0.5f,    1.0f, 1.0f,1.0f,         -1.0f,  0.0f,  0.0f,  0.0f, 1.0f,
                        -0.5f, -0.5f,  0.5f,    1.0f, 1.0f,1.0f,         -1.0f,  0.0f,  0.0f,  0.0f, 0.0f,
                        -0.5f,  0.5f,  0.5f,    1.0f, 1.0f,1.0f,         -1.0f,  0.0f,  0.0f,  1.0f, 0.0f,

                        0.5f,  0.5f,  0.5f,    1.0f, 1.0f,1.0f,         1.0f,  0.0f,  0.0f,  1.0f, 0.0f,
                        0.5f,  0.5f, -0.5f,    1.0f, 1.0f,1.0f,         1.0f,  0.0f,  0.0f,  1.0f, 1.0f,
                        0.5f, -0.5f, -0.5f,    1.0f, 1.0f,1.0f,         1.0f,  0.0f,  0.0f,  0.0f, 1.0f,
                        0.5f, -0.5f, -0.5f,    1.0f, 1.0f,1.0f,         1.0f,  0.0f,  0.0f,  0.0f, 1.0f,
                        0.5f, -0.5f,  0.5f,    1.0f, 1.0f,1.0f,         1.0f,  0.0f,  0.0f,  0.0f, 0.0f,
                        0.5f,  0.5f,  0.5f,    1.0f, 1.0f,1.0f,         1.0f,  0.0f,  0.0f,  1.0f, 0.0f,

                        -0.5f, -0.5f, -0.5f,    1.0f, 1.0f,1.0f,         0.0f, -1.0f,  0.0f,  0.0f, 1.0f,
                        0.5f, -0.5f, -0.5f,    1.0f, 1.0f,1.0f,         0.0f, -1.0f,  0.0f,  1.0f, 1.0f,
                        0.5f, -0.5f,  0.5f,    1.0f, 1.0f,1.0f,         0.0f, -1.0f,  0.0f,  1.0f, 0.0f,
                        0.5f, -0.5f,  0.5f,    1.0f, 1.0f,1.0f,         0.0f, -1.0f,  0.0f,  1.0f, 0.0f,
                        -0.5f, -0.5f,  0.5f,    1.0f, 1.0f,1.0f,         0.0f, -1.0f,  0.0f,  0.0f, 0.0f,
                        -0.5f, -0.5f, -0.5f,    1.0f, 1.0f,1.0f,         0.0f, -1.0f,  0.0f,  0.0f, 1.0f,

                        -0.5f,  0.5f, -0.5f,    1.0f, 1.0f,1.0f,         0.0f,  1.0f,  0.0f,  0.0f, 1.0f,
                        0.5f,  0.5f, -0.5f,    1.0f, 1.0f,1.0f,         0.0f,  1.0f,  0.0f,  1.0f, 1.0f,
                        0.5f,  0.5f,  0.5f,    1.0f, 1.0f,1.0f,         0.0f,  1.0f,  0.0f,  1.0f, 0.0f,
                        0.5f,  0.5f,  0.5f,    1.0f, 1.0f,1.0f,         0.0f,  1.0f,  0.0f,  1.0f, 0.0f,
                        -0.5f,  0.5f,  0.5f,    1.0f, 1.0f,1.0f,         0.0f,  1.0f,  0.0f,  0.0f, 0.0f,
                        -0.5f,  0.5f, -0.5f,    1.0f, 1.0f,1.0f,         0.0f,  1.0f,  0.0f,  0.0f, 1.0f
                },
                new int[]{
                        // Front face
                        0, 1, 2,
                        3, 4, 5,
                        // Back face
                        6, 7, 8,
                        9, 10, 11,
                        // Top face
                        12, 13, 14,
                        15, 16, 17,
                        // Bottom face
                        18, 19, 20,
                        21, 22, 23,
                        // Right face
                        24, 25, 26,
                        27, 28, 29,
                        // Left face
                        30, 31, 32,
                        33, 34, 35
                },
                new ShaderProgram(new Shader("src/main/resources/shaders/default_vertex.vert", GL_VERTEX_SHADER), new Shader("src/main/resources/shaders/default_fragment.frag", GL_FRAGMENT_SHADER)),
                new Texture[]{  new Texture("src/main/resources/textures/container2.png", 0)
                },
                new Material(   new Texture("src/main/resources/textures/container2.png", 1),
                                new Texture("src/main/resources/textures/container2_specular.png", 2),
                        16f)
        );

        // axis object that is used to draw the x, y and z axis with different colors
        axisObject = new Renderable3D(
                new float[]{
                        -100.0f, 0.0f, 0.0f, 1, 0, 0, 0, 0, 0, 0, 0,
                        100.0f, 0.0f, 0.0f, 1, 0, 0, 0, 0, 0, 0, 0,

                        0.0f, -100.0f, 0.0f, 0, 1, 0, 0, 0, 0, 0, 0,
                        0.0f, 100.0f, 0.0f, 0, 1, 0, 0, 0, 0, 0, 0,

                        0.0f, 0.0f, -100.0f, 0, 0, 1, 0, 0, 0, 0, 0,
                        0.0f, 0.0f, 100.0f, 0, 0, 1, 0, 0, 0, 0, 0,
                },
                new int[]{
                        0, 1,
                        2, 3,
                        4, 5
                },
                new ShaderProgram(new Shader("src/main/resources/shaders/default_vertex.vert", GL_VERTEX_SHADER), new Shader("src/main/resources/shaders/solid_color_fragment.frag", GL_FRAGMENT_SHADER)),
                new Texture[]{new Texture()},
                new Material()
        );
        axisObject.setDrawMode(DrawMode.WIREFRAME);

        objects.add(testObject);
        objects.add(axisObject);

        testObject.setUniform(new Uniform<>("lightPos", lightSource.getTransform().getPosition()));

        windowVariables = WindowVariables.getInstance();
    }

    public void destroy() {
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
        Callbacks.glfwFreeCallbacks(windowID);
        glfwDestroyWindow(windowID);
        glfwTerminate();
    }

    private void initWindow() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() ) {
            System.out.println("Unable to initialize GLFW");
            System.exit(-1);
        }

        glslVersion = "#version 130";
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        windowID = glfwCreateWindow(width, height, title, NULL, NULL);

        if (windowID == NULL) {
            System.out.println("Unable to create window");
            System.exit(-1);
        }

        glfwMakeContextCurrent(windowID);
        glfwSwapInterval(1);

        GL.createCapabilities();

        glfwShowWindow(windowID);

        glEnable(GL_DEPTH_TEST);

        // set resized callback
        glfwSetFramebufferSizeCallback(windowID, (window, width, height) -> {
            GL20.glViewport(0, 0, width, height);
            MainCamera.getInstance().setAspect((float)width / (float)height);
        });

        // initialize the keyboard handler
        KeyboardHandler.initialize(windowID);
        MouseHandler.initialize(windowID);
    }

    private void initImGui() {
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
        io.addConfigFlags(ImGuiConfigFlags.DpiEnableScaleFonts);


        // add the font Nihonium113-Console.ttf found in resources to the atlas
        ImFontConfig fontConfig = new ImFontConfig();
        fontConfig.setGlyphRanges(io.getFonts().getGlyphRangesDefault());
        appFont = io.getFonts().addFontFromFileTTF("src/main/resources/Nihonium113-Console.ttf", 14, fontConfig);

    }


    public void run() {

        while (!glfwWindowShouldClose(windowID)) {
            // clear the previous frame
            GL11.glClearColor(0.1f, 0.09f, 0.1f, 1.0f);
            GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // create a new frame for both ImGui and GLFW
            imGuiGlfw.newFrame();
            ImGui.newFrame();

            // update the window data
            windowVariables.updateGlobalVariables(windowID);

            // update the camera
            MainCamera.update();

            // render the objects
            testObject.rotate(new Vector3f(0, 0.01f, 0));

            for(Renderable3D object : objects) {
                object.setUniform(new Uniform<>("lightPos", lightSource.getTransform().getPosition()));
                object.setUniform(new Uniform<>("viewPos", MainCamera.getInstance().getPosition()));

                // only one light source for now, so sending it to all
                object.setUniform(new Uniform<>("lightAmbient", lightSource.getLightData().getAmbient()));
                object.setUniform(new Uniform<>("lightDiffuse", lightSource.getLightData().getDiffuse()));
                object.setUniform(new Uniform<>("lightSpecular", lightSource.getLightData().getSpecular()));

                object.render();
            }

            // render the UI
            defaultEngineEditorUI.renderUI();
            ImGui.render();
            imGuiGl3.renderDrawData(ImGui.getDrawData());

            // manage the viewports of the UI
            if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
                final long backupWindowID = org.lwjgl.glfw.GLFW.glfwGetCurrentContext();
                ImGui.updatePlatformWindows();
                ImGui.renderPlatformWindowsDefault();
                GLFW.glfwMakeContextCurrent(backupWindowID);
            }

            // swap the buffers and poll for events
            GLFW.glfwSwapBuffers(windowID);
            GLFW.glfwPollEvents();
        }
    }

}