package ska.sudoku

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.ViewModelProvider
import ska.sudoku.compose.Main

@ExperimentalAnimationApi
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: SudokuViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SudokuViewModel::class.java)

        setContent {
            Main(viewModel, viewModel.getCellRects().observeAsState(emptyList()))
        }

        viewModel.resultLiveData.observe(this,
            { result ->
                Toast.makeText(this, result.message(), Toast.LENGTH_SHORT).show()
            })
    }

    private fun Result.message() =
        error.message(this@MainActivity, duration)
}