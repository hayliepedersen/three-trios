package cs3500.threetrios;

import java.util.List;
import java.util.Random;

import cs3500.threetrios.controller.TriosController;
import cs3500.threetrios.model.AnyPlayer;
import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.CardCell;
import cs3500.threetrios.model.CardModel;
import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.Hole;
import cs3500.threetrios.model.HumanPlayer;
import cs3500.threetrios.model.MachinePlayer;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.ThreeTriosModel;
import cs3500.threetrios.model.TriosModel;
import cs3500.threetrios.strategy.GoForCorners;
import cs3500.threetrios.strategy.HighestFlipNum;
import cs3500.threetrios.view.ThreeTriosGraphicsView;

/**
 * Represents a runner for the ThreeTrios game.
 */
public final class ThreeTrios {
  /**
   * Represents a main class for running the game of ThreeTrios.
   *
   * @param args the arguments to initialize the game with
   */
  public static void main(String[] args) {
    Card corruptKing = new CardModel("CorruptKing", "7", "3", "9", "A");
    Card angryDragon = new CardModel("AngryDragon", "2", "8", "9", "9");
    Card windBird = new CardModel("WindBird", "7", "2", "5", "3");
    Card heroKnight = new CardModel("HeroKnight", "A", "2", "4", "4");
    Card worldDragon = new CardModel("WorldDragon", "8", "3", "5", "7");
    Card skyWhale = new CardModel("SkyWhale", "4", "5", "9", "9");
    Card systems = new CardModel("Systems", "1", "4", "3", "7");
    Card algo = new CardModel("algo", "3", "8", "A", "7");
    Card linAlg = new CardModel("LinAlg", "8", "1", "2", "3");
    Card stats = new CardModel("Stats", "6", "3", "7", "7");
    Card engineering = new CardModel("Engineering", "5", "1", "2", "A");

    Cell[][] bigGrid = new Cell[5][3];
    bigGrid[0][0] = new CardCell();
    bigGrid[1][0] = new CardCell();
    bigGrid[2][0] = new CardCell();
    bigGrid[3][0] = new CardCell();
    bigGrid[4][0] = new CardCell();
    bigGrid[0][1] = new Hole();
    bigGrid[1][1] = new Hole();
    bigGrid[2][1] = new Hole();
    bigGrid[3][1] = new Hole();
    bigGrid[4][1] = new Hole();
    bigGrid[0][2] = new CardCell();
    bigGrid[1][2] = new CardCell();
    bigGrid[2][2] = new CardCell();
    bigGrid[3][2] = new CardCell();
    bigGrid[4][2] = new CardCell();

    List<Card> deck = List.of(corruptKing, angryDragon, windBird, heroKnight, worldDragon,
            skyWhale, systems, algo, stats, linAlg, engineering);

    TriosModel model = new ThreeTriosModel(bigGrid, deck, new Random(1));

    String player1Type = args[0];
    String player2Type = args[1];

    ThreeTriosGraphicsView viewPlayer1 = new ThreeTriosGraphicsView(model, Player.RED);
    ThreeTriosGraphicsView viewPlayer2 = new ThreeTriosGraphicsView(model, Player.BLUE);

    AnyPlayer player1 = configurePlayer(player1Type, model, Player.RED);
    AnyPlayer player2 = configurePlayer(player2Type, model, Player.BLUE);

    TriosController controller1 = new TriosController(model, player1, viewPlayer1);
    TriosController controller2 = new TriosController(model, player2, viewPlayer2);

    viewPlayer1.addFeatures(controller1);
    viewPlayer2.addFeatures(controller2);

    viewPlayer1.makeVisible();
    viewPlayer2.makeVisible();

    model.startGame();
  }

  /**
   * Configures a player based off of command-line arguments.
   *
   * @param type the argument
   * @param model the model
   * @param playerColor the player color
   * @return either a human or machine player
   */
  private static AnyPlayer configurePlayer(String type, TriosModel model, Player playerColor) {
    switch (type.toLowerCase()) {
      case "human":
        return new HumanPlayer(model, playerColor);
      case "flips":
        return new MachinePlayer(model, playerColor, new HighestFlipNum());
      case "corners":
        return new MachinePlayer(model, playerColor, new GoForCorners());
      default:
        return null; // Invalid type
    }
  }
}
