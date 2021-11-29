package src.brick_strategies;
import danogl.collisions.Collision;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Ball;
import src.gameobjects.Puck;

import java.util.Random;

import static danogl.collisions.Layer.STATIC_OBJECTS;

public class PuckStrategy extends RemoveBrickStrategyDecorator{


    private CollisionStrategy toBeDecorated;
    private ImageReader imageReader;
    private SoundReader soundReader;

    public PuckStrategy(CollisionStrategy toBeDecorated,
                        ImageReader imageReader,
                        SoundReader soundReader) {
        super(toBeDecorated);

        this.toBeDecorated = toBeDecorated;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        if(toBeDecorated.getGameObjectCollection().removeGameObject(thisObj, STATIC_OBJECTS)) {
            counter.decrement();

            if(otherObj.getClass() == Ball.class ){
                for (int i = 0; i < 3; i++) {
                    float ballWidth = thisObj.getDimensions().x()/3;
                    float yBallVec = thisObj.getTopLeftCorner().y();
                    float xBallVec = thisObj.getTopLeftCorner().x();
                    xBallVec += ballWidth*i;
                    Puck newBall = createBall(imageReader, soundReader, new Vector2(xBallVec, yBallVec));
                    toBeDecorated.getGameObjectCollection().addGameObject(newBall);
                }


    }}


}
    private Puck createBall(ImageReader imageReader, SoundReader soundReader, Vector2 place) {
        //creating ball
        Renderable ballImage = imageReader.readImage("assets/mockBall.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");
        Puck ball = new Puck(place ,new Vector2(50, 50), ballImage, collisionSound);
        float ballVelX = -300;
        float ballVelY = -300;
        Random rand = new Random();
        if (rand.nextBoolean()) {
            ballVelX *= -1;
        }
        if (rand.nextBoolean()) {
            ballVelY *= -1;
        }
        ball.setVelocity(new Vector2(ballVelX, ballVelY));


        return ball;
    }

}


