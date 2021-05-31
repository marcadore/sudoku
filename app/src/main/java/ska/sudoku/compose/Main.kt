package ska.sudoku.compose

import android.graphics.Rect
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ska.sudoku.R
import ska.sudoku.SudokuViewModel
import ska.sudoku.popup.PopupOverlay

@ExperimentalAnimationApi
@Composable
fun Main(
    viewModel: SudokuViewModel,
    cellRects: State<List<Rect>>
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffdddddd))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                AndroidView({ context ->
                    RecyclerView(context).apply {
                        layoutManager = GridLayoutManager(context, SudokuViewModel.max)
                        adapter = viewModel.gridAdapter
                        setHasFixedSize(true)
                    }
                }, modifier = Modifier.fillMaxWidth())

                AndroidView({ context ->
                    PopupOverlay(context).apply {
                        touchedListener = viewModel::fillCell
                    }
                }, update = {
                    it.buttons.addAll(cellRects.value)
                })
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = viewModel::onResetClicked) {
                    Text(stringResource(R.string.button_reset))
                }
                val enabled: Boolean by viewModel.solveEnabled.observeAsState(true)
                Button(
                    onClick = viewModel::onSolveClicked,
                    enabled = enabled
                ) {
                    Text(stringResource(R.string.button_solve))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        val loadingVisible: Boolean by viewModel.loadingVisible.observeAsState(false)
        AnimatedVisibility(visible = loadingVisible) {
            CircularProgressIndicator()
        }
    }
}