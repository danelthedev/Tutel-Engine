#version 330 core

layout(location = 0) in vec3 aPos;
layout(location = 1) in vec3 aColor;
layout(location = 2) in vec2 aTexCoord;

out vec3 vertColor;
out vec2 texCoord;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

void main() {
    // if transform is not set, set it to identity matrix
    if (projection == mat4(0.0)) {
        gl_Position = vec4(aPos, 1.0);
    }
    else{
        gl_Position = projection * view * model * vec4(aPos, 1.0);
    }

    vertColor = aColor;
    texCoord = aTexCoord;
}