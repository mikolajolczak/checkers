package checkers.src.main.java;

import java.util.ArrayList;

public class MoveService {
  private final Move move;
  private final TurnManager turnManager;
  private final BoardState boardState;
  private final MoveGenerator moveGenerator;

  public MoveService(Move moveParam, TurnManager turnManagerParam,
                     BoardState boardStateParam,
                     MoveGenerator moveGeneratorParam) {
    move = moveParam;
    turnManager = turnManagerParam;
    boardState = boardStateParam;
    moveGenerator = moveGeneratorParam;
  }

  public boolean canSelectPiece(int row, int col, BoardState boardStateParam) {
    int value = boardStateParam.getPiece(row, col);
    boolean isCurrentPiece = value == turnManager.getCurrentColor() || value == turnManager.getCurrentKingColor();
    return isCurrentPiece && (move.canIMove(col, row) || move.canITake(col, row, boardStateParam));
  }

  public boolean isLegalMove(int row, int col, int firstClickCol, int firstClickRow, int firstClickColor) {
    return move.isItLegalSecondClickMove(col, row, firstClickCol, firstClickRow, firstClickColor);
  }

  public boolean mustTake() {
    return move.checkAllPiecesPossibleTakes(turnManager.getCurrentColor(), turnManager.getCurrentKingColor(), boardState);
  }
  public ArrayList<BotDecision> getPossibleMoves(BoardState boardState) {
    return moveGenerator.getPossibleMoves(boardState);
  }
}
