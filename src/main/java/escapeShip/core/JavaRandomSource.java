package escapeShip.core;

import java.util.Random;

public final class JavaRandomSource implements RandomSource {
    private final Random random = new Random();

    @Override
    public int nextInt(int bound) {
        return random.nextInt(bound);
    }

    @Override
    public double nextDouble() {
        return random.nextDouble();
    }
}
