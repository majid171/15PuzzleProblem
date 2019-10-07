import java.util.Arrays;

public class Puzzle {
    public enum DIRECTION {UP, DOWN, LEFT, RIGHT}

    private final char[][] puzzle;
    private static final char[][] goal = {{'1', '2', '3', '4'}, {'5', '6', '7', '8'}, {'9', 'A', 'B', 'C'}, {'D', 'E', 'F', '0'}};
    private String path = "";
    private int zeroX, zeroY;
    private int h1; // Heuristics Function (Misplaced Tiles)
    private int h2; // Heuristics Function (Manhattan Distance)

    public Puzzle(char[][] puzzle) {
        this.puzzle = puzzle;
        findZeroTile();
        calculateH1();
        calculateH2();
    }

    public Puzzle(Puzzle newPuzzle) {
        puzzle = new char[newPuzzle.puzzle.length][newPuzzle.puzzle[0].length];

        for (int i = 0; i < puzzle.length; i++) {
            puzzle[i] = Arrays.copyOf(newPuzzle.puzzle[i], puzzle[i].length);
        }

        h1 = newPuzzle.h1;
        h2 = newPuzzle.h2;
        zeroX = newPuzzle.zeroX;
        zeroY = newPuzzle.zeroY;
        path = newPuzzle.path;
    }

    public void calculateH1(){
        for(int i = 0; i < puzzle.length; i++){
            for(int j = 0; j < puzzle[i].length; j++){
                if(puzzle[i][j] != goal[i][j]) h1++;
            }
        }
    }

    public void calculateH2(){

        for(int i = 0; i < puzzle.length; i++){
            for(int j = 0; j < puzzle[i].length; j++){
                char tile = puzzle[i][j];
                if(tile != '0'){
                    int tileValue = Integer.parseInt("" + tile,16);
                    int targetX = (tileValue - 1) / 4;
                    int targetY = (tileValue - 1) % 4;
                    int dx = i - targetX;
                    int dy = j - targetY;
                    h2 += Math.abs(dx) + Math.abs(dy);
                }
            }
        }
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

    private void setTile(int i, int j, char tile) {
        puzzle[i][j] = tile;
    }

    private char getTile(int i, int j) {
        return puzzle[i][j];
    }

    private void findZeroTile() {
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                if (puzzle[i][j] == '0') {
                    zeroY = i;
                    zeroX = j;
                }
            }
        }
    }

    public String getPath() {
        return path;
    }

    public int getH1(){
        return h1;
    }

    public int getH2(){
        return h2;
    }

    public char[][] getPuzzle() {
        return puzzle;
    }

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