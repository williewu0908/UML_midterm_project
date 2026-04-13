package midterm_project.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import java.awt.FontMetrics;

// 橢圓
public class Oval extends Shape {
    public Oval(int x, int y) {
        this.x = x;
        this.y = y;
        ports = new Port[4];
        for (int i = 0; i < 4; i++) {
            ports[i] = new Port(this, i);
        }
    }

    @Override
    public Point getPortLocation(int index) {
        switch (index) {
            case 0:
                return new Point(x + w / 2, y); // 上
            case 1:
                return new Point(x + w / 2, y + h); // 下
            case 2:
                return new Point(x, y + h / 2); // 左
            case 3:
                return new Point(x + w, y + h / 2); // 右
            default:
                return new Point(x, y);
        }
    }

    @Override
    public void resizeByPort(int portIndex, Rectangle o, Point newPos) {
        int fixedX = o.x;
        int fixedY = o.y;
        int movingX = o.x;
        int movingY = o.y;

        switch (portIndex) {
            case 0:
                fixedX = o.x;
                movingX = o.x + o.width;
                fixedY = o.y + o.height;
                movingY = newPos.y;
                break;
            case 1:
                fixedX = o.x;
                movingX = o.x + o.width;
                fixedY = o.y;
                movingY = newPos.y;
                break;
            case 2:
                fixedY = o.y;
                movingY = o.y + o.height;
                fixedX = o.x + o.width;
                movingX = newPos.x;
                break;
            case 3:
                fixedY = o.y;
                movingY = o.y + o.height;
                fixedX = o.x;
                movingX = newPos.x;
                break;
        }

        calculateAndSetBounds(fixedX, movingX, fixedY, movingY);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(labelColor);
        g.fillOval(x, y, w, h);
        g.setColor(Color.BLACK);
        g.drawOval(x, y, w, h);

        if (labelName != null && !labelName.isEmpty()) {
            FontMetrics fm = g.getFontMetrics();
            int stringWidth = fm.stringWidth(labelName);
            int stringHeight = fm.getAscent();
            g.drawString(labelName, x + (w - stringWidth) / 2, y + (h + stringHeight) / 2 - 2);
        }

        if (isSelected || isHovered) {
            for (Port port : ports) {
                port.draw(g);
            }
        }
    }

    // 使用了橢圓方程式 (x² / a²) + (y² / b²) <= 1判斷是否在橢圓內
    @Override
    public boolean contains(Point p) {
        double dx = p.x - (x + w / 2.0);
        double dy = p.y - (y + h / 2.0);
        return (dx * dx) / ((w / 2.0) * (w / 2.0)) + (dy * dy) / ((h / 2.0) * (h / 2.0)) <= 1.0;
    }
}