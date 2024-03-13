#version 330 core

in vec3 vertColor;
in vec2 texCoord;

out vec4 FragColor;

uniform sampler2D objTex;

uniform vec3 u_Color;

void main() {
    FragColor = texture(objTex, texCoord) * vec4(vertColor, 1.0);
}