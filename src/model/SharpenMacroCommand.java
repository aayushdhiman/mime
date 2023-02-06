package model;

import java.awt.Color;
import java.util.List;

/**
 * Represents a macro command to sharpen an image using a filter.
 */
public class SharpenMacroCommand extends Filter {
  String filename;
  String refFilename;

  /**
   * Creates a sharpen macro command with a {@code filename} and a {@code refFilename}.
   * Instantiates the filter to be a sharpen filter.
   * @param filename the file to sharpen
   * @param refFilename the new file's name
   */
  public SharpenMacroCommand(String filename, String refFilename) {
    this.filter = new double[][]{
            {-0.125, -0.125, -0.125, -0.125, -0.125},
            {-0.125, 0.25, 0.25, 0.25, -0.125},
            {-0.125, 0.25, 1, 0.25, -0.125},
            {-0.125, 0.25, 0.25, 0.25, -0.125},
            {-0.125, -0.125, -0.125, -0.125, -0.125}
    };
    this.filename = filename;
    this.refFilename = refFilename;
  }

  @Override
  public void doCommand(ImageUtilModelMacro model) {
    List<List<Color>> sharpened = this.applyFilter(model.getImage(filename));
    model.addImage(sharpened, this.refFilename);
  }
}
