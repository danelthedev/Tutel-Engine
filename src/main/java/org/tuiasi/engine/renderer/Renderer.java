package org.tuiasi.engine.renderer;

import org.joml.Vector3f;
import org.tuiasi.engine.global.nodes.spatial.Spatial3D;
import org.tuiasi.engine.renderer.camera.MainCamera;
import org.tuiasi.engine.renderer.light.DirectionalLight;
import org.tuiasi.engine.renderer.light.LightData;
import org.tuiasi.engine.renderer.light.LightSource;
import org.tuiasi.engine.renderer.light.PointLight;
import org.tuiasi.engine.renderer.material.Material;
import org.tuiasi.engine.renderer.primitives.Axes;
import org.tuiasi.engine.renderer.primitives.Cube;
import org.tuiasi.engine.renderer.primitives.Plane;
import org.tuiasi.engine.renderer.renderable.Renderable3D;
import org.tuiasi.engine.renderer.shader.DrawMode;
import org.tuiasi.engine.renderer.shader.Shader;
import org.tuiasi.engine.renderer.shader.ShaderProgram;
import org.tuiasi.engine.renderer.shader.Uniform;
import org.tuiasi.engine.renderer.texture.Texture;
import org.tuiasi.engine.ui.DefaultEngineEditorUI;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class Renderer {

    Renderable3D axis;
    List<Renderable3D> renderables;
    List<LightSource> lightSources;

    DefaultEngineEditorUI editorUI;

    public Renderer() {
        this.renderables = new ArrayList<>();
        this.lightSources = new ArrayList<>();
        this.editorUI = new DefaultEngineEditorUI();

        axis = new Renderable3D(
                Axes.vertexData,
                Axes.indexData,
                new ShaderProgram(new Shader("src/main/resources/shaders/default_vertex.vert", GL_VERTEX_SHADER),
                                    new Shader("src/main/resources/shaders/solid_color_fragment.frag", GL_FRAGMENT_SHADER)),
                new Texture[]{new Texture()},
                new Material(),
                new Spatial3D()
        );
        axis.setDrawMode(DrawMode.WIREFRAME);

        addTestObjects();
        addTestLights();
    }

    public void addTestObjects() {
        Renderable3D plane = new Renderable3D(
                Plane.vertexData,
                Plane.indexData,
                new ShaderProgram(new Shader("src/main/resources/shaders/default_vertex.vert", GL_VERTEX_SHADER),
                        new Shader("src/main/resources/shaders/default_fragment.frag", GL_FRAGMENT_SHADER)),
                new Texture[]{new Texture("src/main/resources/textures/orangOutline.png", 0)},
                new Material(new Texture("src/main/resources/textures/orangOutline.png", 1),
                        new Texture("src/main/resources/textures/container2_specular.png", 2),
                        16f),
                new Spatial3D()
        );
        addRenderable(plane);

        for(int i = 0; i < 5; ++ i) {
            Renderable3D testObject = new Renderable3D(
                    Cube.vertexData,
                    Cube.indexData,
                    new ShaderProgram(new Shader("src/main/resources/shaders/default_vertex.vert", GL_VERTEX_SHADER),
                            new Shader("src/main/resources/shaders/default_fragment.frag", GL_FRAGMENT_SHADER)),
                    new Texture[]{new Texture("src/main/resources/textures/container2.png", 0)},
                    new Material(new Texture("src/main/resources/textures/container2.png", 1),
                            new Texture("src/main/resources/textures/container2_specular.png", 2),
                            16f),
                    new Spatial3D()
            );

            Vector3f randomPos = new Vector3f((float) Math.random() * 10 - 5, (float) Math.random() * 10, (float) Math.random() * 10 - 5);
            Vector3f randomRot = new Vector3f((float) Math.random() * 360, (float) Math.random() * 360, (float) Math.random() * 360);

            testObject.setPosition(randomPos);
            testObject.setRotation(randomRot);

            addRenderable(testObject);
        }
    }

    public void addTestLights(){
        LightSource directionalLight = new DirectionalLight(
                new Spatial3D(  new Vector3f(0, 0, 0), new Vector3f(-0.2f, -1.0f, -0.3f), new Vector3f(0, 0, 0)),
                new LightData(  new Vector3f(.0f, .0f, .0f),
                                new Vector3f(1.0f, 1.0f, 1.0f),
                                new Vector3f(1.0f, 1.0f, 1.0f))
        );
        addLightSource(directionalLight);

        LightSource pointLight = new PointLight(
                new Spatial3D(  new Vector3f(0f, 3f, 0f), new Vector3f(-0.2f, -1.0f, -0.3f), new Vector3f(.2f, .2f, .2f)),
                new LightData(  new Vector3f(.0f, .0f, .0f),
                                new Vector3f(1.0f, 1.0f, 1.0f),
                                new Vector3f(1.0f, 1.0f, 1.0f)),
                1.0f, 0.09f, 0.032f);
        addLightSource(pointLight);

        pointLight = new PointLight(
                new Spatial3D(  new Vector3f(5f, 3f, 0f), new Vector3f(-0.2f, -1.0f, -0.3f), new Vector3f(.2f, .2f, .2f)),
                new LightData(  new Vector3f(.0f, .0f, .0f),
                        new Vector3f(1.0f, 1.0f, 1.0f),
                        new Vector3f(1.0f, 1.0f, 1.0f)),
                1.0f, 0.09f, 0.032f);
        addLightSource(pointLight);

        pointLight = new PointLight(
                new Spatial3D(  new Vector3f(0f, 3f, 5f), new Vector3f(-0.2f, -1.0f, -0.3f), new Vector3f(.2f, .2f, .2f)),
                new LightData(  new Vector3f(.0f, .0f, .0f),
                        new Vector3f(1.0f, 1.0f, 1.0f),
                        new Vector3f(1.0f, 1.0f, 1.0f)),
                1.0f, 0.09f, 0.032f);
        addLightSource(pointLight);
    }

    public void addRenderable(Renderable3D renderable) {
        renderables.add(renderable);
    }

    public void addLightSource(LightSource lightSource) {
        lightSources.add(lightSource);
    }

    public void render() {
        renderObjects();
        renderAxis();
        renderLights();
        editorUI.renderUI();
    }

    private void renderAxis(){
        axis.render();
    }

    private void renderObjects(){
        for (Renderable3D renderable : renderables) {
            setLightUniforms(renderable);
            renderable.render();
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
            if(lightSource instanceof DirectionalLight) {
                renderable.setUniform(new Uniform<>("directionalLights["+directionalIndex+"].direction", ((DirectionalLight)lightSource).getTransform().getRotation()));
                renderable.setUniform(new Uniform<>("directionalLights["+directionalIndex+"].ambient", ((DirectionalLight)lightSource).getLightData().getAmbient()));
                renderable.setUniform(new Uniform<>("directionalLights["+directionalIndex+"].diffuse", ((DirectionalLight)lightSource).getLightData().getDiffuse()));
                renderable.setUniform(new Uniform<>("directionalLights["+directionalIndex+"].specular", ((DirectionalLight)lightSource).getLightData().getSpecular()));
                directionalIndex++;
            }
            if(lightSource instanceof PointLight){
                renderable.setUniform(new Uniform<>("pointLights["+pointIndex+"].position", ((PointLight)lightSource).getTransform().getPosition()));
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
