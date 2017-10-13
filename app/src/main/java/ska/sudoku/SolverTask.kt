package ska.sudoku

import android.os.AsyncTask

class SolverTask(private val max: Int, private val listener: SolverCallback) :
        AsyncTask<Cell, Void, Result>() {

    companion object {
        var startTime: Long = 0
    }

    private val solver: Solver by lazy {
        Solver(max)
    }
    private var currentCell: Int = 0

    interface SolverCallback {
        fun onFinished(result: Result)
    }

    override fun onPreExecute() {
        super.onPreExecute()
        startTime = System.currentTimeMillis()
    }

    override fun doInBackground(vararg params: Cell): Result {
        val grid: List<Cell> = params.toList()
        if (!solver.isSolvable(grid)) {
            cancel(true)
            return Result(grid, Result.Code.NOT_SOLVABLE)
        }

        var lastResult = true
        while (currentCell < (max * max)) {
            if (isCancelled) {
                return Result(grid, Result.Code.USER_CANCELLED)
            }

            val cell = grid[currentCell]
            if (cell.isPreFilled) {
                setCurrentCell(lastResult)
                continue
            }
            lastResult = solver.solveCell(grid, cell)
            setCurrentCell(lastResult)
            Thread.yield()
        }
        return Result(grid)
    }

    override fun onPostExecute(result: Result) {
        super.onPostExecute(result)
        System.out.println(result.grid.toString())
        listener.onFinished(result)
    }

    override fun onCancelled(result: Result) {
        super.onCancelled(result)
        listener.onFinished(result)
    }

    private fun setCurrentCell(success: Boolean) {
        if (success) currentCell++
        else currentCell--
    }
}
