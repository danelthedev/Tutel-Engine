import org.lwjgl.glfw.GLFW;
import org.tuiasi.engine.global.nodes.spatial.Spatial3D;
import org.tuiasi.engine.global.nodes.physics.body.KinematicBody;
import org.tuiasi.engine.logic.IO.KeyboardHandler;
import org.tuiasi.engine.logic.codeProcessor.UserScript;
import org.joml.Vector3f;
import org.tuiasi.engine.logic.AppLogic;

public class PlayerMovement extends UserScript {
    @Override
    public void init() {
    }

    @Override
    public void run() {
        if(KeyboardHandler.isKeyPressed(GLFW.GLFW_KEY_W)){
			((Spatial3D)attachedNode.getValue()).translate(new Vector3f(0.01f,0f,0f));
		}
        if(KeyboardHandler.isKeyPressed(GLFW.GLFW_KEY_S)){
			((Spatial3D)attachedNode.getValue()).translate(new Vector3f(-0.01f,0f,0f));
		}
        if(KeyboardHandler.isKeyPressed(GLFW.GLFW_KEY_A)){
			((Spatial3D)attachedNode.getValue()).translate(new Vector3f(0,0f,-0.01f));
		}
        if(KeyboardHandler.isKeyPressed(GLFW.GLFW_KEY_D)){
			((Spatial3D)attachedNode.getValue()).translate(new Vector3f(0,0f,0.01f));
		}
        if(KeyboardHandler.isKeyPressed(GLFW.GLFW_KEY_LEFT)){
			((Spatial3D)attachedNode.getValue()).rotate(new Vector3f(0.0f,0.1f,0.0f));
		}
        if(KeyboardHandler.isKeyPressed(GLFW.GLFW_KEY_RIGHT)){
			((Spatial3D)attachedNode.getValue()).rotate(new Vector3f(0.0f,-0.1f,0.0f));
		}
    }

    @Override
    public void clean() {
		
    }
}