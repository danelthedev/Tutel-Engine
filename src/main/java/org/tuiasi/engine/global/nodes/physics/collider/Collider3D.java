package org.tuiasi.engine.global.nodes.physics.collider;

import lombok.Getter;
import org.joml.Vector3f;
import org.tuiasi.engine.global.nodes.EditorVisible;
import org.tuiasi.engine.global.nodes.spatial.Spatial3D;
import org.tuiasi.engine.renderer.material.Material;
import org.tuiasi.engine.renderer.mesh.Mesh;
import org.tuiasi.engine.renderer.primitives.Cube;
import org.tuiasi.engine.renderer.renderable.Renderable3D;
import org.tuiasi.engine.renderer.shader.DrawMode;
import org.tuiasi.engine.renderer.shader.Shader;
import org.tuiasi.engine.renderer.shader.ShaderProgram;

import java.util.List;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class Collider3D extends Spatial3D implements Collider {

    @Getter
    private Renderable3D representation;
//    @EditorVisible
    private Boolean enabled = Boolean.TRUE;

    public Collider3D(){
        Mesh cubeMesh = new Mesh("", Cube.vertexData, Cube.indexData);
        representation = new Renderable3D(
                cubeMesh,
                new ShaderProgram(new Shader("C:\\Users\\Danel\\IdeaProjects\\licenta\\src\\main\\resources\\shaders\\default_vertex.vert", GL_VERTEX_SHADER), new Shader("C:\\Users\\Danel\\IdeaProjects\\licenta\\src\\main\\resources\\shaders\\solid_color_fragment.frag", GL_FRAGMENT_SHADER)),
                new Material(),
                new Spatial3D(getPosition(), getRotation(), new Vector3f(1f,1f,1f))
        );
        representation.getMesh().setDrawMode(DrawMode.WIREFRAME);
    }

    public void setPosition(Vector3f newPosition){
        super.setPosition(newPosition);
//        position = newPosition;
        representation.setPosition(getPosition());
    }

    public void setRotation(Vector3f newRotation){
        rotation = newRotation;
        representation.setRotation(getRotation());
    }

    public void setScale(Vector3f newScale){
        scale = newScale;
        representation.setScale(getScale());
    }

    @Override
    public boolean isColliding(Collider other) {
        return false;
    }

    @Override
    public List<Collider> getColliding() {
        return null;
    }

    @Override
    public boolean isOnFloor() {
        return false;
    }

    @Override
    public boolean isOnWall() {
        return false;
    }

    @Override
    public boolean isOnCeiling() {
        return false;
    }
}
