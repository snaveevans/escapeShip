import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by tyler_000 on 3/18/14.
 */
public class DrawingSurface extends JPanel{

    private Dimension dimension;
    private ArrayList<Asteroid> asteroidList;
    private ArrayList<Laser> laserList;
    private EscapeShip escapeShip;
    private boolean pause;
    private int level;

    protected void paintComponent(Graphics g){
        //System.out.println("paintComponent");
        g.setColor(Color.lightGray);
        g.fillRect(0, 0, (int)dimension.getWidth(), (int)dimension.getHeight());

        for(Asteroid asteroidObject: asteroidList){
            drawAsteroid(g,asteroidObject);
        }

        for(Laser laserObject: laserList){
            drawLaser(g, laserObject);
        }

        drawEscapeShip(g,escapeShip);

        if(pause)
            drawPause(g);

        drawStats(g);
        
    }

    public DrawingSurface(Dimension dimension){
        this.dimension = dimension;
    }

    public void change(GameLoop gameLoop){
        asteroidList = gameLoop.asteroidList;
        laserList = gameLoop.laserList;
        escapeShip = gameLoop.escapeShip;
        pause = gameLoop.pause;
        level = gameLoop.getLevel();
    }

    private void drawAsteroid(Graphics g, Asteroid asteroid){
        g.setColor(Color.black);
        g.drawRect((int)asteroid.xCoordinate,(int)asteroid.yCoordinate,asteroid.size,asteroid.size);
    }

    private void drawLaser(Graphics g, Laser laser){
        g.setColor(Color.red);
        g.drawRect((int)laser.xCoordinate,(int)laser.yCoordinate,1,6);
    }

    private void drawEscapeShip(Graphics g, EscapeShip escapeShip){
        g.setColor(Color.blue);
        g.drawRect((int)escapeShip.xCoordinate-escapeShip.size/2,(int)escapeShip.yCoordinate,escapeShip.size,escapeShip.size);
    }

    private void drawPause(Graphics g){
        g.setColor(Color.black);
        g.drawString("Paused: Press 'P' to continue or 'R' to Restart",((int)dimension.getWidth()/2)-135,(int)dimension.getWidth()/2);
        //g.drawString("Press 'R' to restart",((int)dimension.getWidth()/2)-40,(int)dimension.getWidth()/2+20);
    }

    private void drawStats(Graphics g){
        g.setColor(Color.black);
        g.drawString(escapeShip.writeDistanceTraveled() + " " + writeLevel(), 3, 13);

        g.setColor(Color.red);
        int x = 5;
        for(int i = 0; i < escapeShip.getLaserBatterAmmo();i++){
            x += 5;
            g.drawRect((int)dimension.getWidth()-x,5,1,7);
        }
        g.drawRect((int)dimension.getWidth()-7,1,-1*(120-1*escapeShip.getRechargeRate()),2);

    }

    private String writeLevel(){
        return "Level: " + level;
    }
}
