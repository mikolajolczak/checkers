package checkers.src.main.java;

public class PromotionEvaluator {

  public int evaluatePromotionChance(BotDecision decision, BoardState boardState,
                                     PlayerConfiguration playerConfiguration) {
    int movedPiece = boardState.getPiece(decision.toRow(), decision.toCol());

    if (canPromoteToQueen(boardState, movedPiece, playerConfiguration.getBotColor())) {
      return GameConstants.SCORE_CHANCE_FOR_QUEEN;
    }
    return 0;
  }

  public boolean canPromoteToQueen(BoardState boardState, int movedPiece, int botColor) {
    return isChanceForQueen(botColor, boardState, movedPiece);
  }

  public boolean isChanceForQueen(int colorToCheck, BoardState boardState, int pieceType) {
    if (PieceRules.isKing(pieceType)) {
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

  private boolean hasPieceOnPromotionRow(BoardState boardState, int colorToCheck, int targetRow) {
    for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
      if (boardState.getPiece(targetRow, col) == colorToCheck) {
        return true;
      }
    }
    return false;
  }
}

