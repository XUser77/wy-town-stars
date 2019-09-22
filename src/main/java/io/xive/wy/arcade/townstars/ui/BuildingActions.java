package io.xive.wy.arcade.townstars.ui;

import io.xive.wy.arcade.townstars.game.Building;
import io.xive.wy.arcade.townstars.game.BuildingTune;
import io.xive.wy.arcade.townstars.game.Craft;
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

  public BuildingActions(Building building, JFrame parent) {
    setTitle("Building: " + building.getName());
    setLayout(new java.awt.GridLayout(0, 1));
    setModal(true);
    setLocationRelativeTo(parent);

    if (building.getStorageCapacity() > 0) {
      add(new JLabel("Storage for " + String.join(",", building.getStoresCraftTypes())));
      add(new JLabel("Stored: " + groupCrafts(building.getStoredCrafts())));
      add(new JLabel("Usage: " + building.getStoredCrafts().length + " / " + building.getStorageCapacity()));
    } else if (building.isTradeDepot()) {
      add(new JLabel("Trade depot"));
    } else {
      add(new JLabel("Can produce: " + String.join(", ", building.getProducesCrafts())));
      if (building.getCrafting() != null) {
        add(new JLabel("Producing " + building.getCrafting().getName()));
      } else if (building.getCraftOutside() != null) {
        add(new JLabel("Produced " + building.getCraftOutside().getName()));
      } else {
        add(new JLabel("Free"));
        if (building.getCraftsInside().length > 0) {
          add(new JLabel("Contains: " + groupCrafts(building.getCraftsInside())));
        } else {
          add(new JLabel("Empty"));
        }
      }

    }

    pack();

  }

  private String groupCrafts(Craft[] crafts) {
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
