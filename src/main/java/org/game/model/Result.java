package org.game.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record Result(
        String[][] matrix,
        BigDecimal reward,
        Map<String, List<String>> appliedWinningCombinations,
        String appliedBonusSymbol
) {
}
