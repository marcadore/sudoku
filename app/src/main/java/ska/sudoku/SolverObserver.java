package ska.sudoku;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

class SolverObserver extends LiveData<SolverTask.Result> implements SolverTask.SolverCallback {

    private SolverTask solverTask;

    void query(final List<Cell> grid, final int max) {
        solverTask = new SolverTask(max, this);
        solverTask.execute(grid.toArray(new Cell[grid.size()]));
    }

    void cancel() {
        if (solverTask != null) solverTask.cancel(true);
    }

    @Override
    public void onFinished(@NonNull SolverTask.Result result) {
        setValue(result);
    }
}
