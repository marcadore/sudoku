package ska.sudoku

class Cell internal constructor(internal val nr: Int) {
    var value: Int = 0
    var isPreFilled = false
        private set

    fun setPreFilled(value: Int) {
        isPreFilled = value != 0
        this.value = value
    }

    override fun toString() = value.toString()
}
