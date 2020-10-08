package ska.sudoku

class Cell internal constructor(internal val nr: Int) {
    enum class State { EMPTY, USER, SOLVED }

    var value: Int = 0
    var state = State.EMPTY

    fun prefill(value: Int) {
        state = if (value == 0) State.EMPTY else State.USER
        this.value = value
    }

    fun markAsSolved() {
        state = State.SOLVED
    }

    override fun toString() = value.toString()
}
