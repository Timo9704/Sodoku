package wbh.ema.sodoku;

import java.util.Random;
import java.util.LinkedList;
import java.util.Arrays;

public class Sodoku4x4 {

    int board[][] = new int[4][4];
    Random random;

    public Sodoku4x4() {
        random = new Random();
        int board[][] = new int[4][4];
        fillEntry(board, 0, 0);
        this.board = board;
    }

    /* rekursive Methode mit Backtracking ; erzeugt loesbares
    Sodoku */
    private boolean fillEntry(int[][] board, int row, int col) {
        if (row == 4) return true;
        LinkedList<Integer> candidates = new
                LinkedList<Integer>(Arrays.asList(1, 2, 3, 4));
        for (int i = 0; i < 4; i++) {
            int index = random.nextInt(candidates.size());
            int nextTry = candidates.get(index);
            if (availableInRow(board, row, nextTry) &&
                    availableInColumn(board, col, nextTry) &&
                    availableInBox(board, row, col, nextTry)) {
                board[row][col] = nextTry;
                if (fillEntry(board, col == 3 ? row + 1 : row, (col + 1) % 4))
                    return true;
            }
            candidates.remove(index);
        }
        // System.out.println("Backtrack row " + row + " col " + col );
        board[row][col] = 0; // verwerfe Wahl f r dieses Feld
        return false;
    }

    private boolean availableInRow(int[][] board, int row, int
            num) {
        for (int i = 0; i < 4; i++) {
            if (num == board[row][i]) return false;
        }
        return true;
    }

    private boolean availableInColumn(int[][] board, int col, int
            num) {
        for (int i = 0; i < 4; i++) {
            if (num == board[i][col]) return false;
        }
        return true;
    }

    private boolean availableInBox(int[][] board, int row, int
            col, int num) {
        row = row > 1 ? 2 : 0;
        col = col > 1 ? 2 : 0;
        for (int y = row; y < row + 2; y++) {
            for (int x = col; x < col + 2; x++) {
                if (num == board[y][x]) return false;
            }
        }
        return true;
    }

    public int getValue(int row, int col) {
        return board[row][col];
    }

    public void clearValue(int row, int col) {
        board[row][col] = 0;
    }

    public void clearRandomFields(int howmany) {
        LinkedList<Integer> candidates = new LinkedList<Integer>
                (Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15));
        for (int i = 0; i < howmany; i++) {
            int index = random.nextInt(candidates.size());
            int valueToRemove = candidates.get(index);
            int row = valueToRemove / 4;
            int col = valueToRemove - row * 4;
            board[row][col] = 0;
            candidates.remove(index);
        }
    }

    /**
     * Prüf- und Schreibfunktion, ob Wert in dieser Zelle erlaubt ist und
     * schreiben des Wertes in Zelle
     *
     * @param  row Zeile
     * @param  col Spalte
     * @param  num angeklickter Wert
     * @return      Wert konnte gesetzt werden (true/false)
     */
    public boolean trySetValue(int row, int col, int num) {
        boolean availableInRow = availableInRow(board, row, num);
        boolean availableInColumn = availableInColumn(board, col, num);
        boolean availableInBox = availableInBox(board, row, col, num);

        if(availableInRow && availableInColumn && availableInBox) {
            board[row][col] = num;
            return true;
        }
        return false;
    }

    /**
     * Prüffunktion, ob alle Felder nicht mehr mit 0 belegt sind = Spielende
     *
     * @return      Spielende eingetreten (true/false)
     */
    public boolean solved() {
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board.length; j++) {
                if(board[i][j] == 0){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        String result = "+-------+\n";
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                result += "|" + board[row][col];
            }
            result += "|\n";
        }
        result += "+-------+\n";
        return result;
    }
}