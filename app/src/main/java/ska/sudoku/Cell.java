package ska.sudoku;

public class Cell {

    private int nr;
    private int value;
    private boolean isPreFilled = false;

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

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
