package entities;

import main.Game;
import world.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {

    public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(8*16, 0, 16, 16);
    public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(9*16, 0, 16, 16);
    public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(9*16, 16, 16, 16);
    public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(0, 16, 16, 16);
    public static BufferedImage GUN_RIGHT = Game.spritesheet.getSprite(128, 16, 16, 16);
    public static BufferedImage GUN_LEFT = Game.spritesheet.getSprite(128, 32, 16, 16);
    public static BufferedImage GUN_UP = Game.spritesheet.getSprite(128, 48, 16, 16);
    public static BufferedImage GUN_DOWN = Game.spritesheet.getSprite(128, 64, 16, 16);

    protected  int x;
    protected int y;
    protected int z;
    protected int width;
    protected int height;
    protected BufferedImage sprite;

    private int maskX, maskY, maskW, maskH;

    public Entity(int x, int y, int width, int height, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;

        this.maskX = 0;
        this.maskY = 0;
        this.maskW = width;
        this.maskH = height;
    }

    public void tick() {

    }

    public static boolean isColliding(Entity e1, Entity e2) {
        Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskX, e1.getY() + e1.maskY, e1.maskW, e1.maskH);
        Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskX, e2.getY() + e2.maskY, e2.maskW, e2.maskH);

        return e1Mask.intersects(e2Mask) && e1.z == e2.z;
    }

    public void render(Graphics g) {
        g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    public void setMask(int maskX, int maskY, int maskW, int maskH) {
        this.maskX = maskX;
        this.maskY = maskY;
        this.maskW = maskW;
        this.maskH = maskH;
    }
}
