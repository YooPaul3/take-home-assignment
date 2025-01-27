package org.game.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Probabilities(
        @JsonProperty("standard_symbols") List<StandardSymbolProbabilities> standardSymbols,
        @JsonProperty("bonus_symbols") BonusSymbolProbabilities bonusSymbols
) {
}
