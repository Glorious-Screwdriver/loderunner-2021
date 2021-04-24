package ru.codebattle.client;

import ru.codebattle.client.api.BoardElement;
import ru.codebattle.client.api.BoardPoint;
import ru.codebattle.client.api.GameBoard;
import ru.codebattle.client.api.LoderunnerAction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Main {
    private static final String SERVER_ADDRESS = "https://dojorena.io/codenjoy-contest/board/player/dojorena378?code=6192664095630323037";

    public static void main(String[] args) throws IOException {
        ReconnectableLodeRunnerClientWrapper client = new ReconnectableLodeRunnerClientWrapper(SERVER_ADDRESS, Main::doAction);
        client.run();
        System.in.read();
        client.initiateExit();
    }

    private static LoderunnerAction doAction(GameBoard gameBoard) {
        Steps steps = new Steps(gameBoard);

        ArrayList<Optional<LoderunnerAction>> stepList = new ArrayList<>(Arrays.asList(
                steps.avoidEnemies(),
                steps.avoidPlayers(),
                steps.findGold()
        ));

        for (Optional<LoderunnerAction> step : stepList) {
            if (step.isPresent())
                return step.get();
        }

        return LoderunnerAction.DO_NOTHING;
//        throw new UnsupportedOperationException();
    }
}

