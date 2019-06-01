package ska.sudoku

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ska.sudoku.databinding.MainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewModelProviders.of(this).get(SudokuViewModel::class.java).let {
            DataBindingUtil.setContentView<MainBinding>(this, R.layout.main).viewModel = it
            it.resultLiveData.observe(this,
                    Observer { result ->
                        Toast.makeText(this, result.message(), Toast.LENGTH_SHORT).show()
                    })
        }
    }

    private fun Result.message(): String =
            when (error) {
                Result.Code.NO_ERROR -> getString(R.string.toast_solved_time, duration)
                Result.Code.NOT_SOLVABLE -> getString(R.string.toast_not_solvable)
                else -> getString(R.string.toast_unknown_error)
            }
}
