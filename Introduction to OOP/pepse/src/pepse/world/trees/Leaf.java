package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.Random;

/**
 * The leaf object extending the block properties with more "leafy" properties.
 * The creation will always be from the Tree class.
 */
public class Leaf extends Block {
    private static final Color LEAF_COLOR = new Color(50, 200, 30);
    private Vector2 initPosition;
    private final Random random;
    private static final float FADEOUT_TIME = 20;
    private Transition<Float> xOscillator, aOscillator;

    /**
     * Initialize a new instance of the leaf.
     * Setting the dimensions, the mass, and initiate the lifeProcess of the leaf.
     *
     * @param topLeftCorner place for the leaf.
     * @param random        already with the right seed.
     */
    public Leaf(Vector2 topLeftCorner, Random random) {
        super(topLeftCorner, new RectangleRenderable(ColorSupplier.approximateColor(LEAF_COLOR)));
        this.random = random;
        initPosition = getCenter();
        setTag("leaf");
        //option to control the size of the leaves:
        setDimensions(new Vector2(27, 27));
        physics().setMass(0);
        lifeProcess();
    }

    /**
     * When the leaf have a collision, call movementStopper which will remove all transitions that
     * are in charge of the blowing in the wind properties.
     *
     * @param other
     * @param collision
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        movementStopper();
    }

    /**
     * Making the lead to fall, and initiating the movementMaker which making the leafs to be
     * blow in the wind and to move from side to side while falling.
     */
    private void lifeProcess() {
        // making the leaf to move
        float FALLING_TIME = 120;
        float fallingTime = random.nextFloat()* FALLING_TIME + FALLING_TIME;
        new ScheduledTask(this, fallingTime, false,
                this::fallMaker);
        new ScheduledTask(this, random.nextFloat()*2+2, false, this::movementMaker);
    }

    /**
     * Making the fall of the leaf to the ground, making the lead to fade out and initiating the regenerate method
     * when the leaf is completely faded out.
     */
    private void fallMaker() {
        float LEAF_SPEED_DOWN = 20;
        transform().setVelocityY(LEAF_SPEED_DOWN);
        float FALL_MAKER = 60;
        float regenerateTime = random.nextFloat()* FALL_MAKER + FALL_MAKER;
        renderer().fadeOut(FADEOUT_TIME, () -> new ScheduledTask(this, regenerateTime, false,
                this::regenerate));
    }

    /**
     * Making the leaf to regenerate in his original place when was created, with his full opacity, and velocity=0.
     */
    private void regenerate() {
        transform().setVelocityY(0);
        transform().setCenter(initPosition);
        renderer().setOpaqueness(1);
        lifeProcess();

    }

    /**
     * Making the leafs to blow in the wind and to move horizontally.
     */
    private void movementMaker() {
        float MAX_HORIZONTAL_SPEED = 10;
        float horizontalSpeed = random.nextFloat()* MAX_HORIZONTAL_SPEED + MAX_HORIZONTAL_SPEED;
        float cycleLength = 3;
        aOscillator = new Transition<Float>(this, a -> renderer().setRenderableAngle(a),
                -20f, 20f, Transition.LINEAR_INTERPOLATOR_FLOAT, cycleLength,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        xOscillator = new Transition<Float>(this, v -> transform().setVelocityX(v),
                -horizontalSpeed, horizontalSpeed, Transition.LINEAR_INTERPOLATOR_FLOAT,
                2 + random.nextFloat()*2+2, Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
    }

    /**
     * Stopping the movement of the leafs horizontally, and the effect of blowing in the wind (changing the angle).
     * Also stopping the leaf from moving down from the ground.
     */
    private void movementStopper() {
        removeComponent(aOscillator);
        removeComponent(xOscillator);
        transform().setVelocity(Vector2.ZERO);
    }

}

