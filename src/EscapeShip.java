import java.awt.*;

/**
 * Created by Tyler on 3/11/14.
 */
public class EscapeShip extends GameObjects {

    //ship is 1/32 of the screen
    private double distanceTraveled;
    private int laserBatterAmmo;
    private int rechargeRate;
    public static final int RECHARGEMAX = 90;//divide by 60 to get seconds
    private  Dimension dimension;
    protected int size;
    private boolean moveLeft;
    private boolean moveRight;
    private static double travelSpeed;
    private static double speed;
    private double xSpeed;

    /*-----------------------------MAJOR REWORK-------------------------------------
                        Rework EscapeShip
                    * Create shape of EscapeShip
                    * Ensure boundaries are not surpassed by ship
    -------------------------------MAJOR REWORK-----------------------------------*/

    public EscapeShip(Dimension dimension){
        size = 12;

        this.dimension = dimension;

        startingPosition();
    }

    protected void startingPosition(){
        travelSpeed = 1;
        speed = 1.75;
        distanceTraveled = 0;
        laserBatterAmmo = 5;
        rechargeRate = RECHARGEMAX;

        yCoordinate = dimension.getHeight() * 14.5/16;
        xCoordinate = (dimension.getWidth() / 2) - size/2;

        moveLeft = moveRight = false;
    }

    public Laser[] fireLaser(){

        Laser leftLaser = new Laser(xCoordinate, yCoordinate, LEFT, dimension);
        Laser rightLaser = new Laser(xCoordinate, yCoordinate, RIGHT, dimension);
        laserBatterAmmo--;
        return new Laser[]{leftLaser.returnLaser(),rightLaser.returnLaser()};
    }

    public boolean canFire(){
        if(laserBatterAmmo>0)
            return true;
        else
            return false;
    }

    public int getLaserBatterAmmo(){
        return laserBatterAmmo;
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
        //move
        distanceTraveled += travelSpeed;

        if(moveLeft && moveRight)//both keys true, don't move
            xSpeed = 0;
        else if(moveLeft && !moveRight && xCoordinate > 9)//move left
            xSpeed = -speed;
        else if(moveRight && !moveLeft && xCoordinate < dimension.getWidth()-15)//move right
            xSpeed = speed;
        else//both keys false, don't move
            xSpeed = 0;
        xCoordinate += xSpeed;

        //charge laser batteries
        if(laserBatterAmmo != 5){
            rechargeRate--;
            if(rechargeRate==0){
                rechargeRate = RECHARGEMAX;
                laserBatterAmmo++;
            }
        }
    }

    public String writeDistanceTraveled(){
        return("Distance: " +(int)distanceTraveled);
    }

    public int getRechargeRate(){
        return rechargeRate;
    }

    public int getRechargeMax(){
        return RECHARGEMAX;
    }

    public double getDistanceTraveled(){
        return distanceTraveled;
    }

    public static void updateSpeed(double speedIncrease) {
        if(travelSpeed < 2){
            travelSpeed *= speedIncrease;
        }
        if(speed < 3){
            speed *= speedIncrease;
        }
    }

}
