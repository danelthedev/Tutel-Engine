package org.tuiasi.engine.logic.IO;

import org.joml.Vector2d;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class MouseHandler implements IInputHandler{

    private static long window;
    private static final boolean[] buttons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
    private static double xpos, ypos, lastX, lastY, xoffset, yoffset, scroll;

    private static final double RESET_THRESHOLD = 0.001; // Adjust as needed
    private static double lastActivityTime = 0;

    private MouseHandler() {
        // Private constructor to prevent instantiation
    }

    public static void initialize(long window) {
        MouseHandler.window = window;

        // callback for clicked buttons
        GLFWMouseButtonCallback mouseButtonCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if (button >= 0 && button < buttons.length) {
                    buttons[button] = action != GLFW.GLFW_RELEASE;
                }
            }
        };

        GLFW.glfwSetMouseButtonCallback(window, mouseButtonCallback);

        // callback for mouse movement
        GLFW.glfwSetCursorPosCallback(window, (window1, xpos, ypos) -> {
            MouseHandler.lastX = MouseHandler.xpos;
            MouseHandler.lastY = MouseHandler.ypos;

            MouseHandler.xpos = xpos;
            MouseHandler.ypos = ypos;

            MouseHandler.xoffset = xpos - lastX;
            MouseHandler.yoffset = lastY - ypos;

            lastActivityTime = GLFW.glfwGetTime();
        });

        // callback for mouse scroll
        GLFW.glfwSetScrollCallback(window, (window1, xoffset, yoffset) -> {
            MouseHandler.scroll = yoffset;
        });
    }

    public static boolean isButtonPressed(int button) {
        if (button >= 0 && button < buttons.length) {
            return buttons[button];
        }
        return false;
    }



    public static Vector2d getMousePosition() {
        return new Vector2d(xpos, ypos);
    }

    public static Vector2d getLastMousePosition() {
        return new Vector2d(lastX, lastY);
    }

    public static Vector2d getMouseOffset() {
        double currentTime = GLFW.glfwGetTime();
        if (currentTime - lastActivityTime > RESET_THRESHOLD) {
            xoffset = 0;
            yoffset = 0;
        }

        return new Vector2d(xoffset, yoffset);
    }

    public static void cleanup() {
        GLFW.glfwSetMouseButtonCallback(window, null);
        GLFW.glfwSetCursorPosCallback(window, null);
        GLFW.glfwSetScrollCallback(window, null);
    }

}
