package midterm_project.modes;

import java.awt.event.MouseEvent;
import java.util.List;

import midterm_project.Canvas;
import midterm_project.shapes.Shape;

import java.util.function.BiFunction;

import midterm_project.shapes.Port;
import midterm_project.lines.Line;

public class CreateLinkMode extends Mode {
    private BiFunction<Port, Port, Line> lineFactory;
    private Port startPort = null;
    private Line previewLine = null;

    public CreateLinkMode(Canvas canvas, BiFunction<Port, Port, Line> lineFactory) {
        super(canvas);
        this.lineFactory = lineFactory;
    }

    // 尋找起點
    @Override
    public void mousePressed(MouseEvent e) {
        // 從最上層圖形尋找是否點擊在 port 上
        List<Shape> shapes = canvas.getShapes();
        for (int i = shapes.size() - 1; i >= 0; i--) {
            Shape s = shapes.get(i);
            Port p = s.getPortAt(e.getPoint());
            if (p != null) {
                startPort = p;

                // 根據類型產生對應的預覽線段
                previewLine = lineFactory.apply(startPort, null);
                if (previewLine != null) {
                    previewLine.setTempEndPoint(e.getPoint());
                    canvas.setPreviewLine(previewLine);
                }
                break;
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (startPort != null && previewLine != null) {
            previewLine.setTempEndPoint(e.getPoint());
        }
        updateHoverState(e); // 拖曳時也顯示目標物件的 ports
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (startPort != null) {
            // 確認釋放點是否有另一個 port (且不屬於自己 parent)
            Port endPort = null;
            List<Shape> shapes = canvas.getShapes();
            for (int i = shapes.size() - 1; i >= 0; i--) {
                Shape s = shapes.get(i);
                Port p = s.getPortAt(e.getPoint());
                if (p != null && p.getParent() != startPort.getParent()) {
                    endPort = p;
                    break;
                }
            }

            // 如果合法，產生正式線段
            if (endPort != null) {
                Line finalLine = lineFactory.apply(startPort, endPort);
                if (finalLine != null) {
                    canvas.addLine(finalLine);
                }
            }
        }

        // 重置狀態
        startPort = null;
        previewLine = null;
        canvas.setPreviewLine(null);
        canvas.repaint();
    }
}