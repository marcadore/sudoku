package ska.sudoku;

import android.arch.lifecycle.LiveData;

import java.util.List;

class SolverObserver extends LiveData<Solver.Result> implements Solver.SolverCallback {

    private Solver solver;

    void query(final List<Cell> grid, final int max) {
        solver = new Solver(max, this);
        solver.execute(grid);
    }

    void cancel() {
        if (solver != null) solver.cancel(true);
    }

    @Override
    public void onFinished(Solver.Result result) {
        setValue(result);
    }
}
