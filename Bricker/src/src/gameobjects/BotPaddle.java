package src.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;


/**
 * Object that extends paddle, but moving with different logic (automated. no input user).
 */
public class BotPaddle extends Paddle {
    private static final float PADDLE_SPEED = 150;
    private GameObject gameObjectToFollow;
    private boolean botState;

    /**
     * Creating the new botPaddle when the state refer for the good or bad bot.
     * GoodBot logic - when the ball is below, it will try to block the ball from falling down, and the opposite
     * when the ball beneath.
     * BadBot logic - same as good bot but the opposite.
     *
     * @param topLeftCorner
     * @param dimensions
     * @param renderable
     * @param inputListener
     * @param windowDimensions
     * @param minDistanceFromEdge
     * @param gameObjectToFollow
     * @param botState
     */
    public BotPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                     UserInputListener inputListener, Vector2 windowDimensions,
                     int minDistanceFromEdge, GameObject gameObjectToFollow, boolean botState) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions, minDistanceFromEdge);
        this.gameObjectToFollow = gameObjectToFollow;
        this.botState = botState;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDir = Vector2.ZERO;
        if (this.botState) {
            movementDir = goodBotPattern(movementDir);
        } else {
            movementDir = badBotPattern(movementDir);
        }


        setVelocity(movementDir.mult(PADDLE_SPEED));
    }

    private Vector2 badBotPattern(Vector2 movementDir) {
        if (gameObjectToFollow.getCenter().y() > getCenter().y()) {
            if (gameObjectToFollow.getCenter().x() < getCenter().x()) {
                movementDir = Vector2.LEFT;
            }
            if (gameObjectToFollow.getCenter().x() > getCenter().x()) {
                movementDir = Vector2.RIGHT;
            }
        }

        if (gameObjectToFollow.getCenter().y() < getCenter().y()) {
            if (gameObjectToFollow.getCenter().x() < getCenter().x()) {
                movementDir = Vector2.RIGHT;
            }
            if (gameObjectToFollow.getCenter().x() > getCenter().x()) {
                movementDir = Vector2.LEFT;
            }
        }
        return movementDir;
    }

    private Vector2 goodBotPattern(Vector2 movementDir) {
        if (gameObjectToFollow.getCenter().y() > getCenter().y()) {
            if (gameObjectToFollow.getCenter().x() < getCenter().x()) {
                movementDir = Vector2.RIGHT;
            }
            if (gameObjectToFollow.getCenter().x() > getCenter().x()) {
                movementDir = Vector2.LEFT;
            }
        }

        if (gameObjectToFollow.getCenter().y() < getCenter().y()) {
            if (gameObjectToFollow.getCenter().x() < getCenter().x()) {
                movementDir = Vector2.LEFT;
            }
            if (gameObjectToFollow.getCenter().x() > getCenter().x()) {
                movementDir = Vector2.RIGHT;
            }
        }
        return movementDir;
    }


}
