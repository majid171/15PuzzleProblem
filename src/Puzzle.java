import java.util.Arrays;

public class Puzzle {
    public enum DIRECTION {UP, DOWN, LEFT, RIGHT}

    private final char[][] puzzle;
    private static final char[][] goal = {{'1', '2', '3', '4'}, {'5', '6', '7', '8'}, {'9', 'A', 'B', 'C'}, {'D', 'E', 'F', '0'}};
    private String path = "";
    private int zeroX, zeroY;

    public Puzzle(char[][] puzzle) {
        this.puzzle = puzzle;
        findZeroTile();
    }
    public Puzzle(Puzzle newPuzzle) {
        puzzle = new char[newPuzzle.puzzle.length][newPuzzle.puzzle[0].length];

        for (int i = 0; i < puzzle.length; i++) {
            puzzle[i] = Arrays.copyOf(newPuzzle.puzzle[i], puzzle[i].length);
        }

        zeroX = newPuzzle.zeroX;
        zeroY = newPuzzle.zeroY;
        path = newPuzzle.path;
    }

    public boolean isSolved() {
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                if (puzzle[i][j] != goal[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean canMove(DIRECTION dir) {
        switch (dir) {
            case UP:
                if (zeroY != 0) {
                    return true;
                }
                break;
            case DOWN:
                if (zeroY != puzzle.length - 1) {
                    return true;
                }
                break;
            case LEFT:
                if (zeroX != 0) {
                    return true;
                }
                break;
            case RIGHT:
                if (zeroX != puzzle[zeroY].length - 1) {
                    return true;
                }
                break;
        }
        return false;
    }

    public void move(DIRECTION dir) {
        switch (dir) {
            case UP:
                swap(zeroY, zeroX, (zeroY - 1), zeroX);
                path += "U";
                break;
            case DOWN:
                swap(zeroY, zeroX, (zeroY + 1), zeroX);
                path += "D";
                break;
            case LEFT:
                swap(zeroY, zeroX, zeroY, (zeroX - 1));
                path += "L";
                break;
            case RIGHT:
                swap(zeroY, zeroX, zeroY, (zeroX + 1));
                path += "R";
                break;
        }
    }

    // Override function used to print the path
    public void move(char dir) {
        switch (dir) {
            case 'U':
                swap(zeroY, zeroX, (zeroY - 1), zeroX);
                break;
            case 'D':
                swap(zeroY, zeroX, (zeroY + 1), zeroX);
                break;
            case 'L':
                swap(zeroY, zeroX, zeroY, (zeroX - 1));
                break;
            case 'R':
                swap(zeroY, zeroX, zeroY, (zeroX + 1));
                break;
        }
    }

    private void swap(int y1, int x1, int y2, int x2) {
        char previous = getTile(y1, x1);
        setTile(y1, x1, getTile(y2, x2));
        setTile(y2, x2, previous);
        zeroY = y2;
        zeroX = x2;
    }

    private void setTile(int y, int x, char tile) {
        puzzle[y][x] = tile;
    }
    private char getTile(int y, int x) {
        return puzzle[y][x];
    }

    private void findZeroTile() {
        for (int y = 0; y < puzzle.length; ++y) {
            for (int x = 0; x < puzzle[y].length; ++x) {
                if (puzzle[y][x] == '0') {
                    zeroY = y;
                    zeroX = x;
                }
            }
        }
    }

    public String getPath() {
        return path;
    }

    public char[][] getPuzzle() {
        return puzzle;
    }

    @Override
    public String toString() {
        String output = new String();
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                output += puzzle[i][j];
            }
            output += "\n";
        }
        return output;
    }
}