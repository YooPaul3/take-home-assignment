package org.game.model;

public enum InputArguments {
    BETTING_AMOUNT("--betting-amount"),
    CONFIGURATION_FILE("--config");


    public final String argument;

    InputArguments(String argument) {
        this.argument = argument;
    }
}
