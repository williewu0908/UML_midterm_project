package midterm_project;

import midterm_project.lines.Line;
import midterm_project.shapes.CompositeShape;
import midterm_project.shapes.Shape;

import java.util.ArrayList;
import java.util.List;

// Model
public class UMLDocument {
    private List<Shape> shapes = new ArrayList<>(); // 儲存畫布上所有的圖形與群組
    private List<Line> lines = new ArrayList<>(); // 儲存畫布上所有的連線

    // Observer Pattern: 觀察者清單
    private List<Runnable> listeners = new ArrayList<>();

    // 註冊觀察者
    public void addListener(Runnable listener) {
        listeners.add(listener);
    }

    // 通知所有觀察者資料已改變
    public void notifyListeners() {
        for (Runnable listener : listeners) {
            listener.run();
        }
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void addShape(Shape s) {
        shapes.add(s);
        notifyListeners();
    }

    public void addLine(Line l) {
        lines.add(l);
        notifyListeners();
    }

    // 把物件推到最上層
    public void moveShapeToFront(Shape s) {
        if (shapes.remove(s)) {
            shapes.add(s); // 加到 List 最後面，繪製時會在最上面
            notifyListeners();
        }
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
            notifyListeners();
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
        // 為了維持 CompositeShape 以外 Shape 的乾淨，所以沒有 Percoloating Up
        if (selected.size() == 1 && selected.get(0) instanceof CompositeShape) {
            CompositeShape group = (CompositeShape) selected.get(0);
            shapes.remove(group);
            for (Shape child : group.getChildren()) {
                child.setSelected(true); // 群組解散後預設選取內部元素
                shapes.add(child);
            }
            notifyListeners();
        }
    }
}
