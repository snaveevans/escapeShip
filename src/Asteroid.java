import java.awt.*;

/**
 * Created by tyler_000 on 3/18/14.
 */
public class Asteroid extends GameObjects {

    protected int size;

    public Asteroid(Dimension dimension){
        System.out.println("Asteroid Contructor");
        xCoordinate = (Math.random()*((dimension.getWidth()/2-0)+ dimension.getWidth()/2));
        yCoordinate = 1;

        size = 10;

        ySpeed = 1;

        if(((int)(Math.random()*10)%2)==1){
            xSpeed = -.25;
        }
        else{
            xSpeed =.25;
        }
    }

    @Override
    protected void move() {

    }

    @Override
    protected void blowUp() {

    }

    @Override
    public void update() {
        yCoordinate = yCoordinate + ySpeed;

        //xCoordinate = xCoordinate + xSpeed;
    }

    @Override
    public void updateSpeed(int speedIncrease) {

    }

    public boolean offScreen(int height) {
        if (xCoordinate < -20)
            return true;
        if (yCoordinate > height + 20)
            return true;

        return false;
    }
}
