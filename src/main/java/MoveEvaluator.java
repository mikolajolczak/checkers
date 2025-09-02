package checkers.src.main.java;

public class MoveEvaluator {
  private final Move move;
  private final PlayerConfiguration playerConfiguration;
  private final MoveExecutor moveExecutor;

  public MoveEvaluator(Move moveParam,
                       PlayerConfiguration playerConfigurationParam,
                       MoveExecutor moveExecutorParam) {
    move = moveParam;
    playerConfiguration = playerConfigurationParam;
    moveExecutor = moveExecutorParam;
  }

  public int evaluateMove(BotDecision decisionParam, BoardState boardState) {
    moveExecutor.applyMoveToBoard(decisionParam, boardState, playerConfiguration);

    int score = 0;
    score += evaluatePlayerThreats(decisionParam, boardState);
    score += evaluateTakeOpportunities(boardState);
    score += evaluateQueenPromotionChance(decisionParam, boardState);

    return score;
  }

  private int evaluatePlayerThreats(BotDecision decisionParam, BoardState boardState) {
    if (!playerCanTakeAfterMove(boardState)) {
      return 0;
    }

    int movedPiece = boardState.getPiece(decisionParam.toRow(), decisionParam.toCol());
    return boardState.isItKing(movedPiece)
        ? -GameConstants.SCORE_PLAYER_THREAT_KING
        : -GameConstants.SCORE_PLAYER_THREAT;
  }

  private boolean playerCanTakeAfterMove(BoardState boardState) {
    return move.checkAllPiecesPossibleTakes(
        playerConfiguration.getHumanColor(),
        playerConfiguration.getHumanKingColor(),
        boardState);
  }

  private int evaluateTakeOpportunities(BoardState boardState) {
    if (botCanTakeAfterMove(boardState)) {
      return GameConstants.SCORE_TAKE_POSSIBLE;
    }
    return 0;
  }

  private boolean botCanTakeAfterMove(BoardState boardState) {
    return move.checkAllPiecesPossibleTakes(
        playerConfiguration.getBotColor(),
        playerConfiguration.getBotKingColor(),
        boardState);
  }

  private int evaluateQueenPromotionChance(BotDecision botDecisionParam,
                                           BoardState boardState) {
    int movedPiece = boardState.getPiece(botDecisionParam.toRow(),botDecisionParam.toCol());

    if (canPromoteToQueen(boardState, movedPiece)) {
      return GameConstants.SCORE_CHANCE_FOR_QUEEN;
    }
    return 0;
  }

  private boolean canPromoteToQueen(BoardState boardState,
                                    int movedPiece) {
    return isChanceForQueen(
        playerConfiguration.getBotColor(),
        boardState,
        movedPiece);
  }

  public boolean isChanceForQueen(int colorToCheck, BoardState boardState,
                                  int pieceType) {
    if (boardState.isItKing(pieceType)) {
      return false;
    }

    int promotionRow = getPromotionRow(colorToCheck);
    return hasPieceOnPromotionRow(boardState, colorToCheck, promotionRow);
  }


  private int getPromotionRow(int colorToCheck) {
    return (colorToCheck == GameConstants.BLACK)
        ? GameConstants.LAST_ROW_INDEX
        : 0;
  }

  private boolean hasPieceOnPromotionRow(BoardState boardState,
                                         int colorToCheck, int targetRow) {
    for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
      if (boardState.getPiece(targetRow, col) == colorToCheck) {
        return true;
      }
    }
    return false;
  }
}
