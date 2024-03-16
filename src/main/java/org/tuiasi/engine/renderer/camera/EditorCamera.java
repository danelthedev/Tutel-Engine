package org.tuiasi.engine.renderer.camera;

import org.tuiasi.engine.global.WindowVariables;

public class EditorCamera extends Camera {
    private static EditorCamera instance;

    private EditorCamera(float fov, float aspect, float near, float far) {
        super(fov, aspect, near, far);
    }

    public static EditorCamera getInstance() {
        WindowVariables windowVariables = WindowVariables.getInstance();

        if (instance == null) {
            instance = new EditorCamera((float) Math.toRadians(45.0f), (float) windowVariables.getWidth() / windowVariables.getHeight(), 0.1f, 1000.0f);
        }
        return instance;
    }
}

