package main;

import java.util.Vector;

public class Node {
    public Vector<Arc> ArceAdiacente = new Vector<Arc>();

    public int x;
    public int y;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }
}