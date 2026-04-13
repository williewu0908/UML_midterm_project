package midterm_project.lines;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;

import midterm_project.shapes.Port;

public class GeneralizationLine extends Line {
    public GeneralizationLine(Port start, Port end) {
        super(start, end);
    }

    @Override
    public void draw(Graphics g) {
        Point p1 = startPort.getLocation();
        Point p2 = (endPort != null) ? endPort.getLocation() : tempEndPoint;
        if (p2 == null)
            return;

        // 此處避免線條穿過箭頭內部，可計算縮進(若需要)
        // 為了簡單，先直接畫線到底，然後畫白色填充的三角形
        drawBasicLine(g, p1, p2);

        Graphics2D g2 = (Graphics2D) g.create();
        int ARROW_SIZE = 15;
        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;
        double angle = Math.atan2(dy, dx);
        g2.translate(p2.x, p2.y);
        g2.rotate(angle);

        Polygon triangle = new Polygon();
        triangle.addPoint(0, 0);
        triangle.addPoint(-ARROW_SIZE, -ARROW_SIZE / 2);
        triangle.addPoint(-ARROW_SIZE, ARROW_SIZE / 2);
        g2.setColor(Color.WHITE);
        g2.fillPolygon(triangle);
        g2.setColor(Color.BLACK);
        g2.drawPolygon(triangle);
        g2.dispose();
    }
}