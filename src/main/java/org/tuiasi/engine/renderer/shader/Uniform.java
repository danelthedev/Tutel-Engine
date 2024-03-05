package org.tuiasi.engine.renderer.shader;

import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL20.*;

@Data @AllArgsConstructor
public class Uniform {
    private String name;
    private int location;
    List<Float> value;

    public Uniform(String name) {
        this.name = name;
        this.location = glGetUniformLocation(glGetInteger(GL_CURRENT_PROGRAM), name);
        if (location == -1) {
            System.err.println("Warning: Uniform variable '" + name + "' not found in the shader program.");
        }

        value = new ArrayList<>();
    }

    public Uniform(String name, List<Float> value){
        this.name = name;
        this.value = value;
        this.location = glGetUniformLocation(glGetInteger(GL_CURRENT_PROGRAM), name);
        if (location == -1) {
            System.err.println("Warning: Uniform variable '" + name + "' not found in the shader program.");
        }

        setUniform(value);
    }

    public void use(){
        int size = value.size();
        switch(size){
            case 1:
                glUniform1f(location, value.get(0));
                break;
            case 2:
                glUniform2f(location, value.get(0), value.get(1));
                break;
            case 3:
                glUniform3f(location, value.get(0), value.get(1), value.get(2));
                break;
            case 4:
                glUniform4f(location, value.get(0), value.get(1), value.get(2), value.get(3));
                break;
            default:
                System.err.println("Error: Unsupported uniform type.");
        }
    }

    public void setUniform(List<Float> value) {
        this.value = value;
    }

    public void setUniform(Float[] value){
        this.value = new ArrayList<>();
        this.value.addAll(Arrays.asList(value));
    }
}