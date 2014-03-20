import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Tyler on 3/11/14.
 */
public class GameLoop {

    private int distanceTillNextLevel;
    private int previousDistanceTillNextLevel;
    private int level;
    private double gameSpeed;
    private int asteroidWait = 0;
    protected ArrayList<Laser> laserList;
    protected ArrayList<Asteroid> asteroidList;
    protected EscapeShip escapeShip;
    private Dimension dimension;
    private double waitMin = .05;
    private double waitMax = .1;
    private boolean pause;
    private boolean gameOver;


    public GameLoop(Dimension dimension){
       this.dimension = dimension;
       escapeShip = new EscapeShip(this.dimension);

       start();

    }

    public void start(){
        gameOver = false;
        distanceTillNextLevel = 1000;
        previousDistanceTillNextLevel = 0;
        level = 1;
        gameSpeed = 1;
        laserList = new ArrayList<Laser>();
        asteroidList = new ArrayList<Asteroid>();
        pause = false;
        Laser.resetSpeedModifier();
        Asteroid.resetSpeedModifier();
        pause();
    }

    public void pause(){
        if(!pause && !gameOver)//if it isn't paused, pause it, the game will not continue if it is gameOver
            pause = true;
        else if(pause && !gameOver)//if it is paused unpause it, the game will not continue if it is gameOver
            pause = false;
    }

    public boolean getPause(){
        return pause;
    }

    public void restart(){
        if(pause){//if the game is paused restart it
            start();
            escapeShip.startingPosition();
        }
    }

    public void gameOver(){
        gameOver = true;
        pause = true;
    }

    public void throwAsteroid(){
        Asteroid asteroid = new Asteroid(dimension);
        asteroidList.add(asteroid);
    }

    public void moveLeft(boolean isMoving){
        escapeShip.move(isMoving, GameObjects.LEFT);
    }

    public void moveRight(boolean isMoving){
        escapeShip.move(isMoving, GameObjects.RIGHT);
    }

    public void fireLaser(){
        if(escapeShip.canFire()){
            Laser temp[] =escapeShip.fireLaser();
            laserList.add(temp[0]);
            laserList.add(temp[1]);
        }
    }

    private void increaseLevel(){
        level++;
        gameSpeed *= 1.06;

        Asteroid.updateSpeed(gameSpeed);

        Laser.updateSpeed(gameSpeed);

        escapeShip.updateSpeed(gameSpeed);
    }

    public GameLoop update(){

        if(!pause){
            if(asteroidWait <= 0 && asteroidList.size() < 10+level){//wait around 1-2 and there are already several asteroids
                asteroidWait = (int)(60*(waitMin+(Math.random()*((waitMax-waitMin)+1))));
                throwAsteroid();
                //System.out.println(asteroidWait);
            }

            asteroidWait--;

            if(Collider.remover(asteroidList,laserList,escapeShip,dimension))
                gameOver();

            //check to up level
            if(escapeShip.getDistanceTraveled()-(previousDistanceTillNextLevel) > distanceTillNextLevel){
                previousDistanceTillNextLevel = distanceTillNextLevel;
                distanceTillNextLevel += 250;
                increaseLevel();
            }

            escapeShip.update();
        }

        return this;
    }

    public int getLevel(){
        return level;
    }

}
