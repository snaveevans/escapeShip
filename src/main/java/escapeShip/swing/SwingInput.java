package escapeShip.swing;

import escapeShip.core.GameWorld;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public final class SwingInput implements KeyListener {
    private final GameWorld world;

    public SwingInput(GameWorld world) {
        this.world = world;
    }

    @Override
    public void keyTyped(KeyEvent event) {
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_A || event.getKeyCode() == KeyEvent.VK_LEFT) {
            world.moveLeft(true);
        }
        if (event.getKeyCode() == KeyEvent.VK_D || event.getKeyCode() == KeyEvent.VK_RIGHT) {
            world.moveRight(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_A || event.getKeyCode() == KeyEvent.VK_LEFT) {
            world.moveLeft(false);
        }
        if (event.getKeyCode() == KeyEvent.VK_D || event.getKeyCode() == KeyEvent.VK_RIGHT) {
            world.moveRight(false);
        }
        if (event.getKeyCode() == KeyEvent.VK_SPACE) {
            world.fireLaser();
        }
        if (event.getKeyCode() == KeyEvent.VK_P) {
            world.pause();
        }
        if (event.getKeyCode() == KeyEvent.VK_R) {
            world.restart();
        }
    }
}
