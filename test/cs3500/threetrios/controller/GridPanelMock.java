package cs3500.threetrios.controller;

import cs3500.threetrios.view.AbstractGamePanel;

/**
 * Represents a mock for a grid panel.
 */
public class GridPanelMock extends AbstractGamePanel {

  @Override
  protected int getCellSize() {
    return 180;
  }
}
