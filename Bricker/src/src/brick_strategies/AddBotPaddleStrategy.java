package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;
import src.gameobjects.BotPaddle;


import java.util.Random;

import static danogl.collisions.Layer.STATIC_OBJECTS;

/**
 * Adding a bot paddle that can help of distract the user (0.5 prob).
 */
public class AddBotPaddleStrategy extends RemoveBrickStrategyDecorator {

    private CollisionStrategy toBeDecorated;
    private ImageReader imageReader;
    private UserInputListener inputListener;
    private Vector2 windowDimensions;
    private BrickerGameManager gameManager;
    private BotPaddle newPaddle;

    /**
     * @param toBeDecorated The base collision strategy
     * @param imageReader imageReader
     * @param inputListener
     * @param windowDimensions
     * @param gameManager
     */
    public AddBotPaddleStrategy(CollisionStrategy toBeDecorated,
                                ImageReader imageReader,
                                UserInputListener inputListener,
                                Vector2 windowDimensions, BrickerGameManager gameManager) {
        super(toBeDecorated);
        this.toBeDecorated = toBeDecorated;
        this.imageReader = imageReader;
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.gameManager = gameManager;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        if(toBeDecorated.getGameObjectCollection().removeGameObject(thisObj, STATIC_OBJECTS)) {
            counter.decrement();
            BotPaddle newPaddle = createBotPaddle(toBeDecorated, imageReader, inputListener, windowDimensions);
            this.newPaddle = newPaddle;
            toBeDecorated.getGameObjectCollection().addGameObject(newPaddle);




        }






    }
    private BotPaddle createBotPaddle(CollisionStrategy toBeDecorated, ImageReader imageReader, UserInputListener inputListener, Vector2 windowDimensions) {
        boolean botState;
        Random rnd = new Random();
        botState = rnd.nextBoolean();
        Renderable paddleImageGood = imageReader.readImage("assets/botGood.png", false);
        Renderable paddleImageBad = imageReader.readImage("assets/botBad.png", false);
        if(botState) {
            float y = rnd.nextInt((490 - 250) + 1) + 250;

            BotPaddle botPaddle = new BotPaddle(Vector2.ZERO, new Vector2(100, 20),
                    paddleImageGood, inputListener, windowDimensions,5, gameManager.getBall(), botState);
            botPaddle.setCenter(new Vector2((windowDimensions.x() / 2),  y));
            return botPaddle;
        }

        else {
            BotPaddle botPaddle = new BotPaddle(Vector2.ZERO, new Vector2(100, 20),
                    paddleImageBad, inputListener, windowDimensions,5, gameManager.getBall(), botState);
            botPaddle.setCenter(new Vector2((windowDimensions.x() / 2),  (windowDimensions.y()/2)));
            return botPaddle;
        }

    }


}
