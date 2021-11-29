package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;


/**
 *  Decorator is really great for this scenario, because we want to remove brick on each strategy
 *  but to add more functionality in each strategy. all the
 *  classes that are added in this package, extend this decorator to assign their own
 *  functionality to the blocks they create
 */
public abstract class RemoveBrickStrategyDecorator implements CollisionStrategy {
    private CollisionStrategy toBeDecorated;
    private GameObjectCollection gameObjectCollection;

    /**
     * @param toBeDecorated
     */
    public RemoveBrickStrategyDecorator(CollisionStrategy  toBeDecorated) {
        this.toBeDecorated = toBeDecorated;
    }

        @Override
        public void onCollision (GameObject thisObj, GameObject otherObj, Counter counter){


        }

        @Override
        public GameObjectCollection getGameObjectCollection () {
            return toBeDecorated.getGameObjectCollection();
        }
    }
//}