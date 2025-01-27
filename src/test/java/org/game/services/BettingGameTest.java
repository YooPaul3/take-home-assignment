package org.game.services;

import org.game.model.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.game.testdata.ConfigTestData.config;
import static org.junit.jupiter.api.Assertions.*;

class BettingGameTest {

    @Mock
    private MatrixGenerator matrixGenerator;

    @Mock
    private RewardCalculator rewardCalculator;

    private final BigDecimal betAmount = BigDecimal.valueOf(100);

    private BettingGame bettingGame;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bettingGame = new BettingGame(config, matrixGenerator, rewardCalculator);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("oneWinningCombinationProvider")
    void shouldWinWithOneWinningCombination(String winCombination, String[][] matrix, String symbol) {
        Mockito.when(matrixGenerator.generate()).thenReturn(matrix);

        Result result = bettingGame.play(betAmount);

        assertTrue(compareMatrix(matrix, result.matrix()));
        assertTrue(result.appliedWinningCombinations().get(symbol).contains(winCombination));
    }

    private static Stream<Arguments> oneWinningCombinationProvider() {
        return Stream.of(
                Arguments.of("same_symbol_3_times", new String[][]{{"A", "X", "X"}, {"X", "X", "X"}, {"A", "A", "X"}}, "A"),
                Arguments.of("same_symbol_4_times", new String[][]{{"A", "A", "X"}, {"X", "X", "X"}, {"A", "A", "X"}}, "A"),
                Arguments.of("same_symbol_5_times", new String[][]{{"A", "A", "X"}, {"X", "A", "X"}, {"A", "A", "X"}}, "A"),
                Arguments.of("same_symbol_6_times", new String[][]{{"A", "A", "X"}, {"X", "A", "A"}, {"A", "A", "X"}}, "A"),
                Arguments.of("same_symbol_7_times", new String[][]{{"A", "A", "A"}, {"X", "A", "A"}, {"A", "A", "X"}}, "A"),
                Arguments.of("same_symbol_8_times", new String[][]{{"A", "A", "A"}, {"A", "A", "A"}, {"A", "A", "X"}}, "A"),
                Arguments.of("same_symbol_9_times", new String[][]{{"A", "A", "A"}, {"A", "A", "A"}, {"A", "A", "A"}}, "A"),
                Arguments.of("same_symbols_horizontally", new String[][]{{"B", "B", "B"}, {"X", "X", "X"}, {"X", "X", "X"}}, "B"),
                Arguments.of("same_symbols_horizontally", new String[][]{{"X", "X", "X"}, {"B", "B", "B"}, {"X", "X", "X"}}, "B"),
                Arguments.of("same_symbols_horizontally", new String[][]{{"X", "X", "X"}, {"X", "X", "X"}, {"B", "B", "B"}}, "B"),
                Arguments.of("same_symbols_vertically", new String[][]{{"C", "X", "X"}, {"C", "X", "X"}, {"C", "X", "X"}}, "C"),
                Arguments.of("same_symbols_vertically", new String[][]{{"X", "C", "X"}, {"X", "C", "X"}, {"X", "C", "X"}}, "C"),
                Arguments.of("same_symbols_vertically", new String[][]{{"X", "X", "C"}, {"X", "X", "C"}, {"X", "X", "C"}}, "C"),
                Arguments.of("same_symbols_diagonally_left_to_right", new String[][]{{"D", "X", "X"}, {"X", "D", "X"}, {"X", "X", "D"}}, "D"),
                Arguments.of("same_symbols_diagonally_right_to_left", new String[][]{{"X", "X", "E"}, {"X", "E", "X"}, {"E", "X", "X"}}, "E")
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("appliedBonusProvider")
    void appliedBonus(String appliedBonus, String[][] matrix) {
        Mockito.when(matrixGenerator.generate()).thenReturn(matrix);

        Result result = bettingGame.play(betAmount);

        assertTrue(compareMatrix(matrix, result.matrix()));
        assertEquals(appliedBonus, result.appliedBonusSymbol());
    }

    private static Stream<Arguments> appliedBonusProvider() {
        return Stream.of(
                Arguments.of(Named.of("displayed when winning 10x", "10x"), new String[][]{{"10x", "X", "X"}, {"X", "X", "X"}, {"A", "A", "A"}}),
                Arguments.of(Named.of("displayed when winning 5x", "5x"), new String[][]{{"X", "X", "X"}, {"X", "5x", "X"}, {"A", "A", "A"}}),
                Arguments.of(Named.of("displayed when winning +1000", "+1000"), new String[][]{{"X", "X", "X"}, {"X", "X", "A"}, {"A", "A", "+1000"}}),
                Arguments.of(Named.of("displayed when winning +500", "+500"), new String[][]{{"X", "+500", "X"}, {"X", "X", "X"}, {"A", "A", "A"}}),
                Arguments.of(Named.of("not displayed when MISS", null), new String[][]{{"MISS", "X", "X"}, {"X", "X", "X"}, {"A", "A", "A"}}),
                Arguments.of(Named.of("not displayed when not winning", null), new String[][]{{"10x", "X", "X"}, {"X", "X", "X"}, {"X", "A", "A"}}),
                Arguments.of(Named.of("not displayed when null", null), new String[][]{{"X", "X", "X"}, {"X", "X", "X"}, {"A", "A", "A"}})
        );
    }

    @Test
    void shouldWinWithFullMatrixAndNoBonus() {
        Mockito.when(matrixGenerator.generate()).thenReturn(
                new String[][]{{"F", "F", "F"}, {"F", "F", "F"}, {"F", "F", "F"}}
        );

        Result result = bettingGame.play(betAmount);

        List<String> expectedWinningCombinations = List.of(
                "same_symbol_9_times",
                "same_symbols_horizontally",
                "same_symbols_horizontally",
                "same_symbols_horizontally",
                "same_symbols_vertically",
                "same_symbols_vertically",
                "same_symbols_vertically",
                "same_symbols_diagonally_left_to_right",
                "same_symbols_diagonally_right_to_left"
        );

        assertEquals(expectedWinningCombinations.size(), result.appliedWinningCombinations().get("F").size());
        assertIterableEquals(expectedWinningCombinations, result.appliedWinningCombinations().get("F"));

    }

    private boolean compareMatrix(String[][] matrix1, String[][] matrix2) {
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1[i].length; j++) {
                if (!matrix1[i][j].equals(matrix2[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }
}
