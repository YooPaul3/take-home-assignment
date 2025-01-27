package org.game.services;

import org.game.model.Config;
import org.game.model.Symbol;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;

public class RewardCalculator {

    private final Config config;

    public RewardCalculator(Config config) {
        this.config = config;
    }

    public BigDecimal calculate(BigDecimal betAmount, Map<String, List<String>> winningSymbols, String appliedBonus) {
        BigDecimal winningMultiplier = winningSymbols.entrySet().stream()
                .map(entry -> betAmount.multiply(winningCombinationsMultiplierForSymbol(entry.getKey(), entry.getValue())))
                .reduce(ZERO, BigDecimal::add);

        return applyBonus(winningMultiplier, appliedBonus);
    }

    private BigDecimal winningCombinationsMultiplierForSymbol(String symbol, List<String> appliedCombinations) {
        BigDecimal winningCombinationsMultiplier = appliedCombinations.stream()
                .map(winningCombination -> config.winCombinations().get(winningCombination).rewardMultiplier())
                .reduce(ONE, BigDecimal::multiply);
        return config.symbols().get(symbol).rewardMultiplier().multiply(winningCombinationsMultiplier);
    }

    private BigDecimal applyBonus(BigDecimal winningMultiplier, String appliedBonus) {
        Symbol bonus = config.symbols().get(appliedBonus);

        if (bonus == null) {
            return winningMultiplier;
        }

        if (winningMultiplier.compareTo(ZERO) == 0) {
            return winningMultiplier;
        }

        switch (bonus.impact()) {
            case EXTRA_BONUS -> {
                return winningMultiplier.add(bonus.extra());
            }
            case MULTIPLY_REWARD -> {
                return winningMultiplier.multiply(bonus.rewardMultiplier());
            }
        }

        return winningMultiplier;
    }
}
