package org.game;

import org.game.model.Config;
import org.game.model.Result;
import org.game.services.*;

import java.math.BigDecimal;
import java.util.Map;

import static org.game.model.InputArguments.BETTING_AMOUNT;
import static org.game.model.InputArguments.CONFIGURATION_FILE;

public class BettingGameApplication {
    public static void main(String[] args) {
        try {
            Map<String, String> programArguments = new ProgramArgumentsResolver().resolve(args);
            Config config = new ConfigDeserializer().deserialize(programArguments.get(CONFIGURATION_FILE.argument));
            MatrixGenerator matrixGenerator = new MatrixGenerator(config);
            RewardCalculator rewardCalculator = new RewardCalculator(config);

            String bettingAmount = programArguments.get(BETTING_AMOUNT.argument);
            System.out.printf("Betting %s $$$ %n", bettingAmount);
            BettingGame bettingGame = new BettingGame(config, matrixGenerator, rewardCalculator);

            Result result = bettingGame.play(new BigDecimal(bettingAmount));

            String outputJson = new ResultSerializer().serialize(result);
            System.out.println(outputJson);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

}