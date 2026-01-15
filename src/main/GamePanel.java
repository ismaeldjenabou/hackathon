package src.main;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import src.entity.Player;
import src.tile.TileManager;

public class GamePanel extends JPanel implements Runnable {
    // SCREEN SETTINGS
    final int originalTitleSize = 16; // 16 * 16, tile or size of the sprite
    final int scale = 3; // scale for the tile to make it bigger or smaller
    BufferedImage homeBackground;
    public final int tileSize = originalTitleSize * scale; // 48*48, size of the tile on the screen
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    // screen is 16 * 12 so a ration of 4 * 3
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    // WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int maxWorldWidth = tileSize * maxWorldCol;
    public final int maxWorldHeight = tileSize * maxWorldRow;

    // FPS
    int FPS = 60;

    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread gamThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Player player = new Player(this, keyH);
    public boolean started = false;

    public GamePanel() {

      try {
        homeBackground = ImageIO.read(getClass().getResource("/res/home.jpg"));
      } catch (IOException e) {
        e.printStackTrace();
      }
        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // preffered size for the window
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true); // the gamepanel can be focused on to read key input
    }

    public void startGameThread() {

        gamThread = new Thread(this);
        gamThread.start();
    }

    //draw and update at the fps rate with System.nanoTime() to get the current time
    @Override
    public void run() {
        // execution loop
        double drawInterval = 1000000000.0 / FPS; // nanoseconds per frame
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gamThread != null) {
            // 1 UPDATE: update game state
            if (!started && keyH.stp){
              started = true;
            }else if(started && keyH.stp){
              started = false;
            }
            if (started){
              update();
            }

            // 2 DRAW: repaint the screen
            repaint();

            try {
                double remaining = nextDrawTime - System.nanoTime();
                remaining = remaining / 1000000; // to milliseconds

                if (remaining < 0) {
                    remaining = 0;
                }

                Thread.sleep((long) remaining);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        player.update();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        if (!started){
          g2.drawImage(homeBackground,0,0,getWidth(),getHeight(),null);
        }else{
          tileM.draw(g2);
          player.draw(g2);
        }

        g2.dispose();
    }
}
