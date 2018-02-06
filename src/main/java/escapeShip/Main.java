package escapeShip;

import java.awt.*;
import javax.swing.*;

/**
 * Created by Tyler on 3/11/14.
 */
public class Main {
    public static Timer t;
    public static final int UPS = 60;//updates per second
    public static void main(String[] args){
        MainFrame mainFrame = new MainFrame();
        EventQueue.invokeLater(mainFrame);
        t = new Timer((1000/UPS), mainFrame);
        t.start();
    }
}
