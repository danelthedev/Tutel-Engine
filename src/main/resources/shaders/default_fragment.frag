#version 330 core
out vec4 fragColor;

in vec3 vertColor; // Received from the vertex shader
in vec3 normal;
in vec2 texCoord;
in vec3 pos;

uniform sampler2D tex0;
uniform sampler2D tex1;

uniform bool hasTexture;

uniform vec3 lightPos;
uniform vec3 viewPos;

uniform vec3 lightAmbient;
uniform vec3 lightDiffuse;
uniform vec3 lightSpecular;

uniform vec3 materialAmbient;
uniform vec3 materialDiffuse;
uniform vec3 materialSpecular;
uniform float materialShininess;

vec3 lighting(vec3 objectColor, vec3 pos, vec3 normal, vec3 lightPos, vec3 viewPos,
              vec3 lightAmbient, vec3 lightDiffuse, vec3 lightSpecular,
              vec3 materialAmbient, vec3 materialDiffuse, vec3 materialSpecular, float specPower)
{
    // position related variables
    vec3 L = normalize(lightPos - pos);
    vec3 V = normalize(viewPos - pos);
    vec3 N = normalize(normal);
    vec3 R = normalize(reflect(L, N)); // Normal ar fi ca L sa fie negat

    float diffCoef = max(dot(L, N), 0.0);

    // Check if the fragment is facing the light source
    float specCoef = pow(max(dot(R, V), 0.0), specPower);

    vec3 ambientColor = lightAmbient * materialAmbient;
    vec3 diffuseColor = lightDiffuse * (diffCoef * materialDiffuse);//* dot(L,N);
    vec3 specularColor = lightSpecular * (specCoef * materialSpecular);
    vec3 col = ( ambientColor + diffuseColor + specularColor) * objectColor;

    return clamp(col, 0, 1);
}


void main()
{
    float specPower = 64;

    vec3 color = lighting(vertColor, pos, normal, lightPos, viewPos,
                          lightAmbient, lightDiffuse, lightSpecular,
                          materialAmbient, materialDiffuse, materialSpecular, materialShininess);

    if(hasTexture)
        fragColor = mix(texture(tex0, texCoord), texture(tex1, texCoord), 0.2) * vec4(color, 1.0);
    else
        fragColor = vec4(color, 1.0);
}