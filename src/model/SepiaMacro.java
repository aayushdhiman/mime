package model;

import java.awt.Color;
import java.util.List;

/**
 * Represents a macro command to turn an image into sepia colors.
 */
public class SepiaMacro extends AbstractMacro {
  double[][] filter;
  String filename;
  String filenameReference;

  /**
   * Creates a SepiaMacro wiht a filename and a filenameReference.
   *
   * @param filename          the image to sepia
   * @param filenameReference what to call the sepia-scaled image
   */
  public SepiaMacro(String filename, String filenameReference) {
    this.filename = filename;
    this.filenameReference = filenameReference;
    this.filter = new double[][]{
            {0.393, 0.769, 0.189},
            {0.349, 0.686, 0.168},
            {0.272, 0.534, 0.131}
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
        double[][] sepiaColor = this.multiplyMatrix(this.filter, colorMatrix);
        Color sepiaPixel = new Color(rgbCap((int) (sepiaColor[0][0])),
                rgbCap((int) (sepiaColor[1][0])),
                rgbCap((int) (sepiaColor[2][0])));
        pixels.get(h).set(w, sepiaPixel);
      }
    }
    model.addImage(pixels, this.filenameReference);
  }
}