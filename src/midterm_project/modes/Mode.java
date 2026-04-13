package midterm_project.modes;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import midterm_project.Canvas;
import midterm_project.shapes.Shape;

// 擔任 State (狀態)，定義具體的滑鼠行為。
// 模式基底，繼承 MouseAdapter 方便只實作需要的 Mouse Event
public abstract class Mode extends MouseAdapter {
    protected Canvas canvas;

    public Mode(Canvas canvas) {
        this.canvas = canvas;
    }

    public void mouseMoved(MouseEvent e) {
        updateHoverState(e);
    }

    // 檢查滑鼠下方是否有圖形
    protected void updateHoverState(MouseEvent e) {
        List<Shape> shapes = canvas.getShapes();
        boolean hit = false;

        // 從最上層圖形開始檢查
        for (int i = shapes.size() - 1; i >= 0; i--) {
            Shape s = shapes.get(i);
            if (!hit && s.contains(e.getPoint())) {
                s.setHovered(true);
                hit = true;
            } else {
                s.setHovered(false);
            }
        }
        canvas.repaint();
    }
}