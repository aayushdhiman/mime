package model;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

/**
 * Model implementation for PPM images. Supports performing commands such as load, save, and
 * others on PPM images that are read using {@code readPPM()}.
 */
public class PPMUtilModel extends AbstractUtilModelMacro {
  Map<String, Integer> maxValues;

  /**
   * Creates a {@code PPMUtilModel}. Initializes the fields {@code imageReferences} and {@code
   * maxValues} to be both be an empty {@code HashMap}
   */
  public PPMUtilModel() {
    this.maxValues = new HashMap<>();
  }

  /**
   * Read an image file in the PPM format and print the colors.
   *
   * @param filename the path of the file.
   */
  private List<List<Color>> readPPM(String filename, String destFilename)
          throws FileNotFoundException {
    Scanner sc;

    try {
      sc = new Scanner(new FileInputStream(filename));
    } catch (FileNotFoundException e) {
      throw new FileNotFoundException("File " + filename + " not found!");
    }
    StringBuilder builder = new StringBuilder();

    //read the file line by line, and populate a string. This will throw away any comment lines
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.charAt(0) != '#') {
        builder.append(s).append(System.lineSeparator());
      }
    }

    //now set up the scanner to read from the string we just built
    sc = new Scanner(builder.toString());

    String token;

    token = sc.next();
    if (!token.equals("P3")) {
      System.out.println("Invalid PPM file: plain RAW file should begin with P3");
    }

    int width = sc.nextInt();
    int height = sc.nextInt();
    Integer maxValue = sc.nextInt();

    this.maxValues.put(destFilename, maxValue);

    List<List<Color>> imagePixels = new ArrayList<List<Color>>();

    for (int h = 0; h < height; h += 1) {
      imagePixels.add(new ArrayList<Color>());
    }

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int red = sc.nextInt();
        int green = sc.nextInt();
        int blue = sc.nextInt();
        Color pixel = new Color(red, green, blue);
        imagePixels.get(i).add(pixel);
      }
    }
    return imagePixels;
  }

  /**
   * Gets the max value of an RGB value for this file.
   *
   * @param referenceName the name of the file to get the max value of
   * @return the max value of the file
   * @throws IllegalArgumentException if the referenceName is not already loaded
   */
  private int getMaxValue(String referenceName) throws IllegalArgumentException {
    try {
      return this.maxValues.get(referenceName);
    } catch (NullPointerException e) {
      throw new IllegalArgumentException("Provided filename has not been loaded into the " +
              "system!.");
    }
  }

  @Override
  public void loadImage(String filename, String destFilename) throws FileNotFoundException {
    List<List<Color>> imagePixels = this.readPPM(filename, destFilename);
    this.imageReferences.put(destFilename, imagePixels);
  }

  @Override
  public void saveImage(String filepath, String filenameReference) throws IOException {
    if (!this.imageReferences.containsKey(filenameReference)) {
      throw new IllegalArgumentException("Reference name has not been loaded yet.");
    }

    int height = this.getHeight(filenameReference);
    int width = this.getWidth(filenameReference);
    int maxVal = this.getMaxValue(filenameReference);

    List<List<Color>> saveImage = this.imageReferences.get(filenameReference);

    StringBuilder saveMe = new StringBuilder();
    // adds PPM file signature
    saveMe.append("P3\n");
    // formats integer height/width/maxVal into Strings and adds them
    saveMe.append(String.format("%d %d %d\n", width, height, maxVal));

    // formats the RGB values into integers and adds them
    for (int h = 0; h < height; h += 1) {
      for (int w = 0; w < width; w += 1) {
        saveMe.append(String.format("%d ", saveImage.get(h).get(w).getRed()));
        saveMe.append(String.format("%d ", saveImage.get(h).get(w).getGreen()));
        saveMe.append(String.format("%d ", saveImage.get(h).get(w).getBlue()));
      }
      saveMe.append("\n");
    }

    // writes to file
    FileWriter saver = new FileWriter(filepath + ".ppm");
    saver.write(saveMe.toString());
    saver.flush(); // saves file instantly (flushes cache)
    saver.close();
  }

  @Override
  public void grayscale(Grayscale g, String filename, String destFile) {
    super.grayscale(g, filename, destFile);
    this.maxValues.put(destFile, this.getMaxValue(filename));
  }

  @Override
  public void flipHorizontal(String filenameReference, String newReferenceName) {
    super.flipHorizontal(filenameReference, newReferenceName);
    this.maxValues.put(newReferenceName, this.getMaxValue(filenameReference));
  }

  @Override
  public void flipVertical(String filenameReference, String newReferenceName) {
    super.flipVertical(filenameReference, newReferenceName);
    this.maxValues.put(newReferenceName, this.getMaxValue(filenameReference));
  }

  @Override
  public void brighten(int increment, String filenameReference, String newReferenceName)
          throws IllegalArgumentException {
    super.brighten(increment, filenameReference, newReferenceName);
    this.maxValues.put(newReferenceName, this.getMaxValue(filenameReference));
  }

  @Override
  public void addImage(List<List<Color>> pixels, String filenameReference) {
    this.imageReferences.put(filenameReference, pixels);
  }
}
