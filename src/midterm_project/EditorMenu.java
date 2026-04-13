package midterm_project;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

// 包含 Edit 這個 Menu，裡面放有 Group 跟 Ungroup 兩個 JMenuItem
public class EditorMenu extends JMenuBar {
    private Canvas canvas;

    public EditorMenu(Canvas canvas) {
        this.canvas = canvas;

        JMenu editMenu = new JMenu("Edit");

        JMenuItem groupItem = new JMenuItem("Group");
        groupItem.addActionListener(e -> {
            canvas.groupSelectedShapes();
        });

        JMenuItem ungroupItem = new JMenuItem("Ungroup");
        ungroupItem.addActionListener(e -> {
            canvas.ungroupSelectedShapes();
        });

        JMenuItem labelItem = new JMenuItem("Label");
        labelItem.addActionListener(e -> {
            canvas.customizeSelectedLabel();
        });

        editMenu.add(groupItem);
        editMenu.add(ungroupItem);
        editMenu.add(labelItem);

        add(editMenu);
    }
}
