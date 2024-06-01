package org.tuiasi.engine.renderer.camera;

import org.joml.Vector2d;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.tuiasi.engine.logic.AppLogic;
import org.tuiasi.engine.logic.EngineState;
import org.tuiasi.engine.logic.IO.KeyboardHandler;
import org.tuiasi.engine.logic.IO.MouseHandler;
import org.tuiasi.engine.global.WindowVariables;

public class MainCamera {
    private static Camera instance;

    public static Camera getInstance() {
        WindowVariables windowVariables = WindowVariables.getInstance();

        if (instance == null) {
            instance = new Camera((float) Math.toRadians(45.0f), (float) windowVariables.getWidth() / windowVariables.getHeight(), 0.1f, 1000.0f);
        }
        return instance;
    }

    // thread safe method to set the instance to another camera
    public static synchronized void setInstance(Camera camera) {
        instance = camera;
    }

    public static void update(){
        // update the camera
        if(instance == null)
            getInstance();

        instance.calculateViewMatrix();
        instance.calculateProjectionMatrix();

        cameraManipulationLogic();
    }

    private static void cameraManipulationLogic(){
        if(AppLogic.getEngineState() == EngineState.EDITOR) {
            if (MouseHandler.isButtonPressed(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
                GLFW.glfwSetInputMode(WindowVariables.getInstance().getWindowID(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                editorCameraMovement();
                editorCameraRotation();
            }
            else
                GLFW.glfwSetInputMode(WindowVariables.getInstance().getWindowID(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        }
    }

    private static void editorCameraMovement(){
        // Get the camera's front vector
        Vector3f cameraFront = instance.getCameraFront();

        float speed = 0.003f; // Adjust as needed
        if (KeyboardHandler.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT))
            speed *= 2;

        // Move forward/backward
        if (KeyboardHandler.isKeyPressed(GLFW.GLFW_KEY_W)) {
            instance.translate(cameraFront.x * speed, cameraFront.y * speed, cameraFront.z * speed);
        }
        if (KeyboardHandler.isKeyPressed(GLFW.GLFW_KEY_S)) {
            instance.translate(-cameraFront.x * speed, -cameraFront.y * speed, -cameraFront.z * speed);
        }

        // Calculate right vector (for strafing)
        Vector3f cameraRight = new Vector3f();
        cameraFront.cross(new Vector3f(0, 1, 0), cameraRight).normalize();

        // Move left/right (strafe)
        if (KeyboardHandler.isKeyPressed(GLFW.GLFW_KEY_A)) {
            instance.translate(-cameraRight.x * speed, -cameraRight.y * speed, -cameraRight.z * speed);
        }
        if (KeyboardHandler.isKeyPressed(GLFW.GLFW_KEY_D)) {
            instance.translate(cameraRight.x * speed, cameraRight.y * speed, cameraRight.z * speed);
        }

        // Move up when pressing space and down when pressing left control
        if (KeyboardHandler.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
            instance.translate(0, speed, 0);
        }
        if (KeyboardHandler.isKeyPressed(GLFW.GLFW_KEY_LEFT_CONTROL)) {
            instance.translate(0, -speed, 0);
        }
    }

    private static void editorCameraRotation(){
        Vector2d mouseDelta = MouseHandler.getMouseOffset();
        // Update rotation based on mouse offset
        float sensitivity = 0.015f;
        instance.rotate((float) (mouseDelta.y * sensitivity), (float) (mouseDelta.x * sensitivity), 0);
    }


}

