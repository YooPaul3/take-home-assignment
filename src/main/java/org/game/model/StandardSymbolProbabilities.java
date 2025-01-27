package org.game.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record StandardSymbolProbabilities(
        Integer column,
        Integer row,
        @JsonProperty("symbols") Map<String, Integer> standardProbabilities
) {
}
