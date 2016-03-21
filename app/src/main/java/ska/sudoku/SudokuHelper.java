package ska.sudoku;

import android.graphics.Point;

public class SudokuHelper {

    public static Point getSection(int cell, int max) {
        Point p = new Point();
        int itemsPerSection = max / 3;

        p.x = (cell % max) / itemsPerSection;
        p.y = cell / (max * itemsPerSection);

        return p;
    }
}
