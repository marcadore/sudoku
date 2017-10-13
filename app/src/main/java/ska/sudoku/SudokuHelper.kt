package ska.sudoku

internal class SudokuHelper {

    fun isEvenSection(cellNr: Int, maxValue: Int): Boolean {
        val (x, y) = getSection(cellNr, maxValue)
        return (x + y) % 2 == 0
    }

    fun getSection(cell: Int, max: Int): Pair<Int, Int> {
        val itemsPerSection = max / 3

        val x = cell % max / itemsPerSection
        val y = cell / (max * itemsPerSection)

        return Pair(x, y)
    }

    fun checkValue(grid: List<Cell>, cell: Cell, value: Int, maxValue: Int): Boolean {
        return checkRow(grid, cell, value, maxValue) &&
                checkColumn(grid, cell, value, maxValue) &&
                checkSection(grid, cell, value, maxValue)
    }

    private fun checkRow(grid: List<Cell>, cell: Cell, value: Int, maxValue: Int): Boolean {
        val row = cell.nr / maxValue
        return (0 until maxValue)
                .none { value == grid[it + maxValue * row].value }
    }

    private fun checkColumn(grid: List<Cell>, cell: Cell, value: Int, maxValue: Int): Boolean {
        val column = cell.nr % maxValue
        return (0 until maxValue)
                .none { value == grid[it * maxValue + column].value }
    }

    private fun checkSection(grid: List<Cell>, cell: Cell, value: Int, maxValue: Int): Boolean {
        val (sectionColumn, sectionRow) = getSection(cell.nr, maxValue)
        val itemsPerSection = maxValue / 3

        return (0 until itemsPerSection)
                .map { it + sectionRow * maxValue * 3 + sectionColumn * itemsPerSection }
                .none { firstRow ->
                    (0 until itemsPerSection)
                            .map { firstRow + it * maxValue }
                            .filter { cell.nr != it }
                            .any { value == grid[it].value }
                }
    }
}
