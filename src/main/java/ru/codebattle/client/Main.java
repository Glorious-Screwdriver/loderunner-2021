package ru.codebattle.client;

import ru.codebattle.client.api.BoardPoint;
import ru.codebattle.client.api.GameBoard;
import ru.codebattle.client.api.LoderunnerAction;

import java.io.IOException;

import java.util.*;

public class Main {
    private static final String SERVER_ADDRESS = "https://dojorena.io/codenjoy-contest/board/player/dojorena378?code=6192664095630323037";
//    static LoderunnerAction action = LoderunnerAction.GO_LEFT;
//    static int boardSize;

    public static void main(String[] args) throws IOException {
        ReconnectableLodeRunnerClientWrapper client = new ReconnectableLodeRunnerClientWrapper(SERVER_ADDRESS, Main::doAction);
        client.run();
        System.in.read();
        client.initiateExit();
    }

    private static LoderunnerAction doAction(GameBoard gameBoard) {
/*
        boardSize = gameBoard.size();
        BoardPoint position = gameBoard.getMyPosition();
        int[][] weightArray = gameBoard.getWeightArray();
        AStar astar = new AStar(weightArray, position.getX(), position.getY(), false);
        List<Node> path = astar.findPathTo(20, 20);

        if (path != null) {
            path.forEach((n) -> {
                System.out.print("[" + n.x + ", " + n.y + "] ");
                weightArray[n.y][n.x] = -1;
            });
            System.out.printf("\nTotal cost: %.02f\n", path.get(path.size() - 1).g);

            for (int[] maze_row : weightArray) {
                for (int maze_entry : maze_row) {
                    switch (maze_entry) {
                        case 0:
                            System.out.print("_");
                            break;
                        case -1:
                            System.out.print("*");
                            break;
                        default:
                            System.out.print("#");
                    }
                }
                System.out.println();
            }
        }
*/
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

