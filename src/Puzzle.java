import java.util.Arrays;

public class Puzzle {

    public enum DIRECTION {UP, DOWN, LEFT, RIGHT} // An Enum for direction options
    private final char[][] puzzle; // The puzzle Board
    private static final char[][] goal = {{'1', '2', '3', '4'}, {'5', '6', '7', '8'}, {'9', 'A', 'B', 'C'}, {'D', 'E', 'F', '0'}}; // Goal State
    private String path = ""; // Path from root to current Puzzle
    private int zeroX, zeroY; // Position of the 'blank'
    private int g; // G value, i.e. depth of node
    private Puzzle parent; // Parent of the current Node

    public Puzzle(char[][] puzzle) {
        this.puzzle = puzzle;
        findZeroTile();
        g = 0;
        parent = null;
    }

    // Deep copy of the puzzle object, used when expanding node into children
    public Puzzle(Puzzle p) {
        puzzle = new char[p.puzzle.length][p.puzzle[0].length];

        for (int i = 0; i < puzzle.length; i++) {
            puzzle[i] = Arrays.copyOf(p.puzzle[i], puzzle[i].length);
        }

        parent = p;
        g = parent.g() + 1;
        zeroX = p.zeroX;
        zeroY = p.zeroY;
        path = p.path;
    }

    // Misplaced tiles heuristic
    public int h1(){

        int h1 = 0;

        for(int i = 0; i < puzzle.length; i++){
            for(int j = 0; j < puzzle[i].length; j++){
                if(puzzle[i][j] != goal[i][j]) h1++;
            }
        }

        return h1;
    }

    // Manhattan distance heuristic
    public int h2(){

        int h2 = 0;

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
        return h2;
    }

    // Check whether a current Puzzle has been solved
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

    // Check if we can move the black in a certain direction
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

    // Moving the blank a certain direction
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

    // Override function used to print the path after solution was found
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

    // Used to swap 2 tiles, then updates the blank tile position
    private void swap(int i1, int j1, int i2, int j2) {
        char previous = getTile(i1, j1);
        setTile(i1, j1, getTile(i2, j2));
        setTile(i2, j2, previous);
        zeroY = i2;
        zeroX = j2;
    }

    private void setTile(int i, int j, char tile) {
        puzzle[i][j] = tile;
    }

    private char getTile(int i, int j) {
        return puzzle[i][j];
    }

    // Used to find the initial location of the 'blank'
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

    public String getPath() {
        return path;
    }

    public char[][] getPuzzle() {
        return puzzle;
    }

    public int g(){
        return g;
    }

    public int f1(){
        return g() + h1();
    }

    public int f2(){
        return g() + h2();
    }

}