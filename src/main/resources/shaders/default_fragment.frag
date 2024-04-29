#version 330 core
out vec4 fragColor;

in vec3 vertColor;
in vec3 normal;
in vec2 texCoord;
in vec3 pos;

uniform vec3 lightPos;
uniform vec3 viewPos;

uniform vec3 lightAmbient;
uniform vec3 lightDiffuse;
uniform vec3 lightSpecular;

uniform sampler2D diffuseMap;
uniform sampler2D specularMap;
uniform float materialShininess;

vec3 lighting(vec3 objectColor, vec3 pos, vec3 normal, vec3 lightPos, vec3 viewPos,
              vec3 lightAmbient, vec3 lightDiffuse, vec3 lightSpecular,
              sampler2D materialDiffuse, sampler2D materialSpecular, float specPower)
{
    // position related variables
    vec3 L = normalize(lightPos - pos);
    vec3 V = normalize(viewPos - pos);
    vec3 N = normalize(normal);
    vec3 R = normalize(reflect(-L, N));

    float diffCoef = max(dot(L, N), 0.0);

    // Check if the fragment is facing the light source
    float specCoef = pow(max(dot(R, V), 0.0), specPower);
    // if any component of the normal is negative, the fragment is facing away from the light source
    if(diffCoef == 0)
        specCoef = 0;

    vec3 ambientColor = lightAmbient * vec3(texture(materialDiffuse, texCoord));
    vec3 diffuseColor = lightDiffuse * (diffCoef * vec3(texture(materialDiffuse, texCoord))) * dot(L,N);
    vec3 specularColor = lightSpecular * (specCoef * vec3(texture(materialSpecular, texCoord)));
    vec3 col = ( ambientColor + diffuseColor + specularColor) * objectColor;


    return clamp(col, 0, 1);
}


void main()
{
    vec3 color = lighting(vertColor, pos, normal, lightPos, viewPos,
                          lightAmbient, lightDiffuse, lightSpecular,
                          diffuseMap, specularMap, materialShininess);

    fragColor = vec4(color, 1.0);
}