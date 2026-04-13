package midterm_project.lines;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;

import midterm_project.shapes.Port;

public class CompositionLine extends Line {
    public CompositionLine(Port start, Port end) {
        super(start, end);
    }

    @Override
    public void draw(Graphics g) {
        Point p1 = startPort.getLocation();
        Point p2 = (endPort != null) ? endPort.getLocation() : tempEndPoint;
        if (p2 == null)
            return;
        drawBasicLine(g, p1, p2);

        Graphics2D g2 = (Graphics2D) g.create();
        int ARROW_SIZE = 16;
        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;
        double angle = Math.atan2(dy, dx);
        g2.translate(p2.x, p2.y);
        g2.rotate(angle);

        Polygon diamond = new Polygon();
        diamond.addPoint(0, 0);
        diamond.addPoint(-ARROW_SIZE / 2, -ARROW_SIZE / 3);
        diamond.addPoint(-ARROW_SIZE, 0);
        diamond.addPoint(-ARROW_SIZE / 2, ARROW_SIZE / 3);
        g2.setColor(Color.WHITE);
        g2.fillPolygon(diamond);
        g2.setColor(Color.BLACK);
        g2.drawPolygon(diamond);
        g2.dispose();
    }
}
