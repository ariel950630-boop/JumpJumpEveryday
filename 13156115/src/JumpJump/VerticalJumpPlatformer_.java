package JumpJump;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VerticalJumpPlatformer_ {
    public static void main(String[] args) {
        JFrame frame = new JFrame("JumpJumpEveryday");
        frame.setSize(600, 660);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.PINK);
        panel.setBounds(0, 0, 600, 660);

        // 加入背景圖
        ImageIcon backgroundIcon = new ImageIcon(VerticalJumpPlatformer_.class.getResource("/images/export.jpeg"));
        JLabel imageLabel = new JLabel(backgroundIcon);
        imageLabel.setBounds(0, 0, 600, 660);
        panel.add(imageLabel);

        // 加入圖片按鈕
        ImageIcon startIcon = new ImageIcon(VerticalJumpPlatformer_.class.getResource("/images/unnamed.png")); // 改成你自己的圖片
        JButton startButton = new JButton(startIcon);
        startButton.setBounds(180, 250, startIcon.getIconWidth(), startIcon.getIconHeight());
        startButton.setBorderPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.setFocusPainted(false);
        startButton.setOpaque(false);

        // 放到背景圖片之上
        imageLabel.setLayout(null);
        imageLabel.add(startButton);

        // 加入遊戲面板
        Game gamePanel = new Game();
        gamePanel.setBounds(0, 0, 600, 660);
        gamePanel.setVisible(false);
        panel.add(gamePanel);

        // 按下按鈕後開始遊戲
        startButton.addActionListener(e -> {
            startButton.setVisible(false);
            imageLabel.setVisible(false); // 隱藏背景圖
            gamePanel.setVisible(true);
            gamePanel.requestFocusInWindow();
            gamePanel.start();
        });

        frame.add(panel);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);  
    } 
}
