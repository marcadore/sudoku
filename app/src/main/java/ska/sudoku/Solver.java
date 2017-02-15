package ska.sudoku;

import android.graphics.Point;
import android.os.AsyncTask;

public class Solver extends AsyncTask<Grid, Void, Solver.Result> {

    interface SolverCallback {
        void onFinished(Result result);
    }

    private final int max;

    private int currentCell;
    private SolverCallback listener;
    private static long startTime;

    public Solver(int max, SolverCallback listener) {
        this.max = max;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        startTime = System.currentTimeMillis();
    }

    @Override
    protected Result doInBackground(Grid... params) {
        Grid grid = params[0];
        if (!isSolvable(grid)) {
            cancel(true);
            return new Result(grid, Result.Code.NOT_SOLVABLE);
        }

        boolean lastResult = true;
        while (currentCell < (max * max)) {
            if (isCancelled()) {
                return new Result(grid, Result.Code.USER_CANCELLED);
            }

            Cell cell = grid.getCell(currentCell);
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

    private boolean solver(Grid grid, Cell cell) {
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

    private boolean isSolvable(Grid grid) {
        for (int i = 0; i < max * max; i++) {
            Cell cell = grid.getCell(i);
            if (cell.isPreFilled()) {
                if (!isUniqueInRow(grid, i) || !isUniqueInColumn(grid, i) || !isUniqueInSection(grid, i))
                    return false;
            }
        }
        return true;
    }

    private boolean isUniqueInRow(Grid grid, int cellNr) {
        int value = grid.getCell(cellNr).getValue();
        int row = cellNr / max;
        for (int i = 0; i < max; i++) {
            int currentNr = i + max * row;
            if (currentNr == cellNr) continue;
            if (grid.getCell(currentNr).getValue() == value) return false;
        }
        return true;
    }

    private boolean isUniqueInColumn(Grid grid, int cellNr) {
        int value = grid.getCell(cellNr).getValue();
        int column = cellNr % max;
        for (int i = 0; i < max; i++) {
            int currentNr = i * max + column;
            if (currentNr == cellNr) continue;
            if (grid.getCell(currentNr).getValue() == value) return false;
        }
        return true;
    }

    private boolean isUniqueInSection(Grid grid, int cellNr) {
        int value = grid.getCell(cellNr).getValue();
        Point p = SudokuHelper.getSection(cellNr, max);
        int itemsPerSection = max / 3;
        int sectionRow = p.y;
        int sectionColumn = p.x;

        for (int i = 0; i < itemsPerSection; i++) {
            int firstRow = i + sectionRow * max * 3 + sectionColumn * itemsPerSection;
            for (int j = 0; j < itemsPerSection; j++) {
                int currentCell = firstRow + j * max;
                if (cellNr != currentCell)
                    if (grid.getCell(currentCell).getValue() == value)
                        return false;
            }
        }
        return true;
    }

    public static class Result {
        public enum Code { NO_ERROR, NOT_SOLVABLE, USER_CANCELLED }

        private final Grid grid;
        private final Code error;
        private final long duration;

        Result(Grid grid) {
            this(grid, Code.NO_ERROR);
        }

        Result(Grid grid, Code error) {
            this.grid = grid;
            this.error = error;
            this.duration = calculateDuration();
        }

        public Grid getGrid() {
            return grid;
        }

        public long getDuration() {
            return duration;
        }

        public Code getError() {
            return error;
        }

        private long calculateDuration() {
            return System.currentTimeMillis() - startTime;
        }
    }
}
