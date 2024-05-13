package org.tuiasi.engine.renderer.renderable;

import lombok.Data;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.tuiasi.engine.global.nodes.EditorVisible;
import org.tuiasi.engine.global.nodes.spatial.Spatial3D;
import org.tuiasi.engine.renderer.camera.MainCamera;
import org.tuiasi.engine.renderer.material.Material;
import org.tuiasi.engine.renderer.shader.DrawMode;
import org.tuiasi.engine.renderer.shader.ShaderProgram;
import org.tuiasi.engine.renderer.shader.Uniform;
import org.tuiasi.engine.renderer.texture.Texture;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

// TODO: Switch individual sampler2D uniforms to a single array of samplers

@Data
public class Renderable3D extends Spatial3D implements IRenderable {
    // mesh data
    int VAO, VBO, EBO;
    FloatBuffer verticesBuffer;
    IntBuffer indicesBuffer;

    // material data
    @EditorVisible
    Material material;

    // shader data
    ShaderProgram shaderProgram;

    // draw mode
    @EditorVisible
    DrawMode drawMode = DrawMode.FILLED;

    public Renderable3D(float[] vertices, int[] indices, ShaderProgram shaderProgram, Material material, Spatial3D transform){
        ByteBuffer byteBuffer = BufferUtils.createByteBuffer(vertices.length * Float.BYTES);
        verticesBuffer = byteBuffer.asFloatBuffer();
        verticesBuffer.put(vertices);
        verticesBuffer.flip();

        ByteBuffer indicesByteBuffer = BufferUtils.createByteBuffer(indices.length * Integer.BYTES);
        indicesBuffer = indicesByteBuffer.asIntBuffer();
        indicesBuffer.put(indices);
        indicesBuffer.flip();

        this.shaderProgram = shaderProgram;

        this.material = material;

        initVertBuf();

        setRotation(transform.getRotation());
        setPosition(transform.getPosition());
        setScale(transform.getScale());

        if(!material.equals(new Material()))
            setMaterialUniforms();
    }

    // function that copies all fields from one renderable to another
    public void copy(Renderable3D renderable){
        this.VAO = renderable.getVAO();
        this.VBO = renderable.getVBO();
        this.EBO = renderable.getEBO();
        this.verticesBuffer = renderable.getVerticesBuffer();
        this.indicesBuffer = renderable.getIndicesBuffer();
        this.material = renderable.getMaterial();
        this.shaderProgram = renderable.getShaderProgram();
        setRotation(renderable.getRotation());
        setPosition(renderable.getPosition());
        setScale(renderable.getScale());
        this.drawMode = renderable.getDrawMode();
    }

    public void initVertBuf(){
        // create a vertex array object to store the vertex buffer object
        VAO = glGenVertexArrays();
        glBindVertexArray(VAO);

        // create a vertex buffer object to store the vertices
        VBO = GL15.glGenBuffers();
        GL15.glBindBuffer(GL_ARRAY_BUFFER, VBO);
        GL15.glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);

        // create element buffer object
        EBO = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, EBO);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

        // set the vertex attributes

        // position attribute
        GL20.glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0);
        GL20.glEnableVertexAttribArray(0);

        // normal attribute
        GL20.glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
        GL20.glEnableVertexAttribArray(1);

        // texture attribute
        GL20.glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);
        GL20.glEnableVertexAttribArray(2);

    }

    public void setUniform(Uniform<?> value){
        shaderProgram.setUniform(value);
    }

    private void setModelViewMatrix(){
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

    private void setMaterialUniforms(){
        if(material.getDiffuse() != null && !material.getDiffuse().getPathToTexture().isEmpty()) {
            material.getDiffuse().use();
            shaderProgram.setUniform(new Uniform<>("diffuseMap", material.getDiffuse().getTextureIndex()));
            shaderProgram.setUniform(new Uniform<>("hasDiffuse", true));
        }else {
            shaderProgram.setUniform(new Uniform<>("hasDiffuse", false));
        }

        if(material.getSpecular() != null && !material.getSpecular().getPathToTexture().isEmpty()) {
            material.getSpecular().use();
            shaderProgram.setUniform(new Uniform<>("specularMap", material.getSpecular().getTextureIndex()));
            shaderProgram.setUniform(new Uniform<>("hasSpecular", true));
        }else {
            shaderProgram.setUniform(new Uniform<>("hasSpecular", false));
        }

        setUniform(new Uniform<>("materialShininess", material.getShininess()));
    }

    @Override
    public void render() {
        shaderProgram.use();

        setModelViewMatrix();
        setMaterialUniforms();

        glBindVertexArray(VAO);

        if(drawMode == DrawMode.FILLED)
            GL20.glDrawElements(GL11.GL_TRIANGLES, indicesBuffer.capacity(), GL_UNSIGNED_INT, 0);
        else
            GL20.glDrawElements(GL11.GL_LINES, indicesBuffer.capacity(), GL_UNSIGNED_INT, 0);
    }

}
