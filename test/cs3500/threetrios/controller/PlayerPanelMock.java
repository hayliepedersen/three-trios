package cs3500.threetrios.controller;

import cs3500.threetrios.view.AbstractGamePanel;

/**
 * Represents a mock for player panels.
 */
public class PlayerPanelMock extends AbstractGamePanel {

  @Override
  protected int getCellSize() {
    return 180;
  }
}
