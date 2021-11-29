package src.gameobjects;

import danogl.GameObject;

import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * The main paddle of the game.
 */
public class Paddle extends GameObject {

    private static final float MOVEMENT_SPEED = 300;
    private UserInputListener inputListener;
    private Vector2 windowDimensions;
    private int minDistanceFromEdge;
    private int numOfCollisions = 0;

    /**
     * This class is in charge of creating the game's paddle. It obtains the player's location, size,
     * picture to render, and a variable to receive input from. It produces a paddle object and
     * updates it in real time as the game progresses.
     *
     * @param topLeftCorner
     * @param dimensions
     * @param renderable
     * @param inputListener
     * @param windowDimensions
     * @param minDistanceFromEdge
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener, Vector2 windowDimensions, int minDistanceFromEdge) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.minDistanceFromEdge = minDistanceFromEdge;
    }

    /**
     * The method update has two implications: ensuring that the paddle cannot leave the screen
     * (i.e., cannot leave the borders) and receiving the player's input and reacting appropriately.
     *
     * @param deltaTime
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDir = Vector2.ZERO;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            movementDir = movementDir.add(Vector2.LEFT);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            movementDir = movementDir.add(Vector2.RIGHT);
        }


        setVelocity(movementDir.mult(MOVEMENT_SPEED));
        if (getTopLeftCorner().x() < minDistanceFromEdge) {
            setTopLeftCorner(new Vector2(5, getTopLeftCorner().y()));
        }
        if (getTopLeftCorner().x() > windowDimensions.x() - minDistanceFromEdge - getDimensions().x()) {
            setTopLeftCorner(new Vector2(windowDimensions.x() - getDimensions().x() - 5, getTopLeftCorner().y()));
        }
    }

}
