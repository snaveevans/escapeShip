import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.awt.event.ActionListener;

/**
 * Created by Tyler on 3/11/14.
 */
public class MainFrame extends JFrame implements Runnable, ActionListener{

    JFrame frame;
    GameLoop gameLoop;
    DrawingSurface drawingSurface;
    private int height = 525;
    private int width = 350;
    private Dimension dimension = new Dimension(width, height);

    public MainFrame(){

    }

    @Override
    public void run() {
        frame = new JFrame();
        setSize(dimension); //16:9 resolution
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Escape Ship");
        setFocusable(true);

        gameLoop = new GameLoop(dimension);
        drawingSurface = new DrawingSurface(dimension);
        add(drawingSurface);
        addKeyListener(new KeyCatcher());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //ArrayList[] temp = gameLoop.update();
        drawingSurface.change(gameLoop.update());
        drawingSurface.repaint();
    }

    /*--------------------------------------------------------------------------------
                                Keyboard Listener
    --------------------------------------------------------------------------------*/
    class KeyCatcher implements KeyListener{

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode()==KeyEvent.VK_A){//start moving left
                gameLoop.moveLeft(true);
            }
            if(e.getKeyCode()==KeyEvent.VK_D){//start moving right
                gameLoop.moveRight(true);
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if(e.getKeyCode()==KeyEvent.VK_A){//stop moving left
                gameLoop.moveLeft(false);
            }
            if(e.getKeyCode()==KeyEvent.VK_D){//stop moving right
                gameLoop.moveRight(false);
            }
            if(e.getKeyCode()==KeyEvent.VK_SPACE){
                gameLoop.fireLaser();
            }
            if(e.getKeyCode()==KeyEvent.VK_P){
                gameLoop.pause();
            }
            if(e.getKeyCode()==KeyEvent.VK_R){
                gameLoop.restart();
            }
        }
    }
}
