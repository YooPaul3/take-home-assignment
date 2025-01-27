package org.game.testdata;

import org.game.model.*;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.game.model.BonusImpact.EXTRA_BONUS;
import static org.game.model.BonusImpact.MULTIPLY_REWARD;
import static org.game.model.SymbolType.BONUS;
import static org.game.model.SymbolType.STANDARD;
import static org.game.model.WinGroup.*;

public class ConfigTestData {

    public static Config config = new Config(
            3,
            3,
            availableSymbols(),
            availableProbabilities(),
            availableWinCombinations()
    );

    private static Map<String, Symbol> availableSymbols() {
        Map<String, Symbol> symbols = new LinkedHashMap<>();
        symbols.put("A", new Symbol(STANDARD, BigDecimal.valueOf(5), null, null));
        symbols.put("B", new Symbol(STANDARD, BigDecimal.valueOf(3), null, null));
        symbols.put("C", new Symbol(STANDARD, BigDecimal.valueOf(2.5), null, null));
        symbols.put("D", new Symbol(STANDARD, BigDecimal.valueOf(2), null, null));
        symbols.put("E", new Symbol(STANDARD, BigDecimal.valueOf(1.2), null, null));
        symbols.put("F", new Symbol(STANDARD, BigDecimal.valueOf(1), null, null));
        symbols.put("10x", new Symbol(BONUS, BigDecimal.valueOf(10), null, MULTIPLY_REWARD));
        symbols.put("5x", new Symbol(BONUS, BigDecimal.valueOf(5), null, MULTIPLY_REWARD));
        symbols.put("+1000", new Symbol(BONUS, null, BigDecimal.valueOf(1000), EXTRA_BONUS));
        symbols.put("+500", new Symbol(BONUS, null, BigDecimal.valueOf(500), EXTRA_BONUS));
        symbols.put("MISS", new Symbol(BONUS, null, null, BonusImpact.MISS));
        return symbols;
    }

    private static Probabilities availableProbabilities() {
        return new Probabilities(
                List.of(
                        new StandardSymbolProbabilities(0, 0, availableStandardSymbolProbabilities()),
                        new StandardSymbolProbabilities(0, 1, availableStandardSymbolProbabilities()),
                        new StandardSymbolProbabilities(0, 2, availableStandardSymbolProbabilities()),
                        new StandardSymbolProbabilities(1, 0, availableStandardSymbolProbabilities()),
                        new StandardSymbolProbabilities(1, 1, availableStandardSymbolProbabilities()),
                        new StandardSymbolProbabilities(1, 2, availableStandardSymbolProbabilities()),
                        new StandardSymbolProbabilities(2, 0, availableStandardSymbolProbabilities()),
                        new StandardSymbolProbabilities(2, 1, availableStandardSymbolProbabilities()),
                        new StandardSymbolProbabilities(2, 2, availableStandardSymbolProbabilities())
                ),
                new BonusSymbolProbabilities(availableBonusSymbolProbabilities())
        );
    }

    private static Map<String, Integer> availableStandardSymbolProbabilities() {
        Map<String, Integer> symbolsProbabilities = new LinkedHashMap<>();

        symbolsProbabilities.put("A", 1);
        symbolsProbabilities.put("B", 2);
        symbolsProbabilities.put("C", 3);
        symbolsProbabilities.put("D", 4);
        symbolsProbabilities.put("E", 5);
        symbolsProbabilities.put("F", 6);

        return symbolsProbabilities;
    }

    private static Map<String, Integer> availableBonusSymbolProbabilities() {
        Map<String, Integer> symbolsProbabilities = new LinkedHashMap<>();

        symbolsProbabilities.put("10x", 1);
        symbolsProbabilities.put("5x", 2);
        symbolsProbabilities.put("+1000", 3);
        symbolsProbabilities.put("+500", 4);
        symbolsProbabilities.put("MISS", 5);

        return symbolsProbabilities;
    }

    private static Map<String, WinCombination> availableWinCombinations() {
        Map<String, WinCombination> winCombinations = new LinkedHashMap<>();
        winCombinations.put("same_symbol_3_times", new WinCombination(BigDecimal.valueOf(1), WinWhen.SAME_SYMBOLS, 3, SAME_SYMBOLS, null));
        winCombinations.put("same_symbol_4_times", new WinCombination(BigDecimal.valueOf(1.5), WinWhen.SAME_SYMBOLS, 4, SAME_SYMBOLS, null));
        winCombinations.put("same_symbol_5_times", new WinCombination(BigDecimal.valueOf(2), WinWhen.SAME_SYMBOLS, 5, SAME_SYMBOLS, null));
        winCombinations.put("same_symbol_6_times", new WinCombination(BigDecimal.valueOf(3), WinWhen.SAME_SYMBOLS, 6, SAME_SYMBOLS, null));
        winCombinations.put("same_symbol_7_times", new WinCombination(BigDecimal.valueOf(5), WinWhen.SAME_SYMBOLS, 7, SAME_SYMBOLS, null));
        winCombinations.put("same_symbol_8_times", new WinCombination(BigDecimal.valueOf(10), WinWhen.SAME_SYMBOLS, 8, SAME_SYMBOLS, null));
        winCombinations.put("same_symbol_9_times", new WinCombination(BigDecimal.valueOf(20), WinWhen.SAME_SYMBOLS, 9, SAME_SYMBOLS, null));
        winCombinations.put("same_symbols_horizontally", new WinCombination(BigDecimal.valueOf(2), WinWhen.LINEAR_SYMBOLS, null, HORIZONTALLY_LINEAR_SYMBOLS, List.of(
                List.of("0:0", "0:1", "0:2"), List.of("1:0", "1:1", "1:2"), List.of("2:0", "2:1", "2:2")
        )));
        winCombinations.put("same_symbols_vertically", new WinCombination(BigDecimal.valueOf(2), WinWhen.LINEAR_SYMBOLS, null, VERTICALLY_LINEAR_SYMBOLS, List.of(
                List.of("0:0", "1:0", "2:0"), List.of("0:1", "1:1", "2:1"), List.of("0:2", "1:2", "2:2")
        )));
        winCombinations.put("same_symbols_diagonally_left_to_right", new WinCombination(BigDecimal.valueOf(5), WinWhen.LINEAR_SYMBOLS, null, LTR_DIAGONALLY_LINEAR_SYMBOLS, List.of(
                List.of("0:0", "1:1", "2:2")
        )));
        winCombinations.put("same_symbols_diagonally_right_to_left", new WinCombination(BigDecimal.valueOf(5), WinWhen.LINEAR_SYMBOLS, null, RTL_DIAGONALLY_LINEAR_SYMBOLS, List.of(
                List.of("0:2", "1:1", "2:0")
        )));
        return winCombinations;
    }
}
