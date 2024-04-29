package org.tuiasi.engine.renderer.renderable;

import lombok.Data;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
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
public class Renderable3D implements IRenderable{
    // mesh data
    int VAO, VBO, EBO;
    FloatBuffer verticesBuffer;
    IntBuffer indicesBuffer;

    // texture data
    Texture[] textures;
    boolean hasTexture;

    // material data
    Material material;

    // shader data
    ShaderProgram shaderProgram;

    // position and rotation
    Spatial3D transform;

    // draw mode
    DrawMode drawMode = DrawMode.FILLED;

    public Renderable3D(){

    }

    public Renderable3D(float[] vertices, int[] indices, ShaderProgram shaderProgram, Texture[] texture, Material material){
        ByteBuffer byteBuffer = BufferUtils.createByteBuffer(vertices.length * Float.BYTES);
        verticesBuffer = byteBuffer.asFloatBuffer();
        verticesBuffer.put(vertices);
        verticesBuffer.flip();

        ByteBuffer indicesByteBuffer = BufferUtils.createByteBuffer(indices.length * Integer.BYTES);
        indicesBuffer = indicesByteBuffer.asIntBuffer();
        indicesBuffer.put(indices);
        indicesBuffer.flip();

        this.shaderProgram = shaderProgram;

        this.textures = texture;
        if(texture != null && !texture[0].getPathToTexture().isEmpty())
            hasTexture = true;

        this.material = material;

        initVertBuf();

        transform = new Spatial3D();

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
        this.textures = renderable.getTextures();
        this.hasTexture = renderable.isHasTexture();
        this.material = renderable.getMaterial();
        this.shaderProgram = renderable.getShaderProgram();
        this.transform = renderable.getTransform();
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
        GL20.glVertexAttribPointer(0, 3, GL_FLOAT, false, 11 * Float.BYTES, 0);
        GL20.glEnableVertexAttribArray(0);

        // color attribute
        GL20.glVertexAttribPointer(1, 3, GL_FLOAT, false, 11 * Float.BYTES, 3 * Float.BYTES);
        GL20.glEnableVertexAttribArray(1);

        // normal attribute
        GL20.glVertexAttribPointer(2, 3, GL_FLOAT, false, 11 * Float.BYTES, 6 * Float.BYTES);
        GL20.glEnableVertexAttribArray(2);

        // texture attribute
        GL20.glVertexAttribPointer(3, 2, GL_FLOAT, false, 11 * Float.BYTES, 9 * Float.BYTES);
        GL20.glEnableVertexAttribArray(3);

    }

    public void setUniform(Uniform<?> value){
        shaderProgram.setUniform(value);
    }

    public void translate(Vector3f direction){
        transform.translate(direction);
    }

    public void setPosition(Vector3f position){
        transform.setPosition(position);
    }

    public void rotate(Vector3f rotation){
        transform.rotate(rotation);
    }

    public void setRotation(Vector3f rotation){
        transform.setRotation(rotation);
    }

    public void setTexture(Texture texture, int textureIndex){
        this.textures[textureIndex] = texture;
    }

    private void setModelViewMatrix(){
        MainCamera camera = MainCamera.getInstance();

        Matrix4f projectionMatrix = new Matrix4f();
        projectionMatrix.identity();
        projectionMatrix.perspective(camera.getFov(), camera.getAspect(), camera.getNear(), camera.getFar());

        Matrix4f modelMatrix = new Matrix4f();
        modelMatrix.identity();
        modelMatrix.translate(transform.getPosition());
        modelMatrix.rotateX((float) Math.toRadians(transform.getRotation().x));
        modelMatrix.rotateY((float) Math.toRadians(transform.getRotation().y));
        modelMatrix.rotateZ((float) Math.toRadians(transform.getRotation().z));
        modelMatrix.scale(transform.getScale());

        Matrix4f viewMatrix = camera.getViewMatrix();

        setUniform(new Uniform<>("modelViewProjectionMatrix", projectionMatrix.mul(viewMatrix).mul(modelMatrix)));
        setUniform(new Uniform<>("normalMatrix", modelMatrix.invert().transpose()));
        System.out.println(modelMatrix.invert().transpose());
    }

    private void setMaterialUniforms(){
        material.getDiffuse().use();
        shaderProgram.setUniform(new Uniform<>("diffuseMap", material.getDiffuse().getTextureIndex()));

        material.getSpecular().use();
        shaderProgram.setUniform(new Uniform<>("specularMap", material.getSpecular().getTextureIndex()));

        setUniform(new Uniform<>("materialShininess", material.getShininess()));
    }

    @Override
    public void render() {
        shaderProgram.use();

        setModelViewMatrix();

        glBindVertexArray(VAO);

        if(drawMode == DrawMode.FILLED)
            GL20.glDrawElements(GL11.GL_TRIANGLES, indicesBuffer.capacity(), GL_UNSIGNED_INT, 0);
        else
            GL20.glDrawElements(GL11.GL_LINES, indicesBuffer.capacity(), GL_UNSIGNED_INT, 0);
    }

}
