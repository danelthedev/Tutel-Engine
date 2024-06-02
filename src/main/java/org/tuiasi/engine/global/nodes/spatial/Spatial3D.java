package org.tuiasi.engine.global.nodes.spatial;

import lombok.Data;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.tuiasi.engine.global.nodes.EditorVisible;
import org.tuiasi.engine.renderer.camera.MainCamera;

import java.util.ArrayList;
import java.util.List;

@Data
public class Spatial3D extends Spatial {
    @EditorVisible
    protected Vector3f position;
    @EditorVisible
    protected Vector3f rotation;
    @EditorVisible
    protected Vector3f scale;

    private Spatial3D parent;
    private List<Spatial3D> children;

    public Spatial3D(Vector3f position, Vector3f rotation, Vector3f scale){
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        children = new ArrayList<>();
    }

    public Spatial3D(){
        this.position = new Vector3f(0,0,0);
        this.rotation = new Vector3f(0,0,0);
        this.scale = new Vector3f(1,1,1);
        children = new ArrayList<>();
    }

    public void addChild(Spatial3D child){
        children.add(child);
        child.setParent(this);
    }

    public void setPosition(Vector3f newPosition) {
        Vector3f delta = new Vector3f(newPosition).sub(position);
        position = newPosition;
        for (Spatial3D child : children) {
            Vector3f childNewPosition = new Vector3f(child.getPosition()).add(delta);
            child.setPosition(childNewPosition);
        }
    }

    public void translate(Vector3f translation){
        this.position.add(translation);
        for (Spatial3D child : children) {
            child.translate(translation);
        }
    }

    public void setRotation(Vector3f newRotation) {
        Vector3f deltaRotation = new Vector3f(newRotation).sub(rotation);
        rotation = newRotation;
        for (Spatial3D child : children) {
            Vector3f childNewRotation = new Vector3f(child.getRotation()).add(deltaRotation);
            child.setRotation(childNewRotation);
        }
    }

    public void rotate(Vector3f rotation){
        this.rotation.add(rotation);
        for (Spatial3D child : children) {
            child.rotate(rotation);
        }
    }

    public void setScale(Vector3f newScale) {
        Vector3f deltaScale = new Vector3f(newScale).div(scale);
        scale = newScale;
        for (Spatial3D child : children) {
            Vector3f childNewScale = new Vector3f(child.getScale()).mul(deltaScale);
            child.setScale(childNewScale);
        }
    }

    public void scale(Vector3f scaleChange){
        this.scale.mul(scaleChange);
        for (Spatial3D child : children) {
            child.scale(scaleChange);
        }
    }

    public boolean isMouseHovered(){
        // test raycasting
        Vector3f cameraOrigin = MainCamera.getInstance().getPosition();
        Vector3f rayOrigin = new Vector3f(cameraOrigin.x, cameraOrigin.y, cameraOrigin.z);
        Vector3f rayDirection = MainCamera.getInstance().getRayDirection();

        // check if the ray cast from the origin in the direction goes through or near the origin of the world

        Vector3f sphereOrigin = position;
        float sphereRadius = .2f;

        // Calculate the vector from the ray origin to the sphere origin
        Vector3f oc = new Vector3f(sphereOrigin).sub(rayOrigin);

        // Calculate the projection of this vector onto the ray direction
        float t = oc.dot(rayDirection);

        if (t < 0) {
            // The ray is pointing away from the sphere
            return false;
        } else {
            // Calculate the squared length of the vector from the sphere origin to the point on the ray at the projection distance
            Vector3f closestPointOnRay = new Vector3f(rayDirection).mul(t).add(rayOrigin);
            float squaredDistance = new Vector3f(sphereOrigin).sub(closestPointOnRay).lengthSquared();

            if (squaredDistance > sphereRadius * sphereRadius) {
                // The ray misses the sphere
                return false;
            } else {
                // The ray intersects the sphere
                return true;
            }
        }
    }

    @Override
    public Object saveState() {
        return new Spatial3D(position, rotation, scale);
    }

    @Override
    public void loadState(Object state) {
        Spatial3D newState = (Spatial3D) state;
        position = newState.getPosition();
        rotation = newState.getRotation();
        scale = newState.getScale();
    }
}
