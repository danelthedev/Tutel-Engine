package org.tuiasi.engine.renderer;

import org.joml.Vector3f;
import org.tuiasi.engine.global.nodes.Node;
import org.tuiasi.engine.global.nodes.spatial.Spatial3D;
import org.tuiasi.engine.logic.AppLogic;
import org.tuiasi.engine.logic.logger.Log;
import org.tuiasi.engine.renderer.camera.MainCamera;
import org.tuiasi.engine.renderer.light.DirectionalLight;
import org.tuiasi.engine.renderer.light.LightData;
import org.tuiasi.engine.renderer.light.LightSource;
import org.tuiasi.engine.renderer.light.PointLight;
import org.tuiasi.engine.renderer.material.Material;
import org.tuiasi.engine.renderer.mesh.Mesh;
import org.tuiasi.engine.renderer.modelLoader.ModelLoader;
import org.tuiasi.engine.renderer.primitives.Axis;
import org.tuiasi.engine.renderer.primitives.Plane;
import org.tuiasi.engine.renderer.renderable.Renderable3D;
import org.tuiasi.engine.renderer.shader.*;
import org.tuiasi.engine.renderer.texture.Texture;
import org.tuiasi.engine.ui.DefaultEngineEditorUI;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class Renderer {

    Renderable3D axis;
    static List<Renderable3D> renderables;
    static List<LightSource> lightSources;

    DefaultEngineEditorUI editorUI;

    public Renderer() {

        renderables = new ArrayList<>();
        lightSources = new ArrayList<>();
        this.editorUI = new DefaultEngineEditorUI();

        Mesh axisMesh = new Mesh("", Axis.vertexData, Axis.indexData);
        axisMesh.setDrawMode(DrawMode.AXIS);

        axis = new Renderable3D(
                axisMesh,
                new ShaderProgram(new Shader(DefaultShaders.getVertexShader(), GL_VERTEX_SHADER),
                                    new Shader(DefaultShaders.getSolidColorFragmentShader(), GL_FRAGMENT_SHADER)),
                new Material(),
                new Spatial3D()
        );

    }


    public static void addRenderable(Renderable3D renderable) {
        renderables.add(renderable);
    }

    public static void removeRenderable(Renderable3D renderable) {
        renderables.remove(renderable);
    }

    public static void removeAllRenderables() {
        renderables.clear();
    }

    public static void addLightSource(LightSource lightSource) {
        lightSources.add(lightSource);
    }

    public static void removeLightSource(LightSource lightSource) {
        renderables.remove(lightSource.getRepresentation());
        lightSources.remove(lightSource);
    }

    public static void removeAllLightSources() {
        lightSources.clear();
    }

    public void render() {
        renderObjects();
        renderAxis();
        renderLights();
        editorUI.renderUI();

    }

    private void renderAxis(){
        if(axis != null)
            axis.render();
    }

    private void renderObjects(){
        for(int i = 0; i < renderables.size(); ++i){
            setLightUniforms(renderables.get(i));
            renderables.get(i).render();
        }
    }

    private void renderLights(){
        for (LightSource lightSource : lightSources) {
            lightSource.render();
        }
    }

    private void setLightUniforms(Renderable3D renderable) {
        renderable.setUniform(new Uniform<>("viewPos", MainCamera.getInstance().getPosition()));

        int directionalIndex = 0;
        int pointIndex = 0;
        for (LightSource lightSource : lightSources) {
            if(!lightSource.getEnabled())
                continue;

            if(lightSource instanceof DirectionalLight) {
                renderable.setUniform(new Uniform<>("directionalLights["+directionalIndex+"].direction", ((DirectionalLight)lightSource).getRotation()));
                renderable.setUniform(new Uniform<>("directionalLights["+directionalIndex+"].ambient", ((DirectionalLight)lightSource).getLightData().getAmbient()));
                renderable.setUniform(new Uniform<>("directionalLights["+directionalIndex+"].diffuse", ((DirectionalLight)lightSource).getLightData().getDiffuse()));
                renderable.setUniform(new Uniform<>("directionalLights["+directionalIndex+"].specular", ((DirectionalLight)lightSource).getLightData().getSpecular()));
                directionalIndex++;
            }
            if(lightSource instanceof PointLight){
                renderable.setUniform(new Uniform<>("pointLights["+pointIndex+"].position", ((PointLight)lightSource).getPosition()));
                renderable.setUniform(new Uniform<>("pointLights["+pointIndex+"].ambient", ((PointLight)lightSource).getLightData().getAmbient()));
                renderable.setUniform(new Uniform<>("pointLights["+pointIndex+"].diffuse", ((PointLight)lightSource).getLightData().getDiffuse()));
                renderable.setUniform(new Uniform<>("pointLights["+pointIndex+"].specular", ((PointLight)lightSource).getLightData().getSpecular()));
                renderable.setUniform(new Uniform<>("pointLights["+pointIndex+"].constant", ((PointLight)lightSource).getConstant()));
                renderable.setUniform(new Uniform<>("pointLights["+pointIndex+"].linear", ((PointLight)lightSource).getLinear()));
                renderable.setUniform(new Uniform<>("pointLights["+pointIndex+"].quadratic", ((PointLight)lightSource).getQuadratic()));
                pointIndex++;
            }
        }
        renderable.setUniform(new Uniform<>("directionalCount", directionalIndex));
        renderable.setUniform(new Uniform<>("pointCount", pointIndex));
    }

}
