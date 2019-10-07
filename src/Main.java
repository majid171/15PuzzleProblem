import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Queue;

public class Main {

    public static char[][] load(String file){

        char[][] puzzle =  new char[4][4];
        BufferedReader br;
        String line;

        try {
            br = new BufferedReader(new FileReader(file));
            line = br.readLine();
            String[] tiles = line.split(" ");
            int i = 0, j = 0;

            for(String x : tiles){
                if(j == 4){
                    j = 0;
                    i++;
                }
                puzzle[i][j] = x.charAt(0);
                j++;

            }

            br.close();
            return puzzle;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void BFS(Puzzle puzzleInit, Puzzle.DIRECTION[] dir){

        Queue<Puzzle> q = new LinkedList<Puzzle>();
        q.add(puzzleInit);

        while (!q.isEmpty()) {
            Puzzle puzzle = q.poll();
            if (puzzle.isSolved()) {
                printPath(puzzleInit, puzzle.getPath());
                return;
            }
            for (int i = 0; i < dir.length; i++) {
                if (puzzle.canMove(dir[i])) {
                    Puzzle newPuzzle = new Puzzle(puzzle);
                    newPuzzle.move(dir[i]);
                    q.add(newPuzzle);
                }
            }
        }

        System.out.println("Did Not Find Solution!");
    }

    public static void printPath(Puzzle start, String endPath){
        Puzzle startCopy = new Puzzle(start);

        System.out.println(startCopy.toString());

        for(int i = 0; i < endPath.length(); i++){
            char move = endPath.charAt(i);

            startCopy.move(move);

            System.out.println(startCopy.toString());
        }
    }

    public static void main(String args[]){

        String file = "testCase0.txt";
        char temp[][] = load(file);
        Puzzle.DIRECTION[] strategy = {Puzzle.DIRECTION.RIGHT, Puzzle.DIRECTION.DOWN, Puzzle.DIRECTION.UP, Puzzle.DIRECTION.LEFT};
        Puzzle p = new Puzzle(temp); // The initial puzzle
        BFS(p, strategy);

    }
}
