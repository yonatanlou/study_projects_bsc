package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * This class is responsible for creating a paddle, which very similar to the paddle class except its
 * position is in the center of the screen, and it disappears after the variable numOfCollisionsToDissapear (sent through the class
 * constructor) equal the num of collisions of the paddle.
 */
public class MockPaddle extends Paddle {
    private int numOfCollisions;
    private GameObjectCollection gameObjectCollection;
    private int numOfCollisionsToDissapear;
    public static boolean isInstantiated;

    /**
     * @param topLeftCorner
     * @param dimensions
     * @param renderable
     * @param inputListener
     * @param windowDimensions
     * @param gameObjectCollection
     * @param minDistanceFromEdge
     * @param numOfCollisionsToDissapear
     */
    public MockPaddle(Vector2 topLeftCorner,
                      Vector2 dimensions,
                      Renderable renderable,
                      UserInputListener inputListener,
                      Vector2 windowDimensions,
                      GameObjectCollection gameObjectCollection,
                      int minDistanceFromEdge,
                      int numOfCollisionsToDissapear) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions, minDistanceFromEdge);
        this.gameObjectCollection = gameObjectCollection;
        this.numOfCollisionsToDissapear = numOfCollisionsToDissapear;

    }


    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (numOfCollisions == numOfCollisionsToDissapear) {
            removeObject();
            isInstantiated = false;
        }
        numOfCollisions += 1;

    }


    private void removeObject() {
        gameObjectCollection.removeGameObject(this);
    }
}
