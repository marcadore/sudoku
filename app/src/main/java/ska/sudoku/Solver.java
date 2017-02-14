package ska.sudoku;

import android.os.AsyncTask;

public class Solver extends AsyncTask<Void, Void, Void> {

    interface SolverCallback {
        void onSolved(long timeMillis);
        void onCancelled();
    }

    private final int max;

    private int currentCell;
    private Grid grid;
    private int firstCell;
    private SolverCallback listener;
    private long startTime;

    public Solver(Grid grid, int max, SolverCallback listener) {
        this.grid = grid;
        this.max = max;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        startTime = System.currentTimeMillis();
    }

    @Override
    protected Void doInBackground(Void... params) {
        findFirstNotPreFilledCell();

        boolean lastResult = true;
        while (currentCell < (max * max) && currentCell > -1) {
            if (isCancelled()) break;

            Cell cell = grid.getCell(currentCell);
            if (cell.isPreFilled()) {
                setCurrentCell(lastResult);
                continue;
            }
            lastResult = solver(cell);
            if (!lastResult && cell.getNr() == firstCell && cell.getValue() == max)
                break;
            setCurrentCell(lastResult);
//            Log.d(getClass().getSimpleName(), "" + currentCell);
            Thread.yield();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        System.out.println(grid.toString());
        updateGrid();
        if (listener != null)
            listener.onSolved(System.currentTimeMillis() - startTime);
    }

    @Override
    protected void onCancelled(Void result) {
        if (listener != null) listener.onCancelled();
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

    private void findFirstNotPreFilledCell() {
        for (int i = 0; i < max * max; i++) {
            if (!grid.getCell(i).isPreFilled()) {
                firstCell = i;
                break;
            }
        }
    }

    private void updateGrid() {
        for (int i = 0; i < max * max; i++)
            grid.getCell(i).notifyChanged();
    }
}
