package org.tuiasi.engine.renderer.light;

import lombok.Data;
import org.joml.Vector3f;
import org.tuiasi.engine.global.nodes.spatial.Spatial3D;
import org.tuiasi.engine.renderer.material.Material;
import org.tuiasi.engine.renderer.primitives.Cube;
import org.tuiasi.engine.renderer.renderable.Renderable3D;
import org.tuiasi.engine.renderer.shader.DrawMode;
import org.tuiasi.engine.renderer.shader.Shader;
import org.tuiasi.engine.renderer.shader.ShaderProgram;
import org.tuiasi.engine.renderer.texture.Texture;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

@Data
public class LightSource {

    private Renderable3D representation;
    private Spatial3D transform;
    private LightData lightData;

    public LightSource(Spatial3D transform, LightData lightData){
        this.transform = transform;
        this.lightData = lightData;

        representation = new Renderable3D(
                Cube.vertexData,
                Cube.indexData,
                new ShaderProgram(new Shader("src/main/resources/shaders/default_vertex.vert", GL_VERTEX_SHADER), new Shader("src/main/resources/shaders/solid_color_fragment.frag", GL_FRAGMENT_SHADER)),
                new Texture[]{new Texture()},
                new Material(),
                transform
            );
    }

    public void setPosition(Vector3f newPosition){
        transform.setPosition(newPosition);
        representation.setPosition(transform.getPosition());
    }

    public void setRotation(Vector3f newRotation){
        transform.setRotation(newRotation);
        representation.setRotation(transform.getRotation());
    }

    public void render(){
        representation.render();
    }

}
