package org.tuiasi.engine.logic.IO;

import lombok.Getter;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class KeyboardHandler implements IInputHandler{

    private static long window;
    @Getter
    private static final boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];

    private KeyboardHandler() {
        // Private constructor to prevent instantiation
    }

    public static void initialize(long window) {
        KeyboardHandler.window = window;

        GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key >= 0 && key < keys.length) {
                    keys[key] = action != GLFW.GLFW_RELEASE;
                }

            }
        };

        GLFW.glfwSetKeyCallback(window, keyCallback);
    }

    public static boolean isKeyPressed(int keyCode) {
        if (keyCode >= 0 && keyCode < keys.length) {
            return keys[keyCode];
        }
        return false;
    }

    public static boolean isAnyKeyPressed() {
        for (int i = 0; i < keys.length; i++) {
            if (keys[i]) {
                return true;
            }
        }
        return false;
    }


    public static void cleanup() {
        GLFW.glfwSetKeyCallback(window, null);
    }

}
