package main;

import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    static int window_width = 1500;
    static int window_height = 850;

    private static void initUI() {
        JFrame f = new JFrame("Harta Luxemburgului");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setResizable(false);
        f.pack();

//elimina task-bar-ul ca sa nu imi apara in josul paginii
        Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(f.getGraphicsConfiguration());
        int taskBarSize = scnMax.bottom;
        window_height -= taskBarSize;

        Graph.Init(window_width, window_height);

        MyPanel myPanel = new MyPanel();
        myPanel.addMouseListener(myPanel);

        f.add(myPanel);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        //start the graphic execution thread
        SwingUtilities.invokeLater(new Runnable() //new Thread()
        {
            public void run() {
                initUI();
            }
        });
    }
}