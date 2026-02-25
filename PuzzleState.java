public class PuzzleState {
    
    private int[][] puzzle;
    private int misplacedTiles;
    private int depth;
    private PuzzleState parentPuzzle;

    public PuzzleState(int[][] puzzle, int misplacedTiles, int depth, PuzzleState parentPuzzle) {
        this.puzzle = puzzle;
        this.misplacedTiles = misplacedTiles;
        this.depth = depth;
        this.parentPuzzle = parentPuzzle;
    }

    public int[][] getPuzzle() {
        return this.puzzle;
    }

    public int getMisplacedTiles() {
        return this.misplacedTiles;
    }

    public int getDepth() {
        return this.depth;
    }

    public PuzzleState getParentPuzzle() {
        return this.parentPuzzle;
    }

}
