package midterm_project.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public abstract class Shape {
    protected int x, y, w = 100, h = 80; // 預設尺寸
    protected boolean isSelected = false;
    protected boolean isHovered = false;
    protected Port[] ports;

    // Label 屬性
    protected String labelName = "";
    protected Color labelColor = Color.LIGHT_GRAY;

    public void setLabelName(String name) {
        this.labelName = name;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelColor(Color color) {
        this.labelColor = color;
    }

    public Color getLabelColor() {
        return labelColor;
    }

    // 計算滑鼠座標是否落在圖形內
    public abstract boolean contains(Point p);

    // 繪製圖形
    public abstract void draw(Graphics g);

    // 取得節點位置
    public abstract Point getPortLocation(int index);

    // 依據控制節點縮放
    public abstract void resizeByPort(int portIndex, Rectangle originalBounds, Point newPos);

    // 輔助計算並設置新範圍，處理反向拖曳與最小尺寸限制
    protected void calculateAndSetBounds(int fixedX, int movingX, int fixedY, int movingY) {
        int tempW = Math.abs(movingX - fixedX);
        if (tempW < 20) {
            movingX = (movingX >= fixedX) ? fixedX + 20 : fixedX - 20;
        }
        int tempH = Math.abs(movingY - fixedY);
        if (tempH < 20) {
            movingY = (movingY >= fixedY) ? fixedY + 20 : fixedY - 20;
        }
        this.x = Math.min(fixedX, movingX);
        this.y = Math.min(fixedY, movingY);
        this.w = Math.abs(movingX - fixedX);
        this.h = Math.abs(movingY - fixedY);
    }

    // 移動物件
    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    public void setSelected(boolean b) {
        this.isSelected = b;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setHovered(boolean b) {
        this.isHovered = b;
    }

    public boolean isHovered() {
        return isHovered;
    }

    // 在判定範圍時先簡單判斷是否在矩形內
    public Rectangle getBounds() {
        return new Rectangle(x, y, w, h);
    }

    public void setBounds(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    // 檢查滑鼠是否在節點上
    public Port getPortAt(Point p) {
        if (ports != null) {
            for (Port port : ports) {
                if (port.contains(p))
                    return port;
            }
        }
        return null;
    }
}