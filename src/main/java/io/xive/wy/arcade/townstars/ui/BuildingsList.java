package io.xive.wy.arcade.townstars.ui;

import io.xive.wy.arcade.townstars.game.Building;
import javax.swing.JButton;
import javax.swing.JDialog;

public class BuildingsList extends JDialog {

  public int index = -1;

  public BuildingsList(Building[] buildings, JDialog parent) {
    setTitle("Select destination");
    setLayout(new java.awt.GridLayout(0, 16));
    setModal(true);
    setLocationRelativeTo(parent);

    for(int i=0; i<buildings.length; i++) {
      if (buildings[i] != null) {
        add(buildingButton(buildings[i], i));
      }
    }

    pack();

  }

  private JButton buildingButton(Building building, int index) {
    JButton jButton = new JButton(building.getShortName());
    jButton.addActionListener(l -> {
      BuildingsList.this.index = index;
      setVisible(false);
    });
    return jButton;
  }

}
