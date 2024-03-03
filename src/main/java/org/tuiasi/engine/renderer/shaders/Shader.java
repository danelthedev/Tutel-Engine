package org.tuiasi.engine.renderer.shaders;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static org.lwjgl.opengl.GL20.*;

@NoArgsConstructor @Getter @Setter
public class Shader {

    private String shaderCode;
    private int shaderID;

    public Shader(String pathToShader, int shaderType) {
        this.shaderCode = loadShaderCode(pathToShader);
        this.shaderID = createShader(shaderCode, shaderType);
    }

    private String loadShaderCode(String pathToShader) {
        File shaderFile = new File(pathToShader);
        StringBuilder shaderCode = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(shaderFile));
            String line;
            while ((line = reader.readLine()) != null) {
                shaderCode.append(line).append("\n");
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shaderCode.toString();
    }

    private int createShader(String shaderCode, int shaderType) {
        int shaderID = glCreateShader(shaderType);
        glShaderSource(shaderID, shaderCode);
        glCompileShader(shaderID);
        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == 0) {
            System.err.println("Error compiling shader code: " + glGetShaderInfoLog(shaderID, 1024));
            System.exit(1);
        }
        return shaderID;
    }

}
