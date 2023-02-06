package model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * Represents a model for any conventional filetype.
 */
public class FileTypeUtilModel extends AbstractUtilModelMacro {
  /**
   * Creates a FileTypeUtilModel.
   *
   * @param fileType what filetype this model represents
   */
  public FileTypeUtilModel(FileType fileType) {
    super(fileType);
  }

  @Override
  public void loadImage(String filename, String destFilename) throws FileNotFoundException {
    if (this.fileType == FileType.PPM) {
      this.ppmModel.loadImage(filename, destFilename);
    } else {
      BufferedImage img;
      try {
        img = ImageIO.read(new File(filename));
      } catch (IOException e) {
        throw new FileNotFoundException(e.getMessage());
      }

      int height = img.getHeight();
      int width = img.getWidth();

      List<java.util.List<Color>> imagePixels = new ArrayList<List<Color>>();

      for (int i = 0; i < height; i += 1) {
        imagePixels.add(new ArrayList<Color>());
      }

      for (int h = 0; h < height; h += 1) {
        for (int w = 0; w < width; w += 1) {
          int rgb = img.getRGB(w, h);
          int blue = rgb & 0xff;
          int green = (rgb & 0xff00) >> 8;
          int red = (rgb & 0xff0000) >> 16;

          imagePixels.get(h).add(new Color(red, green, blue));
        }
      }
      this.imageReferences.put(destFilename, imagePixels);
    }
  }

  @Override
  public void saveImage(String filepath, String filenameReference) throws IOException {
    if (this.fileType == FileType.PPM) {
      ppmModel.saveImage(filepath, filenameReference);
    } else {
      if (!this.imageReferences.containsKey(filenameReference)) {
        throw new IllegalArgumentException("Reference name has not been loaded yet.");
      }

      int height = this.imageReferences.get(filenameReference).size();
      int width = this.imageReferences.get(filenameReference).get(0).size();
      java.util.List<java.util.List<Color>> pixels = this.imageReferences.get(filenameReference);
      BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      for (int h = 0; h < height; h += 1) {
        for (int w = 0; w < width; w += 1) {
          im.setRGB(w, h, pixels.get(h).get(w).getRGB());
        }
      }

      File output;
      switch (this.fileType) {
        case PNG:
          output = new File(filepath);
          ImageIO.write(im, "png", output);
          break;
        case JPG:
          output = new File(filepath);
          ImageIO.write(im, "jpg", output);
          break;
        case BMP:
          output = new File(filepath);
          ImageIO.write(im, "bmp", output);
          break;
        default:
          throw new IllegalArgumentException("Unsupported file type.");
      }
    }
  }

  @Override
  public void addImage(List<List<Color>> pixels, String filenameReference) {
    if (this.fileType == FileType.PPM) {
      ppmModel.addImage(pixels, filenameReference);
    } else {
      this.imageReferences.put(filenameReference, pixels);
    }
  }
}
