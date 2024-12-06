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
import cs3500.threetrios.model.VariantAbstract;
import cs3500.threetrios.model.VariantOneModel;
import cs3500.threetrios.model.VariantTwoModel;
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
    Card corruptKing = new CardModel("CorruptKing", "7", "1", "9", "A");
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

    TriosModel model = getTriosModel(args, bigGrid, deck);

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
   * Creates a trios model based off of command-line arguments.
   *
   * @param args the arguments
   * @param bigGrid the grid to initialize with
   * @param deck the deck to initialize with
   * @return a TriosModel
   */
  private static TriosModel getTriosModel(String[] args, Cell[][] bigGrid, List<Card> deck) {
    TriosModel model;

    model = new ThreeTriosModel(bigGrid, deck, new Random(1));

    if (args.length > 2) {
      boolean reverse = false;
      boolean fallenAce = false;
      boolean same = false;
      boolean plus = false;

      if (args[2].equals("reverse")) {
        // Only reverse rule applied
        reverse = true;
      } if (args[2].equals("fallenAce")) {
        // Only fallenAce rule applied
        fallenAce = true;
      }
      if (args[2].equals("same")) {
        // Only fallenAce rule applied
        same = true;
      }
      if (args[2].equals("plus")) {
        // Only fallenAce rule applied
        plus = true;
      }

      // Combo
      if (args.length > 3) {
        if (args[2].equals("fallenAce") && args[3].equals("reverse") ||
                args[2].equals("reverse") || args[3].equals("fallenAce")) {
          reverse = true;
          fallenAce = true;
        }
        if (args[3].equals("same")) {
          same = true;
        }
        if (args[3].equals("plus")) {
          plus = true;
        }
      }

      if (args.length > 4) {
        if (args[4].equals("same")) {
          same = true;
        }
        if (args[4].equals("plus")) {
          plus = true;
        }
      }

      if ((reverse || fallenAce) && (same || plus)) {
        model = new VariantAbstract(bigGrid, deck, reverse, fallenAce, same, plus,
                new Random(1));
      }

      if (reverse || fallenAce) {
        model = new VariantOneModel(bigGrid, deck, reverse, fallenAce, new Random(1));
      }
      if (same || plus) {
        model = new VariantTwoModel(bigGrid, deck, same, plus, new Random(1));
      }
    }

    return model;
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
