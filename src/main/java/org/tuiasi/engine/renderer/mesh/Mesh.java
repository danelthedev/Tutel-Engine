package org.tuiasi.engine.renderer.mesh;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.tuiasi.engine.global.nodes.EditorVisible;
import org.tuiasi.engine.logic.AppLogic;
import org.tuiasi.engine.renderer.modelLoader.Model;
import org.tuiasi.engine.renderer.modelLoader.ModelLoader;
import org.tuiasi.engine.renderer.primitives.Cube;
import org.tuiasi.engine.renderer.primitives.Plane;
import org.tuiasi.engine.renderer.shader.DrawMode;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

@Data
public class Mesh{

//    @EditorVisible
//    @JsonProperty
    String path="";
    int VAO, VBO, EBO;
    FloatBuffer verticesBuffer;
    IntBuffer indicesBuffer;

//    @EditorVisible
    DrawMode drawMode = DrawMode.FILLED;

    public Mesh(){
        this("", Cube.vertexData, Cube.indexData);
    }

    public Mesh(String path){
        this("", Cube.vertexData, Cube.indexData);
        this.path = path;

        Model model = ModelLoader.load(AppLogic.getWorkingDirectory() + "\\assets\\" + path);

        verticesBuffer = model.getMesh().getVerticesBuffer();
        indicesBuffer = model.getMesh().getIndicesBuffer();

        initVertBuf();
    }


    public Mesh(String path, float[] vertices, int[] indices){
        this.path = path;

        ByteBuffer byteBuffer = BufferUtils.createByteBuffer(vertices.length * Float.BYTES);
        verticesBuffer = byteBuffer.asFloatBuffer();
        verticesBuffer.put(vertices);
        verticesBuffer.flip();

        ByteBuffer indicesByteBuffer = BufferUtils.createByteBuffer(indices.length * Integer.BYTES);
        indicesBuffer = indicesByteBuffer.asIntBuffer();
        indicesBuffer.put(indices);
        indicesBuffer.flip();

        initVertBuf();
    }

    private void initVertBuf(){
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

    public void render() {

        glBindVertexArray(VAO);

        if (drawMode == DrawMode.FILLED)
            GL20.glDrawElements(GL11.GL_TRIANGLES, indicesBuffer.capacity(), GL_UNSIGNED_INT, 0);
        else
        if (drawMode == DrawMode.AXIS){
            glLineWidth(2.0f);
            GL20.glDrawElements(GL11.GL_LINES, indicesBuffer.capacity(), GL_UNSIGNED_INT, 0);
        }
        else
        if (drawMode == DrawMode.WIREFRAME){
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
            GL20.glDrawElements(GL11.GL_TRIANGLES, indicesBuffer.capacity(), GL_UNSIGNED_INT, 0);
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        }

    }
}
