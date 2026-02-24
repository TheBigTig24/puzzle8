import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

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

            // ENTER SOLUTION DEPTH
            boolean isDone3 = false;
            solutionDepthLoop:
            while (!isDone3) {
                System.out.print("Enter Solution Depth (2-20):\n");
                int userInput3 = scanner.nextInt();
                if (userInput3 < 2 || userInput3 > 20) {
                    continue;
                } else {
                    isDone3 = true;
                    break solutionDepthLoop;
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
                        handleNumMisplacedTiles(puzzle, countMisplacedTiles(puzzle));
                        break;

                    case 2:
                        handleManhattanMethod();
                        break;

                    default:
                        continue;
                }
            }

        }
    }

    private static void handleNumMisplacedTiles(int[][] puzzle, int currentMisplacedTiles) {

    }

    private static 

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
        int currentPosition = 1;
        int counter = 0;
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                if (i == 0 && j == 0) {

                } else if (puzzle[i][j] == currentPosition) {
                    counter++;
                    currentPosition++;
                } else {
                    currentPosition++;
                }
            }
        }

        return counter;
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