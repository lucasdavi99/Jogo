package entities;

import main.Game;
import world.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BulletShoot extends Entity{

    private int dx;
    private int dy;
    private double speed = 4;
    private int life = 50, curLife = 0;

public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, int dx, int dy) {
        super(x, y, width, height, sprite);
        this.dx = dx;
        this.dy = dy;
    }


    public void tick() {
        x += (int) (dx * speed);
        y += (int) (dy * speed);
        curLife++;
        if (curLife == life) {
            Game.bullets.remove(this);
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, 3, 3);
    }
}
