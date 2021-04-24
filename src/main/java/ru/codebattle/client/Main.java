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

        if(previous.equals(position)){
            stopCount++;
        }else{
            stopCount=0;
        }
        previous = position;

        AStar astar = new AStar(weightArray, position.getX(), position.getY(), false);


        int minDistance = 300;

        List<AStar.Node> path = astar.findPathTo(position.getX()+1, position.getY());;

        for(BoardPoint gold:gameBoard.getGoldPositions()){
            if(gameBoard.getElementAt(gold.shiftBottom()).equals(BoardElement.BRICK)||
                    gameBoard.getElementAt(gold.shiftBottom()).equals(BoardElement.UNDESTROYABLE_WALL)||
                    gameBoard.getElementAt(gold.shiftBottom()).equals(BoardElement.LADDER)){
                astar = new AStar(weightArray, position.getX(), position.getY(), false);
                List<AStar.Node> tmp = astar.findPathTo(gold.getX(), gold.getY());
                int distance = 300;
                if(tmp!=null){
                    distance = tmp.size();
                }
                if(minDistance > distance) {
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

        if (stopCount>25){
            action = LoderunnerAction.SUICIDE;
        }
        return action;

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

