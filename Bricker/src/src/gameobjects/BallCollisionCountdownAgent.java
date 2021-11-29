package src.gameobjects;

import danogl.GameObject;
import danogl.util.Vector2;
import src.brick_strategies.ChangeCameraStrategy;

/**
 * Class that wrapping the ball object and triggering the ChangeCamera strategy.
 */
public class BallCollisionCountdownAgent extends GameObject {
    private Ball ball;
    private ChangeCameraStrategy owner;
    private int countDownValue;

    /**
     * @param ball           Ball
     * @param owner          ChangeCameraStrategy object
     * @param countDownValue How many collisions till the camerea is off.
     */
    public BallCollisionCountdownAgent(Ball ball,
                                       ChangeCameraStrategy owner,
                                       int countDownValue) {
        super(Vector2.ZERO, Vector2.ZERO, null);

        this.ball = ball;
        this.owner = owner;
        this.countDownValue = countDownValue;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (ball.getCollisionCount() == countDownValue) {
            turnOffCamera();
            ball.setCollisionCount(0);
        }

    }

    private void turnOffCamera() {
        owner.turnOffCameraChange();
    }
}

