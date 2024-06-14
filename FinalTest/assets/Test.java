import org.lwjgl.glfw.GLFW;
import org.tuiasi.engine.global.nodes.spatial.Spatial3D;
import org.tuiasi.engine.global.nodes.physics.body.KinematicBody;
import org.tuiasi.engine.logic.IO.KeyboardHandler;
import org.tuiasi.engine.logic.codeProcessor.UserScript;
import org.joml.Vector3f;
import org.tuiasi.engine.logic.AppLogic;

public class Test extends UserScript {
    @Override
    public void init() {
    }

    @Override
    public void run() {
        if(KeyboardHandler.isKeyPressed(GLFW.GLFW_KEY_W)){
			((KinematicBody)attachedNode.getValue()).setVelocity(new Vector3f(0.01f,0f,0f));
		}
        if(KeyboardHandler.isKeyPressed(GLFW.GLFW_KEY_S)){
			((KinematicBody)attachedNode.getValue()).setVelocity(new Vector3f(-0.01f,0f,0f));
		}
        if(KeyboardHandler.isKeyPressed(GLFW.GLFW_KEY_A)){
			((KinematicBody)attachedNode.getValue()).setVelocity(new Vector3f(0,0f,-0.01f));
		}
        if(KeyboardHandler.isKeyPressed(GLFW.GLFW_KEY_D)){
			((KinematicBody)attachedNode.getValue()).setVelocity(new Vector3f(0,0f,0.01f));
		}
    }

    @Override
    public void clean() {
		
    }
}