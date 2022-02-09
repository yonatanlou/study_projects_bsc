import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;

import pepse.world.trees.Tree;

import java.awt.*;


/**
 * The main of the whole simulation, managing the whole simulation objects and processes.
 */
public class PepseGameManager extends GameManager {
    private final Color SUN_HALO_COLOR = new Color(255, 255, 0, 20);
    private final int groundLayer = Layer.STATIC_OBJECTS + 5;
    private final int leafLayer = Layer.STATIC_OBJECTS + 6;
    private final int SEED = 42;
    private Vector2 dimensions;
    private int groundLeft, groundRight;
    Terrain terrain;
    Tree tree;
    Avatar avatar;
    private final int[] WorldLayers = {groundLayer, groundLayer - 1, groundLayer + 1, groundLayer - 10};


    @Override
    /**
     * This class is initializing all the objects of the simulation.
     * We're also managing the layers of the game, and deciding the whole collision rules between objects.
     *
     * @param imageReader
     * @param soundReader
     * @param inputListener
     * @param windowController
     */
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowController.setTargetFramerate(80);
        dimensions = windowController.getWindowDimensions();

        Sky.create(gameObjects(), windowController.getWindowDimensions(), Layer.BACKGROUND);

        int CYCLE_LENGTH = 30;
        GameObject sun = Sun.create(gameObjects(), Layer.BACKGROUND, windowController.getWindowDimensions(), CYCLE_LENGTH);

        terrain = new Terrain(gameObjects(), groundLayer,
                windowController.getWindowDimensions(), SEED);

        terrain.createInRange(0, (int) windowController.getWindowDimensions().x());
        tree = new Tree(gameObjects(), groundLayer,
                windowController.getWindowDimensions(), SEED);
        tree.createInRange(0, (int) windowController.getWindowDimensions().x());
        gameObjects().layers().shouldLayersCollide(leafLayer, groundLayer, true);

        GameObject sunHalo = SunHalo.create(gameObjects(), Layer.BACKGROUND + 10, sun, SUN_HALO_COLOR);
        sunHalo.addComponent(deltaTime -> sunHalo.setCenter(sun.getCenter()));
        
        Night.create(gameObjects(), Layer.FOREGROUND, windowController.getWindowDimensions(), CYCLE_LENGTH);

        float posX = windowController.getWindowDimensions().x() / 2;
        Avatar avatar = Avatar.create(gameObjects(), Layer.DEFAULT, Vector2.of(posX, terrain.groundHeightAt(posX) - 300),
                inputListener, imageReader);
        this.avatar = avatar;
        gameObjects().layers().shouldLayersCollide(groundLayer, Layer.DEFAULT, true);
        gameObjects().layers().shouldLayersCollide(groundLayer - 1, Layer.DEFAULT, true);
        setCamera(new Camera(avatar, Vector2.ZERO,
                windowController.getWindowDimensions(), windowController.getWindowDimensions()));



    }

    /**
     * Each update we are implemnting the infinte world in a few simple conditions, when we care to delete the
     * unused gameobjects (the one that are far enough from the camera.
     * Also, if the avatar is low in energy, the sky will be red.
     *
     * @param deltaTime
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float minX = camera().screenToWorldCoords(Vector2.ZERO).x();
        float maxX = camera().screenToWorldCoords(dimensions).x();
        int GROUND_INFINITE_INCREMENT = Block.SIZE * 5;
        while (minX < groundLeft) {
            terrain.createInRange(groundLeft - GROUND_INFINITE_INCREMENT, groundLeft);
            tree.createInRange(groundLeft - GROUND_INFINITE_INCREMENT, groundLeft);
            groundLeft -= GROUND_INFINITE_INCREMENT;
        }
        while (maxX > groundRight) {
            terrain.createInRange(groundRight, groundRight + GROUND_INFINITE_INCREMENT);
            tree.createInRange(groundRight, groundRight + GROUND_INFINITE_INCREMENT);
            groundRight += GROUND_INFINITE_INCREMENT;

        }
        if (maxX < groundRight - 2 * GROUND_INFINITE_INCREMENT || minX > groundLeft + 2 * GROUND_INFINITE_INCREMENT) {
            minX = GROUND_INFINITE_INCREMENT * (float) Math.floor(minX / GROUND_INFINITE_INCREMENT);
            maxX = GROUND_INFINITE_INCREMENT * (float) Math.ceil(maxX / GROUND_INFINITE_INCREMENT);
            deleteOutside(minX - Block.SIZE, maxX + Block.SIZE);
            groundLeft = (int) minX;
            groundRight = (int) maxX;


        }

        if (avatar.getEnergy() < 10) {
            Sky.setSkyColor(Color.decode("#ff5252"));
        } else {
            Sky.setSkyColor(Color.decode("#80C6E5"));
        }
    }

    /**
     * deleting the objects in the interval that cannot be seen by the camera.
     *
     * @param minX
     * @param maxX
     */
    private void deleteOutside(float minX, float maxX) {
        for (int layer : WorldLayers) {
            for (GameObject block : gameObjects().objectsInLayer(layer)) {
                if (block.getCoordinateSpace() == CoordinateSpace.WORLD_COORDINATES &&
                        (block.getCenter().x() < minX || block.getCenter().x() > maxX)) {
                    gameObjects().removeGameObject(block, layer);
                }
            }
        }
    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}
