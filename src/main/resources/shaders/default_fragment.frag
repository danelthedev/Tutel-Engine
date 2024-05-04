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

    // generic
    vec3 position;
    vec3 direction;
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;

    // point light
    float constant;
    float linear;
    float quadratic;
};

uniform lightData directionalLight;
uniform lightData pointLights[3];

uniform sampler2D diffuseMap;
uniform sampler2D specularMap;
uniform float materialShininess;

vec3 CalcDirLight(lightData light, vec3 normal, vec3 viewDir)
{
    vec3 lightDir = normalize(-light.direction);
    // diffuse shading
    float diff = max(dot(normal, lightDir), 0.0);
    // specular shading
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), materialShininess);
    // combine results
    vec3 ambient  = light.ambient  * vec3(texture(diffuseMap, texCoord));
    vec3 diffuse  = light.diffuse  * diff * vec3(texture(diffuseMap, texCoord));
    vec3 specular = light.specular * spec * vec3(texture(specularMap, texCoord));
    return (ambient + diffuse + specular);
}

vec3 CalcPointLight(lightData light, vec3 normal, vec3 fragPos, vec3 viewDir)
{
    vec3 lightDir = normalize(light.position - fragPos);
    // diffuse shading
    float diff = max(dot(normal, lightDir), 0.0);
    // specular shading
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), materialShininess);
    // attenuation
    float distance    = length(light.position - fragPos);
    float attenuation = 1.0 / (light.constant + light.linear * distance +
    light.quadratic * (distance * distance));
    // combine results
    vec3 ambient  = light.ambient  * vec3(texture(diffuseMap, texCoord));
    vec3 diffuse  = light.diffuse  * diff * vec3(texture(diffuseMap, texCoord));
    vec3 specular = light.specular * spec * vec3(texture(specularMap, texCoord));
    ambient  *= attenuation;
    diffuse  *= attenuation;
    specular *= attenuation;
    return (ambient + diffuse + specular);
}

void main()
{
    vec3 normal = normalize(normal);
    vec3 viewDir = normalize(viewPos - pos);

//    vec3 color = CalcDirLight(directionalLight, normal, viewDir);
    vec3 color = vec3(0);
    for(int i = 1; i < 3; i++)
        color += CalcPointLight(pointLights[i], normal, pos, viewDir);

    fragColor = vec4(color, 1.0);
}