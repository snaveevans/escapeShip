import java.awt.*;

/**
 * Created by Tyler on 3/11/14.
 */
public class EscapeShip extends GameObjects {

    //ship is 1/32 of the screen
    private double distanceTraveled;
    private int laserBatterAmmo;
    private int rechargeRate;
    public static final int RECHARGEMAX = (int)1.5*Main.UPS;//1.5 seconds
    private  Dimension dimension;
    protected int size;
    private boolean moveLeft;
    private boolean moveRight;
    private static double travelSpeed;
    private static double speed;
    private double xSpeed;
    static private final double SECTOCROSS = 2;

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
        travelSpeed = (double)60/Main.UPS;
        speed = dimension.getWidth()/(double)(Main.UPS*SECTOCROSS);
        distanceTraveled = 0;
        laserBatterAmmo = 5;
        rechargeRate = RECHARGEMAX;

        yCoordinate = dimension.getHeight() * 14.8/16;
        xCoordinate = (dimension.getWidth() / 2) - size/2;

        moveLeft = moveRight = false;

        gamePolygon = new GamePolygon(xPoints(),yPoints(),18);
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
        else if(moveLeft && !moveRight && xCoordinate > 13)//move left
            xSpeed = -speed;
        else if(moveRight && !moveLeft && xCoordinate < dimension.getWidth()-21)//move right
            xSpeed = speed;
        else//both keys false, don't move
            xSpeed = 0;
        xCoordinate += xSpeed;

        gamePolygon = new GamePolygon(xPoints(),yPoints(),18);

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

    public int[] xPoints(){
        int pointX1, pointX2, pointX3, pointX4, pointX5, pointX6, pointX7, pointX8, pointX9;
        int pointX10, pointX11, pointX12, pointX13, pointX14, pointX15, pointX16, pointX17, pointX18;

//        private int height = 525;
//        private int width = 350;

        pointX1 = (int)xCoordinate;
        pointX2 = (int)(xCoordinate + dimension.getWidth()/(350/3));
        pointX3 = (int)(xCoordinate + dimension.getWidth()/(350/10));
        pointX4 = (int)(xCoordinate + dimension.getWidth()/(350/10));
        pointX5 = (int)(xCoordinate + dimension.getWidth()/(350/12));
        pointX6 = (int)(xCoordinate + dimension.getWidth()/(350/12));
        pointX7 = (int)(xCoordinate + dimension.getWidth()/(350/10));
        pointX8 = (int)(xCoordinate + dimension.getWidth()/(350/10));
        pointX9 = (int)(xCoordinate + dimension.getWidth()/(350/3));
        pointX10 = (int)xCoordinate;
        pointX11 = (int)(xCoordinate - dimension.getWidth()/(350/3));
        pointX12 = (int)(xCoordinate - dimension.getWidth()/(350/10));
        pointX13 = (int)(xCoordinate - dimension.getWidth()/(350/10));
        pointX14 = (int)(xCoordinate - dimension.getWidth()/(350/12));
        pointX15 = (int)(xCoordinate - dimension.getWidth()/(350/12));
        pointX16 = (int)(xCoordinate - dimension.getWidth()/(350/10));
        pointX17 = (int)(xCoordinate - dimension.getWidth()/(350/10));
        pointX18 = (int)(xCoordinate - dimension.getWidth()/(350/3));

        return new int[]{pointX1, pointX2, pointX3, pointX4, pointX5, pointX6, pointX7, pointX8, pointX9, pointX10, pointX11, pointX12, pointX13, pointX14, pointX15, pointX16, pointX17, pointX18};
    }

    public int[] yPoints(){
        int pointY1, pointY2, pointY3, pointY4, pointY5, pointY6, pointY7, pointY8, pointY9;
        int pointY10, pointY11, pointY12, pointY13, pointY14, pointY15, pointY16, pointY17, pointY18;

        pointY1 = (int)(yCoordinate - dimension.getHeight()/(525/10));
        pointY2 = (int)(yCoordinate - dimension.getHeight()/(525/2));
        pointY3 = (int)(yCoordinate - dimension.getHeight()/(525/4));
        pointY4 = (int)(yCoordinate - dimension.getHeight()/(525/6));
        pointY5 = (int)(yCoordinate - dimension.getHeight()/(525/6));
        pointY6 = (int)(yCoordinate + dimension.getHeight()/(525/5));
        pointY7 = (int)(yCoordinate + dimension.getHeight()/(525/5));
        pointY8 = (int)(yCoordinate + dimension.getHeight()/(525/4));
        pointY9 = (int)(yCoordinate + dimension.getHeight()/(525/2));
        pointY10 = (int)(yCoordinate + dimension.getHeight()/(525/5));
        pointY11 = (int)(yCoordinate + dimension.getHeight()/(525/2));
        pointY12 = (int)(yCoordinate + dimension.getHeight()/(525/4));
        pointY13 = (int)(yCoordinate + dimension.getHeight()/(525/5));
        pointY14 = (int)(yCoordinate + dimension.getHeight()/(525/5));
        pointY15 = (int)(yCoordinate - dimension.getHeight()/(525/6));
        pointY16 = (int)(yCoordinate - dimension.getHeight()/(525/6));
        pointY17 = (int)(yCoordinate - dimension.getHeight()/(525/4));
        pointY18 = (int)(yCoordinate - dimension.getHeight()/(525/2));

        return new int[]{pointY1, pointY2, pointY3, pointY4, pointY5, pointY6, pointY7, pointY8, pointY9, pointY10, pointY11, pointY12, pointY13, pointY14, pointY15, pointY16, pointY17, pointY18};
    }

}
