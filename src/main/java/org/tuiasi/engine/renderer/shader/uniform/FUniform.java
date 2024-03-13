package org.tuiasi.engine.renderer.shader.uniform;

import lombok.Getter;
import lombok.Setter;

import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL20.*;

@Getter @Setter
public class FUniform extends Uniform<Float>{

    public FUniform(String name, Float value) {
        super(name);
        setValue(value);
        if (getLocation() == -1) {
            System.err.println("Warning: Uniform variable '" + name + "' not found in the shader program.");
        }
        setValue(value);
    }

    public FUniform(String name, Float value, int shaderProgram) {
        super(name);
        setLocation(shaderProgram);
        setValue(value);
        if (getLocation() == -1) {
            System.err.println("Warning: Uniform variable '" + name + "' not found in the shader program.");
        }
        setValue(value);
    }


    @Override
    public void setUniform(Float value) {
        setValue(value);
    }

    @Override
    public void use() {
        glUniform1f(getLocation(), getValue());
    }
}
