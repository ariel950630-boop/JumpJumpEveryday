package JumpJump;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Game extends JPanel implements KeyListener {

    private Player player;// 遊戲中的主角物件，負責控制角色的行為
    private List<Platform> platforms;// 儲存所有平台的列表，用來管理角色可以踩在上面的物件
    private boolean isGameOver = false;// 紀錄遊戲是否結束，true 表示遊戲已結束
    public int score = -540;// 初始分數設為 -540，因為每個平台 +60，總共 9 個初始平台
    private int maxHeightReached = 440;// 記錄角色跳到的最高位置，用來判斷畫面是否要向下捲動
    private Timer timer;
    private int oriX = 200; // 初始平台 X
    private Timer timeLimitTimer; // 時間限制計時器



    //進入遊戲的入口
    public Game() {
        setPreferredSize(new Dimension(600, 650));//設定遊戲視窗大小
        setBackground(Color.WHITE);//設定遊戲視窗顏色

        player = new Player(260, 520); // 初始站在地板上
        platforms = new ArrayList<>();//建立平台的ArrayList

        setFocusable(true);//讓這個元件可以獲得鍵盤焦點
        addKeyListener(this);//把目前這個物件註冊為鍵盤事件的監聽器。
    }

    @Override
    // 當鍵盤按鍵被按下時會呼叫這個方法
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT) { // 如果按的是鍵盤的左箭頭
            player.moveLeft();// 呼叫 player 的 moveLeft() 方法，讓角色往左移動
        } else if (code == KeyEvent.VK_RIGHT) {  // 如果按的是鍵盤的右箭頭
            player.moveRight();// 呼叫 player 的 moveRight() 方法，讓角色往右移動
        } else if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_UP) {// 如果按的是空白鍵 或 向上箭頭（跳躍）
            player.jump();// 呼叫 player 的 jump() 方法，讓角色跳起來
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    //在開始遊戲或重新開始遊戲時，初始化所有遊戲相關的狀態與物件
    public void start() {
        isGameOver = false;
        score = -540;
        maxHeightReached = 440;
        platforms.clear();

        player.setPosition(260, 520);//放置角色的初始位置
        player.setOnPlatform(true);//把角色設置為站在平台上
        player.setFalling(false);//把角色設置為沒有在掉落

        // 初始地板
        Platform floor = new Platform(0, 580, 600, 80, true);
        floor.setInitialPlatform(true);
        platforms.add(floor);

        // 其他初始平台：從 500 開始往上每 60 生成
        for (int i = 0; i < 9; i++) {
            int y = 500 - i * 60;
            platforms.add(generatePlatform(y));
        }

        timer = new Timer(16, e -> update());//每16毫秒呼叫一次update()
        timer.start();
        
       
        if (timeLimitTimer != null) {
            timeLimitTimer.stop(); // 停止舊的（如果存在）
        }

        // ★ 新增：時間限制計時器（20 秒）
        timeLimitTimer = new Timer(20000, e -> {
            if (!isGameOver) {
                JOptionPane.showMessageDialog(this, "Time is up! Game over!");
                gameOver();
            }
        });
        timeLimitTimer.setRepeats(false); // 只執行一次
        timeLimitTimer.start();


        requestFocusInWindow();//請求鍵盤焦點，讓 KeyListener 能正常接收鍵盤輸入
    }

    //負責更新狀況的方法
    public void update() {
        if (isGameOver) return;

        player.update();

        boolean onAnyPlatform = false;
        Platform initialFloor = null;

        //確認角色是否站在任何一個檯子上
        for (Platform p : platforms) {
            if (p.isInitial()) {
                initialFloor = p;
            }
            if (p.checkLanding(player)) {
                onAnyPlatform = true;
                player.setOnPlatform(true);
            }
        }

        //如果沒站在檯子上就改變他的狀態(變成正在落下)
        if (!onAnyPlatform) {
            player.setOnPlatform(false);
            if (!player.isFalling()) {
                player.setFalling(true);
            }
        }

        // 玩家離開地板後移除初始地板
        if (initialFloor != null && !player.getOnPlatform()) {
            platforms.remove(initialFloor);
        }

        // 背景滾動（當玩家跳得比之前高）
        int currentY = player.getY();
        if (currentY < maxHeightReached) {
            int dy = maxHeightReached - currentY;
            maxHeightReached = currentY;

            for (Platform p : platforms) {
                p.moveDown(dy);
            }
        }

        // 清除掉出畫面的平台
        platforms.removeIf(p -> !p.isInitial() && p.getY() > 600);

        // 保持平台數量至少 10 個
        while (platforms.size() < 10) {
            int highestY = 650;
            for (Platform p : platforms) {
                if (p.getY() < highestY) {
                    highestY = p.getY();
                }
            }
            int newY = highestY - 60;
            Platform newPlatform = generatePlatform(newY);
            platforms.add(newPlatform);
        }

        //分數超過10000或角色掉出視窗就呼叫gameover()結束遊戲
        if (score >= 10000 || player.getY() > 650) {
            gameOver();
        }

     // 讓玩家固定在畫面中 300 的高度位置
        int viewThreshold = 300;
        if (player.getY() < viewThreshold) {
            int dy = viewThreshold - player.getY();

            // 移動所有平台
            for (Platform p : platforms) {
                p.moveDown(dy);
            }

            // 將玩家往下移動()
            player.setPosition(player.getX(), player.getY() + dy);

        }

        repaint();
    }

    //處理結束遊戲後要做的事
    public void gameOver() {
        isGameOver = true;
        timer.stop();
        // 跳出小視窗顯示分數，並詢問玩家是否重新開始遊戲
        int option = JOptionPane.showConfirmDialog(this,
                "Game Over! Score: " + score + "\nDo you want to start over?",
                "Game over",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            start();
        } else {
            System.exit(0);
        }
    }

    //建立平台們
    public Platform generatePlatform(int y) {
        int offset = (int)(Math.random() * 301) - 170; // 偏移 [-170, +130]
        oriX += offset;

        // 限制 oriX 在合理範圍內（畫面寬 500，平台寬 150）
        if (oriX < 0) oriX += 170;
        if (oriX > 450) oriX -= 170;
        score = score+60;
        return new Platform(oriX, y, 150, 20, false);
    }


    @Override
    //畫出圖案
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Platform p : platforms) {
            p.draw(g);
        }

        player.draw(g);

        g.setColor(Color.BLACK);//設定左上角的分數是黑色
        g.setFont(new Font("SansSerif", Font.BOLD, 18)); // 設定字型、樣式、大小
        g.drawString("Score: " + score, 10, 20);
    }

    //Java 應用程式的進入點
    public static void main(String[] args) {
        JFrame frame = new JFrame("天天向上跳");
        Game gamePanel = new Game();
        frame.add(gamePanel);//加入遊戲視窗
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        gamePanel.start();
    }
}
