package ska.sudoku;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener, Solver.SolverCallback {

    private static final int MAX = 9;
    private GridView gridView;
    private Grid grid;
    private View solveButton;
    private View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        grid = new Grid(MAX);
        gridView = (GridView) findViewById(R.id.grid);
        gridView.setGrid(grid, MAX);

        solveButton = findViewById(R.id.button_solve);
        solveButton.setOnClickListener(this);
        findViewById(R.id.button_reset).setOnClickListener(this);

        progressBar = findViewById(R.id.progress);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_solve:
                v.setEnabled(false);
                gridView.disableCellViews();
                progressBar.setVisibility(View.VISIBLE);

                new Solver(grid, MAX, this).execute();
                break;
            case R.id.button_reset:
                grid = new Grid(MAX);
                gridView.setGrid(grid, MAX);
                solveButton.setEnabled(true);
                break;
        }

    }

    @Override
    public void onSolved(long timeMillis) {
        if (progressBar != null)
            progressBar.setVisibility(View.GONE);
        String msg = getString(R.string.toast_solved_time, timeMillis);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
