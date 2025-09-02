package checkers.src.main.java;

import java.util.ArrayList;

public class MoveGenerator {

  private final CaptureRules captureRules;
  private final MoveRules moveRules;
  private final RegularMoveGenerator regularMoveGenerator;
  private final KingMoveGenerator kingMoveGenerator;
  private final CaptureGenerator captureGenerator;
  private final PlayerConfiguration playerConfiguration;

  public MoveGenerator(PlayerConfiguration playerConfiguration) {
    this.captureRules = new CaptureRules();
    this.moveRules = new MoveRules();
    this.regularMoveGenerator = new RegularMoveGenerator();
    this.kingMoveGenerator = new KingMoveGenerator();
    this.captureGenerator = new CaptureGenerator();
    this.playerConfiguration = playerConfiguration;
  }

  public ArrayList<BotDecision> getPossibleMoves(BoardState boardState) {
    ArrayList<BotDecision> possibleMoves = new ArrayList<>();

    boolean mustTake = captureRules.checkAllPiecesPossibleCaptures(
        playerConfiguration.getBotColor(),
        playerConfiguration.getBotKingColor(),
        boardState);

    for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
      for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
        int piece = boardState.getPiece(row, col);

        if (!isBotPiece(piece)) {
          continue;
        }

        if (mustTake && captureRules.canCapture(col, row, boardState)) {
          generateCaptureMoves(row, col, piece, possibleMoves, boardState);
        } else if (!mustTake && moveRules.canMove(col, row, boardState)) {
          generateRegularMoves(row, col, piece, possibleMoves, boardState);
        }
      }
    }

    return possibleMoves;
  }

  private boolean isBotPiece(int piece) {
    return piece == playerConfiguration.getBotColor() ||
        piece == playerConfiguration.getBotKingColor();
  }

  private void generateCaptureMoves(int row, int col, int piece,
                                    ArrayList<BotDecision> moves, BoardState boardState) {
    if (PieceRules.isKing(piece)) {
      captureGenerator.findKingCaptures(row, col, piece, moves, boardState);
    } else {
      captureGenerator.findRegularCaptures(row, col, piece, moves, boardState);
    }
  }

  private void generateRegularMoves(int row, int col, int piece,
                                    ArrayList<BotDecision> moves, BoardState boardState) {
    if (PieceRules.isKing(piece)) {
      kingMoveGenerator.findKingMoves(row, col, piece, moves, boardState);
    } else {
      regularMoveGenerator.findRegularPieceMoves(row, col, piece, moves, boardState);
    }
  }
}