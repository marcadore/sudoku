package ska.sudoku

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ska.sudoku.databinding.MainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: SudokuViewModel
    private lateinit var binding: MainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SudokuViewModel::class.java)
        
        binding = MainBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        setContentView(binding.root)

        viewModel.resultLiveData.observe(this,
                { result ->
                    Toast.makeText(this, result.message(), Toast.LENGTH_SHORT).show()
                })
    }

    override fun onResume() {
        super.onResume()

        binding.overlay.touchedListener = viewModel::fillCell
        binding.overlay.post {
            binding.overlay.buttons.addAll(viewModel.getCellRects())
        }
    }

    override fun onPause() {
        binding.overlay.touchedListener = null
        binding.overlay.buttons.clear()
        super.onPause()
    }

    private fun Result.message() =
        error.message(this@MainActivity, duration)
}
