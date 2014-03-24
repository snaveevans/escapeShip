import java.awt.*;
import java.util.Random;

/**
 * Created by tyler_000 on 3/18/14.
 */
public class Asteroid extends GameObjects {

    protected int size;

    static private double speedModifier = 1;
    static private final double SECTOCROSS = 3.5;//seconds to cross the screen

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
                    *
                    * Pretty much done
    -------------------------------MAJOR REWORK-----------------------------------*/

    public Asteroid(Dimension dimension){
        Random random = new Random();
        size = random.nextInt(10)+10;
        size = (int)(dimension.getWidth()/(350/size));
        int topRandX = random.nextInt((int)dimension.getWidth()+20)-9;
        int botRandX = random.nextInt((int)dimension.getWidth())-1;

        xCoordinate = topRandX;
        yCoordinate = -10;

        gamePolygon = new GamePolygon(eightSidedX(),eightSidedY(),8);

        xSpeed = (double)(botRandX - topRandX)/(double)(Main.UPS*SECTOCROSS);
        ySpeed = dimension.getHeight()/(double)(Main.UPS*SECTOCROSS);

        double tempModifier = speedModifier + ((double)(random.nextInt(30)-15)/100);

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

        gamePolygon = new GamePolygon(eightSidedX(),eightSidedY(),8);
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

    public int[] eightSidedX(){
        int pointX1, pointX2, pointX3, pointX4, pointX5, pointX6, pointX7, pointX8;

        pointX1 = (int)xCoordinate -(size/2);
        pointX2 = (int)xCoordinate -(size/5);
        pointX3 = (int)xCoordinate +(size/5);
        pointX4 = (int)xCoordinate +(size/2);
        pointX5 = (int)xCoordinate +(size/2);
        pointX6 = (int)xCoordinate +(size/5);
        pointX7 = (int)xCoordinate -(size/5);
        pointX8 = (int)xCoordinate -(size/2);

        return new int[]{pointX1, pointX2, pointX3, pointX4, pointX5, pointX6, pointX7, pointX8};
    }

    public int[] eightSidedY(){
        int pointY1, pointY2, pointY3, pointY4, pointY5, pointY6, pointY7, pointY8;

        pointY1 = (int)yCoordinate -(size/5);
        pointY2 = (int)yCoordinate -(size/2);
        pointY3 = (int)yCoordinate -(size/2);
        pointY4 = (int)yCoordinate -(size/5);
        pointY5 = (int)yCoordinate +(size/5);
        pointY6 = (int)yCoordinate +(size/2);
        pointY7 = (int)yCoordinate +(size/2);
        pointY8 = (int)yCoordinate +(size/5);

        return new int[]{pointY1, pointY2, pointY3, pointY4, pointY5, pointY6, pointY7, pointY8};
    }


}
