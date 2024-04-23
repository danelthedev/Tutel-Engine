#version 330 core

out vec4 FragColor;

in vec3 vertColor; // Received from the vertex shader

void main() {
    FragColor = vec4(vertColor, 1.0);
}