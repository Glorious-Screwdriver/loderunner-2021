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
    static BoardPoint pPos = new BoardPoint(-1, -1);
    static int repeat = 0;
    static int repeatTresh = 10;
    static boolean retreat = false;

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
                steps.findGold()
        ));

        for (Optional<LoderunnerAction> step : stepList) {
            if (step.isPresent())
                return step.get();
        }

        throw new RuntimeException("Не выполнился ни один шаг");
    }

    public static class Steps {
        GameBoard gameBoard;
        BoardPoint position;

        int[][] weightArray;

        public Steps(GameBoard gameBoard) {
            this.gameBoard = gameBoard;

            position = gameBoard.getMyPosition();
            weightArray = gameBoard.getWeightArray();
        }

        public Optional<LoderunnerAction> avoidEnemies() {
            BoardPoint posL = position.shiftLeft().shiftBottom();
            BoardPoint posLL = position.shiftLeft().shiftLeft();
            BoardElement elementL = gameBoard.getElementAt(posL);
            BoardElement elementLL = gameBoard.getElementAt(posLL);
            if (elementL.equals(BoardElement.BRICK) && (
                    elementLL.equals(BoardElement.ENEMY_RIGHT) ||
                    elementLL.equals(BoardElement.ENEMY_PIPE_RIGHT) ||
                    elementLL.equals(BoardElement.ENEMY_LADDER) ||
                    elementLL.equals(BoardElement.OTHER_HERO_RIGHT) ||
                    elementLL.equals(BoardElement.OTHER_HERO_LEFT)))
            return Optional.of(LoderunnerAction.DRILL_LEFT);

            BoardPoint posR = position.shiftRight().shiftBottom();
            BoardPoint posRR = position.shiftRight().shiftRight();
            BoardElement elementR = gameBoard.getElementAt(posR);
            BoardElement elementRR = gameBoard.getElementAt(posRR);
            if (elementR.equals(BoardElement.BRICK) &&(
                    elementRR.equals(BoardElement.ENEMY_LEFT) ||
                    elementRR.equals(BoardElement.ENEMY_PIPE_LEFT) ||
                    elementRR.equals(BoardElement.ENEMY_LADDER) ||
                    elementRR.equals(BoardElement.OTHER_HERO_RIGHT) ||
                    elementRR.equals(BoardElement.OTHER_HERO_LEFT)))
                return Optional.of(LoderunnerAction.DRILL_RIGHT);
            return Optional.empty();
        }

        public Optional<LoderunnerAction> findGold() {
            int minDistance = 300;
            if (retreat) {
                repeat--;
                if (repeat == 0) {
                    retreat = false;
                }
            }
            if (pPos.equals(position)) {
                if (!retreat) {
                    repeat++;
                }
                if (repeatTresh <= repeat) {
                    retreat = true;
                }
            }
            System.out.println(retreat ? "retreat" : "atac");
            pPos = position;
            AStar aStar;
            List<AStar.Node> path = new ArrayList<>();
            List<BoardPoint> targets;
            if (!retreat) {
                targets = gameBoard.getGoldPositions();
                if (!gameBoard.underPills) {
                    targets.addAll(gameBoard.getShadowPills());
                }
            } else {
                targets = gameBoard.getPortals();
            }
            for (BoardPoint gold : targets) {

                aStar = new AStar(weightArray, position.getX(), position.getY(), false);
                List<AStar.Node> tmp = aStar.findPathTo(gold.getX(), gold.getY());
                int distance = 400;
                if (tmp != null) distance = tmp.size();
                if (minDistance > distance) {
                    minDistance = distance;
                    path = tmp;
                }
            }

            LoderunnerAction action;
            if (!path.isEmpty()) {
                AStar.Node nextNode = path.get(1);
                if (position.getX() == nextNode.x) {
                    action = position.getY() > nextNode.y ? LoderunnerAction.GO_UP : LoderunnerAction.GO_DOWN;
                } else {
                    action = position.getX() > nextNode.x ? LoderunnerAction.GO_LEFT : LoderunnerAction.GO_RIGHT;
                }
            } else {
                System.out.println("alt");
                int x = position.getX();
                int y = position.getY();
                if (weightArray[y][x+1] == 0) {
                    action = LoderunnerAction.GO_RIGHT;
                } else if (weightArray[y-1][x] == 0) {
                    action = LoderunnerAction.GO_UP;
                }else if(weightArray[y][x-1]==0){
                    action = LoderunnerAction.GO_LEFT;
                } else if (weightArray[y+1][x]==0){
                    action = LoderunnerAction.GO_DOWN;
                }else {
                    action = LoderunnerAction.DO_NOTHING;
                }
            }

            return Optional.of(action);
        }
    }
}

