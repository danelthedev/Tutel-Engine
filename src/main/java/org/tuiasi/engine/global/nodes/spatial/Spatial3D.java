package org.tuiasi.engine.global.nodes.spatial;

import lombok.Data;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.tuiasi.engine.global.nodes.EditorVisible;
import org.tuiasi.engine.renderer.camera.MainCamera;

@Data
public class Spatial3D extends Spatial{

    @EditorVisible
    private Vector3f position;
    @EditorVisible
    private Vector3f rotation;
    @EditorVisible
    private Vector3f scale;

    public Spatial3D(Vector3f position, Vector3f rotation, Vector3f scale){
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Spatial3D(){
        this.position = new Vector3f(0,0,0);
        this.rotation = new Vector3f(0,0,0);
        this.scale = new Vector3f(1,1,1);
    }


    public void translate(Vector3f translation){
        this.position.add(translation);
    }

    public void rotate(Vector3f rotation){
        this.rotation.add(rotation);
    }

    public void scale(Vector3f scale){
        this.scale.add(scale);
    }


    public boolean isMouseHovered(){
        // test raycasting
        Vector4f cameraOrigin = MainCamera.getInstance().getPosition();
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
}
