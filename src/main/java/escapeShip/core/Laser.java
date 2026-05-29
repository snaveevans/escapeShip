package escapeShip.core;

public final class Laser {
    private static final double SECONDS_TO_CROSS = 1.75;

    private double x;
    private double y;
    private final double ySpeed;
    private final double width;
    private final double height;

    public Laser(double shipX, double shipY, GameSize size, int updatesPerSecond) {
        this.width = size.getWidth() / (350.0 / 3.0);
        this.height = size.getHeight() / (525.0 / 9.0);
        this.x = shipX - width / 2.0;
        this.y = shipY - size.getHeight() / (525.0 / 13.0);
        this.ySpeed = size.getHeight() / (updatesPerSecond * SECONDS_TO_CROSS);
    }

    public void update() {
        y -= ySpeed;
    }

    public boolean isOffScreen() {
        return y + height < 0;
    }

    public Rect getBounds() {
        return new Rect(x, y, width, height);
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
}
