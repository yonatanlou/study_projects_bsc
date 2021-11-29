package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

import static danogl.collisions.Layer.STATIC_OBJECTS;

/**
 * When a brick is  removed from the game, it running through this layer. At this point the
 * brick already had some functionality  assigned to it when it was created and should now work. Here we remove only
 * the brick and update global brick counter. Actually this is the main class the we decorate with the whole other
 * strategies.
 * .
 */
public class RemoveBrickStrategy implements CollisionStrategy {

    private GameObjectCollection gameObjectCollection;

    /**
     * @param gameObjectCollection
     */
    public RemoveBrickStrategy(GameObjectCollection gameObjectCollection) {

        this.gameObjectCollection = gameObjectCollection;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        if (gameObjectCollection.removeGameObject(thisObj, STATIC_OBJECTS)) {
            counter.decrement();

        }


    }

    @Override
    public GameObjectCollection getGameObjectCollection() {
        return gameObjectCollection;
    }
}
