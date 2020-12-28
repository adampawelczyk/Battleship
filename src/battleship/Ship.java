package battleship;
import java.util.Arrays;

public class Ship {
    String name;
    int size;
    char[] cells;
    int lives;
    int rowBegin;
    int columnBegin;
    int rowEnd;
    int columnEnd;
    boolean isPlaced;

    Ship(String name, int size) {
        this.name = name;
        this.size = size;
        this.lives = size;
        this.cells = new char[size];
        Arrays.fill(cells, 'O');
        this.isPlaced = false;
    }

    void place(int rowBegin, int columnBegin, int rowEnd, int columnEnd) {
        this.rowBegin = rowBegin;
        this.columnBegin = columnBegin;
        this.rowEnd = rowEnd;
        this.columnEnd = columnEnd;
        this.isPlaced = true;
    }

    void hit() {
        lives--;
    }

    int getSize() {
        return this.size;
    }

    int getRowBegin() {
        return rowBegin;
    }

    int getColumnBegin() {
        return columnBegin;
    }

    int getRowEnd() {
        return rowEnd;
    }

    int getColumnEnd() {
        return columnEnd;
    }

    String getName() {
        return name;
    }

    boolean isPlaced() {
        return isPlaced;
    }

    int getLives() {
        return lives;
    }
}
