package model;

import java.awt.Color;
import java.util.List;

/**
 * Represents a macro command to greyscale an image using matrix multiplication.
 */
public class GreyscaleMacro extends AbstractMacro {
  double[][] filter;
  String filename;
  String filenameReference;

  /**
   * Creates a greyscale macro with a filename and filenameReference.
   * @param filename the file to greyscale
   * @param filenameReference the name to call the greyscaled file
   */
  public GreyscaleMacro(String filename, String filenameReference) {
    this.filename = filename;
    this.filenameReference = filenameReference;
    this.filter = new double[][] {
            {0.2126, 0.7152, 0.0722},
            {0.2126, 0.7152, 0.0722},
            {0.2126, 0.7152, 0.0722}
    };
  }

  @Override
  public void doCommand(ImageUtilModelMacro model) {
    List<List<Color>> pixels = model.getImage(this.filename);

    for (int h = 0; h < model.getHeight(this.filename); h += 1) {
      for (int w = 0; w < model.getWidth(this.filename); w += 1) {
        Color pix = pixels.get(h).get(w);
        double[][] colorMatrix = new double[][]{
                {pix.getRed()},
                {pix.getGreen()},
                {pix.getBlue()}
        };
        double[][] greyscaleColor = this.multiplyMatrix(this.filter, colorMatrix);
        Color greyscalePixel = new Color(rgbCap((int) (greyscaleColor[0][0])),
                rgbCap((int) (greyscaleColor[1][0])),
                rgbCap((int) (greyscaleColor[2][0])));
        pixels.get(h).set(w, greyscalePixel);
      }
    }
    model.addImage(pixels, this.filenameReference);
  }
}