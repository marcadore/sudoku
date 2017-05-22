package ska.sudoku;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends LifecycleActivity implements View.OnClickListener, Observer<Solver.Result> {

    public static final int MAX = 9;
    private SudokuViewModel viewModel;
    private View solveButton;
    private View progressBar;
    private GridAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        viewModel = ViewModelProviders.of(this).get(SudokuViewModel.class);

        solveButton = findViewById(R.id.button_solve);
        solveButton.setOnClickListener(this);
        findViewById(R.id.button_reset).setOnClickListener(this);

        progressBar = findViewById(R.id.progress);

        gridAdapter = new GridAdapter(viewModel.getGrid(), MAX);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.grid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, MAX));
        recyclerView.setAdapter(gridAdapter);

        viewModel.getResult().observe(this, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_solve:
                solveButton.setEnabled(false);
                gridAdapter.setCellsDisabled(true);
                progressBar.setVisibility(View.VISIBLE);
                viewModel.solve();
                break;
            case R.id.button_reset:
                viewModel.cancel();
                gridAdapter.updateAdapter(viewModel.getGrid());
                gridAdapter.setCellsDisabled(false);
                solveButton.setEnabled(true);
                break;
        }
    }

    @Override
    public void onChanged(@Nullable Solver.Result result) {
        if (progressBar != null)
            progressBar.setVisibility(View.GONE);
        gridAdapter.updateAdapter(result.getGrid());
        String msg = getMessage(result);
        if (msg != null) Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private String getMessage(Solver.Result result) {
        switch (result.getError()) {
            case NO_ERROR:
                return getString(R.string.toast_solved_time, result.getDuration());
            case NOT_SOLVABLE:
                return getString(R.string.toast_not_solvable);
        }
        return null;
    }
}
