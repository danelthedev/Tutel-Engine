import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.tuiasi.engine.global.nodes.physics.body.KinematicBody;
import org.tuiasi.engine.logic.IO.KeyboardHandler;
import org.tuiasi.engine.logic.codeProcessor.UserScript;
import org.tuiasi.engine.logic.logger.Log;

import java.util.LinkedList;
import java.util.Queue;

// TODO: Fix snake demo script

public class Snake extends UserScript {

    KinematicBody food;

    int direction = 0; // 0 - right | 1 - up | 2 - left | 3 - down

    private Boolean[][] hasSnake;
    private Integer snakeHeadX, snakeHeadY;
    private Integer snakeTailX, snakeTailY;

    private Integer foodX, foodY;

    private Integer delay = 0;

    @Override
    public void init() {
        food = (KinematicBody)root.getNodeByName("Food").getValue();

        hasSnake = new Boolean[11][11];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                hasSnake[i][j] = false;
            }
        }
        snakeHeadX = 5;
        snakeHeadY = 5;

        snakeTailX = 5;
        snakeTailY = 5;

        foodX = 3;
        foodY = 3;

    }

    @Override
    public void run() {

        if (KeyboardHandler.isKeyPressed(GLFW.GLFW_KEY_W)) {
            direction = 1;
        }
        if (KeyboardHandler.isKeyPressed(GLFW.GLFW_KEY_S)) {
            direction = 3;
        }
        if (KeyboardHandler.isKeyPressed(GLFW.GLFW_KEY_D)) {
            direction = 2;
        }
        if (KeyboardHandler.isKeyPressed(GLFW.GLFW_KEY_A)) {
            direction = 0;
        }

        if(delay < 30) {
            delay++;
            return;
        }else {
            delay = 0;
        }

        hasSnake[snakeHeadX][snakeHeadY] = false;
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

        ((KinematicBody)attachedNode.getValue()).setPosition(new Vector3f((float)snakeHeadX-5, 1f, (float)snakeHeadY-5));
        Log.info("snakeHead: (" + snakeHeadX + ", " + snakeHeadY + ")");
        Log.info("food: (" + foodX + ", " + foodY + ")");
        ((KinematicBody)attachedNode.getValue()).setRotation(new Vector3f(0f, (float)rotation, 0f));

        // Check for collisions with the walls
        if (snakeHeadX < 0 || snakeHeadX > 10 || snakeHeadY < 0 || snakeHeadY > 10) {
            System.out.println("Game Over! The snake hit the wall.");
            return;
        }
        else
        // Check for collisions with itself
        if (hasSnake[snakeHeadX][snakeHeadY]) {
            System.out.println("Game Over! The snake collided with itself.");
            return;
        }
        else
        // Check if the snake has eaten the food
        {
            hasSnake[snakeHeadX][snakeHeadY] = true;
            if (snakeHeadX.equals(foodX) && snakeHeadY.equals(foodY)) {
                // Move the food to a new location
                do {
                    foodX = (int) (Math.random() * 10);
                    foodY = (int) (Math.random() * 10);
                    food.setPosition(new Vector3f((float) foodX - 5, 1f, (float) foodY - 5));
                } while (hasSnake[foodX][foodY]);
            }
        }

    }

    @Override
    public void clean() {

    }
}