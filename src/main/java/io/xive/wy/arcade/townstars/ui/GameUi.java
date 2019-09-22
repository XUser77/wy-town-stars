package io.xive.wy.arcade.townstars.ui;

import io.xive.wy.arcade.townstars.exceptions.GameException;
import io.xive.wy.arcade.townstars.game.Building;
import io.xive.wy.arcade.townstars.game.BuildingTune;
import io.xive.wy.arcade.townstars.game.Craft;
import io.xive.wy.arcade.townstars.game.Game;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

public class GameUi extends JFrame {

  public static void main(String[]args)
      throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    GameUi gameUi = new GameUi();
    gameUi.setVisible(true);
  }

  public GameUi() {
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    setLayout( new BorderLayout() );
    JPanel jPanel = new JPanel();
    jPanel.setLayout(new java.awt.GridLayout(1, 0));

    JButton jButton = new JButton("New game");
    jButton.addActionListener(e -> newGame());
    jPanel.add(jButton);

    jButton = new JButton("Skip 10 sec");
    jButton.addActionListener(e -> game.skip(10 * 1000));
    jPanel.add(jButton);

    jButton = new JButton("Skip 60 sec");
    jButton.addActionListener(e -> game.skip(60 * 1000));
    jPanel.add(jButton);

    jButton = new JButton("Skip 10 min");
    jButton.addActionListener(e -> game.skip(10 * 60 * 1000));
    jPanel.add(jButton);


    add(jPanel, BorderLayout.NORTH);

    int imageHeight = topLine + topLineMarginBottom + 16 * (fieldSize + borderWidth) + borderWidth + paddingY * 2;

    setSize(1100, imageHeight + 100);
    setTitle("WY - Town stars");

    bufferedImage = new BufferedImage(paddingX * 2 + 16 * (fieldSize + borderWidth) + borderWidth, imageHeight,
                                      BufferedImage.TYPE_INT_ARGB);

    game = new Game();

    repaintImage();

    new Thread(() -> {
      while (!game.isFinished()) {
        try {
          game.tick();
          repaintGame();
          Thread.sleep(10);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      repaintGame();
    }).start();

    addMouseMotionListener(new MouseMotionListener() {
      @Override
      public void mouseDragged(MouseEvent e) {

      }

      @Override
      public void mouseMoved(MouseEvent e) {
        int imX = e.getX() - (getWidth() / 2 - bufferedImage.getWidth() / 2);
        int imY = e.getY() - imageTopOffset;

        if (imX > paddingX && imX < bufferedImage.getWidth() - paddingX &&
            imY > topLine + topLineMarginBottom) {
          mouseFieldX = (imX - paddingX - borderWidth) / (fieldSize + borderWidth);
          mouseFieldY = (imY - topLine - topLineMarginBottom) / (fieldSize + borderWidth);
          setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else {
          mouseFieldX = -1;
          mouseFieldY = -1;
          setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

      }
    });

    addMouseListener(new MouseListener() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (mouseFieldX != -1 && mouseFieldY != -1) {
          int index = mouseFieldY * 16 + mouseFieldX + 1;
          Building building = game.getBuilding(index);
          if (building != null) {
            BuildingActions buildingActions = new BuildingActions(game.getBuildings(), building, GameUi.this, game.getGameDate(), game.getTradeCrafts());
            buildingActions.setVisible(true);
            if (BuildingActions.ACTION_CRAFT.equals(buildingActions.action)) {
              try {
                game.craft(index, buildingActions.craft);
              } catch (GameException ex) {
                JOptionPane.showMessageDialog(GameUi.this, "Error: " + ex.getMessage());
              }
            } else if (BuildingActions.ACTION_SELL.equals(buildingActions.action)) {
              try {
                game.sell(index);
              } catch (GameException ex) {
                JOptionPane.showMessageDialog(GameUi.this, "Error: " + ex.getMessage());
              }
            } else if (BuildingActions.ACTION_STORE.equals(buildingActions.action)) {
              try {
                game.store(index, buildingActions.storeIndex, buildingActions.craft, 1);
              } catch (GameException ex) {
                JOptionPane.showMessageDialog(GameUi.this, "Error: " + ex.getMessage());
              }
            } else if (BuildingActions.ACTION_CONSUME.equals(buildingActions.action)) {
              try {
                game.consume(index, buildingActions.craft, 1);
              } catch (GameException ex) {
                JOptionPane.showMessageDialog(GameUi.this, "Error: " + ex.getMessage());
              }
            } else if (BuildingActions.ACTION_TRADE.equals(buildingActions.action)) {
              try {
                game.trade(index, buildingActions.craft);
              } catch (GameException ex) {
                JOptionPane.showMessageDialog(GameUi.this, "Error: " + ex.getMessage());
              }
            }
          } else {
            BuildingSelector buildingSelector = new BuildingSelector(game.getAllBuildingTunes(), GameUi.this);
            buildingSelector.setVisible(true);
            BuildingTune buildingTune = buildingSelector.getSelectedBuildingTune();
            if (buildingTune != null) {
              try {
                game.build(buildingTune.getName());
              } catch (GameException ex) {
                JOptionPane.showMessageDialog(GameUi.this, "Error: " + ex.getMessage());
              }
            }
          }
        }
      }

      @Override
      public void mousePressed(MouseEvent e) {

      }

      @Override
      public void mouseReleased(MouseEvent e) {

      }

      @Override
      public void mouseEntered(MouseEvent e) {

      }

      @Override
      public void mouseExited(MouseEvent e) {

      }
    });

  }

  private int fieldSize = 40;
  private int borderWidth = 2;
  private int paddingY = 10;
  private int paddingX = 10;

  private int buildingFontSize = 18;
  private int buildingMiniFontSize = 12;
  private int topFontSize = 16;

  private int topLine = topFontSize * 4;
  private int topLineMarginBottom = buildingFontSize;

  private int imageTopOffset = 100;

  private int mouseFieldX = -1;
  private int mouseFieldY = -1;

  private Game game;
  private final BufferedImage bufferedImage;

  private void repaintGame() {
    synchronized (bufferedImage) {
      repaintImage();
      repaint();
    }
  }

  private void newGame() {
    game = new Game();
  }

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
    String waitingForTrade = "";
    if (game.getTradeCrafts().length > 0) {
      waitingForTrade = "In depot: " + groupCrafts(game.getTradeCrafts());
    }

    g.setFont(new Font("Tahoma", Font.BOLD, topFontSize));
    g.drawString(currency, paddingX, topOffset + topFontSize);
    g.drawString(points, paddingX, (int)(topOffset + topFontSize * 2.5));

    int pointsWidth = g.getFontMetrics().stringWidth(timeString);
    g.drawString(timeString, bufferedImage.getWidth() - paddingX - pointsWidth, topOffset + topFontSize);
    int waitingWidth = g.getFontMetrics().stringWidth(waitingForTrade);
    g.drawString(waitingForTrade, bufferedImage.getWidth() - paddingX - waitingWidth, (int)(topOffset + topFontSize * 2.5));

    topOffset += topFontSize * 4;

    // GRID
    g.setColor(Color.darkGray);
    g.fillRect(paddingX, topOffset,
               16 * (fieldSize + borderWidth) + borderWidth, 16 * (fieldSize + borderWidth) + borderWidth);


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
        g.setFont(new Font("Tahoma", Font.BOLD, buildingFontSize));
        int w = g.getFontMetrics().stringWidth(name);
        g.setColor(Color.BLACK);
        g.drawString(name, paddingX + borderWidth + fieldSize / 2 + x * (fieldSize + borderWidth) - w / 2,
                     fieldSize / 4 + buildingFontSize / 2 + topOffset + borderWidth + y * (fieldSize + borderWidth));

        g.setFont(new Font("Tahoma", Font.PLAIN, buildingMiniFontSize));
        String sub = "";
        if (buildings[i].getStorageCapacity() > 0) {
          sub = buildings[i].getStoredCrafts().length + "/" + buildings[i].getStorageCapacity();
        } else if (buildings[i].isTradeDepot()) {
          sub = "free";
          if (buildings[i].getTradeStartDate() != null) {
            sub = (Game.BUILDING_TRADE_PERIOD - (game.getGameDate() - buildings[i].getTradeStartDate())) / 1000 + " s.";
          }
        } else {
          if (buildings[i].getCraftStartDate() != null) {
            sub = (Game.BUILDING_CRAFT_PERIOD - (game.getGameDate() - buildings[i].getCraftStartDate())) / 1000 + " s.";
          } else if (buildings[i].getCraftOutside() != null) {
            sub = "ready";
          } else {
            sub = "free";
          }
        }

        w = g.getFontMetrics().stringWidth(sub);
        g.drawString(sub, paddingX + borderWidth + fieldSize / 2 + x * (fieldSize + borderWidth) - w / 2,
                     fieldSize * 3 / 4 + buildingMiniFontSize / 2 + topOffset + borderWidth + y * (fieldSize + borderWidth));


      }

      if (mouseFieldX == x && mouseFieldY == y) {
        g.setColor(new Color(0, 0, 0, 127));
        g.fillRect(paddingX + borderWidth + x * (fieldSize + borderWidth),
                   topOffset + borderWidth + y * (fieldSize + borderWidth),
                   fieldSize, fieldSize);
      }

    }

  }

  @Override
  public void paint(Graphics g) {

    g.drawImage(bufferedImage, getWidth() / 2 - bufferedImage.getWidth() / 2,imageTopOffset, null);
  }

  public static String groupCrafts(Craft[] crafts) {
    Map<String, Integer> map = new HashMap<>();
    for (Craft craft : crafts) {
      map.put(craft.getName(), map.getOrDefault(craft.getName(), 0) + 1);
    }

    List<String> results = new ArrayList<>();
    for(Entry<String, Integer> entry: map.entrySet()) {
      results.add(entry.getKey() + ": " + entry.getValue());
    }
    return String.join(", ", results);

  }

}
