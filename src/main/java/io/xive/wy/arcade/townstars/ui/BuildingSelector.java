package io.xive.wy.arcade.townstars.ui;

import io.xive.wy.arcade.townstars.game.Building;
import io.xive.wy.arcade.townstars.game.BuildingTune;
import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class BuildingSelector extends JDialog {

  private BuildingTune selectedBuildingTune;

  public BuildingSelector(BuildingTune[] buildingTunes, JFrame parent) {
    setTitle("Add new building");
    setLayout(new java.awt.GridLayout(0, 1));
    setModal(true);
    setLocationRelativeTo(parent);

    for (BuildingTune buildingTune : buildingTunes) {
      addButton(buildingTune);
    }
    pack();

  }

  private void addButton(BuildingTune buildingTune) {
    String text = buildingTune.getName() + " ($ " + buildingTune.getBuildCost() + ")";
    if (buildingTune.getStorageCapacity() > 0) {
      text += "[Storage(" + buildingTune.getStorageCapacity() + "): " + String.join(", ", buildingTune.getStoresCraftTypes()) + "]";
    } else if (buildingTune.isTradeDepot()) {
      text += "[Trade depot]";
    } else {
      text += "[Produces: " + String.join(", ", buildingTune.getProducesCrafts()) + "]";
    }
    JButton jButton = new JButton(text);
    jButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
    jButton.addActionListener((e) -> {
      selectedBuildingTune = buildingTune;
      setVisible(false);
    });
    add(jButton);
  }

  public BuildingTune getSelectedBuildingTune() {
    return selectedBuildingTune;
  }

}
