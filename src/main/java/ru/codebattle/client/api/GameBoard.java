package ru.codebattle.client.api;

import lombok.Getter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameBoard {

    @Getter
    private final String boardString;
    public boolean underPills;
    private final BoardPoint myPosition;

    public GameBoard(String boardString) {
        this.boardString = boardString.replace("\n", "");
        this.myPosition = getMyPosition(true);
    }

    public int size() {
        return (int) Math.sqrt(boardString.length());
    }

    public BoardPoint getMyPosition() {
        return myPosition;
    }

    public BoardPoint getMyPosition(boolean calculate) {
        for (int i = 0; i < size() * size(); i++) {
            BoardPoint pt = getPointByShift(i);
            BoardElement el = getElementAt(pt);
            List<BoardElement> normal = Arrays.asList(
                    BoardElement.HERO_DIE,
                    BoardElement.HERO_DRILL_LEFT,
                    BoardElement.HERO_DRILL_RIGHT,
                    BoardElement.HERO_FALL_LEFT,
                    BoardElement.HERO_FALL_RIGHT,
                    BoardElement.HERO_LADDER,
                    BoardElement.HERO_PIPE_LEFT,
                    BoardElement.HERO_PIPE_RIGHT,
                    BoardElement.HERO_LEFT,
                    BoardElement.HERO_RIGHT
            );
            List<BoardElement> shadow = Arrays.asList(
                    BoardElement.HERO_SHADOW_DRILL_LEFT,
                    BoardElement.HERO_SHADOW_DRILL_RIGHT,
                    BoardElement.HERO_SHADOW_LADDER,
                    BoardElement.HERO_SHADOW_LEFT,
                    BoardElement.HERO_SHADOW_RIGHT,
                    BoardElement.HERO_SHADOW_FALL_LEFT,
                    BoardElement.HERO_SHADOW_FALL_RIGHT,
                    BoardElement.HERO_SHADOW_PIPE_LEFT,
                    BoardElement.HERO_SHADOW_PIPE_RIGHT
            );
            underPills = multiCheck(shadow, getElementAt(pt));
            if (multiCheck(normal, getElementAt(pt)) || underPills) {
                return pt;
            }
        }
        return new BoardPoint(-1, -1);
    }

    public boolean isGameOver() {
        return boardString.contains("" + BoardElement.HERO_DIE.getSymbol());
    }

    public boolean hasElementAt(BoardPoint point, BoardElement element) {
        if (point.isOutOfBoard(size())) {
            return false;
        }

        return getElementAt(point) == element;
    }

    public BoardElement getElementAt(BoardPoint point) {
        return BoardElement.valueOf(boardString.charAt(getShiftByPoint(point)));
    }

    public void printBoard() {
        for (int i = 0; i < size(); i++) {
            System.out.println(boardString.substring(i * size(), size() * (i + 1)));
        }
    }

    public List<BoardPoint> findAllElements(BoardElement element) {
        List<BoardPoint> result = new ArrayList<>();

        for (int i = 0; i < size() * size(); i++) {
            BoardPoint pt = getPointByShift(i);

            if (hasElementAt(pt, element)) {
                result.add(pt);
            }
        }

        return result;
    }

    public List<BoardPoint> getEnemyPositions() {
        List<BoardPoint> result = findAllElements(BoardElement.ENEMY_LADDER);

        result.addAll(findAllElements(BoardElement.ENEMY_LEFT));
        result.addAll(findAllElements(BoardElement.ENEMY_PIPE_LEFT));
        result.addAll(findAllElements(BoardElement.ENEMY_PIPE_RIGHT));
        result.addAll(findAllElements(BoardElement.ENEMY_RIGHT));
        result.addAll(findAllElements(BoardElement.ENEMY_PIT));
        return result;
    }

    public List<BoardPoint> getOtherHeroPositions() {
        List<BoardPoint> result = findAllElements(BoardElement.OTHER_HERO_LEFT);
        result.addAll(findAllElements(BoardElement.OTHER_HERO_RIGHT));
        result.addAll(findAllElements(BoardElement.OTHER_HERO_LADDER));
        result.addAll(findAllElements(BoardElement.OTHER_HERO_PIPE_LEFT));
        result.addAll(findAllElements(BoardElement.OTHER_HERO_PIPE_RIGHT));
        result.addAll(findAllElements(BoardElement.OTHER_HERO_DRILL_LEFT));
        result.addAll(findAllElements(BoardElement.OTHER_HERO_DRILL_RIGHT));
        result.addAll(findAllElements(BoardElement.OTHER_HERO_FALL_LEFT));
        result.addAll(findAllElements(BoardElement.OTHER_HERO_FALL_RIGHT));

        result.addAll(findAllElements(BoardElement.OTHER_HERO_SHADOW_LEFT));
        result.addAll(findAllElements(BoardElement.OTHER_HERO_SHADOW_RIGHT));
        result.addAll(findAllElements(BoardElement.OTHER_HERO_SHADOW_LADDER));
        result.addAll(findAllElements(BoardElement.OTHER_HERO_SHADOW_PIPE_LEFT));
        result.addAll(findAllElements(BoardElement.OTHER_HERO_SHADOW_PIPE_RIGHT));
        result.addAll(findAllElements(BoardElement.OTHER_HERO_SHADOW_DRILL_LEFT));
        result.addAll(findAllElements(BoardElement.OTHER_HERO_SHADOW_DRILL_RIGHT));
        result.addAll(findAllElements(BoardElement.OTHER_HERO_SHADOW_FALL_LEFT));
        result.addAll(findAllElements(BoardElement.OTHER_HERO_SHADOW_FALL_RIGHT));

        return result;
    }

    public List<BoardPoint> getShadowPills() {
        return findAllElements(BoardElement.SHADOW_PILL);
    }

    public List<BoardPoint> getPortals() {
        return findAllElements(BoardElement.PORTAL);
    }

    public List<BoardPoint> getWallPositions() {
        List<BoardPoint> result = findAllElements(BoardElement.BRICK);
        result.addAll(findAllElements(BoardElement.UNDESTROYABLE_WALL));
        return result;
    }

    public List<BoardPoint> getLadderPositions() {
        List<BoardPoint> result = findAllElements(BoardElement.LADDER);
        result.addAll(findAllElements(BoardElement.HERO_LADDER));
        result.addAll(findAllElements(BoardElement.OTHER_HERO_LADDER));
        result.addAll(findAllElements(BoardElement.ENEMY_LADDER));
        result.addAll(findAllElements(BoardElement.HERO_SHADOW_LADDER));
        result.addAll(findAllElements(BoardElement.OTHER_HERO_SHADOW_LADDER));
        return result;
    }

    public List<BoardPoint> getGoldPositions() {
        List<BoardPoint> result = new ArrayList<>();
        for (int i = 0; i < size() * size(); i++) {
            BoardPoint pt = getPointByShift(i);
            BoardElement el = getElementAt(pt);
            List<BoardElement> golds = Arrays.asList(
                    BoardElement.YELLOW_GOLD,
                    BoardElement.GREEN_GOLD,
            BoardElement.RED_GOLD);
            if(multiCheck(golds,el)){
                result.add(pt);
            }
        }
        return result;
    }

    public List<BoardPoint> getPipePositions() {
        List<BoardPoint> result = findAllElements(BoardElement.PIPE);

        result.addAll(findAllElements(BoardElement.HERO_PIPE_LEFT));
        result.addAll(findAllElements(BoardElement.HERO_PIPE_RIGHT));
        result.addAll(findAllElements(BoardElement.OTHER_HERO_PIPE_LEFT));
        result.addAll(findAllElements(BoardElement.OTHER_HERO_PIPE_RIGHT));
        result.addAll(findAllElements(BoardElement.ENEMY_PIPE_LEFT));
        result.addAll(findAllElements(BoardElement.ENEMY_PIPE_RIGHT));
        result.addAll(findAllElements(BoardElement.HERO_SHADOW_PIPE_LEFT));
        result.addAll(findAllElements(BoardElement.HERO_SHADOW_PIPE_RIGHT));
        result.addAll(findAllElements(BoardElement.OTHER_HERO_SHADOW_PIPE_LEFT));
        result.addAll(findAllElements(BoardElement.OTHER_HERO_SHADOW_PIPE_RIGHT));
        return result;
    }

    public List<BoardPoint> getBarriers() {
        return getWallPositions();
    }

    public boolean hasElementAt(BoardPoint point, BoardElement... elements) {
        return Arrays.stream(elements).anyMatch(element -> hasElementAt(point, element));
    }

    public boolean isNearToElement(BoardPoint point, BoardElement element) {
        if (point.isOutOfBoard(size()))
            return false;

        return hasElementAt(point.shiftBottom(), element)
                || hasElementAt(point.shiftTop(), element)
                || hasElementAt(point.shiftLeft(), element)
                || hasElementAt(point.shiftRight(), element);
    }

    public boolean hasEnemyAt(BoardPoint point) {
        BoardElement element = getElementAt(point);
        List<BoardElement> boardElements = Arrays.asList(
                BoardElement.ENEMY_RIGHT,
                BoardElement.ENEMY_LEFT,
                BoardElement.ENEMY_LADDER,
                BoardElement.ENEMY_PIPE_LEFT,
                BoardElement.ENEMY_PIPE_RIGHT,
                BoardElement.ENEMY_PIT
        );
        return multiCheck(boardElements, element);
    }

    public boolean hasOtherHeroAt(BoardPoint point, boolean needShadow) {
        BoardElement element = getElementAt(point);
        return !needShadow ? (element.equals(BoardElement.OTHER_HERO_LEFT) ||
                element.equals(BoardElement.OTHER_HERO_RIGHT) ||
                element.equals(BoardElement.OTHER_HERO_LADDER) ||
                element.equals(BoardElement.OTHER_HERO_PIPE_LEFT) ||
                element.equals(BoardElement.OTHER_HERO_PIPE_RIGHT) ||
                element.equals(BoardElement.OTHER_HERO_DRILL_LEFT) ||
                element.equals(BoardElement.OTHER_HERO_DRILL_RIGHT) ||
                element.equals(BoardElement.OTHER_HERO_FALL_LEFT) ||
                element.equals(BoardElement.OTHER_HERO_FALL_RIGHT))

                : (element.equals(BoardElement.OTHER_HERO_SHADOW_LEFT) ||
                element.equals(BoardElement.OTHER_HERO_SHADOW_RIGHT) ||
                element.equals(BoardElement.OTHER_HERO_SHADOW_LADDER) ||
                element.equals(BoardElement.OTHER_HERO_SHADOW_PIPE_LEFT) ||
                element.equals(BoardElement.OTHER_HERO_SHADOW_PIPE_RIGHT) ||
                element.equals(BoardElement.OTHER_HERO_SHADOW_DRILL_LEFT) ||
                element.equals(BoardElement.OTHER_HERO_SHADOW_DRILL_RIGHT) ||
                element.equals(BoardElement.OTHER_HERO_SHADOW_FALL_LEFT) ||
                element.equals(BoardElement.OTHER_HERO_SHADOW_FALL_RIGHT));
    }

    public boolean hasWallAt(BoardPoint point) {
        return getWallPositions().contains(point);
    }

    public boolean hasLadderAt(BoardPoint point) {
        return getLadderPositions().contains(point);
    }

    public boolean hasGoldAt(BoardPoint point) {
        BoardElement element = getElementAt(point);
        return element.equals(BoardElement.GREEN_GOLD) ||
                element.equals(BoardElement.YELLOW_GOLD) ||
                element.equals(BoardElement.RED_GOLD);
    }

    public boolean hasPipeAt(BoardPoint point) {
        return getPipePositions().contains(point);
    }

    public boolean hasShadowAt(BoardPoint point) {
        return getShadows().contains(point);
    }

    public boolean hasPortalAt(BoardPoint point) {
        return getPortals().contains(point);
    }

    public boolean hasBarrierAt(BoardPoint point) {
        return getBarriers().contains(point);
    }

    private List<BoardPoint> getShadows() {
        List<BoardPoint> shadows = findAllElements(BoardElement.HERO_SHADOW_LEFT);
        shadows.addAll(findAllElements(BoardElement.HERO_SHADOW_RIGHT));
        shadows.addAll(findAllElements(BoardElement.HERO_SHADOW_LADDER));
        shadows.addAll(findAllElements(BoardElement.HERO_SHADOW_PIPE_LEFT));
        shadows.addAll(findAllElements(BoardElement.HERO_SHADOW_PIPE_RIGHT));
        shadows.addAll(findAllElements(BoardElement.HERO_SHADOW_DRILL_LEFT));
        shadows.addAll(findAllElements(BoardElement.HERO_SHADOW_DRILL_RIGHT));
        shadows.addAll(findAllElements(BoardElement.HERO_SHADOW_FALL_LEFT));
        shadows.addAll(findAllElements(BoardElement.HERO_SHADOW_FALL_RIGHT));

        shadows.addAll(findAllElements(BoardElement.OTHER_HERO_SHADOW_LEFT));
        shadows.addAll(findAllElements(BoardElement.OTHER_HERO_SHADOW_RIGHT));
        shadows.addAll(findAllElements(BoardElement.OTHER_HERO_SHADOW_LADDER));
        shadows.addAll(findAllElements(BoardElement.OTHER_HERO_SHADOW_PIPE_LEFT));
        shadows.addAll(findAllElements(BoardElement.OTHER_HERO_SHADOW_PIPE_RIGHT));
        shadows.addAll(findAllElements(BoardElement.OTHER_HERO_SHADOW_DRILL_LEFT));
        shadows.addAll(findAllElements(BoardElement.OTHER_HERO_SHADOW_DRILL_RIGHT));
        shadows.addAll(findAllElements(BoardElement.OTHER_HERO_SHADOW_FALL_LEFT));
        shadows.addAll(findAllElements(BoardElement.OTHER_HERO_SHADOW_FALL_RIGHT));
        return shadows;
    }

    public int getCountElementsNearToPoint(BoardPoint point, BoardElement element) {
        if (point.isOutOfBoard(size()))
            return 0;

        return boolToInt(hasElementAt(point.shiftLeft(), element)) +
                boolToInt(hasElementAt(point.shiftRight(), element)) +
                boolToInt(hasElementAt(point.shiftTop(), element)) +
                boolToInt(hasElementAt(point.shiftBottom(), element));
    }

    private int getShiftByPoint(BoardPoint point) {
        return point.getY() * size() + point.getX();
    }

    private BoardPoint getPointByShift(int shift) {
        return new BoardPoint(shift % size(), shift / size());
    }

    private int boolToInt(boolean bool) {
        return bool ? 1 : 0;
    }


    // Новый функционал

    public int[][] getWeightArray() {
        int size = size();
        int[][] result = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == 0 || j == 0 || j == size - 1 || i == size - 1) {
                    result[j][i] = -1;
                } else {
                    boolean above = j < myPosition.getY();
                    BoardPoint boardPoint = new BoardPoint(i, j);
                    BoardElement element = getElementAt(boardPoint);

                    List<BoardElement> walkable = Arrays.asList(

                    );
                    List<BoardElement> avoid = Arrays.asList(
                            BoardElement.UNDESTROYABLE_WALL,
                            BoardElement.BRICK,
                            BoardElement.PORTAL
                    );

                    boolean onTheL=i<=myPosition.getX();
                    boolean onTheR=i>myPosition.getX();
                    boolean hasFloorL= hasFloor(boardPoint.shiftLeft());
                    boolean hasFloorR= hasFloor(boardPoint.shiftRight());
                    boolean hasFloor = hasFloor(boardPoint);
                    boolean isWalkable = element.equals(BoardElement.LADDER)||
                            (element.equals(BoardElement.PIPE)&&((hasFloorL&&onTheR)||(hasFloorR&&onTheL)))||
                            element.equals(BoardElement.HERO_PIPE_LEFT)||
                            element.equals(BoardElement.HERO_PIPE_RIGHT)||(hasGoldAt(boardPoint)&&(hasFloorR||hasFloorL||hasFloor));

                    result[j][i] = (above && (hasFloor) || (!above) && (isWalkable || hasFloor || (hasFloorL && onTheR) || (hasFloorR&&onTheL)) ) &&
                            !(multiCheck(avoid, element) || (!underPills) && (hasEnemyAt(boardPoint) ||
                                    hasOtherHeroAt(boardPoint, true))) ? 0 : -1;

                    if (element.equals(BoardElement.PORTAL)&&hasFloor || hasOtherHeroAt(boardPoint, false)) {
                        result[j][i] = 300;
                    }
                }
            }
        }

        return result;
    }

    private boolean hasFloor(BoardPoint point) {
        BoardPoint pointBeneath = point.shiftBottom();
        BoardElement elementBeneath = getElementAt(pointBeneath);
        return elementBeneath.equals(BoardElement.BRICK) ||
                elementBeneath.equals(BoardElement.UNDESTROYABLE_WALL) ||
                elementBeneath.equals(BoardElement.LADDER) ||
                elementBeneath.equals(BoardElement.HERO_LADDER) ||
                elementBeneath.equals(BoardElement.HERO_SHADOW_LADDER);
    }

    private boolean multiCheck(List<BoardElement> list, BoardElement element) {
        for (BoardElement boardElement : list) {
            if (boardElement.equals(element)) return true;
        }
        return false;
    }
}
