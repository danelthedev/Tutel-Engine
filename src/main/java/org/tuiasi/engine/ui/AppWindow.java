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
import org.tuiasi.engine.renderer.renderable.Renderable3D;
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

    // TODO: FIX THE SHADERS!!!!!!

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
    Renderable3D testObject, testObject2;
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


        lightSource = new LightSource(  new Spatial3D(  new Vector3f(10, 0, 2),
                                                        new Vector3f(0, 0, 0),
                                                        new Vector3f(1, 1, 1)),
                                        new LightData(  .5f, .6f, 1),
                                        new Vector3f(1, 1, 1));

        objects = new ArrayList<>();

        testObject = new Renderable3D(
                new float[]{
                        -0.5f, 0.5f, 0.5f,    1.0f, 0.1f, 0.3f,    0.0f, 0.0f,    -0.5f, 0.5f, 0.5f, // Top left
                        0.5f, 0.5f, 0.5f,     1.0f, 0.3f, 0.3f,    1.0f, 0.0f,    0.5f, 0.5f, 0.5f, // Top right
                        -0.5f, -0.5f, 0.5f,   1.0f, 0.3f, 0.3f,    0.0f, 1.0f,    -0.5f, -0.5f, 0.5f, // Bottom left
                        0.5f, -0.5f, 0.5f,    1.0f, 0.3f, 0.3f,    1.0f, 1.0f,    0.5f, -0.5f, 0.5f, // Bottom right

                        // Back face
                        -0.5f, 0.5f, -0.5f,   1.0f, 0.3f, 0.3f,    0.0f, 0.0f,    -0.5f, 0.5f, -0.5f, // Top left
                        0.5f, 0.5f, -0.5f,    1.0f, 0.3f, 0.3f,    1.0f, 0.0f,    0.5f, 0.5f, -0.5f, // Top right
                        -0.5f, -0.5f, -0.5f,  1.0f, 0.3f, 0.3f,    0.0f, 1.0f,    -0.5f, -0.5f, -0.5f, // Bottom left
                        0.5f, -0.5f, -0.5f,   1.0f, 0.3f, 0.3f,    1.0f, 1.0f,    0.5f, -0.5f, -0.5f, // Bottom right
                },
                new int[]{
                        // Front face
                        0, 1, 2,
                        1, 3, 2,
                        // Back face
                        4, 6, 5,
                        5, 6, 7,
                        // Top face
                        0, 4, 1,
                        1, 4, 5,
                        // Bottom face
                        2, 3, 6,
                        3, 7, 6,
                        // Right face
                        1, 5, 3,
                        3, 5, 7,
                        // Left face
                        0, 2, 4,
                        2, 6, 4
                },
                new ShaderProgram(new Shader("src/main/resources/shaders/default_vertex.vert", GL_VERTEX_SHADER), new Shader("src/main/resources/shaders/default_fragment.frag", GL_FRAGMENT_SHADER))
        );

        objects.add(testObject);

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

            for(Renderable3D object : objects) {
                object.getShaderProgram().use();
                object.setUniform(new Uniform<>("lightPos", lightSource.getTransform().getPosition()));
                object.setUniform(new Uniform<>("viewPos", MainCamera.getInstance().getPosition()));

                // only one light source for now, so sending it to all
                object.setUniform(new Uniform<>("lightColor", lightSource.getColor()));
                object.setUniform(new Uniform<>("ambient", lightSource.getLightData().getAmbient()));
                object.setUniform(new Uniform<>("specular", lightSource.getLightData().getSpecular()));


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