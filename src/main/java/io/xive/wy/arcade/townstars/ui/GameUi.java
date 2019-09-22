package io.xive.wy.arcade.townstars.ui;

import io.xive.wy.arcade.townstars.game.Building;
import io.xive.wy.arcade.townstars.game.Game;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class GameUi extends JFrame {

  public static void main(String[]args) {
    GameUi gameUi = new GameUi();
    gameUi.setVisible(true);
  }

  public GameUi() {
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    setSize(1100, 800);
    setTitle("WY - Town stars");

    bufferedImage = new BufferedImage(paddingX * 2 + 16 * (fieldSize + borderWidth) + borderWidth,
                                      topLine + topLineMarginBottom + 16 * (fieldSize + borderWidth) + borderWidth + paddingY * 2,
                                      BufferedImage.TYPE_INT_ARGB);

    game = new Game();

    repaintImage();

    new Thread(() -> {
      while (!game.isFinished()) {
        try {
          game.tick();
          repaintImage();
          repaint();
          Thread.sleep(100);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }).start();

  }

  private int fieldSize = 40;
  private int borderWidth = 2;
  private int paddingY = 10;
  private int paddingX = 10;

  int buildingFontSize = 18;
  int topFontSize = 16;

  private int topLine = topFontSize * 4;
  private int topLineMarginBottom = buildingFontSize;

  private Game game;
  private BufferedImage bufferedImage = new BufferedImage(1028, 768, BufferedImage.TYPE_INT_ARGB);

  private String getTimeString(long timestamp) {
    long hours = timestamp / (60 * 60 * 1000);
    long minutes = (timestamp / (60 * 1000)) % 60;
    long seconds = (timestamp / 1000) % 60;
    return (hours < 10 ? "0" + hours : hours) + ":" +
        (minutes < 10 ? "0" + minutes : minutes) + ":" +
        (seconds < 10 ? "0" + seconds : seconds);
  }

  private void repaintImage() {

    int topOffset = paddingY;

    Graphics2D g = (Graphics2D)bufferedImage.getGraphics();
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                   RenderingHints.VALUE_ANTIALIAS_ON);
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());

    // TOP INFO
    g.setColor(Color.DARK_GRAY);
    g.fillRect(0, 0, bufferedImage.getWidth(), topLine);

    g.setColor(Color.WHITE);
    String currency = "$ " + game.getCurrency();
    String points = game.getPoints() + " points";
    String timeString = getTimeString(game.getGameDate());

    g.setFont(new Font("Tahoma", Font.BOLD, topFontSize));
    g.drawString(currency, paddingX, topOffset + topFontSize);
    g.drawString(points, paddingX, (int)(topOffset + topFontSize * 2.5));

    int pointsWidth = g.getFontMetrics().stringWidth(timeString);
    g.drawString(timeString, bufferedImage.getWidth() - paddingX - pointsWidth, topOffset + topFontSize);

    topOffset += topFontSize * 4;

    // GRID
    g.setColor(Color.darkGray);
    g.fillRect(paddingX, topOffset,
               16 * (fieldSize + borderWidth) + borderWidth, 16 * (fieldSize + borderWidth) + borderWidth);

    g.setFont(new Font("Tahoma", Font.BOLD, buildingFontSize));

    Building[] buildings = game.getBuildings();
    for(int i=1; i<buildings.length; i++) {
      int index = i - 1;
      int x = index % 16;
      int y = index / 16;
      if (buildings[i] == null) g.setColor(Color.LIGHT_GRAY);
      if (buildings[i] != null) {
        if (buildings[i].getStorageCapacity() > 0) {
          g.setColor(Color.YELLOW);
        } else if (buildings[i].getProducesCrafts().length > 0) {
          g.setColor(Color.GREEN);
        } else if (buildings[i].isTradeDepot()) {
          g.setColor(Color.MAGENTA);
        } else {
          g.setColor(Color.BLUE);
        }
      }
      g.fillRect(paddingX + borderWidth + x * (fieldSize + borderWidth),
                 topOffset + borderWidth + y * (fieldSize + borderWidth),
                 fieldSize, fieldSize);

      if (buildings[i] != null) {
        String name = buildings[i].getShortName();
        int w = g.getFontMetrics().stringWidth(name);
        g.setColor(Color.BLACK);
        g.drawString(name, paddingX + borderWidth + fieldSize / 2 + x * (fieldSize + borderWidth) - w / 2,
                     fieldSize / 2 + buildingFontSize / 2 - 2 + topOffset + borderWidth + y * (fieldSize + borderWidth));
      }

    }

  }

  @Override
  public void paint(Graphics g) {

    g.drawImage(bufferedImage, getWidth() / 2 - bufferedImage.getWidth() / 2,50, null);
  }

}
