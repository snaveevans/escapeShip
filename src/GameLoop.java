import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Tyler on 3/11/14.
 */
public class GameLoop {

    private int level;
    private int gameSpeed;
    private int asteroidWait = 0;
    private ArrayList<Laser> laserList;
    private ArrayList<Asteroid> asteroidList;
    private EscapeShip escapeShip;
    private Dimension dimension;
    private double waitMin = .5;
    private double waitMax = 1;

    public GameLoop(Dimension dimension){
       this.dimension = dimension;
        System.out.println("Gameloop Contructor");
        escapeShip = new EscapeShip(this.dimension);

        start();

    }

    public void start(){
        level = 1;
        gameSpeed = 1;
        laserList = new ArrayList<Laser>();
        asteroidList = new ArrayList<Asteroid>();
        System.out.println("Gameloop start");
    }

    public void pause(){

    }

    public void restart(){

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
        Laser temp[] =escapeShip.fireLaser();
        laserList.add(temp[0]);
        laserList.add(temp[1]);
    }

    public ArrayList[] update(){

        if(asteroidWait == 0 && asteroidList.size() < 10+level){//wait around 1-2 and there are already several asteroids
            asteroidWait = (int)(60*(waitMin+(Math.random()*((waitMax-waitMin)+1))));
            throwAsteroid();
        }

        asteroidWait--;

        Collider.remover(asteroidList,laserList,escapeShip,dimension);

        escapeShip.update();

        ArrayList<EscapeShip> temp = new ArrayList<EscapeShip>();
        temp.add(escapeShip);

        return new ArrayList[]{asteroidList,laserList,temp};
    }

}
