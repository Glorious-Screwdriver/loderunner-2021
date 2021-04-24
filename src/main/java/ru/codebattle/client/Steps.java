package ru.codebattle.client;

import ru.codebattle.client.api.BoardPoint;
import ru.codebattle.client.api.GameBoard;
import ru.codebattle.client.api.LoderunnerAction;

import java.util.List;
import java.util.Optional;

public class Steps {
    GameBoard gameBoard;

    int size;
    BoardPoint me;
    int[][] weightArray;

    public Steps(GameBoard gameBoard) {
        this.gameBoard = gameBoard;

        size = gameBoard.size();
        me = gameBoard.getMyPosition();
        weightArray = gameBoard.getWeightArray();
    }

    private LoderunnerAction interpret(List<AStar.Node> path) {
        //TODO Получение LoderunnerAction из List<Node>
        throw new UnsupportedOperationException();
    }

    public Optional<LoderunnerAction> avoidEnemies() {
        throw new UnsupportedOperationException();
    }

    public Optional<LoderunnerAction> avoidPlayers() {
        throw new UnsupportedOperationException();
    }

    public Optional<LoderunnerAction> findGold() {
        throw new UnsupportedOperationException();
    }
}
