package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;
/**
 * This class is in charge of making the text display of the player's remaining lives.
 * To render the text, it uses the TextRenderable type.
 * The GameObjectCollection uses it to generate a game object that indicates how many lives are remaining numerically.
 */
public class NumericLifeCounter extends GameObject {


    private GameObject textObject;
    private Counter livesCounter;

    private GameObjectCollection gameObjectCollection;

    /**
     * Creating the numeric object by the LivesCounter.
     *
     * @param livesCounter
     * @param topLeftCorner
     * @param dimensions
     * @param gameObjectCollection
     */
    public NumericLifeCounter(Counter livesCounter,
                              Vector2 topLeftCorner,
                              Vector2 dimensions,
                              GameObjectCollection gameObjectCollection) {


        super(topLeftCorner, dimensions, null);
        this.livesCounter = livesCounter;
        this.gameObjectCollection = gameObjectCollection;
        GameObject textObject = textCreator();
        this.textObject = textObject;
        displayText(textObject);

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        removeText(textObject);
        GameObject textObject = textCreator();
        this.textObject = textObject;
        displayText(textObject);
    }


    private GameObject textCreator() {
        TextRenderable text = new TextRenderable(String.format("%d", livesCounter.value()));
        text.setColor(Color.lightGray);
        Vector2 topLeftCorner = new Vector2(40, 430);


        return new GameObject(topLeftCorner, new Vector2(10, 35), text);


    }

    private void displayText(GameObject textObject) {
        gameObjectCollection.addGameObject(textObject, Layer.FOREGROUND);

    }

    private void removeText(GameObject textObject) {
        gameObjectCollection.removeGameObject(textObject, Layer.FOREGROUND);

    }

}