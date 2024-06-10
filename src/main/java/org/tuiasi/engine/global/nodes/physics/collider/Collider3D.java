package org.tuiasi.engine.global.nodes.physics.collider;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;
import org.tuiasi.engine.global.nodes.EditorVisible;
import org.tuiasi.engine.global.nodes.Node;
import org.tuiasi.engine.global.nodes.physics.body.IBody;
import org.tuiasi.engine.global.nodes.spatial.Spatial3D;
import org.tuiasi.engine.logic.AppLogic;
import org.tuiasi.engine.renderer.Renderer;
import org.tuiasi.engine.renderer.material.Material;
import org.tuiasi.engine.renderer.mesh.Mesh;
import org.tuiasi.engine.renderer.primitives.Cube;
import org.tuiasi.engine.renderer.renderable.Renderable3D;
import org.tuiasi.engine.renderer.shader.DefaultShaders;
import org.tuiasi.engine.renderer.shader.DrawMode;
import org.tuiasi.engine.renderer.shader.Shader;
import org.tuiasi.engine.renderer.shader.ShaderProgram;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

@Data
@Getter @Setter
public class Collider3D extends Spatial3D {

    private Renderable3D representation;
    @EditorVisible
    @JsonProperty
    private Boolean enabled = Boolean.TRUE;

    public Collider3D(){

        Mesh cubeMesh = new Mesh("", Cube.vertexData, Cube.indexData);
        representation = new Renderable3D(
                cubeMesh,
                new ShaderProgram(
                        new Shader(DefaultShaders.getVertexShader(), GL_VERTEX_SHADER),
                        new Shader(DefaultShaders.getSolidColorFragmentShader(), GL_FRAGMENT_SHADER)),
                new Material(),
                new Spatial3D(getPosition(), getRotation(), new Vector3f(1f,1f,1f))
        );
        representation.getMesh().setDrawMode(DrawMode.WIREFRAME);
    }

    public Collider3D(Vector3f position, Vector3f rotation, Vector3f scale){
        super(position, rotation, scale);
        Mesh cubeMesh = new Mesh("", Cube.vertexData, Cube.indexData);
        representation = new Renderable3D(
                cubeMesh,
                new ShaderProgram(
                        new Shader(DefaultShaders.getVertexShader(), GL_VERTEX_SHADER),
                        new Shader(DefaultShaders.getSolidColorFragmentShader(), GL_FRAGMENT_SHADER)),
                new Material(),
                new Spatial3D(getPosition(), getRotation(), getScale())
        );
        representation.getMesh().setDrawMode(DrawMode.WIREFRAME);
    }

    public Collider3D(Vector3f position, Vector3f rotation, Vector3f scale, Renderable3D representation, Boolean enabled){
        super(position, rotation, scale);
        this.representation = representation;
        this.enabled = enabled;
    }

    public void addToRenderer(){
        Renderer.addRenderable(representation);
    }

    public void setPosition(Vector3f newPosition){
        super.setPosition(newPosition);
        representation.setPosition(getPosition());
    }

    public void setRotation(Vector3f newRotation){
        super.setRotation(newRotation);
        representation.setRotation(getRotation());
    }

    public void setScale(Vector3f newScale){
        super.setScale(newScale);
        representation.setScale(getScale());
    }

    public boolean isColliding(Collider3D other) {
        // Simple collision detection based on position and scale
        Vector3f distance = other.getPosition().sub(this.getPosition(), new Vector3f());
        Vector3f totalScale = this.getScale().add(other.getScale(), new Vector3f());
        return distance.lengthSquared() < totalScale.lengthSquared();
    }

    public Collider3D isColliding(Vector3f position) {
        Vector3f minA = new Vector3f(position).sub(this.getScale().div(2f, new Vector3f()));
        Vector3f maxA = new Vector3f(position).add(this.getScale().div(2f, new Vector3f()));

        // Iterate over all physics nodes
        for (Node<?> node : AppLogic.getPhysicsNodes()) {
            if (node.getValue() instanceof IBody){
                Collider3D other = ((IBody)node.getValue()).getCollider();
                if(other == this)
                    continue;

                Vector3f minB = new Vector3f(other.getPosition()).sub(other.getScale().div(2f, new Vector3f()));
                Vector3f maxB = new Vector3f(other.getPosition()).add(other.getScale().div(2f, new Vector3f()));

                // Check if the dimensions of the two colliders overlap
                if(minA.x <= maxB.x && maxA.x >= minB.x &&
                        minA.y <= maxB.y && maxA.y >= minB.y &&
                        minA.z <= maxB.z && maxA.z >= minB.z) {
                    return other;
                }
            }
        }
        // If no collision was detected, return false
        return null;
    }

    public List<Collider3D> getColliding() {
        List<Collider3D> colliding = new ArrayList<>();
        for (Node<?> node : AppLogic.getPhysicsNodes()) {
            if (node.getValue() instanceof Collider3D) {
                Collider3D other = (Collider3D) node.getValue();
                if (this != other && this.isColliding(other)) {
                    colliding.add(other);
                }
            }
        }
        return colliding;
    }

    public boolean isOnFloor() {
        return false;
    }

    public boolean isOnWall() {
        return false;
    }

    public boolean isOnCeiling() {
        return false;
    }

    @Override
    public Object saveState(){
        return new Collider3D(getPosition(), getRotation(), getScale(), representation, enabled);
    }

    @Override
    public void loadState(Object state){
        Collider3D newState = (Collider3D) state;
        setPosition(newState.getPosition());
        setRotation(newState.getRotation());
        setScale(newState.getScale());
        enabled = newState.getEnabled();
    }
}
