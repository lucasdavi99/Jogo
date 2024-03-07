package world;

import entities.*;
import main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class World {

    private Tile[] tiles;
    public static int WIDTH, HEIGHT;

    public World(String path) {
        try {
            BufferedImage map = ImageIO.read(getClass().getResource(path));
            int[] pixels = new int[map.getWidth() * map.getHeight()];
            WIDTH = map.getWidth();
            HEIGHT = map.getHeight();
            tiles = new Tile[map.getWidth() * map.getHeight()];
            map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());

            for(int xx = 0; xx< map.getWidth(); xx++) {
                for(int yy = 0; yy < map.getHeight(); yy++) {
                    int pixelAtual = pixels[xx + (yy * map.getWidth())];
                    tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);

                    if(pixelAtual == 0xFF000000) {
                        //Grass
                        tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
                    } else if(pixelAtual == 0xFFFFFFFF) {
                        //Wall
                        tiles[xx + (yy * WIDTH)] = new WallTile(xx*16, yy*16, Tile.TILE_WALL);
                    } else if (pixelAtual == 0xFF0026FF) {
                        //Player
                        Game.player.setX(xx*16);
                        Game.player.setY(yy*16);
                    } else if (pixelAtual == 0xFFFF0000) {
                        //Enemy
                        Game.entities.add(new Enemy(xx*16, yy*16, 16, 16, Entity.ENEMY_EN));
                    } else if(pixelAtual == 0xFFFF6A00) {
                        //Weapon
                        Game.entities.add(new Weapon(xx*16, yy*16, 16, 16, Entity.WEAPON_EN));
                    } else if(pixelAtual == 0xFF00FF21) {
                        //LifePack
                        Game.entities.add(new LifePack(xx*16, yy*16, 16, 16, Entity.LIFEPACK_EN));
                    } else if(pixelAtual == 0xFFFFD800) {
                        //Bullet
                        Game.entities.add(new Bullet(xx*16, yy*16, 16, 16, Entity.BULLET_EN));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void render(Graphics g) {
        for (int xx = 0; xx < WIDTH; xx++) {
            for (int yy = 0; yy < HEIGHT; yy++) {
                Tile tile = tiles[xx + (yy * WIDTH)];
                tile.render(g);
            }
        }
    }
}
