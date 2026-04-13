package midterm_project.lines;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import midterm_project.shapes.Port;

public class AssociationLine extends Line {
    public AssociationLine(Port start, Port end) {
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
        int ARROW_SIZE = 10;
        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;
        double angle = Math.atan2(dy, dx);
        g2.translate(p2.x, p2.y);
        g2.rotate(angle);
        g2.drawLine(0, 0, -ARROW_SIZE, -ARROW_SIZE);
        g2.drawLine(0, 0, -ARROW_SIZE, ARROW_SIZE);
        g2.dispose();
    }
}
