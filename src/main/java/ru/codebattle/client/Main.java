package ru.codebattle.client;

import ru.codebattle.client.api.BoardPoint;
import ru.codebattle.client.api.GameBoard;
import ru.codebattle.client.api.LoderunnerAction;

import java.io.IOException;
import java.util.List;

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
        BoardPoint position = gameBoard.getMyPosition();
        int[][] weightArray = gameBoard.getWeightArray();
        AStar astar = new AStar(weightArray, position.getX(), position.getY(), false);
        List<AStar.Node> path = astar.findPathTo(28, 28);
        if (path != null && !path.isEmpty()) {
            AStar.Node nextNode = path.get(1);
            if (position.getX() == nextNode.x) {
                if (position.getY() > nextNode.y) {
                    action = LoderunnerAction.GO_UP;
                } else {
                    action = LoderunnerAction.GO_DOWN;
                }
            } else {
                if (position.getX() > nextNode.x) {
                    action = LoderunnerAction.GO_LEFT;
                } else {
                    action = LoderunnerAction.GO_RIGHT;
                }
            }
        } else {
            action = LoderunnerAction.DO_NOTHING;
        }

//        if (path != null) {
//            path.forEach((n) -> {
//                System.out.print("[" + n.x + ", " + n.y + "] ");
//                weightArray[n.y][n.x] = -1;
//            });
//            System.out.printf("\nTotal cost: %.02f\n", path.get(path.size() - 1).g);
//
//            for (int[] maze_row : weightArray) {
//                for (int maze_entry : maze_row) {
//                    switch (maze_entry) {
//                        case 0:
//                            System.out.print("_");
//                            break;
//                        case -1:
//                            System.out.print("*");
//                            break;
//                        default:
//                            System.out.print("#");
//                    }
//                }
//                System.out.println();
//            }
//        }
        return action;
    }
}

