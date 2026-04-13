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
import java.util.ArrayList;
import java.util.List;

// 擔任 Context (環境)，持有當前的模式並執行繪圖。
// 繪圖區
public class Canvas extends JPanel {
    private List<Shape> shapes = new ArrayList<>();
    private List<Line> lines = new ArrayList<>();
    private Mode currentMode;
    private Shape previewShape = null; // 用來存放拖曳中的預覽圖形
    private Line previewLine = null; // 用來存放拖曳中的預覽連結
    private java.awt.Rectangle selectionBox = null;

    public Canvas() {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        // 預設為選取模式
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
        return shapes;
    }

    public List<Line> getLines() {
        return lines;
    }

    // 把物件推到最上層
    public void moveShapeToFront(Shape s) {
        if (shapes.remove(s)) {
            shapes.add(s); // 加到 List 最後面，繪製時會在最上面
        }
    }

    public void setMode(Mode mode) {
        this.currentMode = mode;
    }

    public Mode getMode() {
        return currentMode;
    }

    public void addShape(Shape s) {
        shapes.add(s);
        repaint();
    }

    public void addLine(Line l) {
        lines.add(l);
        repaint();
    }

    public void setPreviewShape(Shape s) {
        this.previewShape = s;
        repaint();
    }

    public void setPreviewLine(Line l) {
        this.previewLine = l;
        repaint();
    }

    public void setSelectionBox(java.awt.Rectangle rect) {
        this.selectionBox = rect;
        repaint();
    }

    // 建立群組
    public void groupSelectedShapes() {
        List<Shape> selected = new ArrayList<>();
        for (Shape s : shapes) {
            if (s.isSelected()) {
                selected.add(s);
            }
        }

        // 如果超過兩個物件被選取，就建立群組
        if (selected.size() >= 2) {
            CompositeShape group = new CompositeShape(selected);
            group.setSelected(true);
            shapes.removeAll(selected);
            shapes.add(group); // 會自動加到最後面 (最上層)
            repaint();
        }
    }

    // 解散群組
    public void ungroupSelectedShapes() {
        List<Shape> selected = new ArrayList<>();
        for (Shape s : shapes) {
            if (s.isSelected()) {
                selected.add(s);
            }
        }

        // 如果剛好選到一個物件，且該物件是群組，就解散群組
        if (selected.size() == 1 && selected.get(0) instanceof CompositeShape) {
            CompositeShape group = (CompositeShape) selected.get(0);
            shapes.remove(group);
            for (Shape child : group.getChildren()) {
                child.setSelected(true); // 群組解散後預設選取內部元素
                shapes.add(child);
            }
            repaint();
        }
    }

    // 自定義圖形標籤
    public void customizeSelectedLabel() {
        List<Shape> selected = new ArrayList<>();
        for (Shape s : shapes) {
            if (s.isSelected()) {
                selected.add(s);
            }
        }

        // 必須只有一個物件被選取，而且必須是基本物件 (非 CompositeShape)
        if (selected.size() == 1 && !(selected.get(0) instanceof CompositeShape)) {
            Shape target = selected.get(0);

            // 彈跳視窗 Customize Label Style
            JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
            panel.add(new JLabel("Label Name:"));
            JTextField nameField = new JTextField(target.getLabelName());
            panel.add(nameField);

            panel.add(new JLabel("Label Color:"));
            JButton colorButton = new JButton("Choose Color...");
            // 預設顯示標籤原設定
            colorButton.setBackground(target.getLabelColor());

            Color[] selectedColor = { target.getLabelColor() };
            colorButton.addActionListener(ev -> {
                Color c = JColorChooser.showDialog(this, "Choose Label Color", selectedColor[0]);
                if (c != null) {
                    selectedColor[0] = c;
                    colorButton.setBackground(c);
                }
            });
            panel.add(colorButton);

            int result = JOptionPane.showConfirmDialog(this, panel, "Customize Label Style",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                target.setLabelName(nameField.getText());
                target.setLabelColor(selectedColor[0]);
                repaint();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 畫出所有的連線 (先畫線，再畫形狀，確保線在下層)
        for (Line l : lines) {
            l.draw(g);
        }

        // 畫出所有的形狀
        for (Shape s : shapes) {
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