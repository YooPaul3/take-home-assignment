package org.game.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record Symbol(
        SymbolType type,
        @JsonProperty("reward_multiplier") BigDecimal rewardMultiplier,
        BigDecimal extra,
        BonusImpact impact
) {
}
