package src.brick_strategies;


import danogl.GameObject;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;
import src.gameobjects.Ball;
import src.gameobjects.BallCollisionCountdownAgent;

import static danogl.collisions.Layer.STATIC_OBJECTS;

/**
 * Extends the RemoveBrickStrategyDecorator, when this brick is getting smashed, the camera that following the game,
 * is starting to following only the ball (until the ballCollisionAgent is stopping the strategy).
 */
public class ChangeCameraStrategy extends RemoveBrickStrategyDecorator {
    private CollisionStrategy toBeDecorated;
    private BrickerGameManager gameManager;
    private BallCollisionCountdownAgent ballCollisionCountdownAgent;

    /**
     * @param toBeDecorated
     * @param windowController
     * @param gameManager
     */
    public ChangeCameraStrategy(CollisionStrategy toBeDecorated,
                                WindowController windowController, BrickerGameManager gameManager) {
        super(toBeDecorated);
        this.toBeDecorated = toBeDecorated;
        this.gameManager = gameManager;

    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        if (gameManager.getGameObject().removeGameObject(thisObj, STATIC_OBJECTS)) {
            counter.decrement();
            Ball ball = (Ball) otherObj;
            ball.setCollisionCount(0);
            if (gameManager.getCamera() == null) {
                turnOnCamera();
                ballCollisionCountdownAgent = new BallCollisionCountdownAgent((Ball) otherObj,
                        this, 4);
                toBeDecorated.getGameObjectCollection().addGameObject(ballCollisionCountdownAgent);
            }

        }
    }

    public void turnOffCameraChange() {
        gameManager.setCamera(null);
    }

    private void turnOnCamera() {
        gameManager.setCamera(new Camera(
                gameManager.getBall(),            //object to follow
                Vector2.ZERO,    //follow the center of the object
                gameManager.getWindowController().getWindowDimensions().mult(1.2f),  //widen the frame a bit
                gameManager.getWindowController().getWindowDimensions()   //share the window dimensions
        ));
    }

}
