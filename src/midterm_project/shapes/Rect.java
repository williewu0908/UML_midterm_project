package midterm_project.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import java.awt.FontMetrics;

// 矩形
public class Rect extends Shape {
    public Rect(int x, int y) {
        this.x = x;
        this.y = y;
        ports = new Port[8];
        for (int i = 0; i < 8; i++) {
            ports[i] = new Port(this, i);
        }
    }

    // 四個角加上四個邊的中間點
    @Override
    public Point getPortLocation(int index) {
        switch (index) {
            case 0:
                return new Point(x, y); // 左上
            case 1:
                return new Point(x + w, y); // 右上
            case 2:
                return new Point(x, y + h); // 左下
            case 3:
                return new Point(x + w, y + h); // 右下
            case 4:
                return new Point(x + w / 2, y); // 上中
            case 5:
                return new Point(x + w / 2, y + h); // 下中
            case 6:
                return new Point(x, y + h / 2); // 左中
            case 7:
                return new Point(x + w, y + h / 2); // 右中
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
                fixedX = o.x + o.width;
                fixedY = o.y + o.height;
                movingX = newPos.x;
                movingY = newPos.y;
                break;
            case 1:
                fixedX = o.x;
                fixedY = o.y + o.height;
                movingX = newPos.x;
                movingY = newPos.y;
                break;
            case 2:
                fixedX = o.x + o.width;
                fixedY = o.y;
                movingX = newPos.x;
                movingY = newPos.y;
                break;
            case 3:
                fixedX = o.x;
                fixedY = o.y;
                movingX = newPos.x;
                movingY = newPos.y;
                break;
            case 4:
                fixedX = o.x;
                movingX = o.x + o.width;
                fixedY = o.y + o.height;
                movingY = newPos.y;
                break;
            case 5:
                fixedX = o.x;
                movingX = o.x + o.width;
                fixedY = o.y;
                movingY = newPos.y;
                break;
            case 6:
                fixedY = o.y;
                movingY = o.y + o.height;
                fixedX = o.x + o.width;
                movingX = newPos.x;
                break;
            case 7:
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
        g.fillRect(x, y, w, h);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, w, h);

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

    @Override
    public boolean contains(Point p) {
        return new Rectangle(x, y, w, h).contains(p);
    }
}