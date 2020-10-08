package ska.sudoku

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("spanCount")
fun RecyclerView.spanCount(spanCount: Int) {
    (layoutManager as? GridLayoutManager)?.spanCount = spanCount
}
