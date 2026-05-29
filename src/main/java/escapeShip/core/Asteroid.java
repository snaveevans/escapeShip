package escapeShip.core;

public final class Asteroid {
    private static final double SECONDS_TO_CROSS = 3.5;

    private double x;
    private double y;
    private final double xSpeed;
    private final double ySpeed;
    private final int size;

    public Asteroid(GameSize gameSize, int updatesPerSecond, double speedModifier, RandomSource random) {
        int baseSize = random.nextInt(10) + 10;
        this.size = (int) (gameSize.getWidth() / (350.0 / baseSize));
        int topRandX = random.nextInt((int) gameSize.getWidth() + 20) - 9;
        int botRandX = random.nextInt((int) gameSize.getWidth()) - 1;
        this.x = topRandX;
        this.y = -10;

        double modifier = speedModifier + ((double) (random.nextInt(30) - 15) / 100.0);
        this.xSpeed = ((double) (botRandX - topRandX) / (updatesPerSecond * SECONDS_TO_CROSS)) * modifier;
        this.ySpeed = (gameSize.getHeight() / (updatesPerSecond * SECONDS_TO_CROSS)) * modifier;
    }

    public void update() {
        y += ySpeed;
        x += xSpeed;
    }

    public boolean isOffScreen(GameSize gameSize) {
        return x < -20 || x > gameSize.getWidth() + 20 || y > gameSize.getHeight() + 20;
    }

    public PolygonShape getShape() {
        return new PolygonShape(new Vec2[] {
                new Vec2(x - size / 2.0, y - size / 5.0),
                new Vec2(x - size / 5.0, y - size / 2.0),
                new Vec2(x + size / 5.0, y - size / 2.0),
                new Vec2(x + size / 2.0, y - size / 5.0),
                new Vec2(x + size / 2.0, y + size / 5.0),
                new Vec2(x + size / 5.0, y + size / 2.0),
                new Vec2(x - size / 5.0, y + size / 2.0),
                new Vec2(x - size / 2.0, y + size / 5.0)
        });
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public int getSize() { return size; }
}
