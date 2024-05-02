#version 330 core
out vec4 fragColor;

in vec3 vertColor;
in vec3 normal;
in vec2 texCoord;
in vec3 pos;

uniform vec3 viewPos;

struct lightData
{
    int type;

    vec3 position;
    vec3 direction;
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;

    float constant;
    float linear;
    float quadratic;
};

uniform lightData light;

uniform sampler2D diffuseMap;
uniform sampler2D specularMap;
uniform float materialShininess;

vec3 lighting(vec3 objectColor, vec3 pos, vec3 normal, vec3 lightPos, vec3 viewPos,
              vec3 lightAmbient, vec3 lightDiffuse, vec3 lightSpecular,
              sampler2D materialDiffuse, sampler2D materialSpecular, float specPower)
{
    // Calculate the light direction (if the light is a directional light)
    vec3 L;
    if(light.type != 1)
        L = normalize(lightPos - pos);
    else
        L = normalize(-light.direction);

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

    // Calculate point light attenuation
    if(light.type == 3){
        float distance = length(lightPos - pos);
        float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance * distance));
        ambientColor *= attenuation;
        diffuseColor *= attenuation;
        specularColor *= attenuation;
    }

    vec3 col = ( ambientColor + diffuseColor + specularColor) * objectColor;


    return clamp(col, 0, 1);
}


void main()
{
    vec3 color = lighting(vertColor, pos, normal, light.position, viewPos,
                          light.ambient, light.diffuse, light.specular,
                          diffuseMap, specularMap, materialShininess);

    fragColor = vec4(color, 1.0);
}