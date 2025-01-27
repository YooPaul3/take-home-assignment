package org.game.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

public record WinCombination(
        @JsonProperty("reward_multiplier") BigDecimal rewardMultiplier,
        WinWhen when,
        Integer count,
        WinGroup group,
        @JsonProperty("covered_areas") List<List<String>> coveredAreas
) {
}
