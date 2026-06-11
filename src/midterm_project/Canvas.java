package midterm_project;

import javax.swing.*;

import midterm_project.lines.Line;
import midterm_project.modes.Mode;
import midterm_project.modes.SelectMode;
import midterm_project.shapes.CompositeShape;
import midterm_project.shapes.Shape;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

// 擔任 Context (環境)，持有當前的模式並執行繪圖。
// View
public class Canvas extends JPanel {
    private UMLDocument document;
    
    private Mode currentMode;
    private Shape previewShape = null; // 用來存放拖曳中的預覽圖形
    private Line previewLine = null; // 用來存放拖曳中的預覽連結
    private Rectangle selectionBox = null;

    public Canvas(UMLDocument document) {
        this.document = document;
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        // 註冊觀察者：當 Model 資料變更時自動重繪
        document.addListener(() -> repaint());

        // 預設為選取模式

        // ==========================================
        // State Pattern
        // 核心機制：事件委派 (Event Delegation)
        // 畫布本身不處理任何邏輯，而是把所有捕捉到的滑鼠事件
        // 原封不動地轉交給 currentMode (當前模式) 去執行。
        // 這讓你未來新增模式時，完全不需要修改 Canvas 裡的程式碼。
        // 建立 MouseAdapter 物件，負責接收所有的滑鼠事件(MouseAdapter本身是一個空實作，我們只需要重寫需要的方法)
        // ==========================================
        currentMode = new SelectMode(this);
        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                currentMode.mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                currentMode.mouseReleased(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                currentMode.mouseDragged(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                currentMode.mouseMoved(e);
            }
        };
        addMouseListener(adapter);
        addMouseMotionListener(adapter);
    }

    public List<Shape> getShapes() {
        return document.getShapes();
    }

    public List<Line> getLines() {
        return document.getLines();
    }

    // 把物件推到最上層
    public void moveShapeToFront(Shape s) {
        document.moveShapeToFront(s);
    }

    public void setMode(Mode mode) {
        this.currentMode = mode;
    }

    public Mode getMode() {
        return currentMode;
    }

    public void addShape(Shape s) {
        document.addShape(s);
        // 不需手動 repaint，Observer 會處理
    }

    public void addLine(Line l) {
        document.addLine(l);
        // 不需手動 repaint，Observer 會處理
    }

    public void setPreviewShape(Shape s) {
        this.previewShape = s;
        repaint();
    }

    public void setPreviewLine(Line l) {
        this.previewLine = l;
        repaint();
    }

    public void setSelectionBox(Rectangle rect) {
        this.selectionBox = rect;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 畫出所有的連線 (先畫線，再畫形狀，確保線在下層)
        for (Line l : document.getLines()) {
            l.draw(g);
        }

        // 畫出所有的形狀
        for (Shape s : document.getShapes()) {
            s.draw(g);
        }

        // 如果有預覽圖形，就畫出來
        if (previewShape != null) {
            previewShape.draw(g);
        }
        if (previewLine != null) {
            previewLine.draw(g);
        }

        if (selectionBox != null) {
            g.setColor(new Color(0, 0, 255, 50));
            g.fillRect(selectionBox.x, selectionBox.y, selectionBox.width, selectionBox.height);
            g.setColor(Color.BLUE);
            g.drawRect(selectionBox.x, selectionBox.y, selectionBox.width, selectionBox.height);
        }
    }

}