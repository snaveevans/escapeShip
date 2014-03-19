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
    private boolean moveLeft;
    private boolean moveRight;


    public EscapeShip(Dimension dimension){
        distanceTraveled = 0;
        laserBatterAmmo = 5;
        size = 12;

        System.out.println("EscapeShip Contructor");

        this.dimension = dimension;

        yCoordinate = dimension.getHeight() * 14.5/16;
        xCoordinate = (dimension.getWidth() / 2) - size/2;

        moveLeft = moveRight = false;
    }

    public Laser[] fireLaser(){
        Laser leftLaser = new Laser(xCoordinate, yCoordinate, LEFT, dimension);
        Laser rightLaser = new Laser(xCoordinate, yCoordinate, RIGHT, dimension);
        return new Laser[]{leftLaser.returnLaser(),rightLaser.returnLaser()};
    }

    protected void move(boolean isMoving, int leftOrRight) {
        if(isMoving){
            if(leftOrRight == LEFT){
                moveLeft = true;
            }
            else if(leftOrRight == RIGHT){
                moveRight = true;
            }
        }
        else{
            if(leftOrRight == LEFT){
                moveLeft = false;
            }
            else if(leftOrRight == RIGHT){
                moveRight = false;
            }
        }
    }

    @Override
    protected void blowUp() {

    }

    @Override
    public void update() {
        distanceTraveled++;
        if(moveLeft == true && moveRight == true){//both keys true, don't move
            xSpeed = 0;
        }
        else if(moveLeft == true && moveRight == false){//move left
            xSpeed = -1.5;
        }
        else if(moveRight == true && moveLeft == false){//move right
            xSpeed = 1.5;
        }
        else{//both keys false, don't move
            xSpeed = 0;
        }
        xCoordinate += xSpeed;
    }

    @Override
    public void updateSpeed(int speedIncrease) {

    }

}
