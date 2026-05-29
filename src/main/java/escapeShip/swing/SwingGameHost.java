package escapeShip.swing;

import escapeShip.core.GameSize;
import escapeShip.core.GameWorld;

import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class SwingGameHost extends JFrame implements Runnable, ActionListener {
    private static final long serialVersionUID = 1L;

    private final GameSize size = new GameSize(540, 960);
    private final GameWorld world = new GameWorld(size);
    private final SwingRenderer renderer = new SwingRenderer(world);

    @Override
    public void run() {
        setSize((int) size.getWidth(), (int) size.getHeight());
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Escape Ship");
        setFocusable(true);

        add(renderer);
        addKeyListener(new SwingInput(world));
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        world.update();
        renderer.repaint();
    }

    public static void launch() {
        SwingGameHost host = new SwingGameHost();
        EventQueue.invokeLater(host);
        Timer timer = new Timer(1000 / GameWorld.DEFAULT_UPDATES_PER_SECOND, host);
        timer.start();
    }
}
