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

    protected void paintComponent(Graphics g){
        //System.out.println("paintComponent");
        g.setColor(Color.white);
        g.fillRect(0, 0, 10000, 10000);

        for(Asteroid asteroidObject: asteroidList){
            drawAsteroid(g,asteroidObject);
        }

        for(Laser laserObject: laserList){
            drawLaser(g, laserObject);
        }

        drawEscapeShip(g,escapeShip);
        
    }

    public DrawingSurface(Dimension dimension){
        System.out.println("DrawingSurface Contructor");
        this.dimension = dimension;
    }

    public void change(ArrayList[] gameObjects){
        asteroidList = gameObjects[0];
        laserList = gameObjects[1];
        ArrayList<EscapeShip> escapeShipList = gameObjects[2];
        escapeShip = escapeShipList.get(0);
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
        g.setColor(Color.green);
        g.drawRect((int)escapeShip.xCoordinate-escapeShip.size/2,(int)escapeShip.yCoordinate,escapeShip.size,escapeShip.size);
    }
}
