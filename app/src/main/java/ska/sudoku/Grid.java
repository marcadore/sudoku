package ska.sudoku;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

public class Grid {

    private List<Cell> grid = new ArrayList<>();
    private final int maxValue;

    public Grid(int maxValue) {
        this.maxValue = maxValue;
        for (int i = 0; i < maxValue * maxValue; i++) {
            grid.add(new Cell(i));
        }
    }

    public int getMaxValue() {
        return maxValue;
    }

    public Cell getCell(int nr) {
        return grid.get(nr);
    }

    public boolean checkValue(Cell cell, int value) {
        return checkRow(cell, value) && checkColumn(cell, value) && checkSection(cell, value);
    }

    private boolean checkRow(Cell cell, int value) {
        int row = cell.getNr() / maxValue;
        for (int i = 0; i < maxValue; i++) {
            if (grid.get(i + maxValue * row).getValue() == value)
                return false;
        }
        return true;
    }

    private boolean checkColumn(Cell cell, int value) {
        int column = cell.getNr() % maxValue;
        for (int i = 0; i < maxValue; i++) {
            if (grid.get(i * maxValue + column).getValue() == value)
                return false;
        }
        return true;
    }

    private boolean checkSection(Cell cell, int value) {
        Point p = SudokuHelper.getSection(cell.getNr(), maxValue);
        int itemsPerSection = maxValue / 3;
        int sectionRow = p.y;
        int sectionColumn = p.x;

        for (int i = 0; i < itemsPerSection; i++) {
            int firstRow = i + sectionRow * maxValue * 3 + sectionColumn * itemsPerSection;
            for (int j = 0; j < itemsPerSection; j++) {
                int currentCell = firstRow + j * maxValue;
                if (cell.getNr() != currentCell)
                    if (grid.get(currentCell).getValue() == value)
                        return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < grid.size(); i++) {
            if (i % maxValue == 0)
                builder.append('\n');
            if (i % (maxValue * 3) == 0)
                builder.append("-------------------------------\n");
            if (i % 3 == 0)
                builder.append('|');

            builder.append(" " + grid.get(i) + " ");
        }
        return builder.toString();
    }
}
