package ska.sudoku

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.main.*
import ska.sudoku.databinding.MainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: SudokuViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SudokuViewModel::class.java)
        DataBindingUtil.setContentView<MainBinding>(this, R.layout.main).viewModel = viewModel
        viewModel.resultLiveData.observe(this,
                { result ->
                    Toast.makeText(this, result.message(), Toast.LENGTH_SHORT).show()
                })
    }

    override fun onResume() {
        super.onResume()

        overlay.touchedListener = viewModel::fillCell
        overlay.post {
            overlay.buttons.addAll(viewModel.getCellRects())
        }
    }

    override fun onPause() {
        overlay.touchedListener = null
        overlay.buttons.clear()
        super.onPause()
    }

    private fun Result.message(): String =
            when (error) {
                Result.Code.NO_ERROR -> getString(R.string.toast_solved_time, duration)
                Result.Code.NOT_SOLVABLE -> getString(R.string.toast_not_solvable)
                else -> getString(R.string.toast_unknown_error)
            }
}
