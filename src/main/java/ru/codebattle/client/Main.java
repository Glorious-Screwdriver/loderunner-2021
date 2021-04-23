package ru.codebattle.client;

import ru.codebattle.client.api.BoardPoint;
import ru.codebattle.client.api.GameBoard;
import ru.codebattle.client.api.LoderunnerAction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Main {
    private static final String SERVER_ADDRESS = "https://dojorena.io/codenjoy-contest/board/player/dojorena378?code=6192664095630323037";
    static LoderunnerAction action = LoderunnerAction.GO_LEFT;
    static int boardSize;

    public static void main(String[] args) throws IOException {
        ReconnectableLodeRunnerClientWrapper client = new ReconnectableLodeRunnerClientWrapper(SERVER_ADDRESS, Main::doAction);
        client.run();
        System.in.read();
        client.initiateExit();
    }

    private static LoderunnerAction doAction(GameBoard gameBoard) {
        boardSize = gameBoard.size();
        PathFinding pathFinding =new PathFinding(gameBoard.getBooleanArray());
        pathFinding.findPath(gameBoard.getMyPosition(),gameBoard.getMyPosition().shiftRight());
        return LoderunnerAction.SUICIDE;
    }
}

