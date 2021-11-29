package src;

import danogl.collisions.GameObjectCollection;
import src.brick_strategies.BrickStrategyFactory;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.util.Counter;
import src.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;
public class BrickerGameManager extends GameManager {

    public final static int BORDER_WIDTH = 20;

    private final static int BRICK_HEIGHT = 15;
    private final static int BRICK_ROWS = 8;
    private final static int BRICK_COLS = 5;
    private final static int SPACE_BETWEEN_BRICKS = 1;

    private static final float BALL_SPEED = 300;
    private static final float PADDLE_HEIGHT = 20;
    private static final float PADDLE_WIDTH = 100;



    private Ball ball;
    private Vector2 windowDimensions;
    private SoundReader soundReader;
    private WindowController windowController;
    private Counter LivesCounter = new Counter(4);
    private int minDistanceFromEdge;
    private final Counter bricksCounter = new Counter(0);
    private UserInputListener inputListener;
    private GraphicLifeCounter graphicLifeCounter;
    private NumericLifeCounter numericLifeCounter;
    private static final float EPSILOM = 1/1000;

    public BrickerGameManager(String windowTitle, Vector2 windowDimension) {
        super(windowTitle, windowDimension);

    }


    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener,
                               WindowController windowController) {
        this.soundReader = soundReader;
        this.windowController = windowController;
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        windowDimensions = windowController.getWindowDimensions();
        ball = createBall(imageReader, soundReader, windowController);

        GameObject background = createBackground(imageReader, windowController);
        //will be use later
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);



        createBorders(windowDimensions);
        createBricks(imageReader, inputListener);
        NumericLifeCounter numericLifeCounter = new NumericLifeCounter(LivesCounter, Vector2.ZERO, Vector2.ZERO, gameObjects());
        this.numericLifeCounter = numericLifeCounter;

        Renderable heartImage = imageReader.readImage("assets/heart.png", false);

        GraphicLifeCounter graphicLifeCounter = new GraphicLifeCounter(Vector2.ZERO, new Vector2(20, 20),
                LivesCounter, heartImage, gameObjects(), 4);
        this.graphicLifeCounter = graphicLifeCounter;


        Renderable paddleImage = imageReader.readImage("assets/paddle.png", false);
        createPaddle(paddleImage, inputListener, windowDimensions, minDistanceFromEdge);



    }

    private GameObject createBackground(ImageReader imageReader, WindowController windowController) {
        GameObject background = new GameObject(
                Vector2.ZERO,
                windowController.getWindowDimensions(),
                imageReader.readImage("assets/DARK_BG2_small.jpeg", false));
        gameObjects().addGameObject(background, Layer.BACKGROUND);
        return background;
    }

    private void createBricks(ImageReader imageReader, UserInputListener inputListener) {
        //create the bricks
        bricksCounter.reset();
        Renderable brickImage = imageReader.readImage("assets/brick.png", true);
        BrickStrategyFactory brickStrategyFactory = new BrickStrategyFactory(gameObjects(), this,
                imageReader, soundReader, inputListener, windowController, windowDimensions );
        float totalXspace = windowDimensions.x() - BORDER_WIDTH * 2 - BRICK_COLS * SPACE_BETWEEN_BRICKS - 1-3*EPSILOM;
        float brickWidth = totalXspace / BRICK_COLS;
        for (int i = 0; i < BRICK_COLS; i++) {
            for (int j = 0; j < BRICK_ROWS; j++) {
                Brick brick = new Brick(new Vector2(BORDER_WIDTH + i *
                        (SPACE_BETWEEN_BRICKS + brickWidth)+EPSILOM, j * (BRICK_HEIGHT + SPACE_BETWEEN_BRICKS)),
                        new Vector2(brickWidth, BRICK_HEIGHT), brickImage,
                        brickStrategyFactory.getStrategy(), bricksCounter);
                bricksCounter.increment();
                gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
            }


        }

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkForGameEnd(deltaTime);

        }



    private void checkForGameEnd(float deltaTime) {
        float ballHeight = ball.getCenter().y();
        String prompt = "";

        if (bricksCounter.value() == 0) {
            prompt += "You Win, Play again?";
            if (windowController.openYesNoDialog(prompt)) {
                windowController.resetGame();
            } else
                windowController.closeWindow();
        }

        if (LivesCounter.value() == 0) {
            prompt += "You Lose, Play again?";
            if (windowController.openYesNoDialog(prompt)) {
                windowController.resetGame();
                LivesCounter = new Counter(4);
            } else
                windowController.closeWindow();

        }


        if (ballHeight > windowDimensions.y()) {
            LivesCounter.decrement();
            repositionBall(ball);
            numericLifeCounter.update(deltaTime);
            graphicLifeCounter.update(deltaTime);

        }


    }

    private Ball createBall(ImageReader imageReader, SoundReader soundReader, WindowController windowController) {
        //creating ball
        Renderable ballImage = imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");
        ball = new Ball(Vector2.ZERO, new Vector2(50, 50), ballImage, collisionSound);
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean()) {
            ballVelX *= -1;
        }
        if (rand.nextBoolean()) {
            ballVelY *= -1;
        }
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
        Vector2 windowDimensions = windowController.getWindowDimensions();
        ball.setCenter(windowDimensions.mult(0.5f));
        gameObjects().addGameObject(ball);
        return ball;
    }

    private void createBorders(Vector2 windowDimensions) {
        //create borders
        gameObjects().addGameObject(
                new GameObject(Vector2.ZERO, new Vector2(BORDER_WIDTH, windowDimensions.y()), null), Layer.STATIC_OBJECTS
        );


        gameObjects().addGameObject(
                new GameObject(new Vector2(windowDimensions.x() - BORDER_WIDTH, 0),
                        new Vector2(BORDER_WIDTH, windowDimensions.y()), null), Layer.STATIC_OBJECTS
        );

        gameObjects().addGameObject(
                new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(), BORDER_WIDTH), null), Layer.STATIC_OBJECTS
        );
    }


    private void createPaddle(Renderable paddleImage, UserInputListener inputListener, Vector2 windowDimensions, int minDistanceFromEdge) {
        GameObject userPaddle = new Paddle(Vector2.ZERO, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                paddleImage, inputListener, windowDimensions, minDistanceFromEdge);

        userPaddle.setCenter(new Vector2((windowDimensions.x() / 2), (int) windowDimensions.y() - 30));
        gameObjects().addGameObject(userPaddle);

    }

    public void repositionBall(GameObject ball) {
        ball.setCenter(windowDimensions.mult(0.5f));
    }

    public GameObjectCollection getGameObject(){
        return gameObjects();
    }

    public Ball getBall() {
        return ball;
    }

    public WindowController getWindowController() {
        return windowController;
    }


    public static void main(String[] args) {
        BrickerGameManager gameManager = new BrickerGameManager("Bricker", new Vector2(700, 500));
        gameManager.run();

    }


}
