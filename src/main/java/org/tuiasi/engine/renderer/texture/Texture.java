package org.tuiasi.engine.renderer.texture;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL14.GL_MIRRORED_REPEAT;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

@Data @AllArgsConstructor
public class Texture implements ITexture {
    private IntBuffer width, height, nrChannels;
    private String pathToTexture, format;
    private ByteBuffer image;
    private int textureID;

    public Texture(){
        this.pathToTexture = "";
        this.format = "";
        this.width = BufferUtils.createIntBuffer(1);
        this.height = BufferUtils.createIntBuffer(1);
        this.nrChannels = BufferUtils.createIntBuffer(1);
        this.textureID = glGenTextures();
    }

    public Texture(String pathToTexture){
        this.pathToTexture = pathToTexture;
        this.format = pathToTexture.split("\\.")[1];

        width = BufferUtils.createIntBuffer(1);
        height = BufferUtils.createIntBuffer(1);
        nrChannels = BufferUtils.createIntBuffer(1);

        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        try {
            image = stbi_load(pathToTexture, width, height, nrChannels, 0);
        }   catch (Exception e){
            e.printStackTrace();
        }

        if (image != null) {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
            glGenerateMipmap(GL_TEXTURE_2D);
        }
        else{
            System.out.println("Failed to load texture at path: " + pathToTexture);
        }

        stbi_image_free(image);
    }

    public void use(){
        if(!pathToTexture.isEmpty()) {
            glBindTexture(GL_TEXTURE_2D, textureID);
//            System.out.println("Using texture " + textureID + " at path: " + pathToTexture);
        }
        else
            glBindTexture(GL_TEXTURE_2D, 0);
    }

}
