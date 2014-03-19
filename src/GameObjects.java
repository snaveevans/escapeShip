/**
 * Created by Tyler on 3/11/14.
 */
public abstract class GameObjects  {
    protected double xCoordinate, yCoordinate;
    protected double xSpeed, ySpeed;
    static public final int LEFT = 0;
    static public final int RIGHT = 1;

    protected abstract void blowUp();

    public abstract void update();

    public abstract void updateSpeed(int speedIncrease);

}
