package ska.sudoku;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener, Solver.SolverCallback {

    private static final int MAX = 9;
    private Grid grid;
    private View solveButton;
    private View progressBar;
    private Solver solver;
    private GridAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        grid = new Grid(MAX);

        solveButton = findViewById(R.id.button_solve);
        solveButton.setOnClickListener(this);
        findViewById(R.id.button_reset).setOnClickListener(this);

        progressBar = findViewById(R.id.progress);

        gridAdapter = new GridAdapter(grid);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.grid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, MAX));
        recyclerView.setAdapter(gridAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_solve:
                solveButton.setEnabled(false);
                gridAdapter.setCellsDisabled(true);
                progressBar.setVisibility(View.VISIBLE);

                solver = new Solver(MAX, this);
                solver.execute(grid);
                break;
            case R.id.button_reset:
                grid = new Grid(MAX);
                gridAdapter.setCellsDisabled(false);
                gridAdapter.setGrid(grid);
                solveButton.setEnabled(true);

                if (solver != null) solver.cancel(true);
                break;
        }
    }

    @Override
    public void onFinished(Solver.Result result) {
        if (progressBar != null)
            progressBar.setVisibility(View.GONE);
        gridAdapter.setGrid(result.getGrid());
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
