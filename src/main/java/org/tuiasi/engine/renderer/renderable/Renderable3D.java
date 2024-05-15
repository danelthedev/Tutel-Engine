package org.tuiasi.engine.renderer.renderable;

import lombok.Data;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.tuiasi.engine.global.nodes.EditorVisible;
import org.tuiasi.engine.global.nodes.spatial.Spatial3D;
import org.tuiasi.engine.renderer.Renderer;
import org.tuiasi.engine.renderer.camera.MainCamera;
import org.tuiasi.engine.renderer.material.Material;
import org.tuiasi.engine.renderer.mesh.Mesh;
import org.tuiasi.engine.renderer.shader.DrawMode;
import org.tuiasi.engine.renderer.shader.Shader;
import org.tuiasi.engine.renderer.shader.ShaderProgram;
import org.tuiasi.engine.renderer.shader.Uniform;
import org.tuiasi.engine.renderer.texture.Texture;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

// TODO: Switch individual sampler2D uniforms to a single array of samplers

@Data
public class Renderable3D extends Spatial3D implements IRenderable {
    // material data
    @EditorVisible
    Material material;

    // shader data
    ShaderProgram shaderProgram;

    // mesh
    @EditorVisible
    Mesh mesh;
    String previousMeshPath = "";

    public Renderable3D() {
        this(new Mesh(),
                new ShaderProgram(new Shader("C:\\Users\\Danel\\IdeaProjects\\licenta\\src\\main\\resources\\shaders\\default_vertex.vert", GL_VERTEX_SHADER),
                        new Shader("C:\\Users\\Danel\\IdeaProjects\\licenta\\src\\main\\resources\\shaders\\default_fragment.frag", GL_FRAGMENT_SHADER)),
                new Material(new Texture(0), new Texture(1), 32.0f),
                new Spatial3D());

        Renderer.addRenderable(this);
    }

    public Renderable3D(Mesh mesh, ShaderProgram shaderProgram, Material material, Spatial3D transform) {
        this.mesh = mesh;
        previousMeshPath = mesh.getPath();

        this.shaderProgram = shaderProgram;
        this.material = material;

        setRotation(transform.getRotation());
        setPosition(transform.getPosition());
        setScale(transform.getScale());

        if (!material.equals(new Material()))
            setMaterialUniforms();

        Renderer.addRenderable(this);
    }

    // function that copies all fields from one renderable to another
    public void copy(Renderable3D renderable) {
        this.mesh = renderable.getMesh();
        this.material = renderable.getMaterial();
        this.shaderProgram = renderable.getShaderProgram();
        setRotation(renderable.getRotation());
        setPosition(renderable.getPosition());
        setScale(renderable.getScale());
    }

    public void setUniform(Uniform<?> value) {
        shaderProgram.setUniform(value);
    }

    private void setModelViewMatrix() {
        MainCamera camera = MainCamera.getInstance();

        Matrix4f projectionMatrix = new Matrix4f();
        projectionMatrix.identity();
        projectionMatrix.perspective(camera.getFov(), camera.getAspect(), camera.getNear(), camera.getFar());

        Matrix4f modelMatrix = new Matrix4f();
        modelMatrix.identity();
        modelMatrix.translate(getPosition());
        modelMatrix.rotateX((float) Math.toRadians(getRotation().x));
        modelMatrix.rotateY((float) Math.toRadians(getRotation().y));
        modelMatrix.rotateZ((float) Math.toRadians(getRotation().z));
        modelMatrix.scale(getScale());

        Matrix4f viewMatrix = camera.getViewMatrix();

        setUniform(new Uniform<>("model", modelMatrix));
        setUniform(new Uniform<>("view", viewMatrix));
        setUniform(new Uniform<>("projection", projectionMatrix));
    }

    private void setMaterialUniforms() {
        if (material.getDiffuse() != null && !material.getDiffuse().getPath().isEmpty()) {
            material.getDiffuse().use();
            shaderProgram.setUniform(new Uniform<>("diffuseMap", material.getDiffuse().getTextureIndex()));
            shaderProgram.setUniform(new Uniform<>("hasDiffuse", true));
        } else {
            shaderProgram.setUniform(new Uniform<>("hasDiffuse", false));
        }

        if (material.getSpecular() != null && !material.getSpecular().getPath().isEmpty()) {
            material.getSpecular().use();
            shaderProgram.setUniform(new Uniform<>("specularMap", material.getSpecular().getTextureIndex()));
            shaderProgram.setUniform(new Uniform<>("hasSpecular", true));
        } else {
            shaderProgram.setUniform(new Uniform<>("hasSpecular", false));
        }

        setUniform(new Uniform<>("materialShininess", material.getShininess()));
    }

    @Override
    public void render() {
        if(!previousMeshPath.equals(mesh.getPath())) {
            previousMeshPath = mesh.getPath();
            mesh = new Mesh(mesh.getPath());
        }

        shaderProgram.use();

        setModelViewMatrix();
        setMaterialUniforms();

        if (mesh == null) {

        }
        else{
            mesh.render();
        }

    }

}
