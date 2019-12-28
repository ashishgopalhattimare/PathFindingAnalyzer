package sample.Constant;

public class Cell {

    public int i, j, distance; // distance is 'g'
    public int p_i, p_j;

    public long f, g, h;
    public boolean inPQ;

    public CellState state;

    // Default Constructor for the MazeController Initialization of the grid cells
    public Cell(int i, int j)
    {
        this.i = i; this.j = j;
        state = CellState.UNVISITED;
    }

    public void AStarStateInitilization() {
        this.f = this.g =
                this.h = Long.MAX_VALUE;
        this.inPQ = false;
    }

    public void setParent(int i, int j) {
        p_i = i; p_j = j;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "i=" + i +
                ", j=" + j +
                ", f=" + f +
                ", g=" + g +
                ", h=" + h +
                '}';
    }
}
