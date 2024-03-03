package org.tuiasi.engine.renderer.renderable;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Renderable3D implements IRenderable{
    int VAO, VBO, EBO;

    FloatBuffer verticesBuffer;
    IntBuffer indicesBuffer;

    public Renderable3D(float[] vertices, int[] indices){
        ByteBuffer byteBuffer = BufferUtils.createByteBuffer(vertices.length * Float.BYTES);
        verticesBuffer = byteBuffer.asFloatBuffer();
        verticesBuffer.put(vertices);
        verticesBuffer.flip();

        ByteBuffer indicesByteBuffer = BufferUtils.createByteBuffer(indices.length * Integer.BYTES);
        indicesBuffer = indicesByteBuffer.asIntBuffer();
        indicesBuffer.put(indices);
        indicesBuffer.flip();

        init();
    }

    public Renderable3D(List<Vector3f> vertices, List<Integer> indices){
        ByteBuffer byteBuffer = BufferUtils.createByteBuffer(vertices.size() * 3 * Float.BYTES);
        verticesBuffer = byteBuffer.asFloatBuffer();
        for(Vector3f vertex : vertices){
            verticesBuffer.put(vertex.x);
            verticesBuffer.put(vertex.y);
            verticesBuffer.put(vertex.z);
        }
        verticesBuffer.flip();

        ByteBuffer indicesByteBuffer = BufferUtils.createByteBuffer(indices.size() * Integer.BYTES);
        indicesBuffer = indicesByteBuffer.asIntBuffer();
        for(Integer index : indices){
            indicesBuffer.put(index);
        }
        indicesBuffer.flip();

        init();
    }

    public void init(){
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
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 3 * Float.BYTES, 0);
        GL20.glEnableVertexAttribArray(0);
    }

    @Override
    public void render() {
        glBindVertexArray(VAO);
//        GL20.glDrawArrays(GL11.GL_TRIANGLES, 0, verticesBuffer.capacity() / 3);
//        System.out.println(EBO);
//        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        GL20.glDrawElements(GL11.GL_TRIANGLES, indicesBuffer.capacity(), GL_UNSIGNED_INT, 0);
    }

}
