package checkers.src.main.java;

public class MoveEvaluator {
  private final BoardController boardController;

  public MoveEvaluator(BoardController boardController) {
    this.boardController = boardController;
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
    return boardController.getMove().isKing(movedPiece)
        ? -GameConstants.SCORE_PLAYER_THREAT_KING
        : -GameConstants.SCORE_PLAYER_THREAT;
  }

  private boolean playerCanTakeAfterMove(BoardState boardState) {
    return boardController.getMove().checkAllPiecesPossibleTakes(
        boardController.getPlayersColor(),
        boardController.getPlayersKingColor(),
        boardState);
  }

  private int evaluateTakeOpportunities(BoardState boardState) {
    if (botCanTakeAfterMove(boardState)) {
      return GameConstants.SCORE_TAKE_POSSIBLE;
    }
    return 0;
  }

  private boolean botCanTakeAfterMove(BoardState boardState) {
    return boardController.getMove().checkAllPiecesPossibleTakes(
        boardController.getBotsColor(),
        boardController.getBotsKingColor(),
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
        boardController.getBotsColor(),
        boardState,
        movedPiece);
  }

  private void applyMoveToBoard(int[] moveArray, BoardState boardState) {
    int moveType = moveArray[GameConstants.MOVE_TYPE];
    int fromRow = moveArray[0], fromCol = moveArray[1];
    int toRow = moveArray[2], toCol = moveArray[3];

    switch (moveType) {
      case GameConstants.MOVE:
        executeRegularMove(fromRow, fromCol, toRow, toCol, boardState);
        break;
      case GameConstants.TAKE:
        executeCapture(fromRow, fromCol, toRow, toCol, boardState);
        break;
      case GameConstants.QUEEN_TAKE:
        executeQueenCapture(fromRow, fromCol, toRow, toCol, boardState);
        break;
    }
  }

  private void executeCapture(int fromRow, int fromCol, int toRow, int toCol,
                              BoardState boardState) {
    boardController.take(fromRow, fromCol, toRow, toCol,
        boardController.getBotsColor(), boardState);
  }

  private void executeQueenCapture(int fromRow, int fromCol, int toRow,
                                   int toCol, BoardState boardState) {
    boardController.queenTake(fromRow, fromCol, toRow, toCol,
        boardController.getBotsKingColor(), boardState);
  }

  private void executeRegularMove(int fromRow, int fromCol, int toRow,
                                  int toCol, BoardState boardState) {
    int piece = boardState.getPiece(fromRow, fromCol);

    boardState.setPiece(fromRow, fromCol, GameConstants.EMPTY);
    boardState.setPiece(toRow, toCol, piece);
  }

  public boolean isChanceForQueen(int colorToCheck, BoardState boardState,
                                  int pieceType) {
    if (isAlreadyKing(pieceType)) {
      return false;
    }

    int promotionRow = getPromotionRow(colorToCheck);
    return hasPieceOnPromotionRow(boardState, colorToCheck, promotionRow);
  }

  private boolean isAlreadyKing(int pieceType) {
    return boardController.getMove().isKing(pieceType);
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
