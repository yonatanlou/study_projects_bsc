package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * This class is in responsible for designing the graphic representation of the player's remaining life.
 * It uses the gameObjectCollection to construct a game object to display the lives left,
 * and it renders the graphic provided in assets to represent each unit of life.
 */
public class GraphicLifeCounter extends GameObject {
    private Vector2 widgetDimensions;
    private Counter livesCounter;
    private Renderable widgetRenderable;
    private GameObjectCollection gameObjectsCollection;
    private int numOfLives;
    private String[] heartsOrder = {"0", "1", "2", "3", "4"};

    /**
     * Creating the hearts object by the numOfLives.
     * @param widgetTopLeftCorner
     * @param widgetDimensions
     * @param livesCounter
     * @param widgetRenderable
     * @param gameObjectsCollection
     * @param numOfLives
     */
    public GraphicLifeCounter(Vector2 widgetTopLeftCorner,
                              Vector2 widgetDimensions,
                              Counter livesCounter,
                              Renderable widgetRenderable,
                              GameObjectCollection gameObjectsCollection,
                              int numOfLives) {
        super(widgetTopLeftCorner, widgetDimensions, null);
        this.widgetDimensions = widgetDimensions;
        this.livesCounter = livesCounter;
        this.widgetRenderable = widgetRenderable;
        this.gameObjectsCollection = gameObjectsCollection;
        this.numOfLives = numOfLives;

        imageDisplay();
    }

    private void imageDisplay() {
        for (int i = 1; i < livesCounter.value() + 1; i++) {
            GameObject image = new GameObject(new Vector2(10 + (widgetDimensions.x() + 5) * (i - 1), 475),
                    widgetDimensions, widgetRenderable);
            image.setTag(heartsOrder[i]);
            gameObjectsCollection.addGameObject(image, Layer.BACKGROUND);
        }
    }

    private void imageRemoval() {
        for (GameObject go : gameObjectsCollection) {
            if (go.getTag().equals(heartsOrder[livesCounter.value() + 1])) {
                gameObjectsCollection.removeGameObject(go, Layer.BACKGROUND);
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        imageRemoval();
        imageDisplay();

    }
}
