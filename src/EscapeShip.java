import java.awt.*;

/**
 * Created by Tyler on 3/11/14.
 */
public class EscapeShip extends GameObjects {

    //ship is 1/32 of the screen
    private int distanceTraveled;
    private int laserBatterAmmo;
    private  Dimension dimension;
    protected int size;


    public EscapeShip(Dimension dimension){
        distanceTraveled = 0;
        laserBatterAmmo = 5;
        size = 12;

        System.out.println("EscapeShip Contructor");

        this.dimension = dimension;

        yCoordinate = dimension.getHeight() * 14.5/16;
        xCoordinate = (dimension.getWidth() / 2) - size/2;
    }

    public Laser[] fireLaser(){
        Laser leftLaser = new Laser(xCoordinate, yCoordinate, Laser.LEFT, dimension);
        Laser rightLaser = new Laser(xCoordinate, yCoordinate, Laser.RIGHT, dimension);
        return new Laser[]{leftLaser.returnLaser(),rightLaser.returnLaser()};
    }

    @Override
    protected void move() {

    }

    @Override
    protected void blowUp() {

    }

    @Override
    public void update() {
        distanceTraveled++;
    }

    @Override
    public void updateSpeed(int speedIncrease) {

    }

}
