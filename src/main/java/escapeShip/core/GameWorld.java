package escapeShip.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class GameWorld {
    public static final int DEFAULT_UPDATES_PER_SECOND = 60;

    private final GameSize size;
    private final int updatesPerSecond;
    private final RandomSource random;
    private final InputState input = new InputState();
    private final List<Laser> lasers = new ArrayList<Laser>();
    private final List<Asteroid> asteroids = new ArrayList<Asteroid>();
    private final Ship ship;

    private int level;
    private boolean paused;
    private boolean gameOver;
    private boolean firstTime = true;
    private int asteroidsHit;
    private int distanceTillNextLevel;
    private int previousDistanceTillNextLevel;
    private double gameSpeed;
    private int asteroidWait;
    private double waitMin;
    private double waitMax;

    public GameWorld(GameSize size) {
        this(size, DEFAULT_UPDATES_PER_SECOND, new JavaRandomSource());
    }

    public GameWorld(GameSize size, int updatesPerSecond, RandomSource random) {
        this.size = size;
        this.updatesPerSecond = updatesPerSecond;
        this.random = random;
        this.ship = new Ship(size, updatesPerSecond);
        start();
    }

    public void start() {
        gameOver = false;
        distanceTillNextLevel = 1000;
        previousDistanceTillNextLevel = 0;
        level = 1;
        gameSpeed = 1;
        asteroidWait = 0;
        lasers.clear();
        asteroids.clear();
        paused = false;
        waitMax = 1.5;
        waitMin = level;
        asteroidsHit = 0;
        ship.reset();
        pause();
    }

    public void pause() {
        if (!paused && !gameOver) {
            paused = true;
        } else if (paused && !gameOver) {
            paused = false;
            firstTime = false;
        }
    }

    public void restart() {
        restart(false);
    }

    public void restart(boolean startImmediately) {
        if (paused) {
            start();
            if (startImmediately) {
                pause();
            }
        }
    }

    public void gameOver() {
        gameOver = true;
        paused = true;
    }

    public void moveLeft(boolean moving) {
        input.setMovingLeft(moving);
    }

    public void moveRight(boolean moving) {
        input.setMovingRight(moving);
    }

    public void fireLaser() {
        if (!paused && ship.canFire()) {
            lasers.add(ship.fire());
        }
    }

    public void update() {
        if (paused) {
            return;
        }

        spawnAsteroidsIfNeeded();
        asteroidWait--;

        CollisionResult collisionResult = CollisionSystem.updateAndRemove(asteroids, lasers, ship, size);
        if (collisionResult.isShipHit()) {
            gameOver();
        }
        asteroidsHit += collisionResult.getAsteroidsHit();

        if (ship.getDistanceTraveled() - previousDistanceTillNextLevel > distanceTillNextLevel) {
            previousDistanceTillNextLevel = distanceTillNextLevel;
            increaseLevel();
            distanceTillNextLevel *= gameSpeed;
        }

        ship.update(input);
    }

    private void spawnAsteroidsIfNeeded() {
        if (asteroids.size() < (level * Math.sqrt(level) + 4) / 2) {
            throwAsteroid();
        } else if (asteroidWait <= 0) {
            asteroidWait = (int) ((updatesPerSecond / 3.0) * (waitMin + (random.nextDouble() * ((waitMax - waitMin) + 1))));
            throwAsteroid();
        }
    }

    private void throwAsteroid() {
        asteroids.add(new Asteroid(size, updatesPerSecond, gameSpeed, random));
    }

    private void increaseLevel() {
        level++;
        waitMax = waitMin / 2.0;
        waitMin = (1.0 / level) * (1.0 / level);
        gameSpeed = (Math.log(level) / 2.0) + 1;
        ship.updateSpeed(gameSpeed);
    }

    public GameSize getSize() { return size; }
    public Ship getShip() { return ship; }
    public List<Laser> getLasers() { return Collections.unmodifiableList(lasers); }
    public List<Asteroid> getAsteroids() { return Collections.unmodifiableList(asteroids); }
    public int getLevel() { return level; }
    public boolean isPaused() { return paused; }
    public boolean isGameOver() { return gameOver; }
    public boolean isFirstTime() { return firstTime; }
    public int getAsteroidsHit() { return asteroidsHit; }
}
