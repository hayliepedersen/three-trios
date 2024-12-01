package cs3500.threetrios.controller;

import java.io.IOException;
import cs3500.threetrios.view.TriosView;

/**
 * Represents a mock of the graphic view.
 */
public class GraphicViewMock implements TriosView {

  @Override
  public void render() throws IOException {
    // not currently necessary
  }

  @Override
  public void makeVisible() {
    // not currently necessary
  }

  @Override
  public void addFeatures(ViewFeatures viewFeatures) {
    // not currently necessary
  }

  @Override
  public void showMessage(String message) {
    // not currently necessary
  }

  @Override
  public void setPlayerTitle(String string) {
    // not currently necessary
  }
}
