public class Sudoku {
  private static final int SIZE = 9;
  private static final int SUBGRIDSIZE = 3;
  int[][] matrix = new int[SIZE][SIZE];

  public Sudoku() {
    generateGrid();
  }

  private void generateGrid() {
    fillDiagonalSubgrids();
    fillRemaining(0, SUBGRIDSIZE);
  }

  private void fillDiagonalSubgrids() {
    for (int i = 0; i < SIZE; i += SUBGRIDSIZE) {
      fillSubgrid(i, i);
    }
  }

  private void fillSubgrid(int row, int col) {
    boolean[] usedNumbers = new boolean[SIZE + 1];
    for (int i = 0; i < SUBGRIDSIZE; i++) {
      for (int j = 0; j < SUBGRIDSIZE; j++) {
        int num;
        do {
          num = (int)(Math.random() * SIZE) + 1;
        } while (usedNumbers[num]);
        usedNumbers[num] = true;
        matrix[row + i][col + j] = num;
      }
    }
  }

  private boolean fillRemaining(int i, int j) {
    if (j >= SIZE && i < SIZE - 1) {
      i++;
      j = 0;
    }
    if (i >= SIZE && j >= SIZE) {
      return true;
    }
    if (i < SUBGRIDSIZE) {
      if (j < SUBGRIDSIZE) {
        j = SUBGRIDSIZE;
      }
    } else if (i < SIZE - SUBGRIDSIZE) {
      if (j == (i / SUBGRIDSIZE) * SUBGRIDSIZE) {
        j += SUBGRIDSIZE;
      }
    } else {
      if (j == SIZE - SUBGRIDSIZE) {
        i++;
        j = 0;
        if (i >= SIZE) {
          return true;
        }
      }
    }

    for (int num = 1; num <= SIZE; num++) {
      if (isSafe(i, j, num)) {
        matrix[i][j] = num;
        if (fillRemaining(i, j + 1)) {
          return true;
        }
        matrix[i][j] = 0;
      }
    }
    return false;
  }

  private boolean isSafe(int i, int j, int num) {
    return !isInRow(i, num) && !isInCol(j, num) && !isInBox(i - i % SUBGRIDSIZE, j - j % SUBGRIDSIZE, num);
  }

  private boolean isInRow(int i, int num) {
    for (int j = 0; j < SIZE; j++) {
      if (matrix[i][j] == num) {
        return true;
      }
    }
    return false;
  }

  private boolean isInCol(int j, int num) {
    for (int i = 0; i < SIZE; i++) {
      if (matrix[i][j] == num) {
        return true;
      }
    }
    return false;
  }

  private boolean isInBox(int boxStartRow, int boxStartCol, int num) {
    for (int i = 0; i < SUBGRIDSIZE; i++) {
      for (int j = 0; j < SUBGRIDSIZE; j++) {
        if (matrix[boxStartRow + i][boxStartCol + j] == num) {
          return true;
        }
      }
    }
    return false;
  }

  public void printGrid() {

    int num = 1;
    System.out.println(" << ");
    System.out.println(" << j\\i 1 2 3  4 5 6  7 8 9");
    System.out.println(" << ");
    for (int r = 0; r < SIZE; r++) {
      System.out.print(" << " + num + "   ");
      num += 1;
      for (int d = 0; d < SIZE; d++) {
        System.out.print((matrix[r][d] != 0) ? matrix[r][d] : "_");
        System.out.print(" ");
        if ((d+1) % 3 == 0) System.out.print(" ");
      }
      System.out.println();
      if ((r+1) % 3 == 0) System.out.println(" << ");
    }
  }
}
