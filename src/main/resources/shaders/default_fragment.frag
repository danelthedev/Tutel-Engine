#version 330 core
out vec4 fragColor;

in vec3 vertColor; // Received from the vertex shader
in vec3 normal;
in vec2 texCoord;
in vec3 pos;

uniform sampler2D tex;
uniform vec3 lightPos;
uniform vec3 viewPos;


vec3 lighting(vec3 objectColor, vec3 pos, vec3 normal, vec3 lightPos, vec3 viewPos,
              vec3 ambient, vec3 lightColor, vec3 specular, float specPower)
{
    vec3 L = normalize(lightPos - pos); // *
    vec3 V = normalize(viewPos - pos);
    vec3 N = normalize(normal);         // *
    vec3 R = normalize(reflect(-L, N));

    float diffCoef = max(dot(L, N), 0.0);
    float specCoef = pow(max(dot(R, V), 0.0), specPower);

    vec3 ambientColor = ambient * lightColor;
    vec3 diffuseColor = diffCoef * lightColor * dot(L,N);
    vec3 specularColor = specCoef * specular * lightColor;
    vec3 col = ( ambientColor + diffuseColor ) * objectColor;

//    vec3 col = (specularColor) * objectColor;

    return clamp(col, 0, 1);
}


void main()
{
    vec3 lightColor = vec3(1.0, 1.0, 1.0);
    vec3 ambient = vec3(0.2);
    vec3 specular = vec3(0.3);
    float specPower = 32;

    vec3 color = lighting(vertColor, pos, normal, lightPos, viewPos, ambient, lightColor, specular, specPower);

    fragColor = vec4(color, 1.0);
//    fragColor = texture(tex, uv.xy) * vec4(color, 1.0);
}