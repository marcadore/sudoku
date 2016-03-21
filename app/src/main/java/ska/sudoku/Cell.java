package ska.sudoku;

public class Cell {

    interface ValueChangedListener {
        void onValueChanged(int value);
    }

    private int nr;
    private int value;
    private boolean isPreFilled = false;
    private ValueChangedListener listener;

    public Cell(int nr) {
        this.nr = nr;
        value = 0;
    }

    public int getNr() {
        return nr;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isPreFilled() {
        return isPreFilled;
    }

    public void setPreFilled(int value) {
        isPreFilled = value != 0;
        setValue(value);
    }

    public void setListener(ValueChangedListener listener) {
        this.listener = listener;
    }

    public void notifyChanged() {
        if (listener != null) {
            listener.onValueChanged(value);
        }
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
