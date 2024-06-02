package org.tuiasi.engine.renderer.light;

import lombok.Data;
import org.joml.Vector3f;
import org.tuiasi.engine.global.nodes.EditorVisible;
import org.tuiasi.engine.global.nodes.spatial.Spatial3D;
import org.tuiasi.engine.renderer.Renderer;
import org.tuiasi.engine.renderer.material.Material;
import org.tuiasi.engine.renderer.mesh.Mesh;
import org.tuiasi.engine.renderer.primitives.Cube;
import org.tuiasi.engine.renderer.renderable.Renderable3D;
import org.tuiasi.engine.renderer.shader.Shader;
import org.tuiasi.engine.renderer.shader.ShaderProgram;
import org.tuiasi.engine.renderer.shader.Uniform;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

@Data
public class LightSource extends Spatial3D {

    private Renderable3D representation;
    @EditorVisible
    private LightData lightData;
    @EditorVisible
    private Boolean enabled = Boolean.TRUE;

    public LightSource(){
        super();
        lightData = new LightData();

        Mesh cubeMesh = new Mesh("", Cube.vertexData, Cube.indexData);
        representation = new Renderable3D(
                cubeMesh,
                new ShaderProgram(new Shader("C:\\Users\\Danel\\IdeaProjects\\licenta\\src\\main\\resources\\shaders\\default_vertex.vert", GL_VERTEX_SHADER), new Shader("C:\\Users\\Danel\\IdeaProjects\\licenta\\src\\main\\resources\\shaders\\solid_color_fragment.frag", GL_FRAGMENT_SHADER)),
                new Material(),
                new Spatial3D(getPosition(), getRotation(), new Vector3f(.2f,.2f,.2f))
        );
    }

    public LightSource(Spatial3D transform, LightData lightData){
        super(transform.getPosition(), transform.getRotation(), transform.getScale());
        this.lightData = lightData;

        Mesh cubeMesh = new Mesh("", Cube.vertexData, Cube.indexData);
        representation = new Renderable3D(
                cubeMesh,
                new ShaderProgram(new Shader("C:\\Users\\Danel\\IdeaProjects\\licenta\\src\\main\\resources\\shaders\\default_vertex.vert", GL_VERTEX_SHADER), new Shader("C:\\Users\\Danel\\IdeaProjects\\licenta\\src\\main\\resources\\shaders\\solid_color_fragment.frag", GL_FRAGMENT_SHADER)),
                new Material(),
                new Spatial3D(getPosition(), getRotation(), new Vector3f(.2f,.2f,.2f))
            );

    }

    public void addToRenderer(){
        Renderer.addLightSource(this);
        Renderer.addRenderable(representation);
    }

    public void setPosition(Vector3f newPosition){
        super.setPosition(newPosition);
        representation.setPosition(getPosition());
    }

    public void setRotation(Vector3f newRotation){
        super.setRotation(newRotation);
        representation.setRotation(getRotation());
    }

    public void render(){
        representation.render();
        if(enabled)
            representation.setUniform(new Uniform<>("color", new Vector3f(1.0f, 1.0f, 1.0f)));
        else
            representation.setUniform(new Uniform<>("color", new Vector3f(0.0f, 0.0f, 0.0f)));
    }

    @Override
    public Object saveState(){
        return new LightSource((Spatial3D) super.saveState(), lightData);
    }

    @Override
    public void loadState(Object state){
        super.loadState(state);
        LightSource lightSource = (LightSource) state;
        this.lightData = lightSource.lightData;
    }
}
