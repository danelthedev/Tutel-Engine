package org.tuiasi.engine.renderer.camera;

import lombok.Data;
import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.tuiasi.engine.logic.IO.MouseHandler;
import org.tuiasi.engine.global.WindowVariables;
import org.tuiasi.engine.misc.MathMisc;

import static java.lang.Math.*;

@Data
public abstract class Camera {
    private float fov;
    private float aspect;
    private float near;
    private float far;
    private Vector4f position;
    private Vector4f rotation;

    private Matrix4f viewMatrix, projectionMatrix;

    public Camera(float fov, float aspect, float near, float far){
        this.fov = fov;
        this.aspect = aspect;
        this.near = near;
        this.far = far;
        this.position = new Vector4f(0, 0, 0, 0);
        this.rotation = new Vector4f(0, (float) (-PI/2), 0, 0);

        this.viewMatrix = new Matrix4f();
    }

    public Camera(float fov, float aspect, float near, float far, Vector4f position, Vector4f rotation) {
        this.fov = fov;
        this.aspect = aspect;
        this.near = near;
        this.far = far;
        this.position = position;
        this.rotation = rotation;

        this.viewMatrix = new Matrix4f();

    }

    public Matrix4f calculateViewMatrix() {

        // use the position vector and front vector in the lookAt function to calculate the view matrix
        viewMatrix = new Matrix4f().lookAt( new Vector3f(position.x, position.y, position.z),
                                            getCameraFront().add(position.x, position.y, position.z),
                                            new Vector3f(0, 1, 0));
        return viewMatrix;
    }

    public Matrix4f calculateProjectionMatrix() {
        projectionMatrix = new Matrix4f().perspective(fov, aspect, near, far);
        return projectionMatrix;
    }

    public void move(float x, float y, float z) {
        position.x += x;
        position.y += y;
        position.z += z;
    }

    public void rotate(float x, float y, float z) {
        // Add rotation
        rotation.x += x;
        rotation.y += y;
        rotation.z += z;

        rotation.x = (float) MathMisc.clamp(rotation.x,  Math.toRadians(-89), Math.toRadians(89));
        rotation.y = (float) MathMisc.wrapAngle(rotation.y, -Math.PI, Math.PI);
        rotation.z = (float) MathMisc.wrapAngle(rotation.z, -Math.PI, Math.PI);
    }

    public void lookAtPosition(Vector4f targetPosition) {
        Vector3f direction = new Vector3f(targetPosition.x - position.x,
                targetPosition.y - position.y,
                targetPosition.z - position.z);
        direction.normalize();

        float pitch = (float) asin(-direction.y);
        float yaw = (float) atan2(direction.x, direction.z);

        rotation.x = pitch;
        rotation.y = (float) ((PI/2) + yaw);
    }

    public Vector3f getCameraFront() {
        Vector3f direction = new Vector3f();
        direction.x = (float) (cos(rotation.y) * cos(rotation.x));
        direction.y = (float) sin(rotation.x);
        direction.z = (float) (sin(rotation.y) * cos(rotation.x));

        return direction.normalize();
    }

    public Vector3f getRayDirection(){
        WindowVariables windowVariables = WindowVariables.getInstance();
        Vector2d mousePos = MouseHandler.getMousePosition();

        // get the ray direction from the camera position and the position of the mouse
        Vector3f rayOrigin = new Vector3f(position.x, position.y, position.z);
        Vector3f rayDirection = new Vector3f();
        float x = (2.0f * (float)mousePos.x) / windowVariables.getWidth() - 1.0f;
        float y = 1.0f - (2.0f * (float)mousePos.y) / windowVariables.getHeight();
        float z = 1.0f;
        Vector4f rayClip = new Vector4f(x, y, z, 1.0f);
        Vector4f rayEye = new Vector4f();
        Matrix4f projCopy = new Matrix4f(projectionMatrix);
        projCopy.invert().transform(rayClip, rayEye);
        rayEye.z = -1.0f;
        rayEye.w = 0.0f;
        Vector4f rayWorld = new Vector4f();
        Matrix4f viewCopy = new Matrix4f(viewMatrix);
        viewCopy.invert().transform(rayEye, rayWorld);
        rayDirection = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
        rayDirection.normalize();
        return rayDirection;
    }

}
