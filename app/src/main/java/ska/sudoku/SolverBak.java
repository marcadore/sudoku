package ska.sudoku;

public class SolverBak {

    private final int max;

    private int currentCell;
    private Grid grid;

    public SolverBak(Grid grid, int max) {
        this.grid = grid;
        this.max = max;

        boolean lastResult = true;
        while (currentCell < (max * max) && currentCell > -1) {
            Cell cell = grid.getCell(currentCell);
            if (cell.isPreFilled()) {
                setCurrentCell(lastResult);
                continue;
            }
            lastResult = solver(cell);
            setCurrentCell(lastResult);
        }
        System.out.println(grid.toString());
    }

    private void setCurrentCell(boolean success) {
        if (success)
            currentCell++;
        else
            currentCell--;
    }

    private boolean solver(Cell cell) {
        if (cell.isPreFilled())
            return true;
        
        int value = cell.getValue() + 1;
        while (value <= max) {
            if (grid.checkValue(cell, value)) {
                cell.setValue(value);
                return true;
            } else {
                value++;
            }
        }
        cell.setValue(0);
        return false;
    }

}
