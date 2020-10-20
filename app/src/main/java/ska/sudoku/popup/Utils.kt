package ska.sudoku.popup

import android.content.res.Resources.getSystem
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

fun View.getLocationOnScreen(): Rect =
    IntArray(2).apply {
        getLocationOnScreen(this)
    }.run {
        Rect(get(0), get(1), get(0) + width, get(1) + height)
    }

private fun MotionEvent.hit(o: Rect?) =
    if (o == null) false
    else (rawX > o.left && rawX < o.right && rawY > o.top && rawY < o.bottom)

fun Iterable<TextView>.findTouched(event: MotionEvent): String? =
    find { event.hit(it.getLocationOnScreen()) }?.text?.toString()

fun Iterable<Rect>.findTouched(event: MotionEvent): Int =
    indexOfFirst { event.hit(it) }

fun Int.toPx(): Int = (this * getSystem().displayMetrics.density).toInt()
