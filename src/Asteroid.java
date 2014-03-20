import java.awt.*;

/**
 * Created by tyler_000 on 3/18/14.
 */
public class Asteroid extends GameObjects {

    protected int size;
    static private double speedMinX = .05;
    static private double speedMinY = .8;

    static private double speedMaxX = .25;
    static private double speedMaxY = 1.2;

    static private double speedModifier = 1;

    private double xSpeed;
    private double ySpeed;

    public Asteroid(Dimension dimension){
        xCoordinate = (Math.random()*((dimension.getWidth()/2-0)+ dimension.getWidth()/2));
        yCoordinate = 1;

        size = 10;

        ySpeed = (speedMinY+(Math.random()*((speedMaxY-speedMinY)+1)));

        xSpeed = (speedMinX+(Math.random()*((speedMaxX-speedMinX)+1)));

        if(((int)(Math.random()*10)%2)==1){
            xSpeed *= -1;
        }
        else{
            xSpeed *= 1;
        }

        ySpeed *= speedModifier;
        xSpeed *= speedModifier;
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
        speedModifier *= speedIncrease;
    }

    public boolean offScreen(Dimension dimension) {
        if (xCoordinate < -20 || xCoordinate > dimension.getWidth()+20)
            return true;
        if (yCoordinate > dimension.getHeight() + 20)
            return true;

        return false;
    }
}
