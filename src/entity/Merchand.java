package src.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Random;

import src.main.GamePanel;

public class Enemy extends Entity {
    GamePanel gp;
    Random random = new Random();
    int actionLockCounter = 0;
    int detectionRange;
    int x;
    int y;

    public Enemy(GamePanel gp, int x, int y) {
        this.gp = gp;
        this.x = x;
        this.y = y;
        detectionRange = gp.tileSize * 3;
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = gp.tileSize - (solidArea.x * 2);
        solidArea.height = gp.tileSize - solidArea.y;

        setDefaultValues();
        getEnemyImage();
    }

    public void getEnemyImage() {
        try {
            up0 = ImageIO.read(getClass().getResourceAsStream("/res/player/p2/p2_up_0.png"));
            up1 = ImageIO.read(getClass().getResourceAsStream("/res/player/p2/p2_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/res/player/p2/p2_up_2.png"));

            down0 = ImageIO.read(getClass().getResourceAsStream("/res/player/p2/p2_down_0.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/res/player/p2/p2_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/res/player/p2/p2_down_2.png"));

            left1 = ImageIO.read(getClass().getResourceAsStream("/res/player/p2/p2_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/res/player/p2/p2_left_2.png"));

            right1 = ImageIO.read(getClass().getResourceAsStream("/res/player/p2/p2_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/res/player/p2/p2_right_2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * this.x;
        worldY = gp.tileSize * this.y;
        speed = 2;
        direction = "down";
    }
    public void setAction() {
        int xDistance = Math.abs(worldX - gp.player.worldX);
        int yDistance = Math.abs(worldY - gp.player.worldY);

        if (xDistance <= detectionRange && yDistance <= detectionRange) {
            if (worldX < gp.player.worldX) direction = "right";
            else if (worldX > gp.player.worldX) direction = "left";

            if (worldY < gp.player.worldY) direction = "down";
            else if (worldY > gp.player.worldY) direction = "up";

        } else {
            actionLockCounter++;
            if (actionLockCounter > 60) {
                boolean validDirection = false;

                while (!validDirection) {
                    int i = random.nextInt(4); // 0 = up, 1 = down, 2 = left, 3 = right
                    switch (i) {
                        case 0 -> direction = "up";
                        case 1 -> direction = "down";
                        case 2 -> direction = "left";
                        case 3 -> direction = "right";
                    }

                    collisionOn = false;
                    int tempX = worldX;
                    int tempY = worldY;

                    switch (direction) {
                        case "up" -> tempY -= speed;
                        case "down" -> tempY += speed;
                        case "left" -> tempX -= speed;
                        case "right" -> tempX += speed;
                    }

                    int originalX = worldX;
                    int originalY = worldY;
                    worldX = tempX;
                    worldY = tempY;
                    gp.cChecker.checkTile(this);
                    validDirection = !collisionOn;

                    worldX = originalX;
                    worldY = originalY;
                }
                actionLockCounter = 0;
            }
        }
    }
    public void update() {
        setAction();

        boolean moving = false;

        collisionOn = false;
        gp.cChecker.checkTile(this);

        if (!collisionOn) {
            switch (direction) {
                case "up" -> worldY -= speed;
                case "down" -> worldY += speed;
                case "left" -> worldX -= speed;
                case "right" -> worldX += speed;
            }
            moving = true;
        }

        // Animation
        if (moving) {
            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum++;
                if (spriteNum > 2) spriteNum = 0;
                spriteCounter = 0;
            }
        } else {
            spriteNum = 1;
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch (direction) {
            case "up" -> image = switch (spriteNum) { case 0 -> up0; case 1 -> up1; default -> up2; };
            case "down" -> image = switch (spriteNum) { case 0 -> down0; case 1 -> down1; default -> down2; };
            case "left" -> image = switch (spriteNum) { case 0,1 -> left1; default -> left2; };
            case "right" -> image = switch (spriteNum) { case 0,1 -> right1; default -> right2; };
        }

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
}
