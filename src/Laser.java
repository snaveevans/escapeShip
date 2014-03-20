import java.awt.*;

/**
 * Created by Tyler on 3/11/14.
 */
public class Laser extends GameObjects {

    static private double ySpeed;
    static private double speedModifier = 1;

    public Laser(double shipX, double shipY, int side, Dimension dimension){
        //laser is +/- 1/64 from the ship
        yCoordinate = shipY - 1; //just 1 pixel above the ship
        if(side == LEFT){//uses subtraction
            xCoordinate = (int)(shipX - (dimension.getWidth() * 1/64));
        }
        else{//uses addition
            xCoordinate = (int)(shipX + (dimension.getWidth() * 1/64));
        }
        ySpeed = 2.916666666666667*speedModifier;
    }

    public Laser returnLaser(){
        return this;
    }

    @Override
    protected void blowUp() {

    }

    @Override
    public void update() {
        yCoordinate -= ySpeed;
    }

    public static void updateSpeed(double speedIncrease) {
        speedModifier *= speedIncrease;
    }
    public static void resetSpeedModifier(){
        speedModifier = 1;
    }

    public boolean offScreen() {
        return false;
    }
}
