package entities;

import main.Game;
import world.Camera;
import world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy extends Entity{

    private int speed = 1;
    private int maskX = 8, maskY = 8, maskW = 10, maskH = 10;

    public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
    }

    public void tick() {
        if (x < Game.player.getX() && World.isFree((x+speed), this.getY()) && isColliding(x + speed, this.getY())) {
            x+=speed;
        } else if (x > Game.player.getX() && World.isFree((x-speed), this.getY()) && isColliding(x - speed, this.getY())) {
            x-=speed;
        }

        if (y < Game.player.getY() && World.isFree(this.getX(), (y+speed)) && isColliding(this.getX(), y + speed)){
            y+=speed;
        } else if (y > Game.player.getY() && World.isFree(this.getX(), (y-speed)) && isColliding(this.getX(), y - speed)){
            y-=speed;
        }
    }

    public boolean isColliding(int xNext, int yNext) {
        Rectangle enemyCurrent = new Rectangle(xNext + maskX, yNext + maskY, maskW, maskH);
        for (int i = 0; i < Game.enemies.size(); i++) {
            Enemy e = Game.enemies.get(i);
            if (e == this) {
                continue;
            }
            Rectangle targetEnemy = new Rectangle(e.getX() + maskX, e.getY() + maskY, maskW, maskH);
            if (enemyCurrent.intersects(targetEnemy)) {
                return false;
            }
        }
        return true;
    }

    public void render(Graphics g) {



        //debug
//        g.setColor(Color.BLUE);
//        g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y, maskW, maskH);
    }
}
