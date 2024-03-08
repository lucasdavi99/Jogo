package entities;

import main.Game;

import java.awt.image.BufferedImage;

public class Enemy extends Entity{

    private int speed = 1;

    public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
    }

    public void tick() {
        if (x < Game.player.getX() && Game.world.isFree((x+speed), this.getY())) {
            x+=speed;
        } else if (x > Game.player.getX() && Game.world.isFree((x-speed), this.getY())) {
            x-=speed;
        }

        if (y < Game.player.getY() && Game.world.isFree(this.getX(), (y+speed))) {
            y+=speed;
        } else if (y > Game.player.getY() && Game.world.isFree(this.getX(), (y-speed))) {
            y-=speed;
        }
    }
}
