package ska.sudoku;

import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

class SudokuViewModel extends ViewModel {

    private static final int MAX = 9;
    private List<Cell> grid = new ArrayList<>();
    private SolverObserver solverObserver;

    public SudokuViewModel() {
        solverObserver = new SolverObserver();
        initialize();
    }

    private void initialize() {
        for (int i = 0; i < MAX * MAX; i++) {
            grid.add(new Cell(i));
        }
    }

    SolverObserver getResult() {
        return solverObserver;
    }

    void onSolveClicked() {
        solverObserver.query(grid, MAX);
    }

    void onResetClicked() {
        solverObserver.cancel();
        grid.clear();
        initialize();
    }

    List<Cell> getGrid() {
        return grid;
    }

    int getMax() {
        return MAX;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < grid.size(); i++) {
            if (i % MAX == 0)
                builder.append('\n');
            if (i % (MAX * 3) == 0)
                builder.append("-------------------------------\n");
            if (i % 3 == 0)
                builder.append('|');

            builder.append(" " + grid.get(i) + " ");
        }
        return builder.toString();
    }
}
