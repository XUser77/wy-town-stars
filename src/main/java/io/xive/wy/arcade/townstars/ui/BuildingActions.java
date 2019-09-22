package io.xive.wy.arcade.townstars.ui;

import io.xive.wy.arcade.townstars.game.Building;
import io.xive.wy.arcade.townstars.game.BuildingTune;
import io.xive.wy.arcade.townstars.game.Craft;
import io.xive.wy.arcade.townstars.game.Game;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class BuildingActions extends JDialog {

  public static final String ACTION_CRAFT = "craft";
  public static final String ACTION_SELL = "sell";
  public static final String ACTION_STORE = "store";
  public static final String ACTION_CONSUME = "consume";

  public String craft = null;
  public int storeIndex = -1;
  public String action = null;
  private Building[] buildings;

  public BuildingActions(Building[] buildings, Building building, JFrame parent, long gameDate) {
    this.buildings = buildings;
    setTitle("Building: " + building.getName());
    setLayout(new java.awt.GridLayout(0, 1));
    setModal(true);
    setLocationRelativeTo(parent);

    if (building.getStorageCapacity() > 0) {
      add(new JLabel("Storage for " + String.join(",", building.getStoresCraftTypes())));
      add(new JLabel("Stored: " + GameUi.groupCrafts(building.getStoredCrafts())));
      add(new JLabel("Usage: " + building.getStoredCrafts().length + " / " + building.getStorageCapacity()));
      String[] uniqueCrafts = getUniqueCrafts(building.getStoredCrafts());
      for(int i=0; i<uniqueCrafts.length; i++) {
        add(storeButton(uniqueCrafts[i]));
        add(consumeButton(uniqueCrafts[i]));
      }
    } else if (building.isTradeDepot()) {
      add(new JLabel("Trade depot"));
    } else {
      add(new JLabel("Can produce: " + String.join(", ", building.getProducesCrafts())));
      if (building.getCrafting() != null) {
        add(new JLabel("Producing " + building.getCrafting().getName()));
        add(new JLabel((Game.BUILDING_CRAFT_PERIOD - (gameDate - building.getCraftStartDate())) / 1000 + " seconds left"));
      } else if (building.getCraftOutside() != null) {
        add(new JLabel("Produced " + building.getCraftOutside().getName()));
        add(storeButton(building.getCraftOutside().getName()));
        add(consumeButton(building.getCraftOutside().getName()));
      } else {
        add(new JLabel("Free"));
        if (building.getCraftsInside().length > 0) {
          add(new JLabel("Contains: " + GameUi.groupCrafts(building.getCraftsInside())));
        } else {
          add(new JLabel("Empty"));
        }
        for(int i=0; i<building.getProducesCrafts().length; i++) {
          add(craftButton(building.getProducesCrafts()[i]));
        }
      }

    }
    JButton jButton = new JButton("Sell for $ " + building.getSellValue());
    jButton.addActionListener(e -> {
      action = ACTION_SELL;
      setVisible(false);
    });
    add(jButton);

    pack();

  }

  private JButton storeButton(String craftName) {
    JButton jButton = new JButton("Store " + craftName);
    jButton.addActionListener(l -> {
      BuildingsList buildingsList = new BuildingsList(buildings, this);
      buildingsList.setVisible(true);
      if (buildingsList.index != -1) {
        action = ACTION_STORE;
        craft = craftName;
        storeIndex = buildingsList.index;
        setVisible(false);
      }
    });
    return jButton;
  }

  private JButton consumeButton(String craftName) {
    JButton jButton = new JButton("Consume " + craftName);
    jButton.addActionListener(l -> {
      action = ACTION_CONSUME;
      craft = craftName;
      setVisible(false);
    });
    return jButton;
  }

  private JButton craftButton(String craftName) {
    JButton jButton = new JButton("Craft: " + craftName);
    jButton.addActionListener(e -> {
      this.action = ACTION_CRAFT;
      this.craft = craftName;
      setVisible(false);
    });
    return jButton;
  }

  private String[] getUniqueCrafts(Craft[] crafts) {
    List<String> result = new ArrayList<>();
    for (Craft craft : crafts) {
      if (!result.contains(craft.getName())) result.add(craft.getName());
    }
    return result.toArray(new String[0]);
  }



}
