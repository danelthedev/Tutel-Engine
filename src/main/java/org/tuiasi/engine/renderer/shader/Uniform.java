package org.tuiasi.engine.renderer.shader;

import lombok.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL20.*;

@Data
public class Uniform<T> {
    private String name;
    private T value;

    private int location, shaderProgram;

    public Uniform(String name) {
        this.name = name;
    }

    public Uniform(String name, T Value) {
        this.name = name;
        this.value = Value;
    }

    public Uniform(String name, T Value, int shaderProgram) {
        this.name = name;
        this.value = Value;
        setLocation(shaderProgram);
    }

    public void setLocation(int shaderProgram) {
        this.shaderProgram = shaderProgram;
        location = glGetUniformLocation(shaderProgram, name);
        if (location == -1) {
            System.err.println("Warning: Uniform variable '" + name + "' not found in the shader program.");
        }
    }

    public void setValue(Object value) {
        this.value = (T) value;
    }

    public void use(){
        if(value instanceof Float)
            glUniform1f(location, (Float) value);
        else if(value instanceof Integer)
            glUniform1i(location, (Integer) value);
        else if(value instanceof Boolean)
            glUniform1i(location, (Boolean) value ? 1 : 0);

        else if(value instanceof Vector2f)
            glUniform2f(location, ((Vector2f) value).x, ((Vector2f) value).y);
        else if(value instanceof Vector3f)
            glUniform3f(location, ((Vector3f) value).x, ((Vector3f) value).y, ((Vector3f) value).z);
        else if(value instanceof Vector4f)
            glUniform4f(location, ((Vector4f) value).x, ((Vector4f) value).y, ((Vector4f) value).z, ((Vector4f) value).w);
    }

}