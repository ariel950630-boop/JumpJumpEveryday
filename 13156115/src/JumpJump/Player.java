package JumpJump;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.IOException;
import java.util.Objects;


public class Player {
    private int x, y;//角色的位置座標，x 是水平位置，y 是垂直位置
    private int width = 60, height = 60;//角色的寬度與高度，都是60像素
    private int velocityY = 0;//垂直方向的速度，用來控制跳躍與下落的動作（往上是負值，往下是正值）
    private int speed = 5; //移動的速度（每次移動的距離）
    private boolean falling = false;//角色是否正在下落，true代表正在落下，false代表沒有（可能是站在平台上）
    private int jumpStrength = 15;//跳躍力量（每次跳躍時向上移動的初速度，數值越大跳越高）
    private boolean onPlatform = true;//角色是否站在平台上，true代表站著，false代表在空中（可能是跳起來或掉下去）
    private Image image;//儲存角色圖片，用來在畫面上顯示角色的外觀

    //初始化一個新的 Player 物件，設定它的初始位置、狀態，以及載入角色的圖片。
    public Player(int x, int y) {
    	
        this.x = x;
        this.y = y;
        this.velocityY = 0;
        this.falling = false;
        this.onPlatform = true;
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/unnamed-removebg-preview (2).png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //每次呼叫時更新角色的垂直位置，模擬重力效果（角色下落）
    public void update() {
        if (falling) {
            velocityY += 1; // 重力加速度
            y += velocityY;
        }
    }
    //讓角色向左移動，但不能超出畫面左邊界。
    public void moveLeft() {
        x -= speed;
        if (x < 0) x = 0;
    }
    //讓角色向右移動，但不能超出畫面右邊界
    public void moveRight() {
        x += speed;
        if (x > 600 - width) x = 600 - width;
    }
    //讓角色從平台上跳起來
    public void jump() {
        if (!falling && onPlatform) {
            velocityY = -jumpStrength;
            falling = true;
            onPlatform = false;
        }
    }
    //讓角色停止掉落
    public void stopFalling() {
        falling = false;
        velocityY = 0;
    }
    //設定角色是否處於「掉落狀態」
    public void setFalling(boolean falling) {
        this.falling = falling;
    }
    //取得角色是否正在掉落
    public boolean isFalling() {
        return falling;
    }
    //設定角色是否站在檯子上
    public void setOnPlatform(boolean onPlatform) {
        this.onPlatform = onPlatform;
        if (onPlatform) {
            falling = false;
            velocityY = 0;
        }
    }

    //取得角色是否「站在平台上」的狀態
    public boolean getOnPlatform() {
        return onPlatform;
    }

    //設定角色的座標位置
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //回傳角色的邊界區域（範圍），用來做碰撞偵測
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() { return x; }// 回傳角色目前的 x 座標
    public int getY() { return y; }// 回傳角色目前的 y 座標
    public int getHeight() { return height; }// 回傳角色的高度（用於碰撞判斷）

    //在畫面上畫出角色
    public void draw(Graphics g) {
    	if (image != null) {
            g.drawImage(image, x, y, width, height, null);
           
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(x, y, width, height);
        }
    }
}


