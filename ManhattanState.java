public class ManhattanState {
    
    private int[][] puzzle;
    private int manhattanCost;
    private int depth;
    private ManhattanState parentPuzzle;

    public ManhattanState(int[][] puzzle, int manhattanCost, int depth, ManhattanState parentPuzzle) {
        this.puzzle = puzzle;
        this.manhattanCost = manhattanCost;
        this.depth = depth;
        this.parentPuzzle = parentPuzzle;
    }

    public int[][] getPuzzle() {
        return this.puzzle;
    }

    public int getManhattanCost() {
        return this.manhattanCost;
    }

    public int getDepth() {
        return this.depth;
    }

    public ManhattanState getParentPuzzle() {
        return this.parentPuzzle;
    }
    
}
