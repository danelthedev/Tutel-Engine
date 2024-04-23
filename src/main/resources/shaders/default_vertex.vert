#version 330 core

layout(location = 0) in vec3 aPos;
layout(location = 1) in vec3 aColor;
layout(location = 2) in vec2 aTexCoord;
layout(location = 3) in vec3 aNormal;

out vec3 vertColor;
out vec2 texCoord;
out vec3 pos;
out vec3 normal;

uniform mat4 modelViewProjectionMatrix;
uniform mat4 normalMatrix;

out vec3 FragPos;

void main()
{
    gl_Position = modelViewProjectionMatrix * vec4(aPos, 1.0);

    normal = vec3(normalMatrix * vec4(aNormal, 1));

    vertColor = aColor;
    pos = aPos;

    texCoord = aTexCoord;
}