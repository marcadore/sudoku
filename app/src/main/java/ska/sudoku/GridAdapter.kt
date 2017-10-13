package ska.sudoku

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText

class GridAdapter(
        private var grid: List<Cell>,
        private val maxValue: Int
) : RecyclerView.Adapter<ViewHolder>() {

    var cellsDisabled = false
    private val helper = SudokuHelper()
    private val listener = object : TextChangedListener {
        override fun onTextChanged(text: String, position: Int) {
            if (text.isEmpty() || cellsDisabled) return

            val cell = grid[position]
            val value = Integer.valueOf(text)

            cell.prefill(value)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.cell, parent, false) as EditText

        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val value = grid[position].value
        holder.editText.setText(
                if (value > 0) Integer.toString(value)
                else null)
        holder.editText.setBackgroundColor(
                if (helper.isEvenSection(position, maxValue)) Color.LTGRAY
                else Color.WHITE)
    }

    override fun getItemCount(): Int =
            Math.pow(maxValue.toDouble(), 2.0).toInt()

    fun updateAdapter(grid: List<Cell>) {
        this.grid = grid
        notifyDataSetChanged()
    }
}

class ViewHolder(
        val editText: EditText,
        private val listener: TextChangedListener
) : RecyclerView.ViewHolder(editText), TextWatcher {

    init {
        editText.addTextChangedListener(this)
    }

    override fun afterTextChanged(s: Editable) {
        listener.onTextChanged(editText.text.toString(), adapterPosition)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}

interface TextChangedListener {
    fun onTextChanged(text: String, position: Int)
}
