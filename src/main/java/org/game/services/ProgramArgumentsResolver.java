package org.game.services;

import org.game.model.InputArguments;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ProgramArgumentsResolver {

    public Map<String, String> resolve(String[] args) {
        Map<String, String> programArguments = toMap(args);

        Arrays.stream(InputArguments.values()).forEach(inputArguments -> {
            if (!programArguments.containsKey(inputArguments.argument)) {
                throw new RuntimeException(String.format("%s argument is required", inputArguments.argument));
            }
        });

        return programArguments;
    }

    private static Map<String, String> toMap(String[] args) {
        Map<String, String> programArguments = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            programArguments.put(args[i], args[i + 1]);
        }
        return programArguments;
    }
}
