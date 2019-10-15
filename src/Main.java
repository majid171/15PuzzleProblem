import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Main {

    public static final int MAX_TIME = 100;

    // Breadth-First-Search implementation
    public static void bfs(Puzzle puzzleInit, Puzzle.DIRECTION[] dir){

        int numberOfNodesExpanded = 0;
        int numberOfNodesAccessed = 0;
        int fringe = 0;

        HashSet<String> seen = new HashSet<String>();
        Queue<Puzzle> q = new LinkedList<Puzzle>();
        q.add(puzzleInit);
        seen.add(puzzleInit.toString());

        while (!q.isEmpty()) {
            Puzzle puzzle = q.poll();
            numberOfNodesAccessed++;
            fringe = Math.max(fringe, q.size());

            if (puzzle.isSolved()) {
                printOutput(puzzleInit, puzzle.getPath(), puzzle.g(), numberOfNodesExpanded, fringe, numberOfNodesAccessed);
                return;
            }

            for (int i = 0; i < dir.length; i++) {
                if (puzzle.canMove(dir[i])) {
                    Puzzle newPuzzle = new Puzzle(puzzle);
                    newPuzzle.move(dir[i]);
                    if(!seen.contains(newPuzzle.toString())) {
                        numberOfNodesExpanded++;
                        q.add(newPuzzle);
                        seen.add(newPuzzle.toString());
                    }
                }
            }
        }

        printCutOff(puzzleInit); // If no solution
    }

    // Greedy-Best-First-Search Implementation
    public static void gbfs(Puzzle puzzleInit, Puzzle.DIRECTION[] dir, int hType){

        int numberOfNodesExpanded = 0;
        int numberOfNodesAccessed = 0;
        int fringe = 0;

        ArrayList<Puzzle> open = new ArrayList<Puzzle>();
        open.add(puzzleInit);
        HashSet<String>seen = new HashSet<String>();
        seen.add(puzzleInit.toString());

        while(!open.isEmpty()){
            if(hType == 1) Collections.sort(open, (a, b) -> a.h1() - b.h1());
            else Collections.sort(open, (a, b) -> a.h2() - b.h2());

            Puzzle puzzle = open.remove(0);
            numberOfNodesAccessed++;
            fringe = Math.max(fringe, open.size());

            if(puzzle.isSolved()){
                printOutput(puzzleInit, puzzle.getPath(), puzzle.g(), numberOfNodesExpanded, fringe, numberOfNodesAccessed);
                return;
            }

            for (int i = 0; i < dir.length; i++) {
                if (puzzle.canMove(dir[i])) {
                    Puzzle newPuzzle = new Puzzle(puzzle);
                    newPuzzle.move(dir[i]);
                    if(!seen.contains(newPuzzle.toString())) {
                        numberOfNodesExpanded++;
                        open.add(newPuzzle);
                        seen.add(newPuzzle.toString());
                    }
                }
            }
        }

        printCutOff(puzzleInit);// If no solution
    }

    // A* Search Implementation
    public static void aStar(Puzzle puzzleInit, Puzzle.DIRECTION[] dir, int hType){

        int numberOfNodesExpanded = 0;
        int numberOfNodesAccessed = 0;
        int fringe = 0;

        ArrayList<Puzzle> open = new ArrayList<Puzzle>();
        open.add(puzzleInit);
        HashSet<String>seen = new HashSet<String>();
        seen.add(puzzleInit.toString());

        while(!open.isEmpty()){
            if(hType == 1) Collections.sort(open, (a, b) -> a.f1() - b.f1());
            else Collections.sort(open, (a, b) -> a.f2() - b.f2());

            Puzzle puzzle = open.remove(0);
            numberOfNodesAccessed++;
            fringe = Math.max(fringe, open.size());

            if(puzzle.isSolved()){
                printOutput(puzzleInit, puzzle.getPath(), puzzle.g(), numberOfNodesExpanded, fringe, numberOfNodesAccessed);
                return;
            }

            for (int i = 0; i < dir.length; i++) {
                if (puzzle.canMove(dir[i])) {
                    Puzzle newPuzzle = new Puzzle(puzzle);
                    newPuzzle.move(dir[i]);
                    if(!seen.contains(newPuzzle.toString())) {
                        numberOfNodesExpanded++;
                        open.add(newPuzzle);
                        seen.add(newPuzzle.toString());
                    }
                }
            }
        }

        printCutOff(puzzleInit); // If no solution
    }

    /*
        Implementation of IDA*
        This is the driver function
    */
    public static void ida(Puzzle p, Puzzle.DIRECTION[] dir, int hType){
        ArrayList<Puzzle>path = new ArrayList<Puzzle>();
        int bound = hType == 1 ? p.h1(): p.h2();

        path.add(p);
        int fringe = 0, numberOfNodesExpanded = 0, numberOfNodesAccessed = 0;

        while(true){
            int temp = search(path,0, bound, dir, numberOfNodesExpanded, fringe, numberOfNodesAccessed, hType);

            if(temp == 0){
                return;
            }

            if(temp > MAX_TIME){ // If no solution in certain amount of time
                printCutOff(p);
                return;
            }
            bound = temp;
        }
    }

    /*
            Implementation of IDA*
            Used to search tree up to a certain threshold
    */
    public static int search(ArrayList<Puzzle> path, int g, int bound, Puzzle.DIRECTION[] dir, int numberOfNodesExpanded, int fringe, int numberOfNodesAccessed, int hType){
        Puzzle curr = path.get(path.size() - 1);
        numberOfNodesAccessed++;

        fringe = Math.max(fringe, path.size());

        int f = g + (hType == 1 ? curr.h1(): curr.h2());
        if(f > bound) return f;

        if(curr.isSolved()){
            printOutput(path.get(0), curr.getPath(), curr.g(), numberOfNodesExpanded, fringe, numberOfNodesAccessed);
            return 0;
        }

        int min = Integer.MAX_VALUE;
        ArrayList<Puzzle> children = getSuccessors(curr, dir);
        for(Puzzle child : children){
            numberOfNodesExpanded++;
            path.add(child);
            int temp = g + (hType == 1 ? child.h1() : child.h2());
            int t = search(path, temp, bound, dir, numberOfNodesExpanded, fringe, numberOfNodesAccessed, hType);
            if (t == 0) {
                return 0;
            }
            if (t < min) min = t;
            path.remove(path.size() - 1);
        }

        return min;
    }

    /*
        Implementation of IDA*
        Function to retrieve children of current puzzle
    */
    public static ArrayList<Puzzle> getSuccessors(Puzzle p, Puzzle.DIRECTION[] dir){

        ArrayList<Puzzle>children = new ArrayList<Puzzle>();

        for (int i = 0; i < dir.length; i++) {
            if (p.canMove(dir[i])) {
                Puzzle newPuzzle = new Puzzle(p);
                newPuzzle.move(dir[i]);
                children.add(newPuzzle);
            }
        }

        return children;
    }

    // Prints path of solution, along with other required information
    public static void printOutput(Puzzle start, String endPath, int depth, int numberOfNodesExpanded, int fringe, int numberOfNodesAccessed){

        // Printing the path
        System.out.println(start.toString());
        for(int i = 0; i < endPath.length(); i++){
            char move = endPath.charAt(i);

            start.move(move);

            System.out.println(start.toString());
        }

        // Printing extra output
        System.out.println("The depth of the solution is: " + depth);
        System.out.println("The number of nodes expanded: " + numberOfNodesExpanded);
        System.out.println("The maximum size of the fringe: " + fringe);
        System.out.println("The number of states accessed: " + numberOfNodesAccessed);
    }

    // Used to write an error message when a cutoff occurs
    public static void printCutOff(Puzzle start){
        System.out.println("A cutoff has occurred...\nNo solution was found for this puzzle state.\n" + start.toString());
    }

    /*
        Function used to load a puzzla from a text file
        Format of file must be ... <1 2 3 ... E F 0>
    */
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

    // Loads puzzle manually by user... <1 2 3... E F 0>
    public static char[][] enterPuzzle(){
        char[][] input = new char[4][4];
        Scanner in = new Scanner(System.in);

        System.out.println("Enter numbers separated by spaces (1 2 3... E F 0)");

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                input[i][j] = in.next().charAt(0);
            }
        }

        return input;
    }

    // Driver function to test the assignment
    public static void run(){
        Scanner in = new Scanner(System.in);
        char [][] input;
        Puzzle.DIRECTION[] strategy = {Puzzle.DIRECTION.RIGHT, Puzzle.DIRECTION.DOWN, Puzzle.DIRECTION.UP, Puzzle.DIRECTION.LEFT};

        System.out.println("1: Enter Puzzle\n2: Enter Test Case File Name");
        int choice = in.nextInt();

        if(choice == 1){
            input = enterPuzzle();
        }
        else if(choice == 2){
            in.nextLine();
            System.out.print("Enter File Name: ");
            String file = in.nextLine();
            input = load(file);
        }
        else{
            System.out.println("Invalid Choice!");
            return;
        }

        Puzzle p = new Puzzle(input);
        System.out.println("1: Breadth First Search\n2: Greedy Best First Search\n3: A*\n4: Recursive Best First Search");
        choice = in.nextInt();

        int hChoice = 0;
        if(choice == 2 || choice == 3 || choice == 4){
            System.out.println("1: Misplaced Tiles --> h1()\n2: Manhattan Distance --> h2()");
            hChoice = in.nextInt();
            if(hChoice < 1 || hChoice > 2){
                System.out.println("Invalid Choice!");
                return;
            }
        }

        if(choice == 1){
            bfs(p, strategy);
        }
        else if(choice == 2){
            if(hChoice == 1){
                gbfs(p, strategy, 1);
            }
            else{
                gbfs(p, strategy, 2);
            }
        }
        else if(choice == 3){
            if(hChoice == 1){
                aStar(p, strategy, 1);
            }
            else{
                aStar(p, strategy, 2);
            }
        }
        else if(choice == 4){
            if(hChoice == 1){
                ida(p, strategy, 1);
            }
            else{
                ida(p, strategy, 2);
            }
        }
        else{
            System.out.println("Invalid Choice");
            return;
        }

        in.close();
    }

    public static void main(String args[]){
        run();
    }
}
