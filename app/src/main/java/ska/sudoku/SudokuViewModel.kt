package ska.sudoku

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SudokuViewModel : ViewModel() {

    companion object {
        const val max = 9
    }

    private var grid: List<Cell> = cleanList()
    private var solverTask: SolverTask? = null

    val solvedEnabled = ObservableBoolean(true)
    val loadingVisible = ObservableInt(View.GONE)
    val gridAdapter = GridAdapter(grid, max)
    val resultLiveData = MutableLiveData<Result>()

    fun onSolveClicked() {
        gridAdapter.cellsDisabled = true
        solvedEnabled.set(false)
        loadingVisible.set(View.VISIBLE)

        solverTask = SolverTask(max) { result ->
            loadingVisible.set(View.GONE)
            gridAdapter.updateAdapter(result.grid)
            resultLiveData.value = result
        }
        solverTask?.execute(*grid.toTypedArray())
    }

    fun onResetClicked() {
        solverTask?.cancel(true)
        grid = cleanList()
        solvedEnabled.set(true)
        gridAdapter.updateAdapter(grid)
        gridAdapter.cellsDisabled = false
    }

    private fun cleanList(): List<Cell> =
            mutableListOf<Cell>().apply {
                (0..max * max).forEach { this += Cell(it) }
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
