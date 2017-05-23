package ska.sudoku;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;

class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    private List<Cell> grid;
    private final int maxValue;
    private boolean listenerDisabled = false;
    private final SudokuHelper helper = new SudokuHelper();

    private ViewHolder.TextChangedListener listener = new GridAdapter.ViewHolder.TextChangedListener() {
        @Override
        public void onTextChanged(String text, int position) {
            if (text.isEmpty() || listenerDisabled) return;

            Cell cell = grid.get(position);
            Integer value = Integer.valueOf(text);
            if (value != null && cell != null)
                cell.setPreFilled(value);
        }
    };

    GridAdapter(@NonNull final List<Cell> grid, final int maxValue) {
        this.grid = grid;
        this.maxValue = maxValue;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EditText view = (EditText) LayoutInflater.from(parent.getContext()).inflate(R.layout.cell, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int value = grid.get(position).getValue();
        holder.editText.setText(value > 0 ? Integer.toString(value) : null);
        holder.editText.setBackgroundColor(
                helper.isEvenSection(position, maxValue) ? Color.LTGRAY : Color.WHITE);
    }

    @Override
    public int getItemCount() {
        return (int) Math.pow(maxValue, 2);
    }

    void updateAdapter(List<Cell> grid) {
        this.grid = grid;
        notifyDataSetChanged();
    }

    void setCellsDisabled(boolean disabled) {
        listenerDisabled = disabled;
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
