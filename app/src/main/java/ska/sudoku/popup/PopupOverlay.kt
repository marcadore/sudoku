package ska.sudoku.popup

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.graphics.Rect
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.PopupWindow
import android.widget.TextView
import ska.sudoku.R

class PopupOverlay : View {

    var buttons: MutableList<Rect> = mutableListOf()
    var touchedListener: ((position: Int, text: String) -> Unit)? = null
    private val numbers: MutableList<TextView> = mutableListOf()
    private var touched: Int = -1
    private var popupWindow: PopupWindow? = null
    private val popupView: View

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        with(context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater) {
            popupView = inflate(R.layout.dialog, null)

            (1..9).forEach { nr ->
                resources.getIdentifier("button$nr", "id", context?.packageName)
                        .takeIf { it != 0 }
                        ?.let { id ->
                            popupView.findViewById<TextView>(id).run {
                                numbers += this
                            }
                        }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                touched = buttons.findTouched(event)
                showPopup(event.rawX.toInt(), event.rawY.toInt())
            }
            MotionEvent.ACTION_UP -> {
                numbers.findTouched(event)
                    ?.let {
                        touchedListener?.invoke(touched, it)
                        touched = -1
                    }
                popupWindow?.dismiss()
                performClick()
            }
            MotionEvent.ACTION_CANCEL -> popupWindow?.dismiss()
        }
        return true
    }

    private fun showPopup(x: Int, y: Int) {
        popupWindow = PopupWindow(popupView, 100.toPx(), 100.toPx(), true)
        popupWindow?.showAtLocation(this@PopupOverlay, Gravity.NO_GRAVITY, x, y)

        popupView.setOnClickListener { _ ->
            popupWindow?.dismiss()
        }

        popupView.post {
            popupWindow?.update(x - popupView.width / 2, y - popupView.height / 2, -1, -1)
        }
    }
}
