package ska.sudoku

import android.os.AsyncTask

class SolverTask(private val max: Int, private val callback: (Result) -> Unit) :
        AsyncTask<Cell, Void, Result>() {

    companion object {
        var startTime: Long = 0
    }

    private val solver: Solver by lazy {
        Solver(max)
    }
    private var currentCell: Int = 0

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
            if (cell.state == Cell.State.USER) {
                nextCell(lastResult)
                continue
            }
            lastResult = solver.solveCell(grid, cell)
            nextCell(lastResult)
            Thread.yield()
        }
        grid.filter { it.state != Cell.State.USER }
                .forEach { it.markAsSolved() }
        return Result(grid)
    }

    override fun onPostExecute(result: Result) {
        super.onPostExecute(result)
        System.out.println(result.grid.toString())
        callback.invoke(result)
    }

    override fun onCancelled(result: Result) {
        super.onCancelled(result)
        callback.invoke(result)
    }

    private fun nextCell(success: Boolean) {
        if (success) currentCell++
        else currentCell--
    }
}
