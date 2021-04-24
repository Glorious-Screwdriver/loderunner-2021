package ru.codebattle.client.api;


import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameBoardTest {

    @Test
    void testGetWeightMap() {
        String grid_before = "";
        String grid_after = "";

        try {
            Path path = Paths.get("src/test/java/resources/grid_before.txt");
            grid_before = String.join("\n", Files.readAllLines(path));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        try {
            Path path = Paths.get("src/test/java/resources/grid_after.txt");
            grid_after = String.join("\n", Files.readAllLines(path));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        GameBoard gameBoard = new GameBoard(grid_before);
        int[][] maze = gameBoard.getWeightArray();

        StringBuilder result = new StringBuilder();
        int length = maze[0].length;

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                switch (maze[i][j]) {
                    case 0:
                        result.append("_");
                        break;
                    case -1:
                        result.append("*");
                        break;
                }
            }
            result.append(i == length - 1 ? "" : "\n");
        }

        assertEquals(grid_after, result.toString());
    }
}
