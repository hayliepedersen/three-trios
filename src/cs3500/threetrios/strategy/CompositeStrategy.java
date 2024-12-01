package cs3500.threetrios.strategy;

import java.util.HashMap;
import java.util.List;

import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.TriosModel;

/**
 * Allows for the combination of player strategies.
 */
public class CompositeStrategy extends Strategy implements PlayerStrategy {
  private final List<PlayerStrategy> strategies;

  /**
   * Initializes a strategies that is composed of multiple strategies.
   *
   * @param strategies the strategies to combine
   */
  public CompositeStrategy(List<PlayerStrategy> strategies) {
    this.strategies = strategies;
  }

  @Override
  public BestMove chooseMove(TriosModel model, Player player) {
    BestMove bestOverallMove = null;
    int highestScore = Integer.MIN_VALUE;

    for (PlayerStrategy strategy : strategies) {
      BestMove strategyMove = strategy.chooseMove(model, player);
      HashMap<String, Integer> cardDirections = null;
      if (strategyMove != null) {
        HashMap<String, HashMap<String, Integer>> directions =
                strategyMove.getCard().setCardDirections();
        cardDirections = directions.get(strategyMove.getCard().getName());
      }

      int row = strategyMove.getRow();
      int col = strategyMove.getCol();
      int cornerIndex = -1;

      // Evaluate the move's score
      int moveScore = evaluateMoveScore(row, col, cornerIndex, cardDirections, model, player);

      if (moveScore > highestScore) {
        highestScore = moveScore;
        bestOverallMove = strategyMove;
      }
    }

    // Return the best move found across all strategies
    return bestOverallMove != null ? bestOverallMove : defaultMove(model, player);
  }

  /**
   * Evaluates the given move's score. A higher defense and lower flip risk make a move better
   *
   * @param row            the row position
   * @param col            the column position
   * @param cardDirections the card directions
   * @param model          the model to read from
   * @param player         the player making the move
   * @return a score representing the quality of the move
   */
  private int evaluateMoveScore(int row, int col, int cornerIndex,
                                HashMap<String, Integer> cardDirections,
                                TriosModel model, Player player) {
    int defenseScore = calculateDefenseScore(cornerIndex, cardDirections);
    int flipRiskScore = calculateFlipRisk(row, col, cardDirections, model, player);

    return defenseScore - flipRiskScore;
  }
}
