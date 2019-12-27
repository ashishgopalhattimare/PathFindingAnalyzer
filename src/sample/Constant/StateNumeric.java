package sample.Constant;

public class StateNumeric
{
    private StateNumeric() {}

    public static CellState GetNumeric(char chr)
    {
        switch (chr)
        {
            case '-': return CellState.UNVISITED;
            case '*': return CellState.WALL;
            case 'x': return CellState.TARGET;
            case '.': return CellState.SOURCE;
        }
        return null;
    }

    public static char GetCharacter(CellState state)
    {
        if(state == CellState.SOURCE)       return 'o';
        if(state == CellState.WALL)         return '*';
        if(state == CellState.TARGET)       return 'x';
        return '-';
    }
}