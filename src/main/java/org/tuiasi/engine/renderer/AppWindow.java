package org.tuiasi.engine.renderer;

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
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.tuiasi.engine.logic.IO.KeyboardHandler;
import org.tuiasi.engine.logic.IO.MouseHandler;
import org.tuiasi.engine.global.WindowVariables;
import org.tuiasi.engine.renderer.Renderer;
import org.tuiasi.engine.renderer.camera.MainCamera;
import org.tuiasi.engine.ui.DefaultEngineEditorUI;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

@Getter @Setter
@NoArgsConstructor
public class AppWindow {

    private int width, height;
    private boolean maximized;
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

    public AppWindow(int width, int height, boolean maximized, String title, Vector4f clearColor, DefaultEngineEditorUI defaultEngineEditorUI){
        //Init class variables
        this.width = width;
        this.height = height;
        this.maximized = maximized;
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
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if ( !glfwInit() ) {
            System.out.println("Unable to initialize GLFW");
            System.exit(-1);
        }

        glslVersion = "#version 130";
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

        if(maximized) {
            // get size of the monitor
            long monitor = glfwGetPrimaryMonitor();
            GLFWVidMode vidMode = glfwGetVideoMode(monitor);
            // get the height of the taskbar
            width = vidMode.width();
            height = vidMode.height();
            // get the taskbar height to calculate the window height
            windowID = glfwCreateWindow(width, height, title, NULL, NULL);
            glfwMaximizeWindow(windowID);
        }else{
            windowID = glfwCreateWindow(width, height, title, NULL, NULL);
        }

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
            this.width = width;
            this.height = height;
        });

        // initialize the keyboard handler
        KeyboardHandler.initialize(windowID);
        MouseHandler.initialize(windowID);


        GL20.glViewport(0, 0, width, height);
        MainCamera.getInstance().setAspect((float)width / (float)height);
    }

    private void initImGui() {
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
        io.addConfigFlags(ImGuiConfigFlags.DpiEnableScaleFonts);

        // add the font found in resources to the atlas
        ImFontConfig fontConfig = new ImFontConfig();
        fontConfig.setGlyphRanges(io.getFonts().getGlyphRangesDefault());

        appFonts = new HashMap<>();
        for(int i = 16; i <= 64; ++ i) {
            InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/Nihonium113-Console.ttf");
            byte[] fontBytes = new byte[0];
            try {
                fontBytes = fontStream.readAllBytes();
            } catch (Exception e) {
                e.printStackTrace();
            }
            appFonts.put(i, io.getFonts().addFontFromMemoryTTF(fontBytes, i, fontConfig));
        }

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