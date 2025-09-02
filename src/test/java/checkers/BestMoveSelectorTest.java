package checkers;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BestMoveSelectorTest {

  private ArrayList<BotDecision> possibleMoves;
  private BoardState mockBoardState;
  private PlayerConfiguration mockPlayerConfiguration;
  private BotDecision move1, move2, move3, move4;

  @BeforeEach
  void setUp() {
    possibleMoves = new ArrayList<>();
    mockBoardState = mock(BoardState.class);
    mockPlayerConfiguration = mock(PlayerConfiguration.class);

    move1 = new BotDecision(0, 0, 1, 1, 0);
    move2 = new BotDecision(1, 1, 2, 2, 1);
    move3 = new BotDecision(2, 2, 3, 3, 0);
    move4 = new BotDecision(3, 3, 4, 4, 1);
  }

  @Test
  void shouldReturnMoveWithHighestScore() {

    possibleMoves.add(move1);
    possibleMoves.add(move2);
    possibleMoves.add(move3);

    BoardState mockCopy = mock(BoardState.class);
    when(mockBoardState.copy()).thenReturn(mockCopy);

    try (MockedStatic<MoveEvaluator> moveEvaluatorMock = Mockito.mockStatic(
        MoveEvaluator.class)) {
      moveEvaluatorMock.when(() -> MoveEvaluator.evaluateMove(move1, mockCopy,
              mockPlayerConfiguration))
          .thenReturn(5);
      moveEvaluatorMock.when(() -> MoveEvaluator.evaluateMove(move2, mockCopy,
              mockPlayerConfiguration))
          .thenReturn(10);
      moveEvaluatorMock.when(() -> MoveEvaluator.evaluateMove(move3, mockCopy,
              mockPlayerConfiguration))
          .thenReturn(3);

      BotDecision result =
          BestMoveSelector.chooseBestMove(possibleMoves, mockBoardState,
              mockPlayerConfiguration);

      assertEquals(move2, result);
      verify(mockBoardState, times(3)).copy();
    }
  }

  @Test
  void shouldReturnFirstMoveWhenAllScoresEqual() {

    possibleMoves.add(move1);
    possibleMoves.add(move2);
    possibleMoves.add(move3);

    BoardState mockCopy = mock(BoardState.class);
    when(mockBoardState.copy()).thenReturn(mockCopy);

    try (MockedStatic<MoveEvaluator> moveEvaluatorMock = Mockito.mockStatic(
        MoveEvaluator.class)) {
      moveEvaluatorMock.when(
              () -> MoveEvaluator.evaluateMove(any(BotDecision.class),
                  eq(mockCopy),
                  eq(mockPlayerConfiguration)))
          .thenReturn(7);

      BotDecision result =
          BestMoveSelector.chooseBestMove(possibleMoves, mockBoardState,
              mockPlayerConfiguration);

      assertEquals(move3, result);
    }
  }

  @Test
  void shouldReturnLastMoveWithHighestScoreOnTie() {

    possibleMoves.add(move1);
    possibleMoves.add(move2);
    possibleMoves.add(move3);

    BoardState mockCopy = mock(BoardState.class);
    when(mockBoardState.copy()).thenReturn(mockCopy);

    try (MockedStatic<MoveEvaluator> moveEvaluatorMock = Mockito.mockStatic(
        MoveEvaluator.class)) {
      moveEvaluatorMock.when(() -> MoveEvaluator.evaluateMove(move1, mockCopy,
              mockPlayerConfiguration))
          .thenReturn(8);
      moveEvaluatorMock.when(() -> MoveEvaluator.evaluateMove(move2, mockCopy,
              mockPlayerConfiguration))
          .thenReturn(8);
      moveEvaluatorMock.when(() -> MoveEvaluator.evaluateMove(move3, mockCopy,
              mockPlayerConfiguration))
          .thenReturn(5);

      BotDecision result =
          BestMoveSelector.chooseBestMove(possibleMoves, mockBoardState,
              mockPlayerConfiguration);

      assertEquals(move2, result);
    }
  }

  @Test
  void shouldHandleSingleMove() {

    possibleMoves.add(move1);

    BoardState mockCopy = mock(BoardState.class);
    when(mockBoardState.copy()).thenReturn(mockCopy);

    try (MockedStatic<MoveEvaluator> moveEvaluatorMock = Mockito.mockStatic(
        MoveEvaluator.class)) {
      moveEvaluatorMock.when(() -> MoveEvaluator.evaluateMove(move1, mockCopy,
              mockPlayerConfiguration))
          .thenReturn(15);

      BotDecision result =
          BestMoveSelector.chooseBestMove(possibleMoves, mockBoardState,
              mockPlayerConfiguration);

      assertEquals(move1, result);
      verify(mockBoardState, times(1)).copy();
    }
  }

  @Test
  void shouldHandleNegativeScores() {

    possibleMoves.add(move1);
    possibleMoves.add(move2);
    possibleMoves.add(move3);

    BoardState mockCopy = mock(BoardState.class);
    when(mockBoardState.copy()).thenReturn(mockCopy);

    try (MockedStatic<MoveEvaluator> moveEvaluatorMock = Mockito.mockStatic(
        MoveEvaluator.class)) {
      moveEvaluatorMock.when(() -> MoveEvaluator.evaluateMove(move1, mockCopy,
              mockPlayerConfiguration))
          .thenReturn(-10);
      moveEvaluatorMock.when(() -> MoveEvaluator.evaluateMove(move2, mockCopy,
              mockPlayerConfiguration))
          .thenReturn(-5);
      moveEvaluatorMock.when(() -> MoveEvaluator.evaluateMove(move3, mockCopy,
              mockPlayerConfiguration))
          .thenReturn(-15);

      BotDecision result =
          BestMoveSelector.chooseBestMove(possibleMoves, mockBoardState,
              mockPlayerConfiguration);

      assertEquals(move2, result);
    }
  }

  @Test
  void shouldReturnFirstMoveWhenNoScoreReachesInitialMax() {

    possibleMoves.add(move1);
    possibleMoves.add(move2);
    possibleMoves.add(move3);

    BoardState mockCopy = mock(BoardState.class);
    when(mockBoardState.copy()).thenReturn(mockCopy);

    try (MockedStatic<MoveEvaluator> moveEvaluatorMock = Mockito.mockStatic(
        MoveEvaluator.class);
         MockedStatic<GameConstants> gameConstantsMock = Mockito.mockStatic(
             GameConstants.class)) {
      gameConstantsMock.when(GameConstants::getInitialSumMax)
          .thenReturn(1000);

      moveEvaluatorMock.when(() -> MoveEvaluator.evaluateMove(move1, mockCopy,
              mockPlayerConfiguration))
          .thenReturn(100);
      moveEvaluatorMock.when(() -> MoveEvaluator.evaluateMove(move2, mockCopy,
              mockPlayerConfiguration))
          .thenReturn(500);
      moveEvaluatorMock.when(() -> MoveEvaluator.evaluateMove(move3, mockCopy,
              mockPlayerConfiguration))
          .thenReturn(200);

      BotDecision result =
          BestMoveSelector.chooseBestMove(possibleMoves, mockBoardState,
              mockPlayerConfiguration);

      assertEquals(move2, result);
    }
  }

  @Test
  void shouldHandleVeryLargeScoresAboveInitialMax() {

    possibleMoves.add(move1);
    possibleMoves.add(move2);

    BoardState mockCopy = mock(BoardState.class);
    when(mockBoardState.copy()).thenReturn(mockCopy);

    try (MockedStatic<MoveEvaluator> moveEvaluatorMock = Mockito.mockStatic(
        MoveEvaluator.class);
         MockedStatic<GameConstants> gameConstantsMock = Mockito.mockStatic(
             GameConstants.class)) {

      gameConstantsMock.when(GameConstants::getInitialSumMax).thenReturn(0);

      moveEvaluatorMock.when(() -> MoveEvaluator.evaluateMove(move1, mockCopy,
              mockPlayerConfiguration))
          .thenReturn(Integer.MAX_VALUE - 1);
      moveEvaluatorMock.when(() -> MoveEvaluator.evaluateMove(move2, mockCopy,
              mockPlayerConfiguration))
          .thenReturn(Integer.MAX_VALUE);

      BotDecision result =
          BestMoveSelector.chooseBestMove(possibleMoves, mockBoardState,
              mockPlayerConfiguration);

      assertEquals(move2, result);
    }
  }

  @Test
  void shouldCreateBoardCopyForEachMove() {

    possibleMoves.add(move1);
    possibleMoves.add(move2);
    possibleMoves.add(move3);
    possibleMoves.add(move4);

    BoardState mockCopy = mock(BoardState.class);
    when(mockBoardState.copy()).thenReturn(mockCopy);

    try (MockedStatic<MoveEvaluator> moveEvaluatorMock = Mockito.mockStatic(
        MoveEvaluator.class)) {
      moveEvaluatorMock.when(
              () -> MoveEvaluator.evaluateMove(any(BotDecision.class),
                  eq(mockCopy),
                  eq(mockPlayerConfiguration)))
          .thenReturn(1);

      BestMoveSelector.chooseBestMove(possibleMoves, mockBoardState,
          mockPlayerConfiguration);

      verify(mockBoardState, times(4)).copy();
    }
  }

  @Test
  void shouldThrowExceptionWhenListEmpty() {

    assertThrows(Exception.class,
        () -> BestMoveSelector.chooseBestMove(possibleMoves, mockBoardState,
            mockPlayerConfiguration));

    verify(mockBoardState, never()).copy();
  }

  @Test
  void shouldHandleScoresEqualAndAboveInitialSumMax() {

    possibleMoves.add(move1);
    possibleMoves.add(move2);
    possibleMoves.add(move3);

    BoardState mockCopy = mock(BoardState.class);
    when(mockBoardState.copy()).thenReturn(mockCopy);

    try (MockedStatic<MoveEvaluator> moveEvaluatorMock = Mockito.mockStatic(
        MoveEvaluator.class);
         MockedStatic<GameConstants> gameConstantsMock = Mockito.mockStatic(
             GameConstants.class)) {

      gameConstantsMock.when(GameConstants::getInitialSumMax)
          .thenReturn(100);

      moveEvaluatorMock.when(() -> MoveEvaluator.evaluateMove(move1, mockCopy,
              mockPlayerConfiguration))
          .thenReturn(100);
      moveEvaluatorMock.when(() -> MoveEvaluator.evaluateMove(move2, mockCopy,
              mockPlayerConfiguration))
          .thenReturn(150);
      moveEvaluatorMock.when(() -> MoveEvaluator.evaluateMove(move3, mockCopy,
              mockPlayerConfiguration))
          .thenReturn(50);

      BotDecision result =
          BestMoveSelector.chooseBestMove(possibleMoves, mockBoardState,
              mockPlayerConfiguration);

      assertEquals(move2, result);
    }
  }

  @Test
  void shouldHandleNullInMovesList() {

    possibleMoves.add(move1);
    possibleMoves.add(null);
    possibleMoves.add(move2);

    BoardState mockCopy = mock(BoardState.class);
    when(mockBoardState.copy()).thenReturn(mockCopy);

    try (MockedStatic<MoveEvaluator> moveEvaluatorMock = Mockito.mockStatic(
        MoveEvaluator.class)) {
      moveEvaluatorMock.when(() -> MoveEvaluator.evaluateMove(move1, mockCopy,
              mockPlayerConfiguration))
          .thenReturn(5);
      moveEvaluatorMock.when(() -> MoveEvaluator.evaluateMove(null, mockCopy,
              mockPlayerConfiguration))
          .thenThrow(new NullPointerException());
      moveEvaluatorMock.when(() -> MoveEvaluator.evaluateMove(move2, mockCopy,
              mockPlayerConfiguration))
          .thenReturn(10);

      assertThrows(NullPointerException.class,
          () -> BestMoveSelector.chooseBestMove(possibleMoves, mockBoardState,
              mockPlayerConfiguration));
    }
  }

  @Test
  void shouldHandleExceptionDuringMoveEvaluation() {

    possibleMoves.add(move1);
    possibleMoves.add(move2);

    BoardState mockCopy = mock(BoardState.class);
    when(mockBoardState.copy()).thenReturn(mockCopy);

    try (MockedStatic<MoveEvaluator> moveEvaluatorMock = Mockito.mockStatic(
        MoveEvaluator.class)) {
      moveEvaluatorMock.when(() -> MoveEvaluator.evaluateMove(move1, mockCopy,
              mockPlayerConfiguration))
          .thenThrow(new RuntimeException("Evaluation Error"));
      moveEvaluatorMock.when(() -> MoveEvaluator.evaluateMove(move2, mockCopy,
              mockPlayerConfiguration))
          .thenReturn(10);

      assertThrows(RuntimeException.class,
          () -> BestMoveSelector.chooseBestMove(possibleMoves, mockBoardState,
              mockPlayerConfiguration));
    }
  }
}