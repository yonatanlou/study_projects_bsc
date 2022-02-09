package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Creating the effect of night in the simulation.
 */
public class Night {
    private static Renderable renderable = new RectangleRenderable(Color.BLACK);
    private static final float MIDNIGHT_OPACITY = 0.6f;
    private static GameObject nightStatic;

    /**
     * We are creating a black rectangle in the dimensions of the window.
     * With the help of the Transition class, we are controlling the time periodically of the
     * cycles of the night.
     * We can also control on the opacity of the night opacity.
     *
     * @param gameObjects      gameObjects of the whole simulation.
     * @param layer            Which layer should the night appear.
     * @param windowDimensions Vector of the dimensions of the window.
     * @param cycleLength      The speed the duration of the night (and the day of course).
     * @return GameObject night
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer, Vector2 windowDimensions,
                                    float cycleLength) {
        GameObject night = new GameObject(Vector2.ZERO, windowDimensions, renderable);
        night.setTag("night");
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(night, layer);
        new Transition<Float>(night, night.renderer()::setOpaqueness, 0f,
                MIDNIGHT_OPACITY, Transition.CUBIC_INTERPOLATOR_FLOAT, cycleLength / 2,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        nightStatic = night;
        return night;
    }


}
