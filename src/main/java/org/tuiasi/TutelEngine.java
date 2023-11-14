package org.tuiasi;

import org.joml.Vector4f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.tuiasi.engine.window.EngineEditorUI;
import org.tuiasi.engine.window.Window;
import org.tuiasi.rendering.Renderer;

import static org.lwjgl.glfw.GLFW.*;

public class TutelEngine {

    // The window
    Window window;
    // The renderer
    Renderer renderer;

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();
        close();
    }

    private void init() {
        window = new Window(1920, 1080, "Tutel Engine", new Vector4f(0.25f, 0.25f, 0.25f, 0.25f), new EngineEditorUI());
        window.init();
    }

    private void loop() {
        double secsPerUpdate = 1.0d / 1.0d;
        double previous = 0;
        double steps = 0.0;

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window.getWindowID()) ) {
            double loopStartTime = GLFW.glfwGetTime();
            double elapsed = loopStartTime - previous;
            previous = loopStartTime;
            steps += elapsed;

            // Handle game logic on fixed framerate
            while (steps >= secsPerUpdate) {
                steps -= secsPerUpdate;
            }

            window.run();
        }
    }

    private void close(){
        window.destroy();
    }

    public static void main(String[] args) {
        new TutelEngine().run();
    }

}
