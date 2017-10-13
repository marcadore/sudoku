package ska.sudoku

import android.arch.lifecycle.LiveData

class SolverObserver : LiveData<Result>(), SolverTask.SolverCallback {

    private var solverTask: SolverTask? = null

    fun query(grid: List<Cell>, max: Int) {
        solverTask = SolverTask(max, this)
        solverTask!!.execute(*grid.toTypedArray())
    }

    fun cancel() {
        solverTask?.cancel(true)
    }

    override fun onFinished(result: Result) {
        value = result
    }

}
