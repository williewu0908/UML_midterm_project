package midterm_project;

import midterm_project.shapes.CompositeShape;
import midterm_project.shapes.Shape;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// Controller
public class EditorController {
    private Component parentWindow;
    private UMLDocument document;

    public EditorController(Component parentWindow, UMLDocument document) {
        this.parentWindow = parentWindow;
        this.document = document;
    }

    public void groupSelectedShapes() {
        document.groupSelectedShapes();
    }

    public void ungroupSelectedShapes() {
        document.ungroupSelectedShapes();
    }

    // 自定義圖形標籤
    public void customizeSelectedLabel() {
        List<Shape> selected = new ArrayList<>();
        for (Shape s : document.getShapes()) {
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
                Color c = JColorChooser.showDialog(parentWindow, "Choose Label Color", selectedColor[0]);
                if (c != null) {
                    selectedColor[0] = c;
                    colorButton.setBackground(c);
                }
            });
            panel.add(colorButton);

            int result = JOptionPane.showConfirmDialog(parentWindow, panel, "Customize Label Style",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                target.setLabelName(nameField.getText());
                target.setLabelColor(selectedColor[0]);
                // 修改完物件屬性後，通知觀察者畫面需要更新
                document.notifyListeners();
            }
        }
    }
}
