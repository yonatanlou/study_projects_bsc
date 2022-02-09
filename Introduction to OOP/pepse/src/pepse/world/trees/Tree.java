package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import pepse.world.Terrain;

import java.awt.*;
import java.util.Objects;
import java.util.Random;

/**
 * The tree class is creating trees with leafs on the top of terrain.
 */
public class Tree extends Terrain {
    private static final Color STEM_COLOR = new Color(100, 50, 20);
    private final Random random;
    private final GameObjectCollection gameObjects;
    private final int STEM_SIZE = 5;
    private final int LEAF_COL_SIZE = 5;
    private final int STEM_GAP = 3 * Block.SIZE;
    private final int seed;

    /**
     * The constructor of the tree which inherits grom GameObject.
     * Creating the Random seed for the whole object.
     *
     * @param gameObjects      gameObjects of the whole simulation.
     * @param groundLayer      The layer of the terrain.
     * @param windowDimensions Vector of the dimensions of the window.
     * @param seed             The seed for the randomness
     */
    public Tree(GameObjectCollection gameObjects, int groundLayer, Vector2 windowDimensions, int seed) {
        super(gameObjects, groundLayer, windowDimensions, seed);
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.seed = seed;
        random = new Random(seed);
    }

    /**
     * Creating the stem of the trees on the terrain with random order.
     * Calling the function leafMaker which is making the leafs on the stem.
     *
     * @param minX The minimal range for the x coord to make the trees.
     * @param maxX The maximal range for the x coord to make the trees.
     */
    public void createInRange(int minX, int maxX) {
        //לColors for the tree components
        Renderable stemRenderable = new RectangleRenderable(ColorSupplier.approximateColor(STEM_COLOR));

        for (int x = Block.SIZE * (minX / Block.SIZE); x <= maxX; x += Block.SIZE) { // Creating columns of blocks
            random.setSeed(Objects.hash(seed, x));
            if (random.nextInt(100) < 10) {
                float fminY = groundHeightAt(x);
                int minY = Block.SIZE * (int) (fminY / Block.SIZE);
                for (int y = minY; y >= minY - STEM_SIZE * Block.SIZE; y -= Block.SIZE) { //making the stem
                    Block stem = new Block(new Vector2(x, y), stemRenderable);
                    stem.setTag("stem");
                    gameObjects.addGameObject(stem, groundLayer - 1);
                }
                leafMaker(x, minY - STEM_SIZE * Block.SIZE); //starting from the top of the stem
            }
        }
    }

    /**
     * Making the leafs for the stem while using the Leaf class
     *
     * @param x int
     * @param y int
     * @see Leaf
     */
    private void leafMaker(int x, int y) {
        //calculating the space which the for loop should run
        for (int leafX = x - (Block.SIZE); leafX <= x + (Block.SIZE); leafX += Block.SIZE) {
            //added 3 blocks for the tree will have a gap for its stem
            for (int leafY = y - STEM_GAP; leafY <= y + LEAF_COL_SIZE * Block.SIZE - STEM_GAP; leafY += Block.SIZE) {
                Block leaf = new Leaf(new Vector2(leafX, leafY), random);
                gameObjects.addGameObject(leaf, groundLayer + 1);
            }
        }
    }
}
