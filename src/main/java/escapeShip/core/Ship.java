package escapeShip.core;

public final class Ship {
    private static final double SECONDS_TO_CROSS = 2;
    private static final int MAX_AMMO = 10;

    private final GameSize gameSize;
    private final int updatesPerSecond;
    private final int rechargeMax;
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
        this.rechargeMax = (int) (0.75 * updatesPerSecond);
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
        x = gameSize.getWidth() / 2.0;
    }

    public Laser fire() {
        laserBatteryAmmo--;
        return new Laser(x, y, gameSize, updatesPerSecond);
    }

    public boolean canFire() {
        return laserBatteryAmmo > 0;
    }

    public void update(InputState input) {
        distanceTraveled += travelSpeed;
        double xSpeed;
        double halfWidth = gameSize.getWidth() / (350.0 / 6.0);
        if (input.isMovingLeft() && input.isMovingRight()) {
            xSpeed = 0;
        } else if (input.isMovingLeft() && x > halfWidth) {
            xSpeed = -speed;
        } else if (input.isMovingRight() && x < gameSize.getWidth() - halfWidth) {
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
        double halfWidth = gameSize.getWidth() / (350.0 / 6.0);
        double innerWidth = gameSize.getWidth() / (350.0 / 2.4);
        double noseHeight = gameSize.getHeight() / (525.0 / 13.0);
        double lowerShoulder = gameSize.getHeight() / (525.0 / 8.0);
        double tailHeight = gameSize.getHeight() / (525.0 / 4.5);
        double tailNotch = gameSize.getHeight() / (525.0 / 5.8);
        return new PolygonShape(new Vec2[] {
                new Vec2(x, y - noseHeight),
                new Vec2(x + halfWidth, y + lowerShoulder),
                new Vec2(x + innerWidth, y + tailNotch),
                new Vec2(x, y + tailHeight),
                new Vec2(x - innerWidth, y + tailNotch),
                new Vec2(x - halfWidth, y + lowerShoulder)
        });
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getDistanceTraveled() { return distanceTraveled; }
    public int getLaserBatteryAmmo() { return laserBatteryAmmo; }
    public int getRechargeRate() { return rechargeRate; }
    public int getRechargeMax() { return rechargeMax; }
}
