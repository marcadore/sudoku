package ska.sudoku

import android.content.Context

class Result(
    val grid: List<Cell> = emptyList(),
    val error: Code = Code.NO_ERROR,
    val startTime: Long = 0
) {

    private val endTime: Long = System.currentTimeMillis()

    enum class Code {
        NO_ERROR,
        NOT_SOLVABLE,
        USER_CANCELLED;

        fun message(context: Context, duration: Long): String =
            when (this) {
                NO_ERROR -> context.getString(R.string.toast_solved_time, duration)
                NOT_SOLVABLE -> context.getString(R.string.toast_not_solvable)
                else -> context.getString(R.string.toast_user_cancelled)
            }
    }

    val duration: Long = endTime - startTime
}
