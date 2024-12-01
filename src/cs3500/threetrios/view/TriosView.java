package cs3500.threetrios.view;

import java.io.IOException;

import cs3500.threetrios.controller.ViewFeatures;

/**
 * To represent the view of the Three Trios model.
 */
public interface TriosView {

  /**
   * Renders a model in some manner (e.g. as text, or as graphics, etc.).
   *
   * @throws IOException if the rendering fails for some reason
   */
  void render() throws IOException;

  /**
   * Makes the view visible.
   */
  void makeVisible();

  /**
   * Adds features.
   *
   * @param viewFeatures the feature to add
   */
  void addFeatures(ViewFeatures viewFeatures);

  /**
   * Displays a message for the user.
   *
   * @param message the message to display
   */
  void showMessage(String message);

  /**
   * Set's the players title depending on which player's panel it is.
   *
   * @param string the player to display
   */
  void setPlayerTitle(String string);
}
