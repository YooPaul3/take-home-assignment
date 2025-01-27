package org.game.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.game.testdata.ConfigTestData.config;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RewardCalculatorTest {

    private final BigDecimal betAmount = BigDecimal.valueOf(100);

    private RewardCalculator rewardCalculator;

    @BeforeEach
    void setUp() {
        rewardCalculator = new RewardCalculator(config);
    }

    @Test
    void shouldCalculateRewardForAppliedCombinationsWithExtraBonus() {
        Map<String, List<String>> appliedWinningCombinations = Map.of(
                "A", List.of("same_symbol_5_times", "same_symbols_vertically"),
                "B", List.of("same_symbol_3_times", "same_symbols_vertically")
        );

        assertEquals(BigDecimal.valueOf(3600), rewardCalculator.calculate(betAmount, appliedWinningCombinations, "+1000"));
    }

    @Test
    void shouldCalculateRewardForAppliedCombinationsWithMultiplierBonus() {
        Map<String, List<String>> appliedWinningCombinations = Map.of(
                "C", List.of("same_symbol_3_times", "same_symbols_horizontally"),
                "D", List.of("same_symbol_6_times", "same_symbols_horizontally", "same_symbols_horizontally")
        );

        assertEquals(BigDecimal.valueOf(29000d), rewardCalculator.calculate(betAmount, appliedWinningCombinations, "10x"));
    }

    @Test
    void shouldCalculateRewardForAppliedCombinationsWithoutBonus() {
        Map<String, List<String>> appliedWinningCombinations = Map.of(
                "E", List.of("same_symbol_3_times", "same_symbols_diagonally_left_to_right"),
                "F", List.of("same_symbol_4_times")
        );
        assertEquals(BigDecimal.valueOf(750d), rewardCalculator.calculate(betAmount, appliedWinningCombinations, null));
    }

    @Test
    void shouldReturnZeroWhenAppliedWinningCombinationsMissing() {
        Map<String, List<String>> appliedWinningCombinations = Map.of();
        assertEquals(BigDecimal.ZERO, rewardCalculator.calculate(betAmount, appliedWinningCombinations, null));
    }

    @Test
    void shouldReturnZeroWhenAppliedWinningCombinationsMissingAndBonusApplied() {
        Map<String, List<String>> appliedWinningCombinations = Map.of();
        assertEquals(BigDecimal.ZERO, rewardCalculator.calculate(betAmount, appliedWinningCombinations, "+500"));
    }

}