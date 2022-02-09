package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Creating the sky of the simulation, with a big rectangle with a default blue color.
 */
public class Sky {
    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");
    private static GameObject skyStatic;

    /**
     * Creating the sky with a big rectangle with the default color, with the window dimensions size.
     *
     * @param gameObjects      gameObjects of the whole simulation.
     * @param windowDimensions Vector of the dimensions of the window.
     * @param skyLayer         The layer of the sky.
     * @return GameObject of the sky.
     */
    public static GameObject create(GameObjectCollection gameObjects,
                                    Vector2 windowDimensions, int skyLayer) {
        GameObject sky = new GameObject(Vector2.ZERO, windowDimensions, new RectangleRenderable(BASIC_SKY_COLOR));
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sky, skyLayer);
        sky.setTag("sky");
        skyStatic = sky;
        return sky;
    }

    /**
     * A simple method which can change the color of the sky.
     *
     * @param color The color of the sky we want to change for.
     */
    public static void setSkyColor(Color color) {
        Renderable renderable = new RectangleRenderable(color);
        skyStatic.renderer().setRenderable(renderable);


    }

}
