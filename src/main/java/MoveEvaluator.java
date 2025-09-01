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

  public int evaluateMove(int[] moveArray, BoardState boardState) {
    applyMoveToBoard(moveArray, boardState);

    int score = 0;
    score += evaluatePlayerThreats(moveArray, boardState);
    score += evaluateTakeOpportunities(boardState);
    score += evaluateQueenPromotionChance(moveArray, boardState);

    return score;
  }

  private int evaluatePlayerThreats(int[] moveArray, BoardState boardState) {
    if (!playerCanTakeAfterMove(boardState)) {
      return 0;
    }

    int movedPiece = boardState.getPiece(moveArray[2], moveArray[3]);
    return move.isKing(movedPiece)
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

  private int evaluateQueenPromotionChance(int[] moveArray,
                                           BoardState boardState) {
    int movedPiece = boardState.getPiece(moveArray[2], moveArray[3]);

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

  private void applyMoveToBoard(int[] moveArray, BoardState boardState) {
    int moveType = moveArray[GameConstants.MOVE_TYPE];
    int fromRow = moveArray[0], fromCol = moveArray[1];
    int toRow = moveArray[2], toCol = moveArray[3];

    switch (moveType) {
      case GameConstants.MOVE:
        moveExecutor.executeNormalMove(fromRow, fromCol, toRow, toCol,playerConfiguration.getBotColor() , boardState);
        break;
      case GameConstants.TAKE:
        moveExecutor.executeCapture(fromRow, fromCol, toRow, toCol, playerConfiguration.getBotColor(), boardState);
        break;
      case GameConstants.QUEEN_TAKE:
        moveExecutor.executeQueenCapture(fromRow, fromCol, toRow, toCol, playerConfiguration.getBotKingColor(), boardState);
        break;
    }
  }

  public boolean isChanceForQueen(int colorToCheck, BoardState boardState,
                                  int pieceType) {
    if (move.isKing(pieceType)) {
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
