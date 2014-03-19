/**
 * Created by Tyler on 3/11/14.
 */
public abstract class GameObjects  {
    protected double xCoordinate, yCoordinate;
    protected double xSpeed, ySpeed;

    protected abstract void move();

    protected abstract void blowUp();

    public abstract void update();

    public abstract void updateSpeed(int speedIncrease);

}
