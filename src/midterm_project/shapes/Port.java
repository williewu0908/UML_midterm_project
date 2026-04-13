package midterm_project.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

// 圖形上的節點
public class Port {
    private Shape parent; // 屬於哪個圖形
    private int index; // 屬於第幾個節點
    private final int PORT_SIZE = 8; // 節點大小

    public Port(Shape parent, int index) {
        this.parent = parent;
        this.index = index;
    }

    public Shape getParent() {
        return parent;
    }

    public int getIndex() {
        return index;
    }

    // 取得節點位置
    public Point getLocation() {
        return parent.getPortLocation(index);
    }

    public void draw(Graphics g) {
        Point p = getLocation();
        g.setColor(Color.BLACK);
        g.fillRect(p.x - PORT_SIZE / 2, p.y - PORT_SIZE / 2, PORT_SIZE, PORT_SIZE);
    }

    // 被點擊時，檢查滑鼠是否在節點上
    public boolean contains(Point p) {
        Point loc = getLocation(); // 取得所屬圖形的節點位置
        return new Rectangle(loc.x - PORT_SIZE / 2, loc.y - PORT_SIZE / 2, PORT_SIZE, PORT_SIZE).contains(p);
    }
}
