package midterm_project;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

// 包含 Edit 這個 Menu，裡面放有 Group 跟 Ungroup 兩個 JMenuItem
public class EditorMenu extends JMenuBar {
    private EditorController controller;

    public EditorMenu(EditorController controller) {
        this.controller = controller;

        JMenu editMenu = new JMenu("Edit");

        JMenuItem groupItem = new JMenuItem("Group");
        groupItem.addActionListener(e -> {
            controller.groupSelectedShapes();
        });

        JMenuItem ungroupItem = new JMenuItem("Ungroup");
        ungroupItem.addActionListener(e -> {
            controller.ungroupSelectedShapes();
        });

        JMenuItem labelItem = new JMenuItem("Label");
        labelItem.addActionListener(e -> {
            controller.customizeSelectedLabel();
        });

        editMenu.add(groupItem);
        editMenu.add(ungroupItem);
        editMenu.add(labelItem);

        add(editMenu);
    }
}
