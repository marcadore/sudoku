package ska.sudoku;

import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

class SudokuViewModel extends ViewModel {

    private List<Cell> grid = new ArrayList<>();
    private final int maxValue;
    private SolverObserver solverObserver;

    public SudokuViewModel() {
        this.maxValue = MainActivity.MAX;
        solverObserver = new SolverObserver();
        initialize();
    }

    private void initialize() {
        for (int i = 0; i < maxValue * maxValue; i++) {
            grid.add(new Cell(i));
        }
    }

    SolverObserver getResult() {
        return solverObserver;
    }

    void onSolveClicked() {
        solverObserver.query(grid, maxValue);
    }

    void onResetClicked() {
        solverObserver.cancel();
        grid.clear();
        initialize();
    }

    List<Cell> getGrid() {
        return grid;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < grid.size(); i++) {
            if (i % maxValue == 0)
                builder.append('\n');
            if (i % (maxValue * 3) == 0)
                builder.append("-------------------------------\n");
            if (i % 3 == 0)
                builder.append('|');

            builder.append(" " + grid.get(i) + " ");
        }
        return builder.toString();
    }
}
