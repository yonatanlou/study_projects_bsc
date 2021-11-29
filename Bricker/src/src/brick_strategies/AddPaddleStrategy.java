package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Paddle;
import src.gameobjects.MockPaddle;

import static danogl.collisions.Layer.STATIC_OBJECTS;

/**
 * This layer is used to add a different paddle when a brick with this feature is got smashed.
 * This class is checking whether we already have a mock paddle active using the variable isinitialized.
 * This is a  decorator extension, which means that functions of this class are attached to the brick via the
 * BrickStrategyFactory. When launching BrickStrategyFactory from
 * this class we extend the decorator which used to assign functionality to the brick.
 */
public class AddPaddleStrategy extends RemoveBrickStrategyDecorator {
    private CollisionStrategy toBeDecorated;
    private ImageReader imageReader;
    private UserInputListener inputListener;
    private Vector2 windowDimensions;

    /**
     * @param toBeDecorated
     * @param imageReader
     * @param inputListener
     * @param windowDimensions
     */
    public AddPaddleStrategy(CollisionStrategy toBeDecorated,
                             ImageReader imageReader,
                             UserInputListener inputListener,
                             Vector2 windowDimensions) {
        super(toBeDecorated);
        this.toBeDecorated = toBeDecorated;
        this.imageReader = imageReader;
        this.inputListener = inputListener;

        this.windowDimensions = windowDimensions;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        toBeDecorated.getGameObjectCollection().removeGameObject(thisObj, STATIC_OBJECTS);
        counter.decrement();
        MockPaddle newPaddle = createPaddle(toBeDecorated, imageReader, inputListener, windowDimensions);
        if (!MockPaddle.isInstantiated) {
            MockPaddle.isInstantiated = true;
            toBeDecorated.getGameObjectCollection().addGameObject(newPaddle);
        }


    }

    private MockPaddle createPaddle(CollisionStrategy toBeDecorated, ImageReader imageReader, UserInputListener inputListener, Vector2 windowDimensions) {
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", false);
        MockPaddle newPaddle = new MockPaddle(Vector2.ZERO, new Vector2(100, 20),
                paddleImage, inputListener, windowDimensions, toBeDecorated.getGameObjectCollection(), 5, 3); ////need to take the vector from gamemanager

        newPaddle.setCenter(new Vector2((windowDimensions.x() / 2), windowDimensions.y() / 2));
        return newPaddle;
    }


}
