package ska.sudoku

import android.graphics.Color
import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView

class GridAdapter(
        private var grid: List<Cell>,
        private val maxValue: Int
) : RecyclerView.Adapter<ViewHolder>() {

    var cellsDisabled = false

    private val onTextChanged: (String, Int) -> Unit = { text, position ->
        if (text.isNotEmpty() && !cellsDisabled) {
            val cell = grid[position]
            val value = Integer.valueOf(text)

            cell.prefill(value)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.cell, parent, false) as EditText

        return ViewHolder(view, onTextChanged)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cell = grid[position]

        holder.editText.setText(
                if (cell.value > 0) Integer.toString(cell.value)
                else null)
        holder.editText.setBackgroundColor(
                if (isEvenSection(position, maxValue)) Color.LTGRAY
                else Color.WHITE)
        holder.editText.setTypeface(holder.editText.typeface,
                when (cell.state) {
                    Cell.State.USER -> Typeface.BOLD
                    else -> Typeface.NORMAL
                })
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
        private val onTextChanged: (String, Int) -> Unit
) : RecyclerView.ViewHolder(editText), TextWatcher {

    init {
        editText.addTextChangedListener(this)
    }

    override fun afterTextChanged(s: Editable) {
        onTextChanged.invoke(editText.text.toString(), adapterPosition)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}
