package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;


import java.awt.*;

/**
 * This class is creating the halo of the sun.
 */
public class SunHalo {
    private static GameObject sun;


    /**
     * Creating a big oval (5x bigger than the sun).
     *
     * @param gameObjects
     * @param layer
     * @param sun
     * @param color
     * @return
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            GameObject sun,
            Color color
    ) {
        SunHalo.sun = sun;
        float SIZE = sun.getDimensions().x() * 5;
        Renderable renderable = new OvalRenderable(color);
        GameObject halo = new GameObject(Vector2.ZERO, new Vector2(SIZE, SIZE), renderable);
        halo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        halo.setTag("halo");
        gameObjects.addGameObject(halo, layer);
        return halo;
    }

    /**
     * Making it possible to the sanHalo to be in same coordinates as the sun in each update.
     */
    @FunctionalInterface
    public interface Component {
        void update(float deltaTime);
    }


}
