package org.tuiasi.engine.renderer.shaders;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static org.lwjgl.opengl.GL20.*;

@NoArgsConstructor @Getter @Setter
public class ShaderProgram {
    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    public ShaderProgram(int vertexShader, int fragmentShader) {
        this.vertexShaderID = vertexShader;
        this.fragmentShaderID = fragmentShader;

        this.programID = glCreateProgram();
        glAttachShader(programID, vertexShader);
        glAttachShader(programID, fragmentShader);

    }

    public void link() {
        glLinkProgram(programID);
        if (glGetProgrami(programID, GL_LINK_STATUS) == 0) {
            System.err.println("Error linking shader program: " + glGetProgramInfoLog(programID, 1024));
            System.exit(1);
        }
        glValidateProgram(programID);
        if (glGetProgrami(programID, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Error validating shader program: " + glGetProgramInfoLog(programID, 1024));
            System.exit(1);
        }

        glDeleteShader(this.vertexShaderID);
        glDeleteShader(this.fragmentShaderID);
    }

    public static void useProgram(int programID) {
        glUseProgram(programID);
    }

}
