import java.awt.*;
import javax.swing.*;

/**
 * Created by Tyler on 3/11/14.
 */
public class Main {
    public static Timer t;
    public static void main(String[] args){
        MainFrame mainFrame = new MainFrame();
        EventQueue.invokeLater(mainFrame);
        t = new Timer(16, mainFrame);
        t.start();
    }
}
