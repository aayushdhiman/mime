package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract class that contains code that has implementations of certain commands that operate
 * on a {@code List} of {@code List} of {@code Color}s.
 */
public abstract class AbstractUtilModelMacro implements ImageUtilModelMacro {
  Map<String, List<List<Color>>> imageReferences;

  /**
   * Enumeration to determine which filetype a file is, allowing the proper creation of a model.
   */
  public enum FileType { PNG, JPG, BMP, PPM }

  protected ImageUtilModelMacro ppmModel;
  protected FileType fileType;

  /**
   * Creates a {@code AbstractUtilModelMacro}. Initializes the field {@code imageReferences}
   * to be an empty {@code HashMap}
   */
  public AbstractUtilModelMacro() {
    this.imageReferences = new HashMap<>();
  }

  /**
   * Creates an {@code AbstractUtilModelMacro}. Initializes the {@code imageReferences},
   * {@code filetype}, and {@code ppmModel} fields.
   * @param fileType the type of file that this model is representing.
   */
  public AbstractUtilModelMacro(FileType fileType) {
    this.imageReferences = new HashMap<>();
    this.fileType = fileType;
    this.ppmModel = new PPMUtilModel();
  }

  @Override
  public int getHeight(String referenceName) {
    if (this.fileType == FileType.PPM) {
      return this.ppmModel.getHeight(referenceName);
    }
    return this.imageReferences.get(referenceName).size();
  }

  @Override
  public int getWidth(String referenceName) {
    if (this.fileType == FileType.PPM) {
      return this.ppmModel.getWidth(referenceName);
    }
    return this.imageReferences.get(referenceName).get(0).size();
  }

  @Override
  public List<List<Color>> getImage(String referenceName) {
    if (this.fileType == FileType.PPM) {
      return this.ppmModel.getImage(referenceName);
    }
    return this.imageReferences.get(referenceName);
  }

  @Override
  public void grayscale(Grayscale g, String filename, String destFile) {
    if (this.fileType == FileType.PPM) {
      this.ppmModel.grayscale(g, filename, destFile);
    } else {

      if (!this.imageReferences.containsKey(filename)) {
        throw new IllegalArgumentException("Reference name has not been loaded yet.");
      }

      List<List<Color>> imagePixels = this.imageReferences.get(filename);
      for (List<Color> imagePixel : imagePixels) {
        for (int i = 0; i < imagePixel.size(); i += 1) {
          int colorValue;
          int redValue = imagePixel.get(i).getRed();
          int greenValue = imagePixel.get(i).getGreen();
          int blueValue = imagePixel.get(i).getBlue();
          switch (g) {
            case Red:
              colorValue = redValue;
              break;
            case Green:
              colorValue = greenValue;
              break;
            case Blue:
              colorValue = blueValue;
              break;
            case Value:
              if (redValue >= greenValue && redValue >= blueValue) {
                colorValue = redValue;
              } else if (greenValue >= redValue && greenValue >= blueValue) {
                colorValue = greenValue;
              } else {
                colorValue = blueValue;
              }
              break;
            case Intensity:
              colorValue = (redValue + greenValue + blueValue) / 3;
              break;
            case Luma:
              colorValue = (int) ((redValue * 0.2126)
                      + (greenValue * 0.7152)
                      + (blueValue * 0.0722));
              break;
            default:
              throw new IllegalArgumentException("Invalid component");
          }
          imagePixel.set(i, new Color(colorValue, colorValue, colorValue));
        }
      }
      this.imageReferences.put(destFile, imagePixels);
    }
  }

  @Override
  public void flipHorizontal(String filenameReference, String newReferenceName) {
    if (this.fileType == FileType.PPM) {
      this.ppmModel.flipHorizontal(filenameReference, newReferenceName);
    } else {


      if (!this.imageReferences.containsKey(filenameReference)) {
        throw new IllegalArgumentException("Reference name has not been loaded yet.");
      }

      int height = this.getHeight(filenameReference);
      int width = this.getWidth(filenameReference);
      List<List<Color>> imagePixels = this.imageReferences.get(filenameReference);
      List<List<Color>> newImage = this.createEmptyImage(height);

      for (int h = 0; h < height; h += 1) {
        for (int w = width - 1; w >= 0; w -= 1) {
          newImage.get(h).add(imagePixels.get(h).get(w));
        }
      }
      this.imageReferences.put(newReferenceName, newImage);
    }
  }

  @Override
  public void flipVertical(String filenameReference, String newReferenceName) {
    if (this.fileType == FileType.PPM) {
      this.ppmModel.flipVertical(filenameReference, newReferenceName);
    } else {
      if (!this.imageReferences.containsKey(filenameReference)) {
        throw new IllegalArgumentException("Reference name has not been loaded yet.");
      }

      int height = this.imageReferences.get(filenameReference).size();
      int width = this.imageReferences.get(filenameReference).get(0).size();
      List<List<Color>> imagePixels = this.getImage(filenameReference);
      List<List<Color>> newImage = this.createEmptyImage(height);

      int rowNum = 0;
      while (rowNum < height) {
        for (int h = height - 1; h >= 0; h -= 1) {
          for (int w = 0; w < width; w += 1) {
            newImage.get(rowNum).add(imagePixels.get(h).get(w));
          }
          rowNum += 1;
        }
      }
      this.imageReferences.put(newReferenceName, newImage);
    }
  }

  /**
   * Creates an empty image to populate with modified image.
   *
   * @param height the height of the image
   * @return the empty image
   */
  private List<List<Color>> createEmptyImage(int height) {
    List<List<Color>> newImage = new ArrayList<List<Color>>();

    for (int i = 0; i < height; i += 1) {
      newImage.add(new ArrayList<Color>());
    }
    return newImage;
  }

  @Override
  public void brighten(int increment, String filenameReference, String newReferenceName)
          throws IllegalArgumentException {
    if (this.fileType == FileType.PPM) {
      this.ppmModel.brighten(increment, filenameReference, newReferenceName);
    } else {
      if (!this.imageReferences.containsKey(filenameReference)) {
        throw new IllegalArgumentException("Reference name has not been loaded yet.");
      }

      java.util.List<java.util.List<Color>> imagePixels =
              this.imageReferences.get(filenameReference);

      for (int h = 0; h < this.getHeight(filenameReference); h += 1) {
        for (int w = 0; w < this.getWidth(filenameReference); w += 1) {
          int redValue = rgbCap(imagePixels.get(h).get(w).getRed() + increment);
          int greenValue = rgbCap(imagePixels.get(h).get(w).getGreen() + increment);
          int blueValue = rgbCap(imagePixels.get(h).get(w).getBlue() + increment);

          imagePixels.get(h).set(w, new Color(redValue, greenValue, blueValue));
        }
      }
      this.imageReferences.put(newReferenceName, imagePixels);
    }
  }

  /**
   * Caps the RGB value to be between 0 and 255.
   *
   * @param value the value to cap between 0 and 255
   * @return the capped value
   */
  private int rgbCap(int value) {
    if (value > 255) {
      return 255;
    } else {
      return Math.max(value, 0);
    }
  }

  @Override
  public void execute(MacroCommand macro) {
    if (this.fileType == FileType.PPM) {
      macro.doCommand(this.ppmModel);
    } else {
      macro.doCommand(this);
    }
  }
}
