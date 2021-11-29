package src.brick_strategies;

import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import src.BrickerGameManager;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;

import java.util.Random;

/**
 * The factory class is used for generating one of the brick removal strategies. It assigns (randomly)
 * a certain strategy from the package.
 */
public class BrickStrategyFactory {
    private CollisionStrategy toBeDecorated;
    private final Random rnd;
    private GameObjectCollection gameObjectCollection;
    private BrickerGameManager gameManager;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;
    private WindowController windowController;
    private Vector2 windowDimensions;

    /**
     * @param gameObjectCollection
     * @param gameManager
     * @param imageReader
     * @param soundReader
     * @param inputListener
     * @param windowController
     * @param windowDimensions
     */
    public BrickStrategyFactory(GameObjectCollection gameObjectCollection,
                                src.BrickerGameManager gameManager,
                                ImageReader imageReader,
                                SoundReader soundReader,
                                UserInputListener inputListener,
                                WindowController windowController,
                                Vector2 windowDimensions) {
        this.gameObjectCollection = gameObjectCollection;
        this.gameManager = gameManager;

        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowController = windowController;
        this.windowDimensions = windowDimensions;
        Random rnd = new Random();
        this.rnd = rnd;

    }

    /**
     * First, the method is containing all the collision strategies in an array.
     * Then, pick up one randomly.
     * If the random number is 5, then we the random one of the pure strategies, and use it as a decorator.
     *
     * @return Collision strategy
     */

    public CollisionStrategy getStrategy() {
        int indexStrategy = rnd.nextInt(6);
        if (indexStrategy == 5) {
            toBeDecorated = new RemoveBrickStrategy(gameObjectCollection);
            //double strategy
            toBeDecorated = getStrategyUtil();
            return toBeDecorated;


        }
        //one strategy
        toBeDecorated = new RemoveBrickStrategy(gameObjectCollection);
        toBeDecorated = getStrategyUtil();
        return toBeDecorated;

    }

    private CollisionStrategy getStrategyUtil() {
        CollisionStrategy[] collisionStrategies = getCollisionStrategies(toBeDecorated);
        int currStrategy = rnd.nextInt(5);
        return collisionStrategies[currStrategy];

    }

    private CollisionStrategy[] getCollisionStrategies(CollisionStrategy toBeDecorated) {
        CollisionStrategy[] collisionStrategies = new CollisionStrategy[6];
        CollisionStrategy collisionStrategy1 = new RemoveBrickStrategy(gameObjectCollection);
        CollisionStrategy collisionStrategy2 = new AddPaddleStrategy(toBeDecorated,
                imageReader, inputListener, windowDimensions);
        CollisionStrategy collisionStrategy3 = new ChangeCameraStrategy(toBeDecorated, windowController,
                gameManager);
        CollisionStrategy collisionStrategy4 = new PuckStrategy(toBeDecorated, imageReader,
                soundReader);
        CollisionStrategy collisionStrategy5 = new AddBotPaddleStrategy(toBeDecorated,
                imageReader, inputListener, windowDimensions, gameManager);

        collisionStrategies[0] = collisionStrategy1;
        collisionStrategies[1] = collisionStrategy2;
        collisionStrategies[2] = collisionStrategy3;
        collisionStrategies[3] = collisionStrategy4;
        collisionStrategies[4] = collisionStrategy5;
        return collisionStrategies;
    }

}


