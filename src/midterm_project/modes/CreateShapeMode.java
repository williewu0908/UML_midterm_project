package midterm_project.modes;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import midterm_project.Canvas;
import midterm_project.shapes.Shape;

import java.util.function.Supplier;

public class CreateShapeMode extends Mode {
    private Supplier<Shape> shapeFactory;
    private Shape preview;
    private Mode previousMode;
    private JButton sourceButton; // 記錄是哪個按鈕發起的

    public CreateShapeMode(Canvas canvas, Supplier<Shape> factory, Mode prev, JButton btn) {
        super(canvas);
        this.shapeFactory = factory;
        this.previousMode = prev;
        this.sourceButton = btn;
    }// 使用方式：new CreateShapeMode(canvas, () -> new Rect(0, 0), canvas.getMode(), btn)

    // 當滑鼠在按鈕上拖動時，由按鈕呼叫此方法，接收按鈕傳來的座標
    public void processExternalDrag(MouseEvent e) {
        // e.getPoint拿到的是相對於sourceButton的座標，要轉換成相對於canvas的座標
        Point canvasPt = SwingUtilities.convertPoint(sourceButton, e.getPoint(), canvas);

        if (preview == null) {
            preview = shapeFactory.get();
            canvas.setPreviewShape(preview);
        }

        // 讓物件中心跟隨滑鼠
        preview.setBounds(canvasPt.x - 50, canvasPt.y - 40, 100, 80);
        canvas.repaint();
    }

    public void processExternalRelease(MouseEvent e) {
        Point canvasPt = SwingUtilities.convertPoint(sourceButton, e.getPoint(), canvas);

        // 判斷放開的位置是否在 Canvas 範圍內
        if (canvas.getBounds().contains(canvasPt)) {
            if (preview != null)
                canvas.addShape(preview);
        }

        canvas.setPreviewShape(null);
        canvas.setMode(previousMode); // 回到原模式
        canvas.repaint();
    }
}