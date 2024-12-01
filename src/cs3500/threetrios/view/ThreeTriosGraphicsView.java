package cs3500.threetrios.view;

import cs3500.threetrios.controller.ViewFeatures;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.ReadOnlyTriosModel;

import java.awt.FlowLayout;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Represents a GUI version of the view for ThreeTrios.
 */
public class ThreeTriosGraphicsView extends JFrame implements TriosView {
  private final JPanel gridPanel;
  private final JPanel redPlayerPanel;
  private final JPanel bluePlayerPanel;
  private final ReadOnlyTriosModel model;
  private String playerTitle;

  /**
   * Sets up the GUI for a game of ThreeTrios.
   *
   * @param model the model to read game data from
   */
  public ThreeTriosGraphicsView(ReadOnlyTriosModel model) {
    super();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.playerTitle = "";
    this.model = model;

    int rows = model.getGridCopy().length;
    int cellSize = 600 / rows;
    int height = (model.getRedHand().size() * cellSize) + cellSize / 2;
    setSize(800, height);
    setLocationRelativeTo(null);

    JPanel mainPanel = new JPanel(new FlowLayout());

    this.gridPanel = new GridPanel(model);
    gridPanel.setSize(new Dimension(600, 600));
    this.redPlayerPanel = new PlayerPanel(Player.RED, model);
    redPlayerPanel.setSize(new Dimension(100, 600));
    this.bluePlayerPanel = new PlayerPanel(Player.BLUE, model);
    bluePlayerPanel.setSize(new Dimension(100, 600));

    mainPanel.add(redPlayerPanel, FlowLayout.LEFT);
    mainPanel.add(gridPanel, FlowLayout.CENTER);
    mainPanel.add(bluePlayerPanel, FlowLayout.RIGHT);

    this.add(mainPanel);
  }

  @Override
  public void render() throws IOException {
    setTitle(playerTitle + "Current Player is " + model.getCurrentPlayer());

    this.repaint();
  }

  @Override
  public void makeVisible() {
    this.setVisible(true);

    setTitle(playerTitle + "Current Player is " + model.getCurrentPlayer());
  }

  @Override
  public void addFeatures(ViewFeatures viewFeatures) {
    GridPanel grid = (GridPanel) this.gridPanel;
    grid.setFeatures(viewFeatures);
    PlayerPanel redPlayer = (PlayerPanel) this.redPlayerPanel;
    redPlayer.setFeatures(viewFeatures);
    PlayerPanel bluePlayer = (PlayerPanel) this.bluePlayerPanel;
    bluePlayer.setFeatures(viewFeatures);
  }

  @Override
  public void showMessage(String message) {
    JOptionPane.showMessageDialog(this, message,
            "Over!", JOptionPane.PLAIN_MESSAGE);
  }

  @Override
  public void setPlayerTitle(String string) {
    this.playerTitle = string;
  }
}
