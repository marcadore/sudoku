package ska.sudoku;

import android.os.AsyncTask;

import java.util.Arrays;
import java.util.List;

class SolverTask extends AsyncTask<Cell, Void, SolverTask.Result> {

    interface SolverCallback {
        void onFinished(Result result);
    }

    private final int max;
    private int currentCell;
    private final SolverCallback listener;
    private static long startTime;
    private final Solver solver;

    SolverTask(int max, SolverCallback listener) {
        this.max = max;
        this.listener = listener;
        solver = new Solver(max);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        startTime = System.currentTimeMillis();
    }

    @Override
    protected Result doInBackground(Cell... params) {
        List<Cell> grid = Arrays.asList(params);
        if (!solver.isSolvable(grid)) {
            cancel(true);
            return new Result(grid, Result.Code.NOT_SOLVABLE);
        }

        boolean lastResult = true;
        while (currentCell < (max * max)) {
            if (isCancelled()) {
                return new Result(grid, Result.Code.USER_CANCELLED);
            }

            Cell cell = grid.get(currentCell);
            if (cell.isPreFilled()) {
                setCurrentCell(lastResult);
                continue;
            }
            lastResult = solver.solveCell(grid, cell);
            setCurrentCell(lastResult);
            Thread.yield();
        }
        return new Result(grid);
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        System.out.println(result.getGrid().toString());
        if (listener != null)
            listener.onFinished(result);
    }

    @Override
    protected void onCancelled(Result result) {
        if (listener != null) listener.onFinished(result);
    }

    private void setCurrentCell(boolean success) {
        if (success)
            currentCell++;
        else
            currentCell--;
    }

    static class Result {
        enum Code { NO_ERROR, NOT_SOLVABLE, USER_CANCELLED }

        private final List<Cell> grid;
        private final Code error;
        private final long duration;

        Result(List<Cell> grid) {
            this(grid, Code.NO_ERROR);
        }

        Result(List<Cell> grid, Code error) {
            this.grid = grid;
            this.error = error;
            this.duration = calculateDuration();
        }

        List<Cell> getGrid() {
            return grid;
        }

        long getDuration() {
            return duration;
        }

        Code getError() {
            return error;
        }

        private long calculateDuration() {
            return System.currentTimeMillis() - startTime;
        }
    }
}
