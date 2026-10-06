package JumpJump;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.IOException;
import java.util.Objects;

public class Platform {
    public int x, y, width, height;// 平台的位置 (x, y) 以及寬度和高度（平台的大小）
    private boolean active = true;// 平台是否處於「活躍」狀態，用來判斷平台是否可互動或顯示
    private boolean initialPlatform = false;  // 是否為地板（咖啡色）
    private Image image;// 平台的圖片，用來畫出平台的外觀


    //建立一個Platform物件
    public Platform(int x, int y, int width, int height ,boolean initialPlatform) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        
       	try {
                image = ImageIO.read(getClass().getResource("/images/log.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
      
    }
    //據平台是否為「初始平台」（地板），載入不同的圖片資源
    private void loadImage() {
        String path = initialPlatform ? "/images/newgrass.png" : "/images/log.png";
        try {
            image = ImageIO.read(getClass().getResource(path));
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            image = null;
        }
    }

    //設定這個平台是否為「初始平台」，並根據狀態變化更新圖片
    public void setInitialPlatform(boolean val) {
    	if (initialPlatform != val) {
            initialPlatform = val;
            loadImage();  // 重新載入對應圖片
        }
    }
    //取得平台是否為初始平台（地板）
    public boolean isInitial() {
        return initialPlatform;
    }
    //讓平台向下移動指定的距離
    public void moveDown(int dy) {
        y += dy;
    }
    //判斷玩家是否成功「著陸」在這個平台上
    public boolean checkLanding(Player player) {
        if (!active) return false;

        Rectangle playerRect = player.getBounds();
        int playerFeet = player.getY() + player.getHeight();

        boolean verticalCheck = (playerFeet >= y - 3) && (playerFeet <= y + 5);
        boolean horizontalCheck = (player.getX() + playerRect.width > x) && (player.getX() < x + width);

        return verticalCheck && horizontalCheck && playerRect.y <= y;
    }
    //把平台畫到畫面上
    public void draw(Graphics g) {
        if (!active) return;
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        } else {
            if (initialPlatform) {
                g.setColor(new Color(139, 69, 19)); // 咖啡色（或其他你想要的顏色）
            } else {
                g.setColor(Color.GRAY);
            }
            g.fillRect(x, y, width, height);
        }
    }

    //回傳平台的y座標（垂直位置）
    public int getY() {
        return y;
    }
    //回傳平台的高度
    public int getHeight() {
        return height;
    }
    //回傳平台是否處於活躍狀態
    public boolean isActive() {
        return active;
    }
    //將平台設定為不活躍
    public void deactivate() {
        active = false;
    }
}

