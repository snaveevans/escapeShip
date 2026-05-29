package escapeShip.swing;

import escapeShip.core.Asteroid;
import escapeShip.core.GameWorld;
import escapeShip.core.Laser;
import escapeShip.core.PolygonShape;
import escapeShip.core.Ship;
import escapeShip.core.Vec2;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

public final class SwingRenderer extends JPanel {
    private static final long serialVersionUID = 1L;

    private GameWorld world;

    public SwingRenderer(GameWorld world) {
        this.world = world;
    }

    public void setWorld(GameWorld world) {
        this.world = world;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        graphics.setColor(Color.lightGray);
        graphics.fillRect(0, 0, (int) world.getSize().getWidth(), (int) world.getSize().getHeight());

        for (Asteroid asteroid : world.getAsteroids()) {
            drawAsteroid(graphics, asteroid);
        }
        for (Laser laser : world.getLasers()) {
            drawLaser(graphics, laser);
        }
        drawShip(graphics, world.getShip());

        if (world.isPaused()) {
            drawPause(graphics);
        }
        drawStats(graphics);
    }

    private void drawAsteroid(Graphics graphics, Asteroid asteroid) {
        graphics.setColor(Color.black);
        graphics.drawPolygon(toAwtPolygon(asteroid.getShape()));
    }

    private void drawLaser(Graphics graphics, Laser laser) {
        graphics.setColor(Color.red);
        graphics.fill3DRect((int) laser.getX(), (int) laser.getY(), (int) laser.getWidth(), (int) laser.getHeight(), false);
    }

    private void drawShip(Graphics graphics, Ship ship) {
        graphics.setColor(Color.blue);
        graphics.drawPolygon(toAwtPolygon(ship.getShape()));
    }

    private Polygon toAwtPolygon(PolygonShape shape) {
        Vec2[] points = shape.getPoints();
        int[] xPoints = new int[points.length];
        int[] yPoints = new int[points.length];
        for (int i = 0; i < points.length; i++) {
            xPoints[i] = (int) points[i].getX();
            yPoints[i] = (int) points[i].getY();
        }
        return new Polygon(xPoints, yPoints, points.length);
    }

    private void drawPause(Graphics graphics) {
        graphics.setColor(Color.black);
        int width = (int) world.getSize().getWidth();
        int height = (int) world.getSize().getHeight();
        if (world.isFirstTime()) {
            graphics.drawString("Welcome!  Use the 'A' & 'D' keys to move", (width / 2) - 115, height / 2);
            graphics.drawString("'Space' to fire one laser, and 'P' to pause/continue", (width / 2) - 150, height / 2 + 20);
            graphics.drawString("Your ammo is displayed in the top right corner", (width / 2) - 128, height / 2 + 40);
        } else if (world.isGameOver()) {
            graphics.drawString("Game Over: press 'R' to start a new game", (width / 2) - 130, width / 2);
        } else {
            graphics.drawString("Paused: Press 'P' to continue or 'R' to Restart", (width / 2) - 135, width / 2);
        }
    }

    private void drawStats(Graphics graphics) {
        Ship ship = world.getShip();
        graphics.setColor(Color.black);
        graphics.drawString("Distance: " + (int) ship.getDistanceTraveled() + " Level: "
                + world.getLevel() + " Asteroids: " + world.getAsteroidsHit(), 3, 13);

        graphics.setColor(Color.red);
        int x = 5;
        for (int i = 0; i < ship.getLaserBatteryAmmo(); i++) {
            x += 5;
            graphics.drawRect((int) world.getSize().getWidth() - x, 5, 1, 7);
        }
        graphics.drawRect((int) world.getSize().getWidth() - 9 - ship.getRechargeMax() + ship.getRechargeRate(),
                1, ship.getRechargeMax() - ship.getRechargeRate(), 1);
    }
}
