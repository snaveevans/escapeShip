import java.awt.*;
import java.util.Random;

/**
 * Created by tyler_000 on 3/18/14.
 */
public class Asteroid extends GameObjects {

    protected int size;

    static private double speedModifier = 1;
    static private final int SECTOCROSS = 3;//seconds to cross the screen

    private double xSpeed;
    private double ySpeed;

    /*-----------------------------MAJOR REWORK-------------------------------------
                        Rework Asteroid
                    * Change actual shape of asteroid
                        * Asteroids should vary in number of sides not general shape
                    * Change size between asteroids
                    * See Collider for reworking as well
                    * Rotate Asteroid
                        * Base off of rotating coordinates
    -------------------------------MAJOR REWORK-----------------------------------*/

    public Asteroid(Dimension dimension){
        Random random = new Random();
        size = random.nextInt(10)+7;
        int topRandX = random.nextInt((int)dimension.getWidth()+20)-9;
        int botRandX = random.nextInt((int)dimension.getWidth())-1;

        xCoordinate = topRandX;
        yCoordinate = -10;

        xSpeed = (double)(botRandX - topRandX)/(double)(Main.UPS*SECTOCROSS);
        ySpeed = dimension.getHeight()/(double)(Main.UPS*SECTOCROSS);

        //System.out.println("TopRand: " + topRandX + " BotRand: " + botRandX);

        //System.out.println("xSpeed: " + xSpeed + " ySpeed: " + ySpeed);

        double tempModifier = speedModifier + ((double)(random.nextInt(30)-15)/100);

        //System.out.println("TempModifier: " + tempModifier);

        ySpeed *= tempModifier;
        xSpeed *= tempModifier;
    }

    public static void resetSpeedModifier(){
        speedModifier = 1;
    }

    @Override
    protected void blowUp() {

    }

    @Override
    public void update() {
        yCoordinate = yCoordinate + ySpeed;

        xCoordinate = xCoordinate + xSpeed;
    }

    public static void updateSpeed(double speedIncrease) {
        speedModifier = speedIncrease;
    }

    public boolean offScreen(Dimension dimension) {
        if (xCoordinate < -20 || xCoordinate > dimension.getWidth()+20)
            return true;
        if (yCoordinate > dimension.getHeight() + 20)
            return true;

        return false;
    }
}
