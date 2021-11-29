package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

/**
 * Interface that gather all the brick objects with this basic two methods.
 */
public interface CollisionStrategy {


    /**
     * @param thisObj The brick
     * @param otherObj The other gameObject that collide with the brick (probably ball or puck)
     * @param counter How many thisObj left in the game
     */
    void onCollision(GameObject thisObj, GameObject otherObj, Counter counter);
    GameObjectCollection getGameObjectCollection();

}


