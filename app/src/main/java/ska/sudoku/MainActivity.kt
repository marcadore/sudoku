package ska.sudoku

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.main.*

class MainActivity : AppCompatActivity(), View.OnClickListener, Observer<Result> {

    lateinit var viewModel: SudokuViewModel
    lateinit var gridAdapter: GridAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        viewModel = ViewModelProviders.of(this).get(SudokuViewModel::class.java)

        button_solve.setOnClickListener(this)
        button_reset.setOnClickListener(this)
        gridAdapter = GridAdapter(viewModel.grid, viewModel.max)

        sudoku_grid.setHasFixedSize(true)
        sudoku_grid.layoutManager = GridLayoutManager(this, viewModel.max)
        sudoku_grid.adapter = gridAdapter

        viewModel.solverObserver.observe(this, this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_solve -> {
                button_solve.isEnabled = false
                gridAdapter.cellsDisabled = true
                loading_bar.visibility = View.VISIBLE
                viewModel.onSolveClicked()
            }
            R.id.button_reset -> {
                viewModel.onResetClicked()
                gridAdapter.updateAdapter(viewModel.grid)
                gridAdapter.cellsDisabled = false
                button_solve.isEnabled = true
            }
        }
    }

    override fun onChanged(result: Result?) {
        loading_bar.visibility = View.GONE
        result?.let {
            gridAdapter.updateAdapter(result.grid)
            getMessage(result).let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getMessage(result: Result): String? =
            when (result.error) {
                Result.Code.NO_ERROR -> getString(R.string.toast_solved_time, result.duration)
                Result.Code.NOT_SOLVABLE -> getString(R.string.toast_not_solvable)
                else -> null
            }
}
