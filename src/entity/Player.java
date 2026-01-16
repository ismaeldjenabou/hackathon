package src.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import src.main.GamePanel;
import src.main.KeyHandler;

import java.io.IOException;
import javax.imageio.ImageIO;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = gp.tileSize - (solidArea.x * 2); // 32
        solidArea.height = gp.tileSize - solidArea.y; // 32

        setDefaultValues();
        getPlayerImage();
    }

    // get the images of the player and put it in a variable
    // example up from boy_up_0.png
    // using ImageIO.read()
    public final void getPlayerImage() {
        try {
            up0 = ImageIO.read(getClass().getResourceAsStream("/res/player/p0/boy_up_0.png"));
            up1 = ImageIO.read(getClass().getResourceAsStream("/res/player/p0/boy_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/res/player/p0/boy_up_2.png"));

            down0 = ImageIO.read(getClass().getResourceAsStream("/res/player/p0/boy_down_0.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/res/player/p0/boy_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/res/player/p0/boy_down_2.png"));

            left1 = ImageIO.read(getClass().getResourceAsStream("/res/player/p0/boy_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/res/player/p0/boy_left_2.png"));

            right1 = ImageIO.read(getClass().getResourceAsStream("/res/player/p0/boy_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/res/player/p0/boy_right_2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // the default value of the speed, the location and the direction the player is
    // facing
    public final void setDefaultValues() {
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";
    }

    // if a key is pressed change the direction, check the collision if he can move,
    // if he can add or substract the speed from his coordonates and select what
    // image to use to maka an animation
    public void update() {
        boolean moving = false;

        // Determine desired direction from input
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            if (keyH.upPressed) {
                direction = "up";
            } else if (keyH.downPressed) {
                direction = "down";
            } else if (keyH.leftPressed) {
                direction = "left";
            } else if (keyH.rightPressed) {
                direction = "right";
            }

            // Check collision before moving
            collisionOn = false;
            gp.cChecker.checkTile(this);

            // If no collision, move in the chosen direction
            if (!collisionOn) {
                switch (direction) {
                    case "up" -> worldY -= speed;
                    case "down" -> worldY += speed;
                    case "left" -> worldX -= speed;
                    case "right" -> worldX += speed;
                }
            }

            moving = true;
        }

        // Animation handling
        if (moving) {
            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum++;
                if (spriteNum > 2)
                    spriteNum = 0;
                spriteCounter = 0;
            }
        } else {
            spriteNum = 1; // idle frame
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch (direction) {
            case "up" -> {
                if (spriteNum == 0)
                    image = up0;
                if (spriteNum == 1)
                    image = up1;
                if (spriteNum == 2)
                    image = up2;
            }
            case "down" -> {
                if (spriteNum == 0)
                    image = down0;
                if (spriteNum == 1)
                    image = down1;
                if (spriteNum == 2)
                    image = down2;
            }
            case "left" -> {
                if (spriteNum == 0)
                    image = left1;
                if (spriteNum == 1)
                    image = left1;
                if (spriteNum == 2)
                    image = left2;
            }
            case "right" -> {
                if (spriteNum == 0)
                    image = right1;
                if (spriteNum == 1)
                    image = right1;
                if (spriteNum == 2)
                    image = right2;
            }
        }
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
}
