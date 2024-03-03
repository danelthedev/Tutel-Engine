package org.tuiasi.engine.renderer.renderable;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Renderable3D implements IRenderable{
    int VAO, VBO, EBO;

    FloatBuffer verticesBuffer;

    public Renderable3D(float[] vertices){
        ByteBuffer byteBuffer = BufferUtils.createByteBuffer(vertices.length * Float.BYTES);
        verticesBuffer = byteBuffer.asFloatBuffer();
        verticesBuffer.put(vertices);
        verticesBuffer.flip();

        init();
    }

    public Renderable3D(List<Vector3f> vertices){
        ByteBuffer byteBuffer = BufferUtils.createByteBuffer(vertices.size() * 3 * Float.BYTES);
        verticesBuffer = byteBuffer.asFloatBuffer();
        for(Vector3f vertex : vertices){
            verticesBuffer.put(vertex.x);
            verticesBuffer.put(vertex.y);
            verticesBuffer.put(vertex.z);
        }
        verticesBuffer.flip();

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

        // set the vertex attributes
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 3 * Float.BYTES, 0);
        GL20.glEnableVertexAttribArray(0);
    }

    @Override
    public void render() {
        glBindVertexArray(VAO);
        GL20.glDrawArrays(GL11.GL_TRIANGLES, 0, verticesBuffer.capacity() / 3);
    }

}
