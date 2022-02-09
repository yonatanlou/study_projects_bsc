package pepse.world;


import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * The avatar class is creating a figure in the simulation that can move inside the world and even jump and fly.
 */
public class Avatar extends GameObject {
    private static final float VELOCITY_X = 300;
    private static final float VELOCITY_Y = -450;
    private static final float MAX_VELOCITY_Y = 550;
    private static final float GRAVITY = 500;
    private static final String[] imagePaths = {"assets/avatar_static.png", "assets/avatar_dynamic1.png", "assets/avatar_dynamic2.png"};
    private float energy;
    private final float MAX_ENERGY = 100F;
    private static ImageReader imageReaderStatic;
    UserInputListener inputListener;


    /**
     * The constructor of the class which inherit from the GameObject class.
     * Defining the base energy for the avatar.
     *
     * @param topLeftCorner Init place of the avatar.
     * @param dimensions The size of the avatar.
     * @param renderable Renderble object to render the image of the figure.
     */
    public Avatar(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        this.energy = 100F;
    }

    /**
     * Creating the avatar with the base image figure, make sure that he can stand
     * on the ground, and setting his gravity.
     *
     * @param gameObjects gameObjects of the whole simulation.
     * @param layer Which layer should the avatar appear.
     * @param topLeftCorner Init place of the avatar.
     * @param inputListener The input of the user to move the avatar.
     * @param imageReader Image renderble to render the avatar image.
     * @return Avatar object.
     */
    public static Avatar create(GameObjectCollection gameObjects, int layer,
                                Vector2 topLeftCorner, UserInputListener inputListener,
                                ImageReader imageReader) {
        imageReaderStatic = imageReader;
        ImageRenderable image = imageReader.readImage(imagePaths[1], true);
        Avatar avatar = new Avatar(topLeftCorner.add(new Vector2(-image.width() / 2, 0)),
                new Vector2(Block.SIZE, Block.SIZE), image);
        avatar.inputListener = inputListener;
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        avatar.transform().setAccelerationY(GRAVITY);
        gameObjects.addGameObject(avatar, layer);
        return avatar;
    }

    /**
     * On each update of the frames, we checking where the user want the avatar to go.
     * The possibilities are: to go LEFT and RIGHT, jump with SPACE, and fly with SPACE and SHIFT.
     * When the avatar is moving, the avatar is changing his behviour with the action he is doing.
     * When the avatar is in motion, his legs will move.
     * When the avatar is flying, his angle will be in 270 degrees.
     *
     * @param deltaTime Time of the simulation
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        this.renderer().setRenderableAngle(0);
        float xVel = 0;
        if(transform().getVelocity().y() > MAX_VELOCITY_Y) {
            transform().setVelocityY(MAX_VELOCITY_Y);
        }
        if (transform().getVelocity().y() == 0 && energy <= MAX_ENERGY) {
            energy += 0.5;
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            xVel -= VELOCITY_X;
            this.renderer().setIsFlippedHorizontally(true);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            xVel += VELOCITY_X;
            this.renderer().setIsFlippedHorizontally(false);
        }
        transform().setVelocityX(xVel);


        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0) {
            transform().setVelocityY(VELOCITY_Y);
        }
        //flying
        if ((inputListener.isKeyPressed(KeyEvent.VK_SPACE)) && (inputListener.isKeyPressed(KeyEvent.VK_SHIFT)) && (energy > 0)) {
            transform().setVelocityY(VELOCITY_Y);
            energy -= 0.5;
            this.renderer().setRenderableAngle(270);
        }
        if ((inputListener.isKeyPressed(KeyEvent.VK_Z)) &&
                        (energy > 0) && inputListener.isKeyPressed(KeyEvent.VK_LEFT))  {
            xVel=0;
            xVel -= VELOCITY_X*3;
            energy -= 0.5;
            transform().setVelocityX(xVel);
        }




        renderableManager();
    }

    /**
     * Control the renderbles when the avatar is moving.
     * When the avatar is on move, we make the Renderble as AnimationRendreble which allowing
     * the image to be more flexible in terms of motion.
     */
    private void renderableManager() {
        if (transform().getVelocity().y() == 0 && transform().getVelocity().x() == 0) {
            String[] imagePaths = {"assets/avatar_static.png", "assets/avatar_dynamic1.png", "assets/avatar_dynamic2.png"};
            Renderable animationRenderable = new AnimationRenderable(imagePaths, imageReaderStatic, true, 0.1);
            this.renderer().setRenderable(animationRenderable);
        }

    }



    /**
     * Get the current amount of energy of the avatar.
     * @return  float energy.
     */
    public float getEnergy() {
        return energy;
    }
}