package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;

import java.util.Random;


/**
 * Making the terrain of the whole simulation.
 */
public class Terrain {
    Random random;
    private final GameObjectCollection gameObjects;
    protected int groundLayer;
    private float groundHeightAtX0;
    private final float phase1;
    private final float phase2;
    private final float phase3;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    protected static final int TERRAIN_DEPTH = 25;

    /**
     * The contructor of the terrain, which initate the random object with a given seed,
     * The initate ground height, and the base random numbers (phases) that will help to generate the
     * random sequence function (groundHeightAt).
     *
     * @param gameObjects
     * @param groundLayer
     * @param windowDimensions
     * @param seed
     */
    public Terrain(GameObjectCollection gameObjects, int groundLayer, Vector2 windowDimensions, int seed) {
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        random = new Random(seed);
        groundHeightAtX0 = windowDimensions.y() * 2 / 3;
        phase1 = random.nextFloat() * 2 * (float) Math.PI;
        phase2 = random.nextFloat() * 2 * (float) Math.PI;
        phase3 = random.nextFloat() * 2 * (float) Math.PI;
    }

    /**
     * Computing the y coord for a given x with a function that uses the 3 random phases from the constructor,
     * with more constants noise which make the surface random and not too extreme.
     *
     * @param x x coord
     * @return the y coord the supposed to fit that particular x coord
     */
    public float groundHeightAt(float x) {
        x /= Block.SIZE * 5;
        float y = (float) (Math.sin(x + phase1) + 3 * Math.sin(.3 * Math.PI * x + phase2) + 2 * Math.sin(.5 * Math.E * x + phase3));
        return (y * Block.SIZE + groundHeightAtX0);
    }


    /**
     * Creting the terrain with blocks given a minimum and maximum x coord using the block class.
     *
     * @param minX
     * @param maxX
     */
    public void createInRange(int minX, int maxX) {
        for (int x = Block.SIZE * (minX / Block.SIZE); x <= maxX; x += Block.SIZE) { // Creating columns of blocks
            float fminY = groundHeightAt(x);
            int minY = Block.SIZE * (int) (fminY / Block.SIZE);
            int maxY = minY + TERRAIN_DEPTH * Block.SIZE;
            for (int y = minY; y <= maxY; y += Block.SIZE) {  // Creating lines of blocks
                Renderable renderable = new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
                Block block = new Block(new Vector2(x, y), renderable);
                block.setTag("ground");
                if (y <= minY + Block.SIZE)
                    gameObjects.addGameObject(block, groundLayer);
                else
                    gameObjects.addGameObject(block, groundLayer - 10);
            }
        }
    }
}
