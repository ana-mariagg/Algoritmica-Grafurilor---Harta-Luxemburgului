package main;

import java.awt.Graphics;
import java.awt.Point;

public class Arc {
    public int capat1;
    public int capat2;
    public int cost;
    public int index;

    private Point start;
    private Point end;

    public Arc(int capat1, int capat2, int cost, Point start, Point end, int index) {
        this.capat1 = capat1;
        this.capat2 = capat2;
        this.cost = cost;
        this.start = start;
        this.end = end;
        this.index = index;
    }

    public void DrawArc(Graphics graphicsComponent) {
        graphicsComponent.drawLine(start.x, start.y, end.x, end.y);
    }
}