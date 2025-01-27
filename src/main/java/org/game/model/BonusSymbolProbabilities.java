package org.game.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record BonusSymbolProbabilities(
        @JsonProperty("symbols") Map<String, Integer> bonusProbabilities
) {
}
