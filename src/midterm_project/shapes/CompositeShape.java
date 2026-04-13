package midterm_project.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

// 群組圖形 (Composite)
public class CompositeShape extends Shape {
    private List<Shape> children = new ArrayList<>(); // 儲存群組內的所有物件

    public CompositeShape(List<Shape> selectedShapes) {
        this.children.addAll(selectedShapes);
        updateBounds();
    }

    public List<Shape> getChildren() {
        return children;
    }

    // 尋找所有物件的最左上角與最右下角，以決定群組的邊界
    private void updateBounds() {
        if (children.isEmpty())
            return;
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        // 遍歷所有物件，更新群組的邊界
        for (Shape s : children) {
            Rectangle r = s.getBounds();
            if (r.x < minX)
                minX = r.x;
            if (r.y < minY)
                minY = r.y;
            if (r.x + r.width > maxX)
                maxX = r.x + r.width;
            if (r.y + r.height > maxY)
                maxY = r.y + r.height;
        }

        this.x = minX;
        this.y = minY;
        this.w = maxX - minX;
        this.h = maxY - minY;
    }

    @Override
    public Point getPortLocation(int index) {
        // 規格定義 composite object 不能變形、無 ports
        return null;
    }

    @Override
    public void resizeByPort(int portIndex, Rectangle originalBounds, Point newPos) {
        // composite object 不能進行縮放拖曳
    }

    @Override
    public void move(int dx, int dy) {
        super.move(dx, dy); // Update self bounds
        for (Shape s : children) {
            s.move(dx, dy);
        }
    }

    // 呼叫群組內所有物件的draw
    @Override
    public void draw(Graphics g) {
        for (Shape s : children) {
            // 紀錄圖形原本的狀態
            boolean wasSelected = s.isSelected();
            boolean wasHovered = s.isHovered();
            // 取消內部圖形的選取外觀，但維持實體繪製
            s.setSelected(false);
            s.setHovered(false);
            s.draw(g);
            // 恢復圖形原本的狀態，但因為已經繪製完了，所以畫面沒有改變（小黑點沒出現）
            s.setSelected(wasSelected);
            s.setHovered(wasHovered);
        }

        if (isSelected || isHovered) {
            g.setColor(new Color(150, 150, 255, 100));
            g.fillRect(x, y, w, h);
            g.setColor(Color.BLUE);
            g.drawRect(x, y, w, h);
        }
    }

    @Override
    public boolean contains(Point p) {
        return getBounds().contains(p);
    }
}