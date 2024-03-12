package entities;

import graficos.SpriteSheet;
import main.Game;
import world.Camera;
import world.World;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends Entity {
    public boolean right, up, left, down;
    public double speed = 2;
    public double life = 100, maxLife = 100;
    public int ammo= 0;

    private int frames = 0;
    private int index = 0;
    private final int maxFrames = 5;
    private final int maxIndex = 3;
    private BufferedImage[] rightPlayer;
    private BufferedImage[] leftPlayer;
    private BufferedImage[] upPlayer;
    private BufferedImage[] downPlayer;

    private boolean moved = false;
    private boolean hasGun = false;
    public boolean shoot = false;

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
            downPlayer[i] = Game.spritesheet.getSprite(32, (i * 16), 16, 16);
        }
    }

    public void tick() {
        moved = false;
        if(right && World.isFree((int) Math.round(this.getX() + speed), this.getY())) {
        moved = true;
        x += speed;
        } else if(left && World.isFree((int) Math.round(this.getX() - speed), this.getY())) {
        moved = true;
        x -= speed;
        }

        if(up && World.isFree(this.getX(),(int) Math.round(this.getY() - speed))) {
            moved = true;
            y-=speed;
        } else if(down && World.isFree(this.getX(), (int) Math.round(this.getY() + speed))){
            moved = true;
            y+=speed;
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

        checkCollisionLifePack();
        checkCollisionAmmo();
        checkCollisionGun();

        if (shoot) {
            shoot = false;
            if (hasGun && ammo > 0) {
                ammo--;
                BulletShoot bullet = getBulletShoot();
                Game.bullets.add(bullet);
            }
        }

        if (life <= 0) {
            Game.entities.clear();
            Game.enemies.clear();
            Game.entities = new ArrayList<>();
            Game.enemies = new ArrayList<>();
            Game.spritesheet = new SpriteSheet("/spritesheet.png");
            Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(32, 0, 16, 16));
            Game.entities.add(Game.player);
            Game.world = new World("/map.png");
            return;
        }

        Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, World.WIDTH * 16 - Game.WIDTH);
        Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, World.HEIGHT * 16 - Game.HEIGHT);
    }

    private BulletShoot getBulletShoot() {
        int dx = 0;
        int dy = 0;
        int px = 0;
        int py = 6;
        if (right) {
            dx = 1;
            px = 6;
        } else if (left) {
            dx = -1;
            px = -6;
        } else if (up) {
            dy = -1;
            py = -6;
        } else if (down) {
            dy = 1;
        } else {
            dx = 1;
            px = 6;
        }
        return new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, dy);
    }

    public void checkCollisionLifePack() {
        for (int i  = 0; i < Game.entities.size(); i++) {
            Entity e = Game.entities.get(i);
            if (Entity.isColliding(this, e) && e instanceof LifePack) {
                life += 8.0;
                if (life >= 100) {
                    life = 100;
                }
                Game.entities.remove(i);
                return;
            }
        }
    }

    public void checkCollisionAmmo() {
        for (int i  = 0; i < Game.entities.size(); i++) {
            Entity e = Game.entities.get(i);
            if (Entity.isColliding(this, e) && e instanceof Bullet) {
                ammo += 20;
                Game.entities.remove(i);
                return;
            }
        }
    }

    public void checkCollisionGun() {
        for (int i  = 0; i < Game.entities.size(); i++) {
            Entity e = Game.entities.get(i);
            if (Entity.isColliding(this, e) && e instanceof Weapon) {
                hasGun = true;
                ammo = 20;
                System.out.println("Pegou a arma");
                Game.entities.remove(i);
                return;
            }
        }
    }

    public void render(Graphics g) {
        int gunWidth = 13;
        int gunHeight = 13;

        if(right) {
            g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
            if (hasGun) {
                g.drawImage(Entity.GUN_RIGHT, this.getX() - Camera.x + 10, this.getY() - Camera.y, gunWidth, gunHeight, null);
            }
        } else if(left) {
            g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
            if (hasGun) {
                g.drawImage(Entity.GUN_LEFT, this.getX() - Camera.x - 10, this.getY() - Camera.y, gunWidth, gunHeight, null);
            }
        } else if(up) {
            g.drawImage(upPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
            if (hasGun) {
                g.drawImage(Entity.GUN_UP, this.getX() - Camera.x, this.getY() - Camera.y - 10, gunWidth, gunHeight, null);
            }
        } else if(down) {
            g.drawImage(downPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
            if (hasGun) {
                g.drawImage(Entity.GUN_DOWN, this.getX() - Camera.x, this.getY() - Camera.y + 10, gunWidth, gunHeight, null);
            }
        } else {
            g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
            if (hasGun) {
                g.drawImage(Entity.GUN_RIGHT, this.getX() - Camera.x + 10, this.getY() - Camera.y, gunWidth, gunHeight, null);
            } else {
                g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
            }
        }
    }
}
