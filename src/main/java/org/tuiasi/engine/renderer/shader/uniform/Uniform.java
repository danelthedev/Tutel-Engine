package org.tuiasi.engine.renderer.shader.uniform;

import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL20.*;

@Data
public abstract class Uniform<T> {
    private String name;
    private T value;

    private int location, shaderProgram;

    public Uniform(String name) {
        this.name = name;
    }

    public void setLocation(int shaderProgram) {
        this.shaderProgram = shaderProgram;
        location = glGetUniformLocation(shaderProgram, name);
    }

    public abstract void setUniform(T value);

    public abstract void use();
}