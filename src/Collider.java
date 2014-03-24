import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Tyler on 3/19/14.
 */
public class Collider {

    static private int asteroidsHit = 0;

    /*-----------------------------MAJOR REWORK-------------------------------------
                        Rework Asteroid
                    * Create Coordinate test before line intersect test
                    * Create Intersect Test
                    * Asteroid Bounce??
    -------------------------------MAJOR REWORK-----------------------------------*/

    public static boolean remover(ArrayList<Asteroid> asteroidList, ArrayList<Laser> laserList, EscapeShip escapeShip,Dimension dimension){

        //check lasers vs asteroids
        Iterator<Laser> itL = laserList.iterator();
        while(itL.hasNext()){
            boolean hitSomething = false;
            Laser laser = itL.next();
            laser.update();
            if(laser.offScreen()){
                itL.remove();
            }
            //asteroids
            Iterator<Asteroid> itA = asteroidList.iterator();
            while(itA.hasNext()){
                final Asteroid asteroid = itA.next();
//                if(new Rectangle((int)asteroid.xCoordinate-(asteroid.size/2),(int)asteroid.yCoordinate-(asteroid.size/2),asteroid.size,asteroid.size).intersects(new Rectangle((int)laser.xCoordinate,(int)laser.yCoordinate,Laser.XSIZE,Laser.YSIZE))){
                if(asteroid.gamePolygon.intersects(new Rectangle((int)laser.xCoordinate,(int)laser.yCoordinate,Laser.xSize,Laser.ySize))){
                    itA.remove();
                    hitSomething = true;
                    asteroidsHit++;
                }
            }
            if(hitSomething)
                itL.remove();
        }

        //check asteroids vs escapeShip
        Iterator<Asteroid> itA = asteroidList.iterator();
        while(itA.hasNext()){
            Asteroid asteroid = itA.next();
            asteroid.update();
            if(asteroid.offScreen(dimension)){
                itA.remove();
            }
            //escapeShip
//            if(new Rectangle((int)asteroid.xCoordinate-(asteroid.size/2),(int)asteroid.yCoordinate-(asteroid.size/2),asteroid.size,asteroid.size).intersects((int)escapeShip.xCoordinate-escapeShip.size/2,(int)escapeShip.yCoordinate,escapeShip.size,escapeShip.size)){
            if(GamePolygon.intersects(asteroid,escapeShip)){
                return true; //game over
            }
        }
        return false;
    }

    public static int asteroidsHit(){
        return asteroidsHit;
    }

    public static void resetAsteroidsHid(){
        asteroidsHit = 0;
    }
}
