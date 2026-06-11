package midterm_project.modes;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.List;

import midterm_project.Canvas;
import midterm_project.shapes.Port;
import midterm_project.shapes.Shape;

public class SelectMode extends Mode {
    private Point startPoint = null;
    private Point lastDragPoint = null;
    private java.awt.Rectangle selectionBox = null;
    private Shape draggingShape = null;

    private Shape resizingShape = null;
    private int resizingPortIndex = -1;
    private java.awt.Rectangle originalResizingBounds = null;

    public SelectMode(Canvas canvas) {
        super(canvas);
    }

    // 單點選取與拖曳
    @Override
    public void mousePressed(MouseEvent e) {
        List<Shape> shapes = canvas.getShapes();
        boolean hit = false; // 標記是否點擊到圖形
        Shape selectedShape = null;

        // 先檢查是否點擊到任何處於選取或 Hover 狀態圖形的 Port (縮放操作優先於移動)
        for (int i = shapes.size() - 1; i >= 0; i--) {
            Shape s = shapes.get(i);
            if (s.isSelected() || s.isHovered()) {
                Port p = s.getPortAt(e.getPoint());
                if (p != null) {
                    resizingShape = s;
                    resizingPortIndex = p.getIndex();
                    originalResizingBounds = s.getBounds();
                    hit = true;
                    selectedShape = s;

                    // 點擊 Port 時同時單獨選取該圖形
                    for (Shape shape : shapes) {
                        shape.setSelected(shape == s);
                    }
                    break;
                }
            }
        }

        // 若沒有點到 Port，則檢查是否點擊到圖形本體
        if (!hit) {
            for (int i = shapes.size() - 1; i >= 0; i--) {
                Shape s = shapes.get(i);
                if (!hit && s.contains(e.getPoint())) {
                    s.setSelected(true);
                    hit = true;
                    selectedShape = s;
                } else {
                    s.setSelected(false);
                }
            }

            if (selectedShape != null) {
                canvas.moveShapeToFront(selectedShape);
                draggingShape = selectedShape;
                lastDragPoint = e.getPoint();
                startPoint = null;
            } else {
                draggingShape = null;
                lastDragPoint = null;
                startPoint = e.getPoint();
            }
        } else {
            // 點到 Port 的情況
            canvas.moveShapeToFront(selectedShape);
            draggingShape = null;
            lastDragPoint = null;
            startPoint = null;
        }

        canvas.repaint();
    }

    // 範圍框選或移動/縮放物件
    @Override
    public void mouseDragged(MouseEvent e) {
        if (resizingShape != null) {
            // 縮放被選取的物件
            resizingShape.resizeByPort(resizingPortIndex, originalResizingBounds, e.getPoint());
            canvas.repaint();
        } else if (draggingShape != null && lastDragPoint != null) {
            // 移動被選取的物件
            int dx = e.getX() - lastDragPoint.x;
            int dy = e.getY() - lastDragPoint.y;
            for (Shape s : canvas.getShapes()) {
                if (s.isSelected()) {
                    s.move(dx, dy);
                }
            }
            lastDragPoint = e.getPoint();
            canvas.repaint();
        } else if (startPoint != null) {
            // 在空白處拖曳才進行框選
            int x = Math.min(startPoint.x, e.getX());
            int y = Math.min(startPoint.y, e.getY());
            int w = Math.abs(startPoint.x - e.getX());
            int h = Math.abs(startPoint.y - e.getY());
            selectionBox = new java.awt.Rectangle(x, y, w, h);
            if (canvas != null) {
                canvas.setSelectionBox(selectionBox);
            }
        }
        updateHoverState(e);
    }

    // 放開滑鼠時檢查框選範圍內的所有圖形或重置狀態
    @Override
    public void mouseReleased(MouseEvent e) {
        if (resizingShape != null) {
            // 結束縮放
            resizingShape = null;
            resizingPortIndex = -1;
            originalResizingBounds = null;
            canvas.repaint();
        } else if (draggingShape != null) {
            // 結束移動
            draggingShape = null;
            lastDragPoint = null;
            canvas.repaint();
        } else if (selectionBox != null) {
            List<Shape> shapes = canvas.getShapes();
            // 檢查canvas內的所有圖形，在框內的就選取
            for (Shape s : shapes) {
                if (selectionBox.contains(s.getBounds())) {
                    s.setSelected(true);
                }
            }
            selectionBox = null;
            canvas.setSelectionBox(null);
            startPoint = null;
            canvas.repaint();
        } else {
            // 單純點擊在空白處
            startPoint = null;
            canvas.repaint();
        }
    }
}