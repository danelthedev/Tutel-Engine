package org.tuiasi.engine.renderer.shader;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class DefaultShaders {
    @Getter
    private static String vertexShader, fragmentShader, solidColorFragmentShader;

    static{
        vertexShader = "#version 330 core\n" +
                        "layout(location = 0) in vec3 aPos;\n" +
                        "layout(location = 1) in vec3 aNormal;\n" +
                        "layout(location = 2) in vec2 aTexCoord;\n" +
                        "out vec3 vertColor;\n" +
                        "out vec2 texCoord;\n" +
                        "out vec3 pos;\n" +
                        "out vec3 normal;\n" +
                        "uniform mat4 model;\n" +
                        "uniform mat4 view;\n" +
                        "uniform mat4 projection;\n" +
                        "out vec3 FragPos;\n" +
                        "void main()\n" +
                        "{\n" +
                        "    gl_Position = projection * view * model * vec4(aPos, 1.0);\n" +
                        "    normal = vec3(transpose(inverse(model)) * vec4(aNormal, 1));\n" +
                        "    pos = vec3(model * vec4(aPos, 1.0));\n" +
                        "    texCoord = aTexCoord;\n" +
                        "}";

        fragmentShader = "#version 330 core\n" +
                "out vec4 fragColor;\n" +
                "in vec3 normal;\n" +
                "in vec2 texCoord;\n" +
                "in vec3 pos;\n" +
                "uniform vec3 viewPos;\n" +
                "struct directionalLight\n" +
                "{\n" +
                "    vec3 direction;\n" +
                "    vec3 ambient;\n" +
                "    vec3 diffuse;\n" +
                "    vec3 specular;\n" +
                "};\n" +
                "struct pointLight\n" +
                "{\n" +
                "    vec3 position;\n" +
                "    vec3 ambient;\n" +
                "    vec3 diffuse;\n" +
                "    vec3 specular;\n" +
                "    float constant;\n" +
                "    float linear;\n" +
                "    float quadratic;\n" +
                "};\n" +
                "uniform directionalLight directionalLights[10];\n" +
                "uniform int directionalCount;\n" +
                "uniform pointLight pointLights[10];\n" +
                "uniform int pointCount;\n" +
                "uniform sampler2D diffuseMap;\n" +
                "uniform bool hasDiffuse;\n" +
                "uniform sampler2D specularMap;\n" +
                "uniform bool hasSpecular;\n" +
                "uniform float materialShininess;\n" +
                "vec3 CalcDirLight(directionalLight light, vec3 normal, vec3 viewDir)\n" +
                "{\n" +
                "    vec3 lightDir = normalize(-light.direction);\n" +
                "    // diffuse shading\n" +
                "    float diff = max(dot(normal, lightDir), 0.0);\n" +
                "    // specular shading\n" +
                "    vec3 reflectDir = reflect(-lightDir, normal);\n" +
                "    float spec = pow(max(dot(viewDir, reflectDir), 0.0), materialShininess);\n" +
                "    if(diff <= 0)\n" +
                "        spec = 0;\n" +
                "    // combine results\n" +
                "    vec3 ambient  = light.ambient;\n" +
                "    vec3 diffuse  = light.diffuse  * diff;\n" +
                "    vec3 specular = light.specular * spec;\n" +
                "    if(hasDiffuse){\n" +
                "        ambient *= vec3(texture(diffuseMap, texCoord));\n" +
                "        diffuse *= vec3(texture(diffuseMap, texCoord));\n" +
                "    }\n" +
                "    else\n" +
                "        diffuse = vec3(0.0);\n" +
                "    if(hasSpecular)\n" +
                "        specular *= vec3(texture(specularMap, texCoord));\n" +
                "    else\n" +
                "        specular = vec3(0.0);\n" +
                "    return (ambient + diffuse + specular);\n" +
                "}\n" +
                "vec3 CalcPointLight(pointLight light, vec3 normal, vec3 fragPos, vec3 viewDir)\n" +
                "{\n" +
                "    vec3 lightDir = normalize(light.position - fragPos);\n" +
                "    // diffuse shading\n" +
                "    float diff = max(dot(normal, lightDir), 0.0);\n" +
                "    // specular shading\n" +
                "    vec3 reflectDir = reflect(-lightDir, normal);\n" +
                "    float spec = pow(max(dot(viewDir, reflectDir), 0.0), materialShininess);\n" +
                "    // attenuation\n" +
                "    float distance = length(light.position - fragPos);\n" +
                "    float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance * distance));\n" +
                "    // combine results\n" +
                "    vec3 ambient  = light.ambient;\n" +
                "    vec3 diffuse  = light.diffuse  * diff;\n" +
                "    vec3 specular = light.specular * spec;\n" +
                "    if(hasDiffuse){\n" +
                "        ambient *= vec3(texture(diffuseMap, texCoord));\n" +
                "        diffuse *= vec3(texture(diffuseMap, texCoord));\n" +
                "    }\n" +
                "    else\n" +
                "        diffuse = vec3(0.0);\n" +
                "    if(hasSpecular)\n" +
                "        specular *= vec3(texture(specularMap, texCoord));\n" +
                "    else\n" +
                "        specular = vec3(0.0);\n" +
                "    ambient  *= attenuation;\n" +
                "    diffuse  *= attenuation;\n" +
                "    specular *= attenuation;\n" +
                "    return (ambient + diffuse + specular) ;\n" +
                "}\n" +
                "void main()\n" +
                "{\n" +
                "    vec3 normal = normalize(normal);\n" +
                "    vec3 viewDir = normalize(viewPos - pos);\n" +
                "    vec3 color = vec3(0.0);\n" +
                "    for(int i = 0; i < directionalCount; i++)\n" +
                "        color += CalcDirLight(directionalLights[i], normal, viewDir);\n" +
                "    for(int i = 0; i < pointCount; i++)\n" +
                "        color += CalcPointLight(pointLights[i], normal, pos, viewDir);\n" +
                "    fragColor = vec4(color, 1.0);\n" +
                "}";

        solidColorFragmentShader = "#version 330 core\n" +
                "out vec4 FragColor;\n" +
                "uniform vec3 color;\n" +
                "void main() {\n" +
                "    FragColor = vec4(color, 1.0);\n" +
                "}";

    }
}
