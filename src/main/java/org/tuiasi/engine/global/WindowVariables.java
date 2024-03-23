package org.tuiasi.engine.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector4f;

@Getter @Setter @AllArgsConstructor
public class WindowVariables {

    private static WindowVariables instance;

    private int width, height;
    private int windowPosX, windowPosY;
    private boolean resized;
    private String title;
    private Vector4f clearColor;
    private float mainMenuHeight;

    private long windowID;

    private WindowVariables() {
        //Init class variables
        this.width = 800;
        this.height = 600;
        this.windowPosX = 100;
        this.windowPosY = 100;
        this.title = "Engine";
        this.clearColor = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
        this.resized = false;
        this.mainMenuHeight = 0;
        this.windowID = 0;
    }

    public static WindowVariables getInstance() {
        if (instance == null) {
            synchronized (WindowVariables.class) {
                if (instance == null) {
                    instance = new WindowVariables();
                }
            }
        }
        return instance;
    }

    public static WindowVariables setInstance(int width, int height, int windowPosX, int windowPosY, String title, Vector4f clearColor, long windowID) {
        if (instance == null) {
            synchronized (WindowVariables.class) {
                if (instance == null) {
                    instance = new WindowVariables(width, height, windowPosX, windowPosY, false, title, clearColor, 0, windowID);
                }
            }
        }
        return instance;
    }

    public void updateGlobalVariables(long windowId) {
        WindowVariables windowVariables = WindowVariables.getInstance();
        windowVariables.setWindowID(windowId);
        // get the window dimensions using glfw
        int[] w = new int[1];
        int[] h = new int[1];
        org.lwjgl.glfw.GLFW.glfwGetWindowSize(windowId, w, h);
        windowVariables.setWidth(w[0]);
        windowVariables.setHeight(h[0]);
        // get the window position using glfw
        int[] x = new int[1];
        int[] y = new int[1];
        org.lwjgl.glfw.GLFW.glfwGetWindowPos(windowId, x, y);
        windowVariables.setWindowPosX(x[0]);
        windowVariables.setWindowPosY(y[0]);
    }

}
