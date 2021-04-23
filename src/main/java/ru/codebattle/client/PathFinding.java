package ru.codebattle.client;

import ru.codebattle.client.api.BoardPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class PathFinding {
    Node[][] grid;
    int gridSize;

    public PathFinding(boolean[][] grid) {
        gridSize = grid[0].length;
        this.grid = new Node[gridSize][gridSize];

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                this.grid[i][j] = new Node(new BoardPoint(i, j), grid[i][j]);
            }
        }
    }

    public void findPath(BoardPoint startPoint, BoardPoint endPoint) {
        Node startNode = new Node(startPoint, true);
        Node endNode = new Node(endPoint, true);

        ArrayList<Node> openSet = new ArrayList<>();
        HashSet<Node> closedSet = new HashSet<>();

        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            Node currentNode = openSet.get(0);
            for (Node node : openSet) {
                if (node.fCost() < currentNode.fCost() || node.fCost() == currentNode.fCost() && node.hCost < currentNode.hCost) {
                    currentNode = node;
                }
            }
            openSet.remove(currentNode);
            closedSet.add(currentNode);

            if (currentNode.equals(endNode)) {
                startNode.retracePath(startNode);
            }
            for (Node neighbour : getNeighbours(currentNode)) {
                if (!neighbour.walkable || closedSet.contains(neighbour)) {
                    continue;
                }

                int newCost = currentNode.gCost + currentNode.getDistanceTo(neighbour);
                if (newCost < neighbour.gCost || openSet.contains(neighbour)) {
                    neighbour.gCost = newCost;
                    neighbour.hCost = neighbour.getDistanceTo(endNode);
                    neighbour.parent = currentNode;

                    if (!openSet.contains(neighbour)) {
                        openSet.add(neighbour);
                    }
                }
            }
        }
    }

    private static class Node {
        public final BoardPoint pos;
        private final boolean walkable;
        Node parent;

        public int gCost, hCost;

        Node(BoardPoint pos, boolean walkable) {
            this.pos = pos;
            this.walkable = walkable;
        }


        public int fCost() {
            return gCost + hCost;
        }

        public int getDistanceTo(Node node) {
            int distX = Math.abs(node.pos.getX() - pos.getX());
            int distY = Math.abs(node.pos.getY() - pos.getY());
            if (distY > distX) {
                return 14 * distX + 10 * (distY - distX);
            } else {
                return 14 * distY + 10 * (distX - distY);
            }
        }
        public void retracePath(Node end) {
            ArrayList<Node> path = new ArrayList<>();
            Node current = end;
            while (!current.equals(this)) {
                path.add(current);
                current = current.parent;
            }
            Collections.reverse(path);
            path.forEach(i -> System.out.println(i.pos.toString()));
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return this.pos.equals(node.pos);
        }
    }

    private ArrayList<Node> getNeighbours(Node node) {
        ArrayList<Node> list = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }

                BoardPoint boardPoint = new BoardPoint(node.pos.getX() + i, node.pos.getY() + j);

                if (boardPoint.getX() >= 0 && boardPoint.getX() < gridSize && boardPoint.getY() >= 0 && boardPoint.getY() < gridSize) {
                    list.add(grid[boardPoint.getX()][boardPoint.getY()]);
                }
            }
        }
        return list;
    }
}
