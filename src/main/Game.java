package main;

import entities.BulletShoot;
import entities.Enemy;
import entities.Entity;
import entities.Player;
import graficos.SpriteSheet;
import graficos.UI;
import world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Game extends Canvas implements Runnable, KeyListener  {

    public static JFrame frame;
    private Thread thread;
    private boolean isRunning = true;
    public static int WIDTH = 320, HEIGHT = 240;
    private final int SCALE = 3;

    private final BufferedImage image;
    private int CUR_LEVEL = 1, MAX_LEVEL = 2;
    private boolean restartGame = false;

    public static List<Entity> entities;
    public static List<Enemy> enemies;
    public static List<BulletShoot> bullets;
    public static SpriteSheet spritesheet;

    public static World world;
    public static Player player;

    public static Random rand;
    public static String gameState = "NORMAL";

    public UI ui;

    public Game() {
        rand = new Random();
        addKeyListener(this);
        setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
        initFrame();
        ui = new UI();
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        entities = new ArrayList<>();
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        spritesheet = new SpriteSheet("/spritesheet.png");

        player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16));
        entities.add(player);

        world = new World("/level1.png");
    }

    public void initFrame() {
        frame = new JFrame("Game 1");
        frame.add(this);
        frame.setResizable(false);//Usuário não irá ajustar janela
        frame.pack();
        frame.setLocationRelativeTo(null);//Janela inicializa no centro
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Fechar o programa por completo
        frame.setVisible(true);//Dizer que estará visível
    }

    //Threads
    public synchronized void start() {
        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }

    public synchronized void stop() {
        isRunning = false;
        try {
            thread.join();
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Game game = new Game();
        game.start();
    }

    public void tick() {

        if (gameState == "NORMAL") {
            restartGame = false;
            for (int i = 0; i < entities.size(); i++) {
                Entity e = entities.get(i);
                e.tick();
            }

            for (int i = 0; i < bullets.size(); i++) {
                bullets.get(i).tick();
            }

            if (enemies.isEmpty()) {
                CUR_LEVEL++;
                if (CUR_LEVEL > MAX_LEVEL) {
                    CUR_LEVEL = 1;
                }
                String newWorld = "level" + CUR_LEVEL + ".png";
                World.restartGame(newWorld);
            }
        } else if (gameState == "GAME_OVER") {
            //Game Over
            if (restartGame) {
                restartGame = false;
                gameState = "NORMAL";
                CUR_LEVEL = 1;
                String newWorld = "level" + CUR_LEVEL + ".png";
                World.restartGame(newWorld);
            }
        }
    }

    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = image.getGraphics();//Renderizar imagens na tela
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        world.render(g);
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            e.render(g);
        }

        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).render(g);
        }

        ui.render(g);

        g.dispose();//Limpar dados de imagem não usados
        g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE,null);
        g.setFont(new Font("arial", Font.BOLD, 20));
        g.setColor(Color.WHITE);
        g.drawString("Ammo: " + player.ammo, 580, 30);
        gameOverText(g);
        bs.show();
    }

    private void gameOverText(Graphics g) {
        if (Objects.equals(gameState, "GAME_OVER")) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(new Color(0, 0, 0, 100));
            g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
            g.setFont(new Font("arial", Font.BOLD, 40));
            g.setColor(Color.WHITE);
            g.drawString("Game Over", (WIDTH*SCALE)/2 - 100, (HEIGHT*SCALE)/2);
            g.drawString("Press Enter to restart", (WIDTH*SCALE)/2 - 200, (HEIGHT*SCALE)/2 + 50);
        }
    }

    @Override
    public void run() {

        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        int frames = 0;
        double timer = System.currentTimeMillis();
        requestFocus();
        while (isRunning == true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            if(delta >= 1) {
                tick(); render();
                frames++;
                delta--;
            }

            if(System.currentTimeMillis() - timer >= 1000) {
                System.out.println("FPS: "+frames);
                frames = 0;
                timer += 1000;
            }
        }

        stop();

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_D) {
            //Direita
            Player player = (Player) entities.get(0);
            player.right = true;
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            //Esquerda
            Player player = (Player) entities.get(0);
            player.left = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_W) {
            //Cima
            Player player = (Player) entities.get(0);
            player.up = true;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            //Baixo
            Player player = (Player) entities.get(0);
            player.down = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            //Atirar
            Player player = (Player) entities.get(0);
            player.shoot = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            restartGame = true;
        }

//        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
//            //Correr
//            Player player = (Player) entities.get(0);
//            player.sprint = true;
//        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_D) {
            //Direita
            Player player = (Player) entities.get(0);
            player.right = false;
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            //Esquerda
            Player player = (Player) entities.get(0);
            player.left = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_W) {
            //Cima
            Player player = (Player) entities.get(0);
            player.up = false;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            //Baixo
            Player player = (Player) entities.get(0);
            player.down = false;
        }
    }
}