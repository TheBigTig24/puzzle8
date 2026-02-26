import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

public class TileSolver {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("CS 4200 Project 1");

        boolean isDone = false;

        // SELECT SINGLE / MULTI PUZZLE MODE
        fullLoop:
        while (!isDone) {
            System.out.print("Select:\n[1] Single Test Puzzle\n[2] Multi-Test Puzzle\n[3] Exit\n");
            int userInput1 = scanner.nextInt();
            if (userInput1 == 3) {
                isDone = true;
                break fullLoop;
            } else if (userInput1 > 3) {
                continue;
            }

            int[][] puzzle = new int[3][3];

            // SELECT INPUT METHOD LOOP
            boolean isDone2 = false;
            innerLoop:
            while (!isDone2) {
                System.out.print("Select Input Method:\n[1] Random\n[2] File\n");
                int userInput2 = scanner.nextInt();
                switch (userInput2) {
                    case 1:
                        puzzle = generateRandomPuzzle();
                        break innerLoop;
                    
                    case 2:
                        puzzle = generateFilePuzzle();
                        break innerLoop;

                    default:
                        continue;
                }
            }

            System.out.println("Puzzle:");
            printPuzzleState(puzzle);

            // SELECT HEURISTIC FUNCTION
            boolean isDone4 = false;
            while (!isDone4) {
                System.out.print("Select H Function:\n[1] H1\n[2] H2");
                int userInput4 = scanner.nextInt();
                switch (userInput4) {
                    case 1:
                        if (checkIfSolvable(puzzle)) {
                            handleNumMisplacedTiles(puzzle, countMisplacedTiles(puzzle));
                        } else {
                            System.out.println("This shit is not solvable.");
                        }
                        break;

                    case 2:
                        if (checkIfSolvable(puzzle)) {
                            handleManhattanMethod();
                        } else {
                            System.out.println("ts not solvable.");
                        }
                        break;

                    default:
                        continue;
                }
            }

        }
    }

    private static void handleNumMisplacedTiles(int[][] puzzle, int currentMisplacedTiles) {
        // init time
        long startTime = System.nanoTime();

        if (currentMisplacedTiles == 0) {
            return;
        }

        // Frontier as a PrioQueue comparing g(n) + h(n)
        PriorityQueue<PuzzleState> frontier = new PriorityQueue<>((a, b) -> {
            int fnA = a.getMisplacedTiles() + a.getDepth();
            int fnB = b.getMisplacedTiles() + b.getDepth();
            return Integer.compare(fnA, fnB);
        });

        // Explored Set as a flattened List of each Puzzle
        Set<List<Integer>> exploredSet = new HashSet<>();

        // prep needed variables
        PuzzleState og = new PuzzleState(puzzle, currentMisplacedTiles, 0, null);
        frontier.add(og);

        Queue<int[][]> moveQueue;

        PuzzleState solution = null;

        int searchCost = 0;

        while (!frontier.isEmpty()) {
            PuzzleState curr = frontier.poll();
            if (exploredSet.contains(flattenPuzzle(curr.getPuzzle()))) {
                continue;
            }

            if (curr.getMisplacedTiles() == 0) {
                solution = new PuzzleState(curr.getPuzzle(), curr.getMisplacedTiles(), curr.getDepth(), curr.getParentPuzzle());
                break;
            }
            moveQueue = addMoves(curr.getPuzzle());
            while (!moveQueue.isEmpty()) {
                int[][] postMoveState = moveQueue.poll();
                frontier.add(new PuzzleState(postMoveState, countMisplacedTiles(postMoveState), curr.getDepth() + 1, curr));
                searchCost++;
            }

            addToExploredSet(exploredSet, curr.getPuzzle());
        }

        // Calculate Time
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);

        // print solution
        backtrackSolution(solution, exploredSet, searchCost);
    }

    private static Queue<int[][]> addMoves(int[][] puzzle) {
        Queue<int[][]> moveQueue = new ArrayDeque<>();
        int rowPos = -1;
        int colPos = -1;
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                if (puzzle[i][j] == 0) {
                    rowPos = i;
                    colPos = j;
                }
            }
        }

        // check position above
        if ( (rowPos - 1) >= 0)
            moveQueue.add(swapTiles(puzzle, rowPos, colPos, rowPos - 1, colPos));

        // check position below
        if ( (rowPos + 1) < 3)
            moveQueue.add(swapTiles(puzzle, rowPos, colPos, rowPos + 1, colPos));

        // check position left
        if ( (colPos - 1) >= 0)
            moveQueue.add(swapTiles(puzzle, rowPos, colPos, rowPos, colPos - 1));

        // check position right
        if ( (colPos + 1) < 3)
            moveQueue.add(swapTiles(puzzle, rowPos, colPos, rowPos, colPos + 1));

        return moveQueue;
    }

    private static int[][] swapTiles(int[][] puzzle, int i0, int j0, int x, int y) {
        int[][] newPuzzle = new int[puzzle.length][];
        for (int i = 0; i < puzzle.length; i++) {
            newPuzzle[i] = puzzle[i].clone();
        }

        newPuzzle[i0][j0] = newPuzzle[x][y];
        newPuzzle[x][y] = 0;
        return newPuzzle;
    }

    private static Set<List<Integer>> addToExploredSet(Set<List<Integer>> set, int[][] puzzle) {
        set.add(flattenPuzzle(puzzle));
        return set;
    }

    private static List<Integer> flattenPuzzle(int[][] puzzle) {
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                list.add(puzzle[i][j]);
            }
        }

        return list;
    }

    private static int countMisplacedTiles(int[][] puzzle) {
        int expectedValue = 0;
        int counter = 0;
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                if (puzzle[i][j] != expectedValue && puzzle[i][j] != 0) {
                    counter++;
                }
                expectedValue++;
            }
        }

        return counter;
    }

    private static boolean checkIfSolvable(int[][] puzzle) {
        List<Integer> flattenedPuzzle = flattenPuzzle(puzzle);
        flattenedPuzzle.remove(flattenedPuzzle.indexOf(0));
        int inversions = 0;
        for (int i = 0; i < flattenedPuzzle.size(); i++) {
            for (int j = i + 1; j < flattenedPuzzle.size(); j++) {
                if (flattenedPuzzle.get(i) > flattenedPuzzle.get(j)) {
                    inversions++;
                }
            }
        }

        return (inversions % 2) == 0  ? true : false;
    }

    private static void backtrackSolution(PuzzleState solution, Set<List<Integer>> exploredSet, int searchCost) {
        PuzzleState curr = solution;
        Stack<PuzzleState> stack = new Stack<>();
        while (curr.getParentPuzzle() != null) {
            stack.push(curr);
            PuzzleState prev = curr.getParentPuzzle();
            curr = prev;
        }
        stack.push(curr);

        curr = stack.pop();
        while (!stack.isEmpty()) {
            System.out.println("Step: " + curr.getDepth());
            printPuzzleState(curr.getPuzzle());
            curr = stack.pop();
        }
        System.out.println("Step: " + curr.getDepth());
        printPuzzleState(curr.getPuzzle());
        System.out.println("Search Cost: " + searchCost);
    }

    private static void handleManhattanMethod() {

    }

    private static int[][] generateRandomPuzzle() {
        int[][] puzzle = new int[3][3];
        List<Integer> nums = new ArrayList<>();
        Collections.addAll(nums, 0, 1, 2, 3, 4, 5, 6, 7, 8);
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                int randomIndex = (int) (Math.random() * ((nums.size())));
                puzzle[i][j] = nums.get(randomIndex);
                nums.remove(randomIndex);
            }
        }
        return puzzle;
    }

    private static int[][] generateFilePuzzle() {
        int [][] puzzle = new int[3][3];
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String inputStr = reader.readLine();
            int input = Integer.parseInt(inputStr);

            for (int i = puzzle.length - 1; i >= 0; i--) {
                for (int j = puzzle[i].length - 1; j >= 0; j--) {
                    puzzle[i][j] = input % 10;
                    input /= (int) 10;
                }
            }

            return puzzle;
        } catch (IOException e) {
            System.out.println("File Read Error.");
            return puzzle;
        }
    }

    private static int[][] generateUserMadePuzzle(Scanner scanner) {
        int[][] puzzle = new int[3][3];
        System.out.println("Enter a string of valid numbers 0-8.");
        int input = scanner.nextInt();
        for (int i = puzzle.length - 1; i >= 0; i--) {
            for (int j = puzzle[i].length - 1; j >= 0; j--) {
                puzzle[i][j] = input % 10;
                input /= (int) 10;
            }
        }
        return puzzle;
    }

    private static void printPuzzleState(int[][] puzzle) {
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                if (j + 1 == puzzle[i].length) {
                    System.out.print(puzzle[i][j] + "\n");
                } else {
                    System.out.print(puzzle[i][j] + " ");
                }
            }
        }
    }
}