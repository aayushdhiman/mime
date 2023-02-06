package model;

/**
 * Contains shared methods for the macro commands, such as matrix multiplication methods and
 * RGB capping methods.
 */
public abstract class AbstractMacro implements MacroCommand {
  /**
   * Caps the RGB value to be between 0 and 255.
   *
   * @param value the value to cap between 0 and 255
   * @return the capped value
   */
  protected int rgbCap(int value) {
    if (value > 255) {
      return 255;
    } else {
      return Math.max(value, 0);
    }
  }

  /**
   * Multiplies an individual cell in the first matrix by the second matrix.
   * @param firstMatrix the first matrix to multiply with
   * @param secondMatrix the second matrix to multiply with
   * @param row the row of the cell
   * @param col the column of the cell
   * @return the result of the multiplication
   */
  protected double multiplyIndividualCell(double[][] firstMatrix, double[][] secondMatrix, int row,
                                        int col) {
    double cell = 0;
    for (int i = 0; i < secondMatrix.length; i += 1) {
      cell += firstMatrix[row][i] * secondMatrix[i][col];
    }
    return cell;
  }

  /**
   * Multiplies two matrices.
   * @param firstMatrix the first matrix to multiply with
   * @param secondMatrix the second matrix to multiply with
   * @return
   */
  protected double[][] multiplyMatrix(double[][] firstMatrix, double[][] secondMatrix) {
    double[][] result = new double[firstMatrix.length][secondMatrix[0].length];

    for (int row = 0; row < result.length; row += 1) {
      for (int col = 0; col < result[row].length; col += 1) {
        result[row][col] = multiplyIndividualCell(firstMatrix, secondMatrix, row, col);
      }
    }
    return result;
  }
}
