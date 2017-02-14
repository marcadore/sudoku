package ska.sudoku;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

public class CellView extends FrameLayout implements TextWatcher, Cell.ValueChangedListener {

    private Cell cell;
    private TextView textView;

    public CellView(Context context) {
        super(context);
        init();
    }

    public CellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CellView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.cell, this);
        textView = (TextView) findViewById(R.id.cell_text);
        textView.addTextChangedListener(this);
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = textView.getText().toString();
        if (text.isEmpty())
            return;
        Integer value = Integer.valueOf(text);
        if (value != null && cell != null)
            cell.setPreFilled(value);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    public void disableEdit() {
        textView.setKeyListener(null);
        textView.removeTextChangedListener(this);
        cell.setListener(this);
    }

    @Override
    public void onValueChanged(final int value) {
        textView.setText("" + value);
        textView.setTextColor(Color.BLACK);
    }

    public void setTextBackgroundColor(int color) {
        textView.setBackgroundColor(color);
    }
}
