package escapeShip;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by tyler_000 on 3/18/14.
 */
public class DrawingSurface extends JPanel{

    private Dimension dimension;
    private GameLoop gameLoop;


    protected void paintComponent(Graphics g){
        //System.out.println("paintComponent");
        g.setColor(Color.lightGray);
        g.fillRect(0, 0, (int)dimension.getWidth(), (int)dimension.getHeight());

        for(Asteroid asteroidObject: gameLoop.asteroidList){
            drawAsteroid(g,asteroidObject);
        }

        for(Laser laserObject: gameLoop.laserList){
            drawLaser(g, laserObject);
        }

        drawEscapeShip(g,gameLoop.escapeShip);

        if(gameLoop.pause)
            drawPause(g);

        drawStats(g);
        
    }

    public DrawingSurface(Dimension dimension){
        this.dimension = dimension;
    }

    public void change(GameLoop gameLoop){
        this.gameLoop = gameLoop;
    }

    private void drawAsteroid(Graphics g, Asteroid asteroid){
        g.setColor(Color.black);
        g.drawPolygon(asteroid.gamePolygon);
    }

    private void drawLaser(Graphics g, Laser laser){
        g.setColor(Color.red);
        g.fill3DRect((int)laser.xCoordinate,(int)laser.yCoordinate,Laser.xSize,Laser.ySize,false);
    }

    private void drawEscapeShip(Graphics g, EscapeShip escapeShip){
        g.setColor(Color.blue);
        g.drawPolygon(escapeShip.gamePolygon);
        //g.fillPolygon(escapeShip.gamePolygon);
    }

    private void drawPause(Graphics g){
        g.setColor(Color.black);
        if(gameLoop.firstTime) {
            g.drawString("Welcome!  Use the 'A' & 'D' keys to move", ((int) dimension.getWidth() / 2) - 115, (int) dimension.getHeight() / 2);
            g.drawString("'Space' to fire, and 'P' to pause/continue", ((int) dimension.getWidth() / 2) - 114, (int) dimension.getHeight() / 2 + 20);
            g.drawString("Your ammo is displayed in the top right corner",((int)dimension.getWidth()/2)-128,(int) dimension.getHeight() / 2 + 40);
        }
        else if(gameLoop.gameOver)
            g.drawString("Game Over: press 'R' to start a new game",((int)dimension.getWidth()/2)-130,(int)dimension.getWidth()/2);
        else
            g.drawString("Paused: Press 'P' to continue or 'R' to Restart",((int)dimension.getWidth()/2)-135,(int)dimension.getWidth()/2);
    }

    private void drawStats(Graphics g){
        g.setColor(Color.black);
        g.drawString(gameLoop.escapeShip.writeDistanceTraveled() + " " + writeLevel() + " Asteroids: " + gameLoop.asteroidsHit, 3, 13);

        g.setColor(Color.red);
        int x = 5;
        for(int i = 0; i < gameLoop.escapeShip.getLaserBatterAmmo();i++){
            x += 5;
            g.drawRect((int)dimension.getWidth()-x,5,1,7);
        }
        g.drawRect((int)dimension.getWidth()-9-gameLoop.escapeShip.getRechargeMax()+gameLoop.escapeShip.getRechargeRate(),1,gameLoop.escapeShip.getRechargeMax()-gameLoop.escapeShip.getRechargeRate(),1);
    }

    private String writeLevel(){
        return "Level: " + gameLoop.level;
    }
}
