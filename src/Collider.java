import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Tyler on 3/19/14.
 */
public class Collider {

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
            Laser laser = itL.next();
            laser.update();
            if(laser.offScreen()){
                itL.remove();
            }
            //asteroids
            Iterator<Asteroid> itA = asteroidList.iterator();
            while(itA.hasNext()){
                Asteroid asteroid = itA.next();
                if(new Rectangle((int)asteroid.xCoordinate,(int)asteroid.yCoordinate,asteroid.size,asteroid.size).intersects(new Rectangle((int)laser.xCoordinate,(int)laser.yCoordinate,1,5))){
                    itA.remove();
                    itL.remove();
                }
            }
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
            if(new Rectangle((int)asteroid.xCoordinate,(int)asteroid.yCoordinate,asteroid.size,asteroid.size).intersects((int)escapeShip.xCoordinate-escapeShip.size/2,(int)escapeShip.yCoordinate,escapeShip.size,escapeShip.size)){
                return true; //game over
            }
        }
        return false;
    }
}
