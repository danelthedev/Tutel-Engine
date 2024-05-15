package org.tuiasi;

import imgui.ImGui;
import org.joml.Vector4f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.tuiasi.engine.logic.AppLogic;
import org.tuiasi.engine.renderer.camera.MainCamera;
import org.tuiasi.engine.ui.AppWindow;
import org.tuiasi.engine.ui.DefaultEngineEditorUI;

import java.util.Scanner;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public class TutelEngine {

    // The window
    AppWindow appWindow;

    // Logic update rate
    private static final double TARGET_UPS = 1.0; // Target updates per second

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");
        init();
        loop();
        close();
    }

    private void init() {
        AppLogic.init();
        appWindow = new AppWindow(1920, 1080, "Tutel Engine", new Vector4f(0.25f, 0.25f, 0.25f, 0.25f), new DefaultEngineEditorUI());
        appWindow.init();
    }

    private void loop() {

        double lastTime = glfwGetTime();
        double elapsedTime;
        double accumulatedTime = 0.0;

        while (!glfwWindowShouldClose(appWindow.getWindowID())) {
            double currentTime = glfwGetTime();
            elapsedTime = currentTime - lastTime;
            lastTime = currentTime;

            accumulatedTime += elapsedTime;

            // Update logic if enough time has elapsed
            while (accumulatedTime >= 1.0 / TARGET_UPS) {
                // Update logic of the engine

                accumulatedTime -= 1.0 / TARGET_UPS;
            }

            appWindow.run();

        }

    }

    private void close(){
        appWindow.destroy();
    }

    public static void main(String[] args) {
        new TutelEngine().run();
    }

}
