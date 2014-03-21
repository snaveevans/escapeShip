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

    public Asteroid(Dimension dimension){
        size = 10;
        Random random = new Random();
        int topRandX = random.nextInt((int)dimension.getWidth()+20)-9;
        int botRandX = random.nextInt((int)dimension.getWidth())-1;

        xCoordinate = topRandX;
        yCoordinate = -10;

        xSpeed = (double)(botRandX - topRandX)/(double)(GameObjects.UPS*SECTOCROSS);
        ySpeed = dimension.getHeight()/(double)(GameObjects.UPS*SECTOCROSS);

        //System.out.println("TopRand: " + topRandX + " BotRand: " + botRandX);

        //System.out.println("xSpeed: " + xSpeed + " ySpeed: " + ySpeed);

        double tempModifier = speedModifier + ((double)(random.nextInt(20)-10)/100);

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
