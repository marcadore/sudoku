package ska.sudoku

import android.graphics.Rect
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield

class SudokuViewModel : ViewModel() {

    companion object {
        const val max = 9
    }

    private var grid: List<Cell> = cleanList()

    private val _solveEnabled = MutableLiveData<Boolean>()
    val solveEnabled: LiveData<Boolean> = _solveEnabled

    private val _loadingVisible = MutableLiveData<Boolean>()
    val loadingVisible: LiveData<Boolean> = _loadingVisible

    val gridAdapter = GridAdapter(grid, max)

    private val _resultLiveData = MutableLiveData<Result>()
    val resultLiveData: LiveData<Result> = _resultLiveData

    private val cellRectsLiveData = MutableLiveData<List<Rect>>()

    var job: Job? = null

    fun onSolveClicked() {
        gridAdapter.cellsDisabled = true
        _solveEnabled.value = false
        _loadingVisible.value = true

        job = viewModelScope.launch {
            val startTime = System.currentTimeMillis()
            val result = solve(max, grid, startTime)
            _loadingVisible.value = false
            gridAdapter.updateAdapter(result.grid)
            _resultLiveData.value = result
            println(this@SudokuViewModel.toString())
        }
    }

    fun onResetClicked() {
        job?.cancel()
        grid = cleanList()
        _solveEnabled.value = true
        gridAdapter.updateAdapter(grid)
        gridAdapter.cellsDisabled = false
        _resultLiveData.value = Result(error = Result.Code.USER_CANCELLED)
        _loadingVisible.value = false
    }

    private fun cleanList(): List<Cell> =
            mutableListOf<Cell>().apply {
                (0..max * max).forEach { this += Cell(it) }
            }

    override fun toString(): String {
        val builder = StringBuilder()
        grid.indices.forEach {

            if (it % max == 0) builder.append("\n")
            if (it % (max * 3) == 0) builder.append("-------------------------------\n")
            if (it % 3 == 0) builder.append("|")

            builder.append(" ${grid[it]} ")
        }
        return builder.toString()
    }

    fun getCellRects(): LiveData<List<Rect>>{
        cellRectsLiveData.value = null
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                delay(500)
            }
            cellRectsLiveData.value = gridAdapter.getCellRects()
        }
        return cellRectsLiveData
    }

    fun fillCell(position: Int, text: String) = gridAdapter.fillCell(position, text)

    private suspend fun solve(max: Int, grid: List<Cell>, startTime: Long): Result {
        val solver = Solver(max)
        var currentCell = 0

        return withContext(Dispatchers.IO) {
            if (!solver.isSolvable(grid)) {
                Result(grid, Result.Code.NOT_SOLVABLE, startTime)
            } else {
                var lastResult = true
                while (currentCell < (max * max)) {
                    yield()

                    if (currentCell == -1) {
                        return@withContext Result(grid, Result.Code.NOT_SOLVABLE, startTime)
                    }

                    val cell = grid[currentCell]
                    if (cell.state == Cell.State.USER) {
                        currentCell = currentCell.nextCell(lastResult)
                        continue
                    }
                    lastResult = solver.solveCell(grid, cell)
                    currentCell = currentCell.nextCell(lastResult)
                }
                grid.filter { it.state != Cell.State.USER }
                    .forEach { it.markAsSolved() }

                Result(grid, startTime = startTime)
            }
        }
    }

    private fun Int.nextCell(success: Boolean): Int =
        if (success) inc()
        else dec()
}
