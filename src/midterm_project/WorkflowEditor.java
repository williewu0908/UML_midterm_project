package midterm_project;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import midterm_project.lines.AssociationLine;
import midterm_project.lines.CompositionLine;
import midterm_project.lines.GeneralizationLine;
import midterm_project.modes.CreateLinkMode;
import midterm_project.modes.CreateShapeMode;
import midterm_project.modes.SelectMode;
import midterm_project.shapes.Oval;
import midterm_project.shapes.Rect;
import midterm_project.shapes.Shape;

import java.util.function.Supplier;

// 主視窗
public class WorkflowEditor extends JFrame {
    private Canvas canvas = new Canvas();
    private List<ToolButton> buttons = new ArrayList<>();
    private ToolButton activeButton;

    public WorkflowEditor() {
        interface ButtonAction {
            void execute(ToolButton btn);
        }
        Map<String, ButtonAction> actions = new LinkedHashMap<>();

        actions.put("Select", btn -> {
            btn.addActionListener(e -> {
                canvas.setMode(new SelectMode(canvas));
                updateButtonStyles(btn);
            });
        });
        actions.put("Rect", btn -> setupCreateButton(btn, () -> new Rect(0, 0)));
        actions.put("Oval", btn -> setupCreateButton(btn, () -> new Oval(0, 0)));
        actions.put("Association", btn -> {
            btn.addActionListener(e -> {
                updateButtonStyles(btn);
                canvas.setMode(new CreateLinkMode(canvas, AssociationLine::new));
            });
        });
        actions.put("Generalization", btn -> {
            btn.addActionListener(e -> {
                updateButtonStyles(btn);
                canvas.setMode(new CreateLinkMode(canvas, GeneralizationLine::new));
            });
        });
        actions.put("Composition", btn -> {
            btn.addActionListener(e -> {
                updateButtonStyles(btn);
                canvas.setMode(new CreateLinkMode(canvas, CompositionLine::new));
            });
        });

        setTitle("Workflow Design Editor Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setJMenuBar(new EditorMenu(canvas));

        // 初始化、清單、佈局、行為綁定放置按鈕的面板
        JPanel toolbar = new JPanel(new GridLayout(actions.size(), 1, 5, 5));
        for (String label : actions.keySet()) {
            ToolButton btn = new ToolButton(label);
            buttons.add(btn);
            toolbar.add(btn);
            actions.get(label).execute(btn); // 直接執行對應的綁定邏輯
        }

        add(toolbar, BorderLayout.WEST);
        add(canvas, BorderLayout.CENTER);

        // 選中第一個按鈕 (Select)
        if (!buttons.isEmpty())
            updateButtonStyles(buttons.get(0));

        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    // 改變按鈕樣式
    private void updateButtonStyles(ToolButton btn) {
        this.activeButton = btn; // 更新當前發光的按鈕
        for (ToolButton b : buttons) {
            b.setCustomSelected(b == btn);
        }
    }

    // 建立圖形
    private void setupCreateButton(ToolButton btn, Supplier<Shape> shapeFactory) {
        btn.addMouseListener(new MouseAdapter() {
            private ToolButton lastActiveBtn;

            // 紀錄這次按下按鈕前的按鈕，
            @Override
            public void mousePressed(MouseEvent e) {
                lastActiveBtn = activeButton; // 備份原按鈕
                updateButtonStyles(btn); // 讓當前按鈕變黑

                // 直接從 canvas 拿目前的 mode 傳進去
                canvas.setMode(new CreateShapeMode(canvas, shapeFactory, canvas.getMode(), btn));
            }

            // 只有CreateShapeMode不一樣
            // 釋放滑鼠，建立物件（如果滑鼠在畫布內）
            @Override
            public void mouseReleased(MouseEvent e) {
                if (canvas.getMode() instanceof CreateShapeMode) {
                    ((CreateShapeMode) canvas.getMode()).processExternalRelease(e);

                    // 讓原本的按鈕亮回來
                    if (lastActiveBtn != null) {
                        updateButtonStyles(lastActiveBtn);
                    }
                }
            }
        });

        // 只有CreateShapeMode不一樣
        // 滑鼠在拖動時，按鈕不斷把座標傳給 CreateShapeMode 畫出預覽圖形
        btn.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // 拖曳動作轉發給 Mode 處理座標轉換與預覽
                if (canvas.getMode() instanceof CreateShapeMode) {
                    ((CreateShapeMode) canvas.getMode()).processExternalDrag(e);
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WorkflowEditor().setVisible(true));
    }
}