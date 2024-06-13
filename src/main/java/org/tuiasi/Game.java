package org.tuiasi;

import org.joml.Vector4f;
import org.lwjgl.Version;
import org.tuiasi.engine.logic.AppLogic;
import org.tuiasi.engine.logic.EngineState;
import org.tuiasi.engine.logic.logger.Log;
import org.tuiasi.engine.ui.AppWindow;
import org.tuiasi.engine.ui.DefaultEngineEditorUI;

import java.io.File;
import java.io.IOException;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public class Game {

    // The window
    AppWindow appWindow;

    // Logic update rate
    private static final double TARGET_UPS = 60.0; // Target updates per second

    public void run() throws IOException, ClassNotFoundException {
        init();
        loop();
        close();
    }

    private void init() throws IOException, ClassNotFoundException {
        AppLogic.init();

        DefaultEngineEditorUI.setDisplayingUI(false);
        DefaultEngineEditorUI.setDisplayingTopBar(false);
        AppLogic.setEngineState(EngineState.PLAY);

        appWindow = new AppWindow(1920, 1080, true, "Game", new Vector4f(0.25f, 0.25f, 0.25f, 0.25f), new DefaultEngineEditorUI());
        appWindow.init();
        Log.init();

        AppLogic.setProjectFile(new File("project.tutel"));
        AppLogic.loadProject();
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
                AppLogic.run();

                accumulatedTime -= 1.0 / TARGET_UPS;
            }

            appWindow.run();

        }

    }

    private void close(){
        appWindow.destroy();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new Game().run();
    }

}
