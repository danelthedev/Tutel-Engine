package org.tuiasi.engine.renderer.camera;

import lombok.Data;
import org.joml.Vector3f;

@Data
public abstract class Camera {
    private float fov;
    private float aspect;
    private float near;
    private float far;
    private Vector3f position;
    private Vector3f rotation;

    public Camera(float fov, float aspect, float near, float far){
        this.fov = fov;
        this.aspect = aspect;
        this.near = near;
        this.far = far;
        this.position = new Vector3f(0, 0, 0);
        this.rotation = new Vector3f(0, 0, 0);
    }

    public Camera(float fov, float aspect, float near, float far, Vector3f position, Vector3f rotation) {
        this.fov = fov;
        this.aspect = aspect;
        this.near = near;
        this.far = far;
        this.position = position;
        this.rotation = rotation;
    }

}
