package escapeShip.core;

public final class CollisionResult {
    private final boolean shipHit;
    private final int asteroidsHit;

    public CollisionResult(boolean shipHit, int asteroidsHit) {
        this.shipHit = shipHit;
        this.asteroidsHit = asteroidsHit;
    }

    public boolean isShipHit() {
        return shipHit;
    }

    public int getAsteroidsHit() {
        return asteroidsHit;
    }
}
