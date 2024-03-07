package entities;

import main.Game;
import world.Camera;
import world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity{
    public boolean right, up, left, down;
    public int speed = 2;

    private int frames = 0;
    private int index = 0;
    private final int maxFrames = 5;
    private final int maxIndex = 3;
    private BufferedImage[] rightPlayer;
    private BufferedImage[] leftPlayer;
    private BufferedImage[] upPlayer;
    private BufferedImage[] downPlayer;

    private boolean moved = false;

    public Player(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);

        rightPlayer = new BufferedImage[4];
        leftPlayer = new BufferedImage[4];
        upPlayer = new BufferedImage[4];
        downPlayer = new BufferedImage[4];

        for(int i = 0; i < 4; i++) {
            rightPlayer[i] = Game.spritesheet.getSprite(64 + (i * 16), 0, 16, 16);
        }

        for(int i = 0; i < 4; i++) {
            leftPlayer[i] = Game.spritesheet.getSprite(64 + (i * 16), 16, 16, 16);
        }

        for (int i = 0; i < 4; i++) {
            upPlayer[i] = Game.spritesheet.getSprite(48, (i * 16), 16, 16);
        }

        for (int i = 0; i < 4; i++) {
            downPlayer[i] = Game.spritesheet.getSprite(32, + (i * 16), 16, 16);
        }
    }

    public void tick() {
        moved = false;
        if(right) {
            moved = true;
            x++;
        } else if(left) {
            moved = true;
            x--;
        }

        if(up) {
            moved = true;
            y--;
        } else if(down) {
            moved = true;
            y++;
        }

        if(moved) {
            frames++;
            if(frames == maxFrames) {
                frames = 0;
                index++;
                if(index > maxIndex) {
                    index = 0;
                }
            }
        }

        Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, World.WIDTH * 16 - Game.WIDTH);
        Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, World.HEIGHT * 16 - Game.HEIGHT);
    }

    public void render(Graphics g) {
        if(right) {
            g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
        } else if(left) {
            g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
        } else if(up) {
            g.drawImage(upPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
        } else if(down) {
            g.drawImage(downPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
        } else {
            g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
        }
    }
}
