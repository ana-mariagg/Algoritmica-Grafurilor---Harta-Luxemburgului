package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class MyPanel extends JPanel implements MouseListener {

    private Graph myGraph = new Graph();

    public MyPanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));

        try {
            myGraph.ReadMap();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        repaint();
    }

    //se apeleaza la repaint()
    protected void paintComponent(Graphics graphicsComponent) {
        super.paintComponent(graphicsComponent);
        myGraph.DrawGraph(graphicsComponent);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        myGraph.NodulCelMaiApropiat(x, y);

        repaint();

        if (myGraph.ApelDijkstra())
            repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    public Dimension getPreferredSize() {
        return new Dimension(1500, 850);
    }
}