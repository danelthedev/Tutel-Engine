import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.tuiasi.engine.global.nodes.Node;
import org.tuiasi.engine.global.nodes.physics.body.KinematicBody;
import org.tuiasi.engine.global.nodes.physics.collider.Collider3D;
import org.tuiasi.engine.logic.AppLogic;
import org.tuiasi.engine.logic.IO.KeyboardHandler;
import org.tuiasi.engine.logic.codeProcessor.UserScript;
import org.tuiasi.engine.logic.logger.Log;
import org.tuiasi.engine.renderer.Renderer;
import org.tuiasi.engine.renderer.modelLoader.Model;
import org.tuiasi.engine.renderer.modelLoader.ModelLoader;
import org.tuiasi.engine.renderer.renderable.Renderable3D;
import org.tuiasi.engine.renderer.shader.ShaderProgram;

import java.util.ArrayList;
import java.util.List;

// TODO: Fix snake demo script

public class Snake extends UserScript {

    List<Node<KinematicBody>> bodyPieces = new ArrayList<>();
    Node<KinematicBody> bodyPiece;

    KinematicBody food;

    int direction = 4; // 0 - right | 1 - up | 2 - left | 3 - down

    private Integer snakeHeadX, snakeHeadY;

    private Integer foodX, foodY;

    private Integer delay = 0;


    // function that returns a new body piece
    private Node<KinematicBody> createBodyPiece() {
        KinematicBody newBody = new KinematicBody();
        Node<KinematicBody> newBodyNode = new Node<>(attachedNode.getParent(), "SnakeBody_" + bodyPieces.size(), newBody);
        newBody.setPosition(new Vector3f(0, 1f, 0));

        Renderable3D renderable = new Renderable3D();
        Renderable3D bodyPieceRenderable = (Renderable3D) bodyPiece.getChildByName("Renderable3D").getValue();
        renderable.setMesh(bodyPieceRenderable.getMesh());
        renderable.setMaterial(bodyPieceRenderable.getMaterial());
        renderable.setShaderProgram(bodyPieceRenderable.getShaderProgram());
        renderable.setPosition(new Vector3f(0, 1f, 0));
        Node<Renderable3D> renderableNode = new Node<>(newBodyNode, "Renderable3D", renderable);

        Collider3D collider = new Collider3D();
        collider.setPosition(new Vector3f(0, 2f, 0));
        collider.setScale(new Vector3f(0.9f, 0.9f, 0.9f));
        Node<Collider3D> colliderNode = new Node<>(newBodyNode, "Collider3D", collider);
        newBody.setCollider(collider);

        AppLogic.getPhysicsNodes().add(newBodyNode);
        bodyPieces.add(newBodyNode);


        // Set the position of the new body piece
        if (bodyPieces.size() == 1) {
            // Set the position to be behind the head
            Vector3f headPosition = new Vector3f((float)snakeHeadX, 1f, (float)snakeHeadY);
            Vector3f newBodyPosition = new Vector3f(0f, 1f, 0f);

            // Determine the position based on the direction of the snake
            switch (direction) {
                case 0: // moving right
                    newBodyPosition = new Vector3f(headPosition.x - 1, 1f, headPosition.y);
                    break;
                case 1: // moving up
                    newBodyPosition = new Vector3f(headPosition.x, 1f, headPosition.y - 1);
                    break;
                case 2: // moving left
                    newBodyPosition = new Vector3f(headPosition.x + 1, 1f, headPosition.y);
                    break;
                case 3: // moving down
                    newBodyPosition = new Vector3f(headPosition.x, 1f, headPosition.y + 1);
                    break;
            }

            newBody.setPosition(newBodyPosition);
        } else {
            // Set the position to be behind the second last item in the list
            KinematicBody secondLastBodyPiece = ((KinematicBody)bodyPieces.get(bodyPieces.size() - 2).getValue());
            Vector3f secondLastBodyPiecePosition = secondLastBodyPiece.getPosition();
            Vector3f newBodyPosition = new Vector3f(0f, 1f, 0f);

            // Determine the position based on the direction of the snake
            switch (direction) {
                case 0: // moving right
                    newBodyPosition = new Vector3f(secondLastBodyPiecePosition.x - 1, 1f, secondLastBodyPiecePosition.y);
                    break;
                case 1: // moving up
                    newBodyPosition = new Vector3f(secondLastBodyPiecePosition.x, 1f, secondLastBodyPiecePosition.y - 1);
                    break;
                case 2: // moving left
                    newBodyPosition = new Vector3f(secondLastBodyPiecePosition.x + 1, 1f, secondLastBodyPiecePosition.y);
                    break;
                case 3: // moving down
                    newBodyPosition = new Vector3f(secondLastBodyPiecePosition.x, 1f, secondLastBodyPiecePosition.y + 1);
                    break;
            }

            newBody.setPosition(newBodyPosition);
        }

        return newBodyNode;
    }

    @Override
    public void init() {
        bodyPieces = new ArrayList<>();

        food = (KinematicBody)root.getNodeByName("Food").getValue();
        bodyPiece = (Node<KinematicBody>)root.getNodeByName("SnakeBody");

        snakeHeadX = 5;
        snakeHeadY = 5;

        foodX = 3;
        foodY = 3;
        food.setPosition(new Vector3f((float) foodX, 1f, (float) foodY));
    }

    @Override
    public void run() {
        KinematicBody head = (KinematicBody)(getAttachedNode().getValue());

        // Check for collisions with the walls
        if (snakeHeadX < 1 || snakeHeadX > 9 || snakeHeadY < 1 || snakeHeadY > 9) {
            this.clean();
            return;
        }
        else if(!head.getCollider().getColliding().isEmpty()) {
            this.clean();
            return;
        }

        if (KeyboardHandler.isKeyPressed(GLFW.GLFW_KEY_S)) {
            if(direction != 3)
                direction = 1;
        }
        if (KeyboardHandler.isKeyPressed(GLFW.GLFW_KEY_W)) {
            if(direction != 1)
                direction = 3;
        }
        if (KeyboardHandler.isKeyPressed(GLFW.GLFW_KEY_A)) {
            if(direction != 0)
                direction = 2;
        }
        if (KeyboardHandler.isKeyPressed(GLFW.GLFW_KEY_D)) {
            if(direction != 2)
                direction = 0;
        }

        if(delay < 20) {
            delay++;
            return;
        }else {
            delay = 0;
        }

        if(direction == 4)
            return;

        if(bodyPieces.size() > 0){
            // starting from the tail, move each piece to the position of the piece in front of it
            for (int i = bodyPieces.size() - 1; i > 0; i--) {
                ((KinematicBody)bodyPieces.get(i).getValue()).setPosition(new Vector3f(((KinematicBody)bodyPieces.get(i - 1).getValue()).getPosition()));
            }
            // move the first piece to where the head is
            ((KinematicBody)bodyPieces.get(0).getValue()).setPosition(new Vector3f((float)snakeHeadX, 1f, (float)snakeHeadY));

        }

        float rotation = 0;
        switch (direction) {
            case 1:
                rotation = 0;
                snakeHeadY++;
                break;
            case 3:
                rotation = 180;
                snakeHeadY--;
                break;
            case 2:
                rotation = -90;
                snakeHeadX--;
                break;
            case 0:
                rotation = 90;
                snakeHeadX++;
                break;
        }


        ((KinematicBody)attachedNode.getValue()).setPosition(new Vector3f((float)snakeHeadX, 1f, (float)snakeHeadY));
        ((KinematicBody)attachedNode.getValue()).setRotation(new Vector3f(0f, (float)rotation, 0f));

        // Check if the snake has eaten the food
        if (snakeHeadX.equals(foodX) && snakeHeadY.equals(foodY)) {
            // Move the food to a new location
            do {
                foodX = (int) (Math.random() * 10);
                if(foodX == 0)
                    foodX = 1;
                foodY = (int) (Math.random() * 10);
                if(foodY == 0)
                    foodY = 1;

                // create a body piece at the tail position
                Node<KinematicBody> newBodyPiece = createBodyPiece();

                // move food
                food.setPosition(new Vector3f((float) foodX, 1f, (float) foodY));
            } while (bodyPieces.stream().anyMatch(bodyPiece -> ((KinematicBody)bodyPiece.getValue()).getPosition().equals(new Vector3f((float)foodX, 1f, (float)foodY))));
        }


    }

    @Override
    public void clean() {
        // delete all body pieces from the node tree and from the renderer
        for (int i = 0; i < bodyPieces.size(); ++ i) {
            AppLogic.removeNodeAndChildren(bodyPieces.get(i));
        }
        bodyPieces.clear();
        direction = 4;
        snakeHeadX = 5;
        snakeHeadY = 5;
        ((KinematicBody)attachedNode.getValue()).setPosition(new Vector3f((float)snakeHeadX, 1f, (float)snakeHeadY));
    }
}