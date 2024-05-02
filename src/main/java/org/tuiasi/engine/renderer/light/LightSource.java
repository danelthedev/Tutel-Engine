package org.tuiasi.engine.renderer.light;

import lombok.Data;
import org.tuiasi.engine.global.nodes.spatial.Spatial3D;
import org.tuiasi.engine.renderer.material.Material;
import org.tuiasi.engine.renderer.renderable.Renderable3D;
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

    private int type = 0;

    public LightSource(Spatial3D transform, LightData lightData){
        this.transform = transform;
        this.lightData = lightData;

        representation = new Renderable3D(
                new float[]{
                        // Front face vertices
                        0.25f, 0.25f, 0.25f,    1.0f,1.0f,1.0f,     0.0f,0.0f,0.0f,     0.0f,0.0f,
                        -0.25f, 0.25f, 0.25f,   1.0f,1.0f,1.0f,     0.0f,0.0f,0.0f,     0.0f,0.0f,
                        -0.25f, -0.25f, 0.25f,  1.0f,1.0f,1.0f,     0.0f,0.0f,0.0f,     0.0f,0.0f,
                        0.25f, -0.25f, 0.25f,   1.0f,1.0f,1.0f,     0.0f,0.0f,0.0f,     0.0f,0.0f,

                        // Back face vertices
                        0.25f, 0.25f, -0.25f,   1.0f,1.0f,1.0f,     0.0f,0.0f,0.0f,     0.0f,0.0f,
                        -0.25f, 0.25f, -0.25f,  1.0f,1.0f,1.0f,     0.0f,0.0f,0.0f,     0.0f,0.0f,
                        -0.25f, -0.25f, -0.25f, 1.0f,1.0f,1.0f,     0.0f,0.0f,0.0f,     0.0f,0.0f,
                        0.25f, -0.25f, -0.25f,  1.0f,1.0f,1.0f,     0.0f,0.0f,0.0f,     0.0f,0.0f,
                },
                new int[]{
                        0, 1, 2,
                        2, 3, 0,
                        4, 5, 6,
                        6, 7, 4,
                        0, 4, 5,
                        5, 1, 0,
                        2, 6, 7,
                        7, 3, 2,
                        0, 4, 7,
                        7, 3, 0,
                        1, 5, 6,
                        6, 2, 1
                },
                new ShaderProgram(new Shader("src/main/resources/shaders/default_vertex.vert", GL_VERTEX_SHADER), new Shader("src/main/resources/shaders/solid_color_fragment.frag", GL_FRAGMENT_SHADER)),
                new Texture[]{new Texture()},
                new Material()
            );
        representation.setPosition(transform.getPosition());
        representation.setRotation(transform.getRotation());
    }

    public void render(){
        representation.render();
    }

}
