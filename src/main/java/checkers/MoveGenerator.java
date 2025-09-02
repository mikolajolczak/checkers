package checkers;

import java.util.ArrayList;

public record MoveGenerator(PlayerConfiguration playerConfiguration) {

  public ArrayList<BotDecision> getPossibleMoves(final BoardState boardState) {
    ArrayList<BotDecision> possibleMoves = new ArrayList<>();

    boolean mustTake = CaptureRules.checkAllPiecesPossibleCaptures(
        playerConfiguration.getBotColor(),
        playerConfiguration.getBotKingColor(),
        boardState);

    for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
      for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
        int piece = boardState.getPiece(row, col);

        if (!isBotPiece(piece)) {
          continue;
        }

        if (mustTake && CaptureRules.canCapture(col, row, boardState)) {
          generateCaptureMoves(row, col, piece, possibleMoves, boardState);
        } else if (!mustTake && MoveRules.canMove(col, row, boardState)) {
          generateRegularMoves(row, col, piece, possibleMoves, boardState);
        }
      }
    }

    return possibleMoves;
  }

  private boolean isBotPiece(final int piece) {
    return piece == playerConfiguration.getBotColor()
        || piece == playerConfiguration.getBotKingColor();
  }

  private void generateCaptureMoves(final int row, final int col,
                                    final int piece,
                                    final ArrayList<BotDecision> moves,
                                    final BoardState boardState) {
    if (PieceRules.isKing(piece)) {
      CaptureGenerator.findKingCaptures(row, col, piece, moves, boardState);
    } else {
      CaptureGenerator.findRegularCaptures(row, col, piece, moves, boardState);
    }
  }

  private void generateRegularMoves(final int row, final int col,
                                    final int piece,
                                    final ArrayList<BotDecision> moves,
                                    final BoardState boardState) {
    if (PieceRules.isKing(piece)) {
      KingMoveGenerator.findKingMoves(row, col, piece, moves, boardState);
    } else {
      RegularMoveGenerator.findRegularPieceMoves(row, col, piece, moves,
          boardState);
    }
  }
}
