package main;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class Menu {

    public String[] options = {"novo jogo", "carregar jogo", "sair"};
    public int currentOption = 0;
    public int maxOption = options.length - 1;
    public boolean up, down, enter;
    public boolean pause = false;

    public void tick() {
        if (up) {
            up = false;
            currentOption--;
            if (currentOption < 0) {
                currentOption = maxOption;
            }
        } else if (down) {
            down = false;
            currentOption++;
            if (currentOption > maxOption) {
                currentOption = 0;
            }
        }

        if (enter) {
            enter = false;
            if (Objects.equals(options[currentOption], "novo jogo") || Objects.equals(options[currentOption], "continuar")) {
                Game.gameState = "NORMAL";
                pause = false;
            } else if (Objects.equals(options[currentOption], "sair")) {
                System.exit(1);
            }
        }
    }

    public void saveGame(String[] val1, int[] val2, int encode) {
        BufferedWriter write = null;
        try{
            write = new BufferedWriter(new FileWriter("save.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < val1.length; i++) {
            String current = val1[i];
            current += ":";
           char[] value = Integer.toString(val2[i]).toCharArray();
           for (int j = 0; j < value.length; j++) {
               value[j] += encode;
               current += value[j];
           }
              try {
                write.write(current);
                if (i < val1.length - 1) {
                     write.newLine();
                }
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
        }
        try {
            write.flush();
            write.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(0, 0, 0, 100));
        g2.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
        g.setColor(Color.YELLOW);
        g.setFont(new Font("arial", Font.BOLD, 36));
        g.drawString("Meu primeiro jogo", (Game.WIDTH * Game.SCALE) / 2 - 150, (Game.HEIGHT * Game.SCALE) / 2 - 160);

        //Menu options
        g.setColor(Color.WHITE);
        g.setFont(new Font("arial", Font.BOLD, 24));

        if (currentOption == 0) {
            g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 70, (Game.HEIGHT * Game.SCALE) / 2 - 60);
        } else {
            g.drawString(" ", (Game.WIDTH * Game.SCALE) / 2 - 20, (Game.HEIGHT * Game.SCALE) / 2 - 60);
        }

        if (!pause) {
            g.drawString("Novo jogo", (Game.WIDTH * Game.SCALE) / 2 - 50, (Game.HEIGHT * Game.SCALE) / 2 - 60);
        } else {
            g.drawString("Continuar", (Game.WIDTH * Game.SCALE) / 2 - 50, (Game.HEIGHT * Game.SCALE) / 2 - 60);
        }

        if (currentOption == 1) {
            g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 95, (Game.HEIGHT * Game.SCALE) / 2 - 20);
        } else {
            g.drawString(" ", (Game.WIDTH * Game.SCALE) / 2 - 20, (Game.HEIGHT * Game.SCALE) / 2 - 20);
        }
        g.drawString("Carregar jogo", (Game.WIDTH * Game.SCALE) / 2 - 75, (Game.HEIGHT * Game.SCALE) / 2 - 20);

        if (currentOption == 2) {
            g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 40, (Game.HEIGHT * Game.SCALE) / 2 + 20);
        } else {
            g.drawString(" ", (Game.WIDTH * Game.SCALE) / 2 - 20, (Game.HEIGHT * Game.SCALE) / 2 + 20);
        }
        g.drawString("Sair", (Game.WIDTH * Game.SCALE) / 2 - 20, (Game.HEIGHT * Game.SCALE) / 2 + 20);
    }
}
