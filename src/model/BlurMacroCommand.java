package model;

import java.awt.Color;
import java.util.List;

/**
 * Represents a macro command to blur an image using a filter.
 */
public class BlurMacroCommand extends Filter {
  String filename;
  String refFilename;

  /**
   * Creates a {@code BlurMacroCommand} with a {@code filename} and a {@code refFilename}.
   * Initializes the filter to be a blur filter.
   * @param filename the file to blur
   * @param refFilename the name of the blurred file
   */
  public BlurMacroCommand(String filename, String refFilename) {
    this.filter = new double[][]{
            {0.0625, 0.125, 0.0625},
            {0.125, 0.25, 0.125},
            {0.0625, 0.125, 0.0625}
    };
    this.filename = filename;
    this.refFilename = refFilename;
  }

  @Override
  public void doCommand(ImageUtilModelMacro model) {
    List<List<Color>> blurred = this.applyFilter(model.getImage(filename));
    model.addImage(blurred, this.refFilename);
  }
}
