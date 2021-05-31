package ska.sudoku

import android.graphics.Color
import android.graphics.Rect
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ska.sudoku.popup.getLocationOnScreen
import kotlin.math.pow

class GridAdapter(
    private var grid: List<Cell>,
    private val maxValue: Int
) : RecyclerView.Adapter<ViewHolder>() {

    var cellsDisabled = false
    private val cells = mutableListOf<TextView>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.cell, parent, false) as TextView

        cells.add(view)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cell = grid[position]

        holder.editText.text = if (cell.value > 0) cell.value.toString() else null
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
            maxValue.toDouble().pow(2.0).toInt()

    fun updateAdapter(grid: List<Cell>) {
        this.grid = grid
        cells.clear()
        notifyDataSetChanged()
    }

    fun fillCell(position: Int, text: String) {
        if (text.isNotEmpty() && !cellsDisabled && position > -1 && position < grid.size) {
            val cell = grid[position]
            val value = Integer.valueOf(text)

            cell.prefill(value)
            notifyItemChanged(position)
        }
    }

    fun getCellRects(): List<Rect> = cells.map { it.getLocationOnScreen() }
}

class ViewHolder(
    val editText: TextView
) : RecyclerView.ViewHolder(editText)
