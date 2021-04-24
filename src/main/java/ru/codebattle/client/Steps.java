package ru.codebattle.client;

import ru.codebattle.client.api.BoardElement;
import ru.codebattle.client.api.BoardPoint;
import ru.codebattle.client.api.GameBoard;
import ru.codebattle.client.api.LoderunnerAction;

import java.util.List;
import java.util.Optional;

public class Steps {
    GameBoard gameBoard;

    int size;
    BoardPoint position;
    int[][] weightArray;

    public Steps(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        size=gameBoard.size();
        position = gameBoard.getMyPosition();
        weightArray = gameBoard.getWeightArray();
    }

    public Optional<LoderunnerAction> avoidEnemies() {
        return Optional.empty();
    }

    public Optional<LoderunnerAction> avoidPlayers() {
        return Optional.empty();
    }

    public Optional<LoderunnerAction> findGold() {
        LoderunnerAction action;
        AStar astar = new AStar(weightArray, position.getX(), position.getY(), false);
        int minDistance = size;
        List<AStar.Node> path = astar.findPathTo(position.getX() + 1, position.getY());

        for (BoardPoint gold : gameBoard.getGoldPositions()) {
            if (gameBoard.getElementAt(gold.shiftBottom()).equals(BoardElement.BRICK) ||
                    gameBoard.getElementAt(gold.shiftBottom()).equals(BoardElement.UNDESTROYABLE_WALL) ||
                    gameBoard.getElementAt(gold.shiftBottom()).equals(BoardElement.LADDER)) {
                astar = new AStar(weightArray, position.getX(), position.getY(), false);
                List<AStar.Node> tmp = astar.findPathTo(gold.getX(), gold.getY());
                int distance = size*2;
                if (tmp != null) {
                    distance = tmp.size();
                }
                if (minDistance > distance) {
                    minDistance = distance;
                    path = tmp;
                }
            }
        }

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

        return Optional.of(action);
    }
}
