package src.gameobjects;

import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * The same as ball, just that we want to classify between the puck and the main ball.
 */
public class Puck extends Ball {

    /**
     * @param topLeftCorner
     * @param dimensions
     * @param renderable
     * @param collisionSound
     */
    public Puck(Vector2 topLeftCorner,
                Vector2 dimensions,
                Renderable renderable,
                Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable, collisionSound);
    }
}
