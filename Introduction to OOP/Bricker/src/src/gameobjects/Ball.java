package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;


public class Ball extends GameObject {
    private Sound collisionSound;
    private int ballCollisionCounter;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner  Position of the object, in window coordinates (pixels).
     *                       Note that (0,0) is the top-left corner of the window.
     * @param dimensions     Width and height in window coordinates.
     * @param renderable     The renderable representing the object. Can be null, in which case
     * @param collisionSound The collisionsound is enable the gameobject to make sound.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;

    }

    /**
     * @param other     the other object that collide with the ball.
     * @param collision information regarding this collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        collisionSound.play();
        ballCollisionCounter += 1;
    }

    /**
     * @return ballCollisionCounter
     */
    public int getCollisionCount() {
        return ballCollisionCounter;
    }

    public void setCollisionCount(int n) {
        ballCollisionCounter = n;
    }


}
