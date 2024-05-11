#version 330 core
out vec4 fragColor;

in vec3 normal;
in vec2 texCoord;
in vec3 pos;

uniform vec3 viewPos;

struct directionalLight
{
    vec3 direction;
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

struct pointLight
{
    vec3 position;
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
    float constant;
    float linear;
    float quadratic;
};

uniform directionalLight directionalLights[10];
uniform int directionalCount;

uniform pointLight pointLights[10];
uniform int pointCount;

uniform sampler2D diffuseMap;
uniform bool hasDiffuse;

uniform sampler2D specularMap;
uniform bool hasSpecular;

uniform float materialShininess;

vec3 CalcDirLight(directionalLight light, vec3 normal, vec3 viewDir)
{
    vec3 lightDir = normalize(-light.direction);
    // diffuse shading
    float diff = max(dot(normal, lightDir), 0.0);
    // specular shading
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), materialShininess);
    if(diff <= 0)
        spec = 0;
    // combine results
    vec3 ambient  = light.ambient;
    vec3 diffuse  = light.diffuse  * diff;
    vec3 specular = light.specular * spec;

    if(hasDiffuse){
        ambient *= vec3(texture(diffuseMap, texCoord));
        diffuse *= vec3(texture(diffuseMap, texCoord));
    }
    else
        diffuse = vec3(0.0);

    if(hasSpecular)
        specular *= vec3(texture(specularMap, texCoord));
    else
        specular = vec3(0.0);

    return (ambient + diffuse + specular);
}

vec3 CalcPointLight(pointLight light, vec3 normal, vec3 fragPos, vec3 viewDir)
{
    vec3 lightDir = normalize(light.position - fragPos);
    // diffuse shading
    float diff = max(dot(normal, lightDir), 0.0);
    // specular shading
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), materialShininess);
    // attenuation
    float distance = length(light.position - fragPos);
    float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance * distance));
    // combine results
    vec3 ambient  = light.ambient;
    vec3 diffuse  = light.diffuse  * diff;
    vec3 specular = light.specular * spec;

    if(hasDiffuse){
        ambient *= vec3(texture(diffuseMap, texCoord));
        diffuse *= vec3(texture(diffuseMap, texCoord));
    }
    else
        diffuse = vec3(0.0);

    if(hasSpecular)
        specular *= vec3(texture(specularMap, texCoord));
    else
        specular = vec3(0.0);

    ambient  *= attenuation;
    diffuse  *= attenuation;
    specular *= attenuation;
    return (ambient + diffuse + specular) ;
}

void main()
{
    vec3 normal = normalize(normal);
    vec3 viewDir = normalize(viewPos - pos);

    vec3 color = vec3(0.0);

    for(int i = 0; i < directionalCount; i++)
        color += CalcDirLight(directionalLights[i], normal, viewDir);

    for(int i = 0; i < pointCount; i++)
        color += CalcPointLight(pointLights[i], normal, pos, viewDir);


    fragColor = vec4(color, 1.0);
}