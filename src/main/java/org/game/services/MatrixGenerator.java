package org.game.services;

import org.game.model.Config;
import org.game.model.Symbol;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.game.model.SymbolType.BONUS;

public class MatrixGenerator {

    private final Config config;

    public MatrixGenerator(Config config) {
        this.config = config;
    }

    public String[][] generate() {
        String[][] matrix = new String[config.columns()][config.rows()];
        AtomicReference<Boolean> bonusSymbolGenerated = new AtomicReference<>(false);

        config.probabilities().standardSymbols().forEach(standardSymbolProbabilities -> {
            Map<String, Integer> symbols = new HashMap<>(standardSymbolProbabilities.standardProbabilities());
            if (!bonusSymbolGenerated.get()) {
                symbols.putAll(config.probabilities().bonusSymbols().bonusProbabilities());
            }
            String randomSymbol = selectRandomSymbol(symbols);
            matrix[standardSymbolProbabilities.row()][standardSymbolProbabilities.column()] = randomSymbol;
            if (isSelectedSymbolBonus(randomSymbol, config.symbols())) {
                bonusSymbolGenerated.set(true);
            }
        });

        return matrix;
    }

    private boolean isSelectedSymbolBonus(String randomSymbol, Map<String, Symbol> symbols) {
        return BONUS == symbols.get(randomSymbol).type();
    }

    private String selectRandomSymbol(Map<String, Integer> symbols) {
        int totalWeight = symbols.values().stream().mapToInt(Integer::intValue).sum();
        int randomValue = new Random().nextInt(totalWeight) + 1;
        AtomicInteger cumulativeSum = new AtomicInteger();

        return symbols.entrySet().stream()
                .takeWhile(entry -> cumulativeSum.addAndGet(entry.getValue()) < randomValue)
                .map(Map.Entry::getKey)
                .reduce(symbols.keySet().iterator().next(), (first, second) -> second);
    }
}
