package ru.codebattle.client;

import ru.codebattle.client.api.BoardElement;
import ru.codebattle.client.api.BoardPoint;
import ru.codebattle.client.api.GameBoard;
import ru.codebattle.client.api.LoderunnerAction;

import java.util.List;
import java.util.Optional;

public class Steps {
    GameBoard gameBoard;
    BoardPoint position;
    int[][] weightArray;

    public Steps(GameBoard gameBoard) {
        this.gameBoard = gameBoard;

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
        int minDistance = 300;

        AStar aStar = new AStar(weightArray, position.getX(), position.getY(), false);
        List<AStar.Node> path = aStar.findPathTo(position.getX() + 1, position.getY());

        for (BoardPoint gold : gameBoard.getGoldPositions()) {
            BoardElement elementUnderGold = gameBoard.getElementAt(gold.shiftBottom());
            if (elementUnderGold.equals(BoardElement.BRICK) ||
                    elementUnderGold.equals(BoardElement.UNDESTROYABLE_WALL) ||
                    elementUnderGold.equals(BoardElement.LADDER)) {
                aStar = new AStar(weightArray, position.getX(), position.getY(), false);
                List<AStar.Node> tmp = aStar.findPathTo(gold.getX(), gold.getY());
                int distance = 300;
                if (tmp != null) distance = tmp.size();
                if (minDistance > distance) {
                    minDistance = distance;
                    path = tmp;
                }
            }
        }

        LoderunnerAction action;
        if (path != null && !path.isEmpty()) {
            AStar.Node nextNode = path.get(1);
            if (position.getX() == nextNode.x) {
                action = position.getY() > nextNode.y ? LoderunnerAction.GO_UP : LoderunnerAction.GO_DOWN;
            } else {
                action = position.getX() > nextNode.x ? LoderunnerAction.GO_LEFT : LoderunnerAction.GO_RIGHT;
            }
        } else {
            action = LoderunnerAction.DO_NOTHING;
        }

        return Optional.of(action);
    }
}
