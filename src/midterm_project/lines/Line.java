package midterm_project.lines;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import midterm_project.shapes.Port;

public abstract class Line {
    protected Port startPort;
    protected Port endPort;
    protected Point tempEndPoint; // 拖曳時預覽位置

    public Line(Port startPort, Port endPort) {
        this.startPort = startPort;
        this.endPort = endPort;
    }

    // CreateLineMode在拖曳時會呼叫這個方法設定一個暫時的終點
    public void setTempEndPoint(Point p) {
        this.tempEndPoint = p;
    }

    public abstract void draw(Graphics g);

    protected void drawBasicLine(Graphics g, Point p1, Point p2) {
        g.setColor(Color.BLACK);
        g.drawLine(p1.x, p1.y, p2.x, p2.y);
    }
}