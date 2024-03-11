package entities;

import main.Game;
import world.Camera;
import world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy extends Entity{

    private double speed = 1;
    private int maskX = 8, maskY = 8, maskW = 10, maskH = 10;
    private int frames = 0;
    private int index = 0;
    private final int maxFrames = 20;
    private final int maxIndex = 1;
    private int life = 10;

    private BufferedImage[] rightEnemy;
    private BufferedImage[] leftEnemy;
    private BufferedImage[] upEnemy;
    private BufferedImage[] downEnemy;

    private String currentDirection;


    public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);

        rightEnemy = new BufferedImage[2];
        leftEnemy = new BufferedImage[2];
        upEnemy = new BufferedImage[2];
        downEnemy = new BufferedImage[2];

        for(int i = 0; i < 2; i++) {
            rightEnemy[i] = Game.spritesheet.getSprite(16, 48 + (i * 16), 16, 16);
        }

        for(int i = 0; i < 2; i++) {
            leftEnemy[i] = Game.spritesheet.getSprite(0, 48 + (i * 16), 16, 16);
        }

        for (int i = 0; i < 2; i++) {
            upEnemy[i] = Game.spritesheet.getSprite(16, 16 + (i * 16), 16, 16);
        }

        for (int i = 0; i < 2; i++) {
            downEnemy[i] = Game.spritesheet.getSprite(0, 16 + (i * 16), 16, 16);
        }
    }

    public void tick() {
        if (!isCollidingWithPlayer()) {
            if (x < Game.player.getX() && World.isFree((int) Math.round(x+speed), this.getY()) && isColliding((int) Math.round(x + speed), this.getY())) {
                currentDirection = "right";
                x+=speed;
            } else if (x > Game.player.getX() && World.isFree((int) Math.round(x-speed), this.getY()) && isColliding((int) Math.round(x - speed), this.getY())) {
                currentDirection = "left";
                x-=speed;
            }

            if (y < Game.player.getY() && World.isFree(this.getX(), (int) Math.round(y+speed)) && isColliding(this.getX(), (int) Math.round(y + speed))){
                currentDirection = "down";
                y+=speed;
            } else if (y > Game.player.getY() && World.isFree(this.getX(), (int) Math.round(y-speed)) && isColliding(this.getX(), (int) Math.round(y - speed))){
                currentDirection = "up";
                y-=speed;
            }
        } else {
            if (Game.rand.nextInt(100) < 10) {
                Game.player.life -= 1;
                System.out.println("Vida: " + Game.player.life);
            }
        }

        frames++;
        if(frames == maxFrames) {
            frames = 0;
            index++;
            if(index > maxIndex) {
                index = 0;
            }
        }

        collidingBullet();

        if (life <= 0) {
            Game.entities.remove(this);
            Game.enemies.remove(this);
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

    public boolean isCollidingWithPlayer() {
        Rectangle enemyCurrent = new Rectangle(this.getX() + maskX, this.getY() + maskY, maskW, maskH);
        Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);
        return enemyCurrent.intersects(player);
    }

    public void collidingBullet() {
        for (int i = 0; i < Game.bullets.size(); i++) {
            Entity e = Game.bullets.get(i);
            if (Entity.isColliding(this, e)) {
                life--;
                Game.bullets.remove(i);
                return;
            }


        }
    }

    public void render(Graphics g) {
        BufferedImage[] currentSprites;

        switch (currentDirection) {
            case "right":
                currentSprites = rightEnemy;
                break;
            case "left":
                currentSprites = leftEnemy;
                break;
            case "up":
                currentSprites = upEnemy;
                break;
            case "down":
                currentSprites = downEnemy;
                break;
            default:
                currentSprites = upEnemy;
        }

        if (index >= 0 && index < currentSprites.length) {
            g.drawImage(currentSprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
        }

        //debug
//        g.setColor(Color.BLUE);
//        g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y, maskW, maskH);
    }
}
