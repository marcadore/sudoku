package ska.sudoku

import android.arch.lifecycle.ViewModel

class SudokuViewModel : ViewModel() {

    val max = 9
    val grid: MutableList<Cell> = mutableListOf()
    val solverObserver = SolverObserver()

    init {
        initialize()
    }

    private fun initialize() {
        (0..max * max).forEach { grid += Cell(it) }
    }

    fun onSolveClicked() {
        solverObserver.query(grid, max)
    }

    fun onResetClicked() {
        solverObserver.cancel()
        grid.clear()
        initialize()
    }

    override fun toString(): String {
        val builder = StringBuilder()
        (0..grid.size).forEach {

            if (it % max == 0) builder.append("\n")
            if (it % (max * 3) == 0) builder.append("-------------------------------\n")
            if (it % 3 == 0) builder.append("|")

            builder.append(" ${grid[it]} ")
        }
        return builder.toString()
    }
}
