package org.tuiasi.engine.ui;

import imgui.ImFont;
import imgui.ImFontConfig;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joml.Vector4f;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.tuiasi.engine.global.IO.KeyboardHandler;
import org.tuiasi.engine.global.IO.MouseHandler;
import org.tuiasi.engine.global.WindowVariables;
import org.tuiasi.engine.renderer.Renderer;
import org.tuiasi.engine.renderer.camera.MainCamera;
import org.tuiasi.engine.renderer.light.DirectionalLight;
import org.tuiasi.engine.renderer.light.PointLight;
import org.tuiasi.engine.renderer.renderable.Renderable3D;

import java.util.HashMap;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
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
    public static HashMap<Integer, ImFont> appFonts;

    Renderer renderer;

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

        windowVariables = WindowVariables.getInstance();
        renderer = new Renderer();
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


        appFonts = new HashMap<>();
        for(int i = 16; i <= 64; ++ i)
            appFonts.put(i, io.getFonts().addFontFromFileTTF("src/main/resources/Nihonium113-Console.ttf", i, fontConfig));

    }


    public void run() {
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

        // Render the UI and the scene
        renderer.render();

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