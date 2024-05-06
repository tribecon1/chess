package chess;

import java.util.HashMap;
import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private int row;
    private int col;
    private static final HashMap<Integer, String> colConverter = new HashMap<>() {{
        put(1, "A");
        put(2, "B");
        put(3, "C");
        put(4, "D");
        put(5, "E");
        put(6, "F");
        put(7, "G");
        put(8, "H");
    }};
    private String officialPosition;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessPosition that)) return false;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "[row: " + row + ", col: " + col + "board pos.: " + officialPosition + "]";
    }

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
        this.officialPosition = String.format("%s%d", colConverter.get(col), row);//added in by TA rec.
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }

    public String getOfficialPosition() {
        return officialPosition;
    }
}
