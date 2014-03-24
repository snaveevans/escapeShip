import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Tyler on 3/11/14.
 */
public class GameLoop {

    protected int level;
    protected ArrayList<Laser> laserList;
    protected ArrayList<Asteroid> asteroidList;
    protected EscapeShip escapeShip;
    protected boolean pause;
    protected boolean gameOver;
    protected boolean firstTime = true;
    protected int asteroidsHit;

    private int distanceTillNextLevel;
    private int previousDistanceTillNextLevel;
    private double gameSpeed;
    private int asteroidWait = 0;
    private double waitMin;
    private double waitMax;
    private Dimension dimension;




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
        waitMax = 1.5;
        waitMin = level;
        Collider.resetAsteroidsHid();
        asteroidsHit = 0;
        pause();
        //System.out.println("Level: " + level + " WaitMax: "+ waitMax + " WaitMin: " + waitMin + " GameSpeed: " + gameSpeed + " DistanceTillNextLevel: " + distanceTillNextLevel);

    }

    public void pause(){
        if(!pause && !gameOver)//if it isn't paused, pause it, the game will not continue if it is gameOver
            pause = true;
        else if(pause && !gameOver) {//if it is paused unpause it, the game will not continue if it is gameOver
            pause = false;
            firstTime = false;
        }
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

    public boolean getGameOver(){
        return gameOver;
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
        if(!pause && escapeShip.canFire()){
            Laser temp[] =escapeShip.fireLaser();
            laserList.add(temp[0]);
            laserList.add(temp[1]);
        }
    }

    private void increaseLevel(){
        level++;
        waitMax = waitMin/2;
        waitMin = (1/(double)level)*(1/(double)level);
        gameSpeed = (Math.log(level)/2)+1;

        //System.out.println("Level: " + level + " WaitMax: " + waitMax + " WaitMin: " + waitMin + " GameSpeed: " + gameSpeed + " DistanceTillNextLevel: " + distanceTillNextLevel);

        Asteroid.updateSpeed(gameSpeed);

        Laser.updateSpeed(gameSpeed);

        EscapeShip.updateSpeed(gameSpeed);
    }

    public GameLoop update(){

        if(!pause){
            if(asteroidList.size() < (level*Math.sqrt(level)+6)/2){
                throwAsteroid();
            }
            else if(asteroidWait <= 0){
                asteroidWait = (int)((Main.UPS/3)*(waitMin+(Math.random()*((waitMax-waitMin)+1))));
                //System.out.println(asteroidWait);
                throwAsteroid();
            }

//            if(asteroidList.size() < (Math.sqrt(level)+2)*2){
//                throwAsteroid();
//            }

            asteroidWait--;

            asteroidsHit = Collider.asteroidsHit();

            if(Collider.remover(asteroidList,laserList,escapeShip,dimension))
                gameOver();

            //check to up level
            if(escapeShip.getDistanceTraveled() -previousDistanceTillNextLevel > distanceTillNextLevel){
                previousDistanceTillNextLevel = distanceTillNextLevel;
                increaseLevel();
                distanceTillNextLevel *= gameSpeed;
            }

            escapeShip.update();
        }

        return this;
    }

    public int getLevel(){
        return level;
    }

    public boolean getFirstTime(){
        return firstTime;
    }

}
