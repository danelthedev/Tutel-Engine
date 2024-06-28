package org.tuiasi.engine.renderer.camera;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.tuiasi.engine.global.nodes.EditorVisible;
import org.tuiasi.engine.global.nodes.spatial.Spatial3D;
import org.tuiasi.engine.logic.IO.MouseHandler;
import org.tuiasi.engine.global.WindowVariables;
import org.tuiasi.engine.misc.MathMisc;

import static java.lang.Math.*;

public class Camera extends Spatial3D {
    @EditorVisible
    @JsonProperty
    @Getter @Setter
    private Float fov;
    @JsonProperty
    @EditorVisible
    @Getter @Setter
    private Float aspect;
    @JsonProperty
    @EditorVisible
    @Getter @Setter
    private Float near;
    @JsonProperty
    @EditorVisible
    @Getter @Setter
    private Float far;
    @JsonProperty
    @EditorVisible
    @Getter @Setter
    private Boolean isMainCamera = Boolean.FALSE;;

    @Getter @JsonIgnore
    private Matrix4f viewMatrix, projectionMatrix;

    public Camera(){
        this.fov = (float) Math.toRadians(45.0f);
        this.aspect = (float) WindowVariables.getInstance().getWidth() / WindowVariables.getInstance().getHeight();
        this.near = 0.1f;
        this.far = 1000.0f;
        this.position = new Vector3f(0, 0, 0);
        this.rotation = new Vector3f(0, -90, 0);

        this.viewMatrix = new Matrix4f();
    }

    public Camera(float fov, float aspect, float near, float far){
        this.fov = fov;
        this.aspect = aspect;
        this.near = near;
        this.far = far;
        this.position = new Vector3f(0, 0, 0);
        this.rotation = new Vector3f(0, (float) (-PI/2), 0);

        this.viewMatrix = new Matrix4f();
    }

    public Camera(float fov, float aspect, float near, float far, Vector3f position, Vector3f rotation) {
        this.fov = fov;
        this.aspect = aspect;
        this.near = near;
        this.far = far;
        this.position = position;
        this.rotation = rotation;

        this.viewMatrix = new Matrix4f();
    }

    public Camera(Spatial3D spatial3D, float fov, float aspect, float near, float far) {
        super(spatial3D.getPosition(), spatial3D.getRotation(), spatial3D.getScale());
        this.fov = fov;
        this.aspect = aspect;
        this.near = near;
        this.far = far;
        this.viewMatrix = new Matrix4f();
    }

    public Matrix4f calculateViewMatrix() {
        viewMatrix = new Matrix4f().lookAt( new Vector3f(position.x, position.y, position.z),
                                            getCameraFront().add(position.x, position.y, position.z),
                                            new Vector3f(0, 1, 0));
        return viewMatrix;
    }

    public Matrix4f calculateProjectionMatrix() {
        projectionMatrix = new Matrix4f().perspective(fov, aspect, near, far);
        return projectionMatrix;
    }

    public void translate(float x, float y, float z) {
        position.x += x;
        position.y += y;
        position.z += z;
    }

    public void rotate(float x, float y, float z) {
        rotation.x += x;
        rotation.y += y;
        rotation.z += z;

        rotation.x = MathMisc.clamp(rotation.x,  -89, 89);
        rotation.y = (float) MathMisc.wrapAngle(rotation.y, -180, 180);
        rotation.z = (float) MathMisc.wrapAngle(rotation.z, -180, 180);
    }

    public void lookAtPosition(Vector4f targetPosition) {
        Vector3f direction = new Vector3f(targetPosition.x - position.x,
                targetPosition.y - position.y,
                targetPosition.z - position.z);
        direction.normalize();

        float pitch = (float) toDegrees(asin(-direction.y));
        float yaw = (float) toDegrees(atan2(direction.x, direction.z));

        rotation.x = pitch;
        rotation.y = 90 + yaw;
    }

    public Vector3f getCameraFront() {
        Vector3f direction = new Vector3f();
        direction.x = (float) (cos(toRadians(rotation.y)) * cos(toRadians(rotation.x)));
        direction.y = (float) sin(toRadians(rotation.x));
        direction.z = (float) (sin(toRadians(rotation.y)) * cos(toRadians(rotation.x)));

        return direction.normalize();
    }

    public Vector3f getRayDirection(){
        WindowVariables windowVariables = WindowVariables.getInstance();
        Vector2d mousePos = MouseHandler.getMousePosition();

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

    @Override
    public Object saveState(){
        return new Camera((Spatial3D)super.saveState(), fov, aspect, near, far);
    }

    @Override
    public void loadState(Object state){
        super.loadState(state);
        Camera camera = (Camera) state;
        this.fov = camera.fov;
        this.aspect = camera.aspect;
        this.near = camera.near;
        this.far = camera.far;
    }

}
