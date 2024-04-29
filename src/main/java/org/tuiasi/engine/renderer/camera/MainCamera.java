package org.tuiasi.engine.renderer.camera;

import org.joml.Vector2d;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.tuiasi.engine.global.IO.KeyboardHandler;
import org.tuiasi.engine.global.IO.MouseHandler;
import org.tuiasi.engine.global.WindowVariables;

public class MainCamera extends Camera {
    private static MainCamera instance;

    private MainCamera(float fov, float aspect, float near, float far) {
        super(fov, aspect, near, far);
    }

    public static MainCamera getInstance() {
        WindowVariables windowVariables = WindowVariables.getInstance();

        if (instance == null) {
            instance = new MainCamera((float) Math.toRadians(45.0f), (float) windowVariables.getWidth() / windowVariables.getHeight(), 0.1f, 1000.0f);
        }
        return instance;
    }

    // thread safe method to set the instance to another camera
    public static synchronized void setInstance(Camera camera) {
        instance = (MainCamera) camera;
    }

    public static void update(){
        // update the camera
        if(instance == null)
            getInstance();

        instance.calculateViewMatrix();

        // if right click is pressed, allow camera to be moved and rotated
        if (MouseHandler.isButtonPressed(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
            // capture the mouse
            GLFW.glfwSetInputMode(WindowVariables.getInstance().getWindowID(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

            cameraMoveLogic();
            cameraRotateLogic();

        }
        else{
            // release the mouse
            GLFW.glfwSetInputMode(WindowVariables.getInstance().getWindowID(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        }

    }

    private static void cameraMoveLogic(){
        // Get the camera's front vector
        Vector3f cameraFront = instance.getCameraFront();

        float speed = 0.001f; // Adjust as needed
        if (KeyboardHandler.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT))
            speed *= 2;

        // Move forward/backward
        if (KeyboardHandler.isKeyPressed(GLFW.GLFW_KEY_W)) {
            instance.move(cameraFront.x * speed, cameraFront.y * speed, cameraFront.z * speed);
        }
        if (KeyboardHandler.isKeyPressed(GLFW.GLFW_KEY_S)) {
            instance.move(-cameraFront.x * speed, -cameraFront.y * speed, -cameraFront.z * speed);
        }

        // Calculate right vector (for strafing)
        Vector3f cameraRight = new Vector3f();
        cameraFront.cross(new Vector3f(0, 1, 0), cameraRight).normalize();

        // Move left/right (strafe)
        if (KeyboardHandler.isKeyPressed(GLFW.GLFW_KEY_A)) {
            instance.move(-cameraRight.x * speed, -cameraRight.y * speed, -cameraRight.z * speed);
        }
        if (KeyboardHandler.isKeyPressed(GLFW.GLFW_KEY_D)) {
            instance.move(cameraRight.x * speed, cameraRight.y * speed, cameraRight.z * speed);
        }

        // Move up when pressing space and down when pressing left control
        if (KeyboardHandler.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
            instance.move(0, speed, 0);
        }
        if (KeyboardHandler.isKeyPressed(GLFW.GLFW_KEY_LEFT_CONTROL)) {
            instance.move(0, -speed, 0);
        }
    }

    private static void cameraRotateLogic(){
        Vector2d mouseDelta = MouseHandler.getMouseOffset();
        // Update rotation based on mouse offset
        float sensitivity = 0.001f;
        instance.rotate((float) (mouseDelta.y * sensitivity), (float) (mouseDelta.x * sensitivity), 0);
    }

}

