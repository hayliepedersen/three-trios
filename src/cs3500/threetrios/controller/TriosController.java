package cs3500.threetrios.controller;

import java.io.IOException;

import cs3500.threetrios.model.AnyPlayer;
import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.CardModel;
import cs3500.threetrios.model.MachinePlayer;
import cs3500.threetrios.model.TriosModel;
import cs3500.threetrios.view.TriosView;

/**
 * Controller for either a human or machine player.
 */
public class TriosController implements ViewFeatures, ModelObservers {
  private final TriosModel model;
  private final AnyPlayer player;
  private final TriosView view;
  private Card selectedCard;

  /**
   * Constructs a human or machine controller.
   *
   * @param model  the model to initialize with
   * @param player the player to play with
   * @param view   the view to display
   */
  public TriosController(TriosModel model, AnyPlayer player, TriosView view) {
    this.model = model;
    this.player = player;
    this.view = view;

    this.view.addFeatures(this);
    this.model.addObservers(this);

    try {
      if (model.isPlayerTurn(player) && player instanceof MachinePlayer) {
        handleMachineTurnPlay();
      }
    } catch (IOException ex) {
      throw new IllegalStateException(ex);
    }
  }

  @Override
  public void cardSelected(Card card) {
    if (model.isPlayerTurn(player) && model.getPlayerHand(player).contains(card)) {
      this.selectedCard = card;
    } else {
      view.showMessage("It's not your turn!");
    }
  }

  @Override
  public Card selectedCard() {
    return selectedCard;
  }

  @Override
  public void cardPlaced(int row, int col, Card card) throws IOException {
    if (selectedCard != null) {
      player.makeMove(row, col, card);
      this.selectedCard = null;
      view.render();
      this.endTurn();
    }
  }

  @Override
  public void endTurn() throws IOException {
    model.endTurn();
    view.render();
  }

  @Override
  public void panelPrint(int rows, int cols, int index, String player) {
    if (rows != -5) {
      System.out.println("row: " + rows
              + " col: " + cols);
    } else {
      System.out.println("Hand index: " + index + " Player: " + player);
    }
  }

  @Override
  public void handleMachineTurnPlay() throws IOException {
    if (model.isPlayerTurn(player) && player instanceof MachinePlayer) {
      player.makeMove(0, 0, new CardModel("N/A", "1", "1", "1", "1"));
      view.render();
      this.endTurn();
    }
  }

  @Override
  public void repaintGrid() {
    try {
      view.render();
    } catch (IOException ex) {
      throw new IllegalStateException(ex);
    }
  }

  @Override
  public void onGameOver(String winner, int winningScore) throws IOException {
    view.showMessage("Game Over! " + "The winner is " + winner + " with a score of "
            + winningScore);
    view.render();
  }

  @Override
  public void onGameTie() throws IOException {
    view.showMessage("Game Over! Tie Game.");
    view.render();
  }

  @Override
  public void modelCardPlaced() throws IOException {
    view.render();
  }

  @Override
  public void onTurnChanged() throws IOException {
    view.render();
  }
}