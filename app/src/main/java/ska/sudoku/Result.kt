package ska.sudoku

class Result(
    val grid: List<Cell>,
    val error: Code = Code.NO_ERROR
) {

    enum class Code {
        NO_ERROR,
        NOT_SOLVABLE,
        USER_CANCELLED
    }

    val duration: Long = System.currentTimeMillis() - SolverTask.startTime
}
