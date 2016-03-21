package ska.sudoku;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class GridView extends LinearLayout {

    private List<CellView> cellViews = new ArrayList<>();

    public GridView(Context context) {
        super(context);
        init();
    }

    public GridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
    }

    public void setGrid(Grid grid) {
        removeAllViews();

        LinearLayout.LayoutParams rowLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams cellLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cellLp.setMargins(1, 1, 1, 1);

        LinearLayout row = null;
        for (int i = 0; i < 9 * 9; i++) {
            if (i % 9 == 0) {
                row = new LinearLayout(getContext());
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setGravity(Gravity.CENTER);
                row.setBackgroundColor(Color.BLACK);
                addView(row, rowLp);
            }
            CellView cellView = new CellView(getContext());
            cellView.setCell(grid.getCell(i));
            tintCell(cellView, i);
            row.addView(cellView, cellLp);
            cellViews.add(cellView);
        }

        invalidate();
        requestLayout();
    }

    public void disableCellViews() {
        for (CellView cellView : cellViews) {
            cellView.disableEdit();
        }
    }

    private void tintCell(CellView cellView, int cellNr) {
        Point p = SudokuHelper.getSection(cellNr, 9);
        if (p.equals(0, 0) || p.equals(2, 0) || p.equals(1, 1) || p.equals(0, 2) || p.equals(2, 2))
            cellView.setTextBackgroundColor(Color.LTGRAY);
    }
}
