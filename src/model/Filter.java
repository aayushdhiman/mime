package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a filter that can be used in any class that is meant to represent a macro command
 * for the {@code ImageUtilModelMacro} class. Creates a filter as a 2D array of doubles, and
 * the public method {@code applyFilter}, which relies on private methods {@code
 * useKernel} and {@code getKernel}
 */
public abstract class Filter extends AbstractMacro {
  protected double[][] filter;

  /**
   * Creates a {@code Filter} object. Sets the {@code filter} field to be null.
   */
  public Filter() {
    filter = null;
  }

  /**
   * Creates a {@code List<List<Color>>} that is the kernel for the filter. If the value that
   * the kernel would overlap is invalid, it is represented by a {@code Color} of 0, 0, 0.
   * @param r the row that the middle pixel is at
   * @param c the column that the middle pixel is at
   * @param image the full image
   * @return the kernel as a 2D {@code List<List<Color>>}
   */
  private List<List<Color>> getKernel(int r, int c, List<List<Color>> image) {
    Color[][] kernel = new Color[this.filter.length][this.filter.length];
    int bounds = (int) Math.floor(this.filter.length / 2.0);
    for (int h = 0; h < this.filter.length; h += 1) {
      for (int w = 0; w < this.filter.length; w += 1) {
        try {
          kernel[h][w] = image.get(h + (r - bounds)).get(w + (c - bounds));
        } catch (IndexOutOfBoundsException e) {
          kernel[h][w] = new Color(0, 0, 0);
        }
      }
    }

    return Arrays.stream(kernel).map(Arrays::asList).collect(Collectors.toList());
  }

  /**
   * Multiplies all the values of the kernel with all the values of the filter and adds them
   * for each channel.
   * @param kernel the kernel to use the filter on
   * @return the new {@code Color} of the pixel
   */
  private Color useKernel(List<List<Color>> kernel) {
    int[] pixelValues = new int[3];
    for (int i = 0; i < kernel.size(); i += 1) {
      for (int j = 0; j < kernel.size(); j += 1) {
        pixelValues[0] += rgbCap((int) (kernel.get(i).get(j).getRed() * this.filter[i][j]));
        pixelValues[1] += rgbCap((int) (kernel.get(i).get(j).getGreen() * this.filter[i][j]));
        pixelValues[2] += rgbCap((int) (kernel.get(i).get(j).getBlue() * this.filter[i][j]));
      }
    }
    return new Color(rgbCap(pixelValues[0]), rgbCap(pixelValues[1]), rgbCap(pixelValues[2]));
  }

  /**
   * Applies the filter to each pixel of the image by properly forming a kernel and
   * multipliying it.
   * @param image the image to use the filter on
   * @return the new image after being filtered
   */
  public List<List<Color>> applyFilter(List<List<Color>> image) {
    int height = image.size();
    int width = image.get(0).size();
    ArrayList<List<Color>> pixels = new ArrayList<>();
    for (int h = 0; h < height; h += 1) {
      pixels.add(new ArrayList<Color>());
    }

    for (int h = 0; h < height; h += 1) {
      for (int w = 0; w < width; w += 1) {
        pixels.get(h).add(this.useKernel(this.getKernel(h, w, image)));
      }
    }
    return pixels;
  }
}
