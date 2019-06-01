package ska.sudoku

internal class Solver(private val max: Int) {

    fun solveCell(grid: List<Cell>, cell: Cell): Boolean {
        if (cell.isPreFilled)
            return true

        var value = cell.value + 1
        while (value <= max) {
            when {
                checkValue(grid, cell, value, max) -> {
                    cell.value = value
                    return true
                }
                else -> value++
            }
        }
        cell.value = 0
        return false
    }

    fun isSolvable(grid: List<Cell>): Boolean {
        return grid
                .filter { it.isPreFilled }
                .none {
                    !isUniqueInRow(grid, it.nr) ||
                            !isUniqueInColumn(grid, it.nr) ||
                            !isUniqueInSection(grid, it.nr)
                }
    }

    private fun isUniqueInRow(grid: List<Cell>, cellNr: Int): Boolean {
        val value = grid[cellNr].value
        val row = cellNr / max

        return (0 until max)
                .map { it + max * row }
                .filter { it != cellNr }
                .none { value == grid[it].value }
    }

    private fun isUniqueInColumn(grid: List<Cell>, cellNr: Int): Boolean {
        val value = grid[cellNr].value
        val column = cellNr % max

        return (0 until max)
                .map { it * max + column }
                .filter { it != cellNr }
                .none { value == grid[it].value }
    }

    private fun isUniqueInSection(grid: List<Cell>, cellNr: Int): Boolean {
        val value = grid[cellNr].value
        val (sectionColumn, sectionRow) = getSection(cellNr, max)
        val itemsPerSection = max / 3

        return (0 until itemsPerSection)
                .map { it + sectionRow * max * 3 + sectionColumn * itemsPerSection }
                .none { firstRow ->
                    (0 until itemsPerSection)
                            .map { firstRow + it * max }
                            .filter { it != cellNr }
                            .any { value == grid[it].value }
                }
    }
}
