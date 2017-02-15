package ska.sudoku;

import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    private Grid grid;
    private boolean listenerDisabled = false;

    private ViewHolder.TextChangedListener listener = new GridAdapter.ViewHolder.TextChangedListener() {
        @Override
        public void onTextChanged(String text, int position) {
            if (text.isEmpty() || listenerDisabled) return;

            Cell cell = grid.getCell(position);
            Integer value = Integer.valueOf(text);
            if (value != null && cell != null)
                cell.setPreFilled(value);
        }
    };

    public GridAdapter(Grid grid) {
        this.grid = grid;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EditText view = (EditText) LayoutInflater.from(parent.getContext()).inflate(R.layout.cell, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int value = grid.getCell(position).getValue();
        holder.editText.setText(value > 0 ? Integer.toString(value) : null);
        holder.editText.setBackgroundColor(
                isEvenSection(position, grid.getMaxValue()) ? Color.LTGRAY : Color.WHITE);
    }

    @Override
    public int getItemCount() {
        return grid.getMaxValue() * grid.getMaxValue();
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
        notifyDataSetChanged();
    }

    public void setCellsDisabled(boolean disabled) {
        listenerDisabled = disabled;
    }

    private boolean isEvenSection(int cellNr, int maxValue) {
        Point p = SudokuHelper.getSection(cellNr, maxValue);
        return p.equals(0, 0) || p.equals(2, 0) || p.equals(1, 1) || p.equals(0, 2) || p.equals(2, 2);
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements TextWatcher {

        interface TextChangedListener {
            void onTextChanged(String text, int position);
        }

        EditText editText;
        private final TextChangedListener listener;

        ViewHolder(EditText itemView, TextChangedListener listener) {
            super(itemView);
            editText = itemView;
            editText.addTextChangedListener(this);
            this.listener = listener;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (listener != null) listener.onTextChanged(editText.getText().toString(), getAdapterPosition());
        }
    }

}
