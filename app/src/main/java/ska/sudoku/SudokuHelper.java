package ska.sudoku;

import android.graphics.Point;

import java.util.List;

class SudokuHelper {

    static Point getSection(final int cell, final int max) {
        Point p = new Point();
        int itemsPerSection = max / 3;

        p.x = (cell % max) / itemsPerSection;
        p.y = cell / (max * itemsPerSection);

        return p;
    }

    static boolean checkValue(final List<Cell> grid, final Cell cell, final int value, final int maxValue) {
        return checkRow(grid, cell, value, maxValue) && checkColumn(grid, cell, value, maxValue) && checkSection(grid, cell, value, maxValue);
    }

    private static boolean checkRow(final List<Cell> grid, final Cell cell, final int value, final int maxValue) {
        int row = cell.getNr() / maxValue;
        for (int i = 0; i < maxValue; i++) {
            if (grid.get(i + maxValue * row).getValue() == value)
                return false;
        }
        return true;
    }

    private static boolean checkColumn(final List<Cell> grid, final Cell cell, final int value, final int maxValue) {
        int column = cell.getNr() % maxValue;
        for (int i = 0; i < maxValue; i++) {
            if (grid.get(i * maxValue + column).getValue() == value)
                return false;
        }
        return true;
    }

    private static boolean checkSection(final List<Cell> grid, final Cell cell, final int value, final int maxValue) {
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
}
