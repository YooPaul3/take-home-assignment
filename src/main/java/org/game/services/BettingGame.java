package org.game.services;

import org.game.model.BonusImpact;
import org.game.model.Config;
import org.game.model.Result;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.game.model.SymbolType.BONUS;
import static org.game.model.SymbolType.STANDARD;
import static org.game.model.WinWhen.LINEAR_SYMBOLS;
import static org.game.model.WinWhen.SAME_SYMBOLS;

public class BettingGame {

    private final Config config;
    private final MatrixGenerator matrixGenerator;
    private final RewardCalculator rewardCalculator;

    public BettingGame(Config config, MatrixGenerator matrixGenerator, RewardCalculator rewardCalculator) {
        this.config = config;
        this.matrixGenerator = matrixGenerator;
        this.rewardCalculator = rewardCalculator;
    }

    public Result play(BigDecimal betAmount) {
        String[][] matrix = matrixGenerator.generate();
        Map<String, List<String>> winningSymbols = new HashMap<>();
        AtomicReference<String> generatedBonus = new AtomicReference<>();

        config.symbols().forEach((symbolName, symbol) -> {
            if (STANDARD == symbol.type()) {
                List<String> appliedWinningCombinations = applyWinningCombinations(matrix, symbolName);
                if (!appliedWinningCombinations.isEmpty()) {
                    winningSymbols.put(symbolName, appliedWinningCombinations);
                }
            }
            if (BONUS == symbol.type() && countOccurrences(matrix, symbolName) == 1) {
                generatedBonus.set(symbolName);
            }
        });

        BigDecimal reward = rewardCalculator.calculate(betAmount, winningSymbols, generatedBonus.get());
        String appliedBonus = filterAppliedBonus(generatedBonus.get(), winningSymbols.isEmpty());

        return new Result(
                matrix,
                reward,
                winningSymbols,
                appliedBonus
        );
    }

    private List<String> applyWinningCombinations(String[][] matrix, String symbol) {
        List<String> winningCombinations = new ArrayList<>();
        Integer symbolOccurrences = countOccurrences(matrix, symbol);

        config.winCombinations().forEach((winCombinationName, winCombination) -> {
            if (SAME_SYMBOLS == winCombination.when() && symbolOccurrences.equals(winCombination.count())) {
                winningCombinations.add(winCombinationName);
            }
            if (LINEAR_SYMBOLS == winCombination.when()) {
                winCombination.coveredAreas().forEach(coveredArea -> {
                    if (isAreaCoveredBySymbol(matrix, coveredArea, symbol)) {
                        winningCombinations.add(winCombinationName);
                    }
                });
            }
        });

        return winningCombinations;
    }

    private boolean isAreaCoveredBySymbol(String[][] matrix, List<String> coveredArea, String symbol) {
        return coveredArea.stream().allMatch(mergedIndex -> {
            String[] index = mergedIndex.split(":");
            return matrix[Integer.parseInt(index[0])][Integer.parseInt(index[1])].equals(symbol);
        });

    }

    private Integer countOccurrences(String[][] matrix, String symbol) {
        AtomicInteger count = new AtomicInteger();
        Arrays.stream(matrix).forEach(row -> Arrays.stream(row).forEach(element -> {
            if (symbol.equals(element)) {
                count.getAndIncrement();
            }
        }));
        return count.get();
    }

    private String filterAppliedBonus(String generatedBonus, boolean isWinning) {
        if (BonusImpact.MISS.name().equalsIgnoreCase(generatedBonus) || isWinning) {
            return null;
        }
        return generatedBonus;
    }
}
