package escapeShip.core;

public final class Ship {
    private static final double SECONDS_TO_CROSS = 2;
    private static final int MAX_AMMO = 5;

    private final GameSize gameSize;
    private final int updatesPerSecond;
    private final int rechargeMax;
    private final int size = 12;
    private double x;
    private double y;
    private double distanceTraveled;
    private int laserBatteryAmmo;
    private int rechargeRate;
    private double travelSpeed;
    private double speed;
    private double speedModifier;

    public Ship(GameSize gameSize, int updatesPerSecond) {
        this.gameSize = gameSize;
        this.updatesPerSecond = updatesPerSecond;
        this.rechargeMax = (int) (1.5 * updatesPerSecond);
        reset();
    }

    public void reset() {
        speedModifier = 1;
        travelSpeed = 60.0 / updatesPerSecond;
        speed = gameSize.getWidth() / (updatesPerSecond * SECONDS_TO_CROSS);
        distanceTraveled = 0;
        laserBatteryAmmo = MAX_AMMO;
        rechargeRate = rechargeMax;
        y = gameSize.getHeight() * 14.8 / 16.0;
        x = (gameSize.getWidth() / 2.0) - size / 2.0;
    }

    public Laser[] fire() {
        laserBatteryAmmo--;
        return new Laser[] {
                new Laser(x, y, true, gameSize, updatesPerSecond),
                new Laser(x, y, false, gameSize, updatesPerSecond)
        };
    }

    public boolean canFire() {
        return laserBatteryAmmo > 0;
    }

    public void update(InputState input) {
        distanceTraveled += travelSpeed;
        double xSpeed;
        if (input.isMovingLeft() && input.isMovingRight()) {
            xSpeed = 0;
        } else if (input.isMovingLeft() && x > 13) {
            xSpeed = -speed;
        } else if (input.isMovingRight() && x < gameSize.getWidth() - 21) {
            xSpeed = speed;
        } else {
            xSpeed = 0;
        }
        x += xSpeed;

        if (laserBatteryAmmo != MAX_AMMO) {
            rechargeRate--;
            if (rechargeRate == 0) {
                rechargeRate = rechargeMax;
                laserBatteryAmmo++;
            }
        }
    }

    public void updateSpeed(double gameSpeed) {
        speedModifier = gameSpeed;
        if (travelSpeed < 2) {
            travelSpeed *= speedModifier;
        }
        if (speed < 3) {
            speed *= speedModifier;
        }
    }

    public PolygonShape getShape() {
        return new PolygonShape(new Vec2[] {
                new Vec2(x, y - gameSize.getHeight() / (525.0 / 2.0)),
                new Vec2(x + gameSize.getWidth() / (350.0 / 3.0), y - gameSize.getHeight() / (525.0 / 4.0)),
                new Vec2(x + gameSize.getWidth() / (350.0 / 10.0), y - gameSize.getHeight() / (525.0 / 6.0)),
                new Vec2(x + gameSize.getWidth() / (350.0 / 10.0), y + gameSize.getHeight() / (525.0 / 5.0)),
                new Vec2(x + gameSize.getWidth() / (350.0 / 12.0), y + gameSize.getHeight() / (525.0 / 5.0)),
                new Vec2(x + gameSize.getWidth() / (350.0 / 12.0), y + gameSize.getHeight() / (525.0 / 5.0)),
                new Vec2(x + gameSize.getWidth() / (350.0 / 10.0), y + gameSize.getHeight() / (525.0 / 5.0)),
                new Vec2(x + gameSize.getWidth() / (350.0 / 10.0), y + gameSize.getHeight() / (525.0 / 4.0)),
                new Vec2(x + gameSize.getWidth() / (350.0 / 3.0), y + gameSize.getHeight() / (525.0 / 2.0)),
                new Vec2(x, y + gameSize.getHeight() / (525.0 / 5.0)),
                new Vec2(x - gameSize.getWidth() / (350.0 / 3.0), y + gameSize.getHeight() / (525.0 / 2.0)),
                new Vec2(x - gameSize.getWidth() / (350.0 / 10.0), y + gameSize.getHeight() / (525.0 / 4.0)),
                new Vec2(x - gameSize.getWidth() / (350.0 / 10.0), y + gameSize.getHeight() / (525.0 / 5.0)),
                new Vec2(x - gameSize.getWidth() / (350.0 / 12.0), y + gameSize.getHeight() / (525.0 / 5.0)),
                new Vec2(x - gameSize.getWidth() / (350.0 / 12.0), y + gameSize.getHeight() / (525.0 / 5.0)),
                new Vec2(x - gameSize.getWidth() / (350.0 / 10.0), y + gameSize.getHeight() / (525.0 / 5.0)),
                new Vec2(x - gameSize.getWidth() / (350.0 / 10.0), y - gameSize.getHeight() / (525.0 / 6.0)),
                new Vec2(x - gameSize.getWidth() / (350.0 / 3.0), y - gameSize.getHeight() / (525.0 / 4.0))
        });
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getDistanceTraveled() { return distanceTraveled; }
    public int getLaserBatteryAmmo() { return laserBatteryAmmo; }
    public int getRechargeRate() { return rechargeRate; }
    public int getRechargeMax() { return rechargeMax; }
}
