import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
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

        gameLoop = new GameLoop(dimension);
        drawingSurface = new DrawingSurface(dimension);
        add(drawingSurface);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ArrayList[] temp = gameLoop.update();
        drawingSurface.change(temp);
        drawingSurface.repaint();
    }
}
