package ska.sudoku;

class Cell {

    private int nr;
    private int value;
    private boolean isPreFilled = false;

    Cell(int nr) {
        this.nr = nr;
        value = 0;
    }

    int getNr() {
        return nr;
    }

    int getValue() {
        return value;
    }

    void setValue(int value) {
        this.value = value;
    }

    boolean isPreFilled() {
        return isPreFilled;
    }

    void setPreFilled(int value) {
        isPreFilled = value != 0;
        setValue(value);
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
