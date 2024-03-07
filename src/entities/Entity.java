package entities;

import main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {

    public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(8*16, 0, 16, 16);
    public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(9*16, 0, 16, 16);
    public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(9*16, 16, 16, 16);
    public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(0, 16, 16, 16);

    protected  int x;
    protected int y;
    protected int width;
    protected int height;
    protected BufferedImage sprite;

    public Entity(int x, int y, int width, int height, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.drawImage(sprite, this.getX(), this.getY(), null);
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
}
