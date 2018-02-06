package escapeShip;

/**
 * Created by Tyler on 3/11/14.
 */
public abstract class GameObjects  {
    protected double xCoordinate, yCoordinate;
    static public final int LEFT = 0;
    static public final int RIGHT = 1;
    protected GamePolygon gamePolygon;

    protected abstract void blowUp();

    public abstract void update();

    public static void updateSpeed(double speedIncrease){};

}
