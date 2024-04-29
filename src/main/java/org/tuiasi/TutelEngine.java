package org.tuiasi;

import org.joml.Vector4f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.tuiasi.engine.ui.AppWindow;
import org.tuiasi.engine.ui.DefaultEngineEditorUI;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public class TutelEngine {

    // The window
    AppWindow appWindow;

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();
        close();
    }

    private void init() {
        appWindow = new AppWindow(1920, 1080, "Tutel Engine", new Vector4f(0.25f, 0.25f, 0.25f, 0.25f), new DefaultEngineEditorUI());
        appWindow.init();
    }

    private void loop() {

        double secsPerUpdate = 1.0d / 60.0d;
        double previous = 0;
        double steps = 0.0;

        while ( !glfwWindowShouldClose(appWindow.getWindowID()) ) {
            double loopStartTime = GLFW.glfwGetTime();
            double elapsed = loopStartTime - previous;
            previous = loopStartTime;
            steps += elapsed;

            // Handle game logic on fixed framerate
            while (steps >= secsPerUpdate) {
                steps -= secsPerUpdate;
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
