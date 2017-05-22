package ska.sudoku;

import android.graphics.Point;
import android.os.AsyncTask;

import java.util.List;

class Solver extends AsyncTask<List<Cell>, Void, Solver.Result> {

    interface SolverCallback {
        void onFinished(Result result);
    }

    private final int max;

    private int currentCell;
    private SolverCallback listener;
    private static long startTime;

    Solver(int max, SolverCallback listener) {
        this.max = max;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        startTime = System.currentTimeMillis();
    }

    @Override
    protected Result doInBackground(List<Cell>... params) {
        List<Cell> grid = params[0];
        if (!isSolvable(grid)) {
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
            lastResult = solver(grid, cell);
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

    private boolean solver(List<Cell> grid, Cell cell) {
        if (cell.isPreFilled())
            return true;
        
        int value = cell.getValue() + 1;
        while (value <= max) {
            if (SudokuHelper.checkValue(grid, cell, value, MainActivity.MAX)) {
                cell.setValue(value);
                return true;
            } else {
                value++;
            }
        }
        cell.setValue(0);
        return false;
    }

    private boolean isSolvable(List<Cell> grid) {
        for (int i = 0; i < max * max; i++) {
            Cell cell = grid.get(i);
            if (cell.isPreFilled()) {
                if (!isUniqueInRow(grid, i) || !isUniqueInColumn(grid, i) || !isUniqueInSection(grid, i))
                    return false;
            }
        }
        return true;
    }

    private boolean isUniqueInRow(List<Cell> grid, int cellNr) {
        int value = grid.get(cellNr).getValue();
        int row = cellNr / max;
        for (int i = 0; i < max; i++) {
            int currentNr = i + max * row;
            if (currentNr == cellNr) continue;
            if (grid.get(currentNr).getValue() == value) return false;
        }
        return true;
    }

    private boolean isUniqueInColumn(List<Cell> grid, int cellNr) {
        int value = grid.get(cellNr).getValue();
        int column = cellNr % max;
        for (int i = 0; i < max; i++) {
            int currentNr = i * max + column;
            if (currentNr == cellNr) continue;
            if (grid.get(currentNr).getValue() == value) return false;
        }
        return true;
    }

    private boolean isUniqueInSection(List<Cell> grid, int cellNr) {
        int value = grid.get(cellNr).getValue();
        Point p = SudokuHelper.getSection(cellNr, max);
        int itemsPerSection = max / 3;
        int sectionRow = p.y;
        int sectionColumn = p.x;

        for (int i = 0; i < itemsPerSection; i++) {
            int firstRow = i + sectionRow * max * 3 + sectionColumn * itemsPerSection;
            for (int j = 0; j < itemsPerSection; j++) {
                int currentCell = firstRow + j * max;
                if (cellNr != currentCell)
                    if (grid.get(currentCell).getValue() == value)
                        return false;
            }
        }
        return true;
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
