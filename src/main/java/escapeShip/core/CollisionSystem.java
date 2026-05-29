package escapeShip.core;

import java.util.Iterator;
import java.util.List;

public final class CollisionSystem {
    private CollisionSystem() {
    }

    public static CollisionResult updateAndRemove(List<Asteroid> asteroids, List<Laser> lasers, Ship ship, GameSize gameSize) {
        int asteroidsHit = 0;
        Iterator<Laser> laserIterator = lasers.iterator();
        while (laserIterator.hasNext()) {
            boolean hitSomething = false;
            Laser laser = laserIterator.next();
            laser.update();
            if (laser.isOffScreen()) {
                laserIterator.remove();
                continue;
            }
            Iterator<Asteroid> asteroidIterator = asteroids.iterator();
            while (asteroidIterator.hasNext()) {
                Asteroid asteroid = asteroidIterator.next();
                if (asteroid.getShape().intersects(laser.getBounds())) {
                    asteroidIterator.remove();
                    hitSomething = true;
                    asteroidsHit++;
                }
            }
            if (hitSomething) {
                laserIterator.remove();
            }
        }

        Iterator<Asteroid> asteroidIterator = asteroids.iterator();
        while (asteroidIterator.hasNext()) {
            Asteroid asteroid = asteroidIterator.next();
            asteroid.update();
            if (asteroid.isOffScreen(gameSize)) {
                asteroidIterator.remove();
                continue;
            }
            if (asteroid.getShape().intersects(ship.getShape())) {
                return new CollisionResult(true, asteroidsHit);
            }
        }
        return new CollisionResult(false, asteroidsHit);
    }
}
