package org.tuiasi.engine.renderer.texture;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.lwjgl.BufferUtils;
import org.tuiasi.engine.global.nodes.EditorVisible;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

@Data @AllArgsConstructor
public class Texture implements ITexture{
    private IntBuffer width, height, nrChannels;
    @EditorVisible
    @JsonProperty
    private String path;
    private String previousPath;
    private String format;
    private ByteBuffer image;
    private Integer textureID;
    @JsonProperty
    private Integer textureIndex;

    public Texture(){
        this.path = "";
        this.previousPath = "";
        this.format = "";
        this.width = BufferUtils.createIntBuffer(1);
        this.height = BufferUtils.createIntBuffer(1);
        this.nrChannels = BufferUtils.createIntBuffer(1);
        this.textureID = glGenTextures();
        this.textureIndex = 0;
    }

    public Texture(int textureIndex){
        this();
        this.textureIndex = textureIndex;
    }

    public Texture(String path, int textureIndex){
        this();
        this.path = path;
        this.previousPath = path;
        createTexture(path, textureIndex);
    }

    private void createTexture(String path, int textureIndex){
        if(path.split("\\.").length < 2){
            this.path = "";
            this.format = "";
            this.width = BufferUtils.createIntBuffer(1);
            this.height = BufferUtils.createIntBuffer(1);
            this.nrChannels = BufferUtils.createIntBuffer(1);
            this.textureID = glGenTextures();
            return;
        }

        this.format = path.split("\\.")[1];

        width = BufferUtils.createIntBuffer(1);
        height = BufferUtils.createIntBuffer(1);
        nrChannels = BufferUtils.createIntBuffer(1);

        this.textureIndex = textureIndex;
        glActiveTexture(GL_TEXTURE0 + textureIndex);
        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        try {
            image = stbi_load(path, width, height, nrChannels, 0);
        }   catch (Exception e){
            e.printStackTrace();
        }

        if (image != null) {
            // check if the image has an alpha channel
            if (nrChannels.get(0) == 4)
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            else if (nrChannels.get(0) == 3)
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
            glGenerateMipmap(GL_TEXTURE_2D);
        }
        else{
            System.out.println("Failed to load texture at path: " + path);
        }

        stbi_image_free(image);
    }

    public void use(){
        if(!previousPath.equals(path)){
            createTexture(path, textureIndex);
            previousPath = path;
        }

        if(!path.isEmpty()) {
            glActiveTexture(GL_TEXTURE0 + textureIndex);
            glBindTexture(GL_TEXTURE_2D, textureID);
        }
        else
            glBindTexture(GL_TEXTURE_2D, 0);
    }

}
