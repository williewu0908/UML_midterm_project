package midterm_project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ToolButton extends JButton {
    private Color hoverColor = new Color(230, 230, 230); // 懸停時的淺灰色
    private Color selectedColor = Color.GRAY; // 選中時的黑色
    private boolean isCustomSelected = false; // 追蹤是否為選中模式

    public ToolButton(String text) {
        super(text);
        setContentAreaFilled(false); // 禁用預設繪製，改由我們手動繪製背景
        setFocusPainted(false); // 移除點擊時的虛線框
        setBorderPainted(false); // 移除預設邊框
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR)); // 滑鼠移上去變手指形狀

        // 監聽滑鼠進入/離開，觸發重繪以顯示 Hover 效果
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                repaint();
            }
        });
    }

    // 提供一個方法來切換選中狀態
    public void setCustomSelected(boolean selected) {
        this.isCustomSelected = selected;
        // 根據狀態切換文字顏色
        setForeground(selected ? Color.WHITE : Color.BLACK);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        // 開啟抗鋸齒（繪製平滑邊緣）
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. 決定背景顏色
        if (isCustomSelected) {
            g2.setColor(selectedColor);
        } else if (getModel().isRollover()) {
            g2.setColor(hoverColor);
        } else {
            g2.setColor(getBackground()); // 預設可能是透明或父元件顏色
        }

        // 2. 繪製背景（可以改成 fillRoundRect 做出圓角效果）
        g2.fillRect(0, 0, getWidth(), getHeight());

        // 3. 呼叫原本的繪製邏輯（會自動畫出按鈕文字）
        g2.dispose();
        super.paintComponent(g);
    }
}