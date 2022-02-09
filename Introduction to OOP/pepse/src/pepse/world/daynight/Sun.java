package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.*;

/**
 * The sun class is creating the sun of the simulation which behave like a real sun.
 * Have a cycle of day and night.
 * The {@link  pepse.world.daynight.SunHalo  SunHalo} is connecting with coordinates to the sun.
 */
public class Sun {
    private static final float SIZE = 2 * Block.SIZE;

    private static Vector2 calcSunPosition(Vector2 windowDimensions, float angle) {
        double a = Math.toRadians(angle);
        double wx = windowDimensions.x();
        double wy = windowDimensions.y();
        return new Vector2((float) ((Math.sin(a) + 1) / 2.1 * wx), (float) ((-Math.cos(a) + 1) / 2.1 * wy));
    }

    /**
     * Creating the sun GameObject.
     * Making a yellow oval shape, which is moving in a big circle in the world using the Transition class.
     *
     * @param gameObjects      gameObjects of the whole simulation.
     * @param layer            The layer which the sun is in.
     * @param windowDimensions Vector of the dimensions of the window.
     * @param cycleLength      The speed the duration of the night (and the day of course).
     * @return GameObject sun
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer, Vector2 windowDimensions,
                                    float cycleLength) {
        Renderable renderable = new OvalRenderable(Color.YELLOW);
        GameObject sun = new GameObject(Vector2.ZERO, new Vector2(SIZE, SIZE), renderable);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag("sun");
        gameObjects.addGameObject(sun, layer);
        new Transition<Float>(sun, angle -> sun.setCenter(calcSunPosition(windowDimensions, angle)),
                0f, 360f, Transition.LINEAR_INTERPOLATOR_FLOAT, cycleLength,
                Transition.TransitionType.TRANSITION_LOOP, null);
        return sun;
    }

}

