package hw05;

import org.junit.Before;
import org.junit.Test;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import model.BlurMacroCommand;
import model.FileTypeUtilModel;
import model.GreyscaleMacro;
import model.ImageUtilModel;
import model.ImageUtilModelMacro;
import model.SepiaMacro;
import model.SharpenMacroCommand;

import static model.AbstractUtilModelMacro.FileType;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for models of all filetypes.
 */
public class FileTypeUtilModelTest {

  ImageUtilModelMacro pngModel;
  ImageUtilModelMacro jpgModel;
  ImageUtilModelMacro bmpModel;


  @Before
  public void init() {
    this.pngModel = new FileTypeUtilModel(FileType.PNG);
    this.jpgModel = new FileTypeUtilModel(FileType.JPG);
    this.bmpModel = new FileTypeUtilModel(FileType.BMP);

    try {
      this.pngModel.loadImage("res/6ColorRGB.png", "sixPNG");
    } catch (FileNotFoundException e) {
      fail(e.getMessage());
    }

    try {
      this.jpgModel.loadImage("res/6ColorRGB.jpg", "sixJPG");
    } catch (FileNotFoundException e) {
      fail(e.getMessage());
    }

    try {
      this.bmpModel.loadImage("res/6ColorRGB.bmp", "sixBMP");
    } catch (FileNotFoundException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testGetHeight() {
    assertEquals(20, this.pngModel.getHeight("sixPNG"));
    assertEquals(20, this.jpgModel.getHeight("sixJPG"));
    assertEquals(20, this.bmpModel.getHeight("sixBMP"));
  }

  @Test
  public void testGetWidth() {
    assertEquals(21, this.pngModel.getWidth("sixPNG"));
    assertEquals(21, this.jpgModel.getWidth("sixJPG"));
    assertEquals(21, this.bmpModel.getWidth("sixBMP"));
  }

  @Test
  public void testGetImage() {
    // test png
    List<List<Color>> image1 = this.pngModel.getImage("sixPNG");

    try {
      this.pngModel.loadImage("res/6ColorRGB.png", "6_1");
    } catch (FileNotFoundException e) {
      fail(e.getMessage());
    }

    List<List<Color>> image2 = this.pngModel.getImage("6_1");

    for (int h = 0; h < image1.size(); h += 1) {
      for (int w = 0; w < image1.get(0).size(); w += 1) {
        assertEquals(image1.get(h).get(w), image2.get(h).get(w));
      }
    }


    // test jpg
    image1 = this.jpgModel.getImage("sixJPG");

    try {
      this.jpgModel.loadImage("res/6ColorRGB.jpg", "6_1");
    } catch (FileNotFoundException e) {
      fail(e.getMessage());
    }
    image2 = this.jpgModel.getImage("6_1");

    for (int h = 0; h < image1.size(); h += 1) {
      for (int w = 0; w < image1.get(0).size(); w += 1) {
        assertEquals(image1.get(h).get(w), image2.get(h).get(w));
      }
    }

    // test bmp
    image1 = this.bmpModel.getImage("sixBMP");

    try {
      this.bmpModel.loadImage("res/6ColorRGB.bmp", "6_1");
    } catch (FileNotFoundException e) {
      fail(e.getMessage());
    }
    image2 = this.bmpModel.getImage("6_1");

    for (int h = 0; h < image1.size(); h += 1) {
      for (int w = 0; w < image1.get(0).size(); w += 1) {
        assertEquals(image1.get(h).get(w), image2.get(h).get(w));
      }
    }
  }

  @Test
  public void testSaveImage() {
    // test png
    try {
      this.pngModel.saveImage("res/saved6Color.png", "sixPNG");
    } catch (IOException e) {
      fail(e.getMessage());
    }

    try {
      this.pngModel.loadImage("res/saved6Color.png", "savedPNG");
    } catch (FileNotFoundException e) {
      fail(e.getMessage());
    }

    List<List<Color>> originalImage = this.pngModel.getImage("sixPNG");
    List<List<Color>> savedImage = this.pngModel.getImage("savedPNG");

    for (int h = 0; h < this.pngModel.getHeight("sixPNG"); h += 1) {
      for (int w = 0; w < this.pngModel.getWidth("sixPNG"); w += 1) {
        assertEquals(originalImage.get(h).get(w), savedImage.get(h).get(w));
      }
    }

    // test jpg
    try {
      this.jpgModel.saveImage("res/saved6Color.jpg", "sixJPG");
    } catch (IOException e) {
      fail(e.getMessage());
    }

    try {
      this.jpgModel.loadImage("res/saved6Color.jpg", "savedJPG");
    } catch (FileNotFoundException e) {
      fail(e.getMessage());
    }

    // test bmp
    try {
      this.bmpModel.saveImage("res/saved6Color.bmp", "sixBMP");
    } catch (IOException e) {
      fail(e.getMessage());
    }

    try {
      this.bmpModel.loadImage("res/saved6Color.bmp", "savedBMP");
    } catch (FileNotFoundException e) {
      fail(e.getMessage());
    }

    originalImage = this.bmpModel.getImage("sixBMP");
    savedImage = this.bmpModel.getImage("savedBMP");

    for (int h = 0; h < this.bmpModel.getHeight("sixBMP"); h += 1) {
      for (int w = 0; w < this.bmpModel.getWidth("sixBMP"); w += 1) {
        assertEquals(originalImage.get(h).get(w), savedImage.get(h).get(w));
      }
    }
  }

  @Test
  public void testRedGreyscale() {
    // tests png
    java.util.List<java.util.List<Color>> imagePixels = this.pngModel.getImage("sixPNG");
    (imagePixels.get(0)).set(0, new Color(195, 165, 230));
    int[] expectedColors = new int[]{195, 165, 230};

    int red = (imagePixels.get(0).get(0)).getRed();
    int green = (imagePixels.get(0).get(0)).getGreen();
    int blue = (imagePixels.get(0).get(0)).getBlue();
    int[] actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    this.pngModel.grayscale(ImageUtilModel.Grayscale.Red, "sixPNG", "redSixPNG");
    imagePixels = this.pngModel.getImage("redSixPNG");
    expectedColors = new int[]{195, 195, 195};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    // tests jpg
    imagePixels = this.jpgModel.getImage("sixJPG");
    (imagePixels.get(0)).set(0, new Color(195, 165, 230));
    expectedColors = new int[]{195, 165, 230};

    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    this.jpgModel.grayscale(ImageUtilModel.Grayscale.Red, "sixJPG", "redSixJPG");
    imagePixels = this.jpgModel.getImage("redSixJPG");
    expectedColors = new int[]{195, 195, 195};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    // tests bmp
    imagePixels = this.bmpModel.getImage("sixBMP");
    (imagePixels.get(0)).set(0, new Color(195, 165, 230));
    expectedColors = new int[]{195, 165, 230};

    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    this.bmpModel.grayscale(ImageUtilModel.Grayscale.Red, "sixBMP", "redSixBMP");
    imagePixels = this.bmpModel.getImage("redSixBMP");
    expectedColors = new int[]{195, 195, 195};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);
  }

  @Test
  public void testGreenGrayscale() {
    // tests png
    java.util.List<java.util.List<Color>> imagePixels = this.pngModel.getImage("sixPNG");
    (imagePixels.get(0)).set(0, new Color(195, 165, 230));
    int[] expectedColors = new int[]{195, 165, 230};

    int red = (imagePixels.get(0).get(0)).getRed();
    int green = (imagePixels.get(0).get(0)).getGreen();
    int blue = (imagePixels.get(0).get(0)).getBlue();
    int[] actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    this.pngModel.grayscale(ImageUtilModel.Grayscale.Green, "sixPNG", "greenSixPNG");
    imagePixels = this.pngModel.getImage("greenSixPNG");
    expectedColors = new int[]{165, 165, 165};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    // tests jpg
    imagePixels = this.jpgModel.getImage("sixJPG");
    (imagePixels.get(0)).set(0, new Color(195, 165, 230));
    expectedColors = new int[]{195, 165, 230};

    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    this.jpgModel.grayscale(ImageUtilModel.Grayscale.Green, "sixJPG", "greenSixJPG");
    imagePixels = this.jpgModel.getImage("greenSixJPG");
    expectedColors = new int[]{165, 165, 165};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    // tests bmp
    imagePixels = this.bmpModel.getImage("sixBMP");
    (imagePixels.get(0)).set(0, new Color(195, 165, 230));
    expectedColors = new int[]{195, 165, 230};

    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    this.bmpModel.grayscale(ImageUtilModel.Grayscale.Green, "sixBMP", "redSixBMP");
    imagePixels = this.bmpModel.getImage("redSixBMP");
    expectedColors = new int[]{165, 165, 165};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);
  }

  @Test
  public void testBlueGrayscale() {
    // tests png
    java.util.List<java.util.List<Color>> imagePixels = this.pngModel.getImage("sixPNG");
    (imagePixels.get(0)).set(0, new Color(195, 165, 230));
    int[] expectedColors = new int[]{195, 165, 230};

    int red = (imagePixels.get(0).get(0)).getRed();
    int green = (imagePixels.get(0).get(0)).getGreen();
    int blue = (imagePixels.get(0).get(0)).getBlue();
    int[] actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    this.pngModel.grayscale(ImageUtilModel.Grayscale.Blue, "sixPNG", "blueSixPNG");
    imagePixels = this.pngModel.getImage("blueSixPNG");
    expectedColors = new int[]{230, 230, 230};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    // tests png
    imagePixels = this.jpgModel.getImage("sixJPG");
    (imagePixels.get(0)).set(0, new Color(195, 165, 230));
    expectedColors = new int[]{195, 165, 230};

    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    this.jpgModel.grayscale(ImageUtilModel.Grayscale.Blue, "sixJPG", "blueSixJPG");
    imagePixels = this.jpgModel.getImage("blueSixJPG");
    expectedColors = new int[]{230, 230, 230};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    // tests bmp
    imagePixels = this.bmpModel.getImage("sixBMP");
    (imagePixels.get(0)).set(0, new Color(195, 165, 230));
    expectedColors = new int[]{195, 165, 230};

    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    this.bmpModel.grayscale(ImageUtilModel.Grayscale.Blue, "sixBMP", "redSixBMP");
    imagePixels = this.bmpModel.getImage("redSixBMP");
    expectedColors = new int[]{230, 230, 230};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);


  }

  @Test
  public void testValueGrayscale() {
    // tests png
    java.util.List<java.util.List<Color>> imagePixels = this.pngModel.getImage("sixPNG");

    // when blue is the highest value
    (imagePixels.get(0)).set(0, new Color(195, 165, 230));
    int[] expectedColors = new int[]{195, 165, 230};
    int red = (imagePixels.get(0).get(0)).getRed();
    int green = (imagePixels.get(0).get(0)).getGreen();
    int blue = (imagePixels.get(0).get(0)).getBlue();
    int[] actualColors = new int[]{red, green, blue};
    assertArrayEquals(expectedColors, actualColors);

    this.pngModel.grayscale(ImageUtilModel.Grayscale.Value, "sixPNG", "valueSixPNG");
    imagePixels = this.pngModel.getImage("valueSixPNG");
    expectedColors = new int[]{230, 230, 230};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};
    assertArrayEquals(expectedColors, actualColors);

    // when green is the highest value
    (imagePixels.get(0)).set(0, new Color(193, 240, 204));
    expectedColors = new int[]{193, 240, 204};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};
    assertArrayEquals(expectedColors, actualColors);

    this.pngModel.grayscale(ImageUtilModel.Grayscale.Value,
            "sixPNG", "valueGreenSixPNG");
    imagePixels = this.pngModel.getImage("valueGreenSixPNG");
    expectedColors = new int[]{240, 240, 240};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};
    assertArrayEquals(expectedColors, actualColors);

    // when red is the highest value
    (imagePixels.get(0)).set(0, new Color(222, 135, 145));
    expectedColors = new int[]{222, 135, 145};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};
    assertArrayEquals(expectedColors, actualColors);

    this.pngModel.grayscale(ImageUtilModel.Grayscale.Value, "sixPNG", "valueRedSixPNG");
    imagePixels = this.pngModel.getImage("valueRedSixPNG");
    expectedColors = new int[]{222, 222, 222};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};
    assertArrayEquals(expectedColors, actualColors);


    // tests jpg
    imagePixels = this.jpgModel.getImage("sixJPG");

    // when blue is the highest value
    (imagePixels.get(0)).set(0, new Color(195, 165, 230));
    expectedColors = new int[]{195, 165, 230};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};
    assertArrayEquals(expectedColors, actualColors);

    this.jpgModel.grayscale(ImageUtilModel.Grayscale.Value, "sixJPG", "valueSixJPG");
    imagePixels = this.jpgModel.getImage("valueSixJPG");
    expectedColors = new int[]{230, 230, 230};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};
    assertArrayEquals(expectedColors, actualColors);

    // when green is the highest value
    (imagePixels.get(0)).set(0, new Color(193, 240, 204));
    expectedColors = new int[]{193, 240, 204};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};
    assertArrayEquals(expectedColors, actualColors);

    this.jpgModel.grayscale(ImageUtilModel.Grayscale.Value,
            "sixJPG", "valueGreenSixJPG");
    imagePixels = this.jpgModel.getImage("valueGreenSixJPG");
    expectedColors = new int[]{240, 240, 240};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};
    assertArrayEquals(expectedColors, actualColors);

    // when red is the highest value
    (imagePixels.get(0)).set(0, new Color(222, 135, 145));
    expectedColors = new int[]{222, 135, 145};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};
    assertArrayEquals(expectedColors, actualColors);

    this.jpgModel.grayscale(ImageUtilModel.Grayscale.Value, "sixJPG", "valueRedSixJPG");
    imagePixels = this.jpgModel.getImage("valueRedSixJPG");
    expectedColors = new int[]{222, 222, 222};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};
    assertArrayEquals(expectedColors, actualColors);


    // tests bmp
    imagePixels = this.bmpModel.getImage("sixBMP");

    // when blue is the highest value
    (imagePixels.get(0)).set(0, new Color(195, 165, 230));
    expectedColors = new int[]{195, 165, 230};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};
    assertArrayEquals(expectedColors, actualColors);

    this.bmpModel.grayscale(ImageUtilModel.Grayscale.Value, "sixBMP", "valueSixBMP");
    imagePixels = this.bmpModel.getImage("valueSixBMP");
    expectedColors = new int[]{230, 230, 230};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};
    assertArrayEquals(expectedColors, actualColors);

    // when green is the highest value
    (imagePixels.get(0)).set(0, new Color(193, 240, 204));
    expectedColors = new int[]{193, 240, 204};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};
    assertArrayEquals(expectedColors, actualColors);

    this.bmpModel.grayscale(ImageUtilModel.Grayscale.Value,
            "sixBMP", "valueGreenSixBMP");
    imagePixels = this.bmpModel.getImage("valueGreenSixBMP");
    expectedColors = new int[]{240, 240, 240};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};
    assertArrayEquals(expectedColors, actualColors);

    // when red is the highest value
    (imagePixels.get(0)).set(0, new Color(222, 135, 145));
    expectedColors = new int[]{222, 135, 145};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};
    assertArrayEquals(expectedColors, actualColors);

    this.bmpModel.grayscale(ImageUtilModel.Grayscale.Value, "sixBMP", "valueRedSixBMP");
    imagePixels = this.bmpModel.getImage("valueRedSixBMP");
    expectedColors = new int[]{222, 222, 222};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};
    assertArrayEquals(expectedColors, actualColors);
  }

  @Test
  public void testFlipHorizontal() {
    // tests png
    java.util.List<java.util.List<Color>> nonFlippedImage = this.pngModel.getImage("sixPNG");
    this.pngModel.flipHorizontal("sixPNG", "horizontalSixPNG");
    java.util.List<java.util.List<Color>> flippedImage = this.pngModel.getImage("horizontalSixPNG");

    for (int h = 0; h < this.pngModel.getHeight("sixPNG"); h += 1) {
      for (int w = 0; w < this.pngModel.getWidth("sixPNG"); w += 1) {
        assertEquals(nonFlippedImage.get(h).get(w),
                flippedImage.get(h).get(this.pngModel.getWidth("horizontalSixPNG") - w - 1));
      }
    }

    // tests jpg
    nonFlippedImage = this.jpgModel.getImage("sixJPG");
    this.jpgModel.flipHorizontal("sixJPG", "horizontalSixJPG");
    flippedImage = this.jpgModel.getImage("horizontalSixJPG");

    for (int h = 0; h < this.jpgModel.getHeight("sixJPG"); h += 1) {
      for (int w = 0; w < this.jpgModel.getWidth("sixJPG"); w += 1) {
        assertEquals(nonFlippedImage.get(h).get(w),
                flippedImage.get(h).get(this.jpgModel.getWidth("horizontalSixJPG") - w - 1));
      }
    }

    // tests bmp
    nonFlippedImage = this.bmpModel.getImage("sixBMP");
    this.bmpModel.flipHorizontal("sixBMP", "horizontalSixBMP");
    flippedImage = this.bmpModel.getImage("horizontalSixBMP");

    for (int h = 0; h < this.bmpModel.getHeight("sixBMP"); h += 1) {
      for (int w = 0; w < this.bmpModel.getWidth("sixBMP"); w += 1) {
        assertEquals(nonFlippedImage.get(h).get(w),
                flippedImage.get(h).get(this.bmpModel.getWidth("horizontalSixBMP") - w - 1));
      }
    }
  }

  @Test
  public void testFlipVertical() {
    // tests png
    java.util.List<java.util.List<Color>> nonFlippedImage = this.pngModel.getImage("sixPNG");
    this.pngModel.flipVertical("sixPNG", "verticalSixPNG");
    java.util.List<java.util.List<Color>> flippedImage = this.pngModel.getImage("verticalSixPNG");

    for (int h = 0; h < this.pngModel.getHeight("sixPNG"); h += 1) {
      for (int w = 0; w < this.pngModel.getWidth("sixPNG"); w += 1) {
        assertEquals(nonFlippedImage.get(h).get(w),
                flippedImage.get(this.pngModel.getHeight("verticalSixPNG") - h - 1).get(w));
      }
    }

    // tests jpg
    nonFlippedImage = this.jpgModel.getImage("sixJPG");
    this.jpgModel.flipVertical("sixJPG", "verticalSixJPG");
    flippedImage = this.jpgModel.getImage("verticalSixJPG");

    for (int h = 0; h < this.jpgModel.getHeight("sixJPG"); h += 1) {
      for (int w = 0; w < this.jpgModel.getWidth("sixJPG"); w += 1) {
        assertEquals(nonFlippedImage.get(h).get(w),
                flippedImage.get(this.jpgModel.getHeight("verticalSixJPG") - h - 1).get(w));
      }
    }

    // tests BMP
    nonFlippedImage = this.bmpModel.getImage("sixBMP");
    this.bmpModel.flipVertical("sixBMP", "verticalSixBMP");
    flippedImage = this.bmpModel.getImage("verticalSixBMP");

    for (int h = 0; h < this.bmpModel.getHeight("sixBMP"); h += 1) {
      for (int w = 0; w < this.bmpModel.getWidth("sixBMP"); w += 1) {
        assertEquals(nonFlippedImage.get(h).get(w),
                flippedImage.get(this.bmpModel.getHeight("verticalSixBMP") - h - 1).get(w));
      }
    }
  }

  @Test
  public void testIntensityGrayscale() {
    // tests png
    java.util.List<java.util.List<Color>> imagePixels = this.pngModel.getImage("sixPNG");
    (imagePixels.get(0)).set(0, new Color(195, 165, 230));
    int[] expectedColors = new int[]{195, 165, 230};
    int red = (imagePixels.get(0).get(0)).getRed();
    int green = (imagePixels.get(0).get(0)).getGreen();
    int blue = (imagePixels.get(0).get(0)).getBlue();
    int[] actualColors = new int[]{red, green, blue};
    assertArrayEquals(expectedColors, actualColors);

    this.pngModel.grayscale(ImageUtilModel.Grayscale.Intensity,
            "sixPNG", "intensitySixPNG");
    imagePixels = this.pngModel.getImage("intensitySixPNG");
    expectedColors = new int[]{196, 196, 196};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};
    assertArrayEquals(expectedColors, actualColors);

    // tests jpg
    imagePixels = this.jpgModel.getImage("sixJPG");
    (imagePixels.get(0)).set(0, new Color(195, 165, 230));
    expectedColors = new int[]{195, 165, 230};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};
    assertArrayEquals(expectedColors, actualColors);

    this.jpgModel.grayscale(ImageUtilModel.Grayscale.Intensity,
            "sixJPG", "intensitySixJPG");
    imagePixels = this.jpgModel.getImage("intensitySixJPG");
    expectedColors = new int[]{196, 196, 196};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};
    assertArrayEquals(expectedColors, actualColors);

    // tests BMP
    imagePixels = this.bmpModel.getImage("sixBMP");
    (imagePixels.get(0)).set(0, new Color(195, 165, 230));
    expectedColors = new int[]{195, 165, 230};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};
    assertArrayEquals(expectedColors, actualColors);

    this.bmpModel.grayscale(ImageUtilModel.Grayscale.Intensity,
            "sixBMP", "intensitySixBMP");
    imagePixels = this.bmpModel.getImage("intensitySixBMP");
    expectedColors = new int[]{196, 196, 196};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};
    assertArrayEquals(expectedColors, actualColors);

  }

  @Test
  public void testLumaGrayscale() {
    // tests png
    java.util.List<java.util.List<Color>> imagePixels = this.pngModel.getImage("sixPNG");
    (imagePixels.get(0)).set(0, new Color(195, 165, 230));
    int[] expectedColors = new int[]{195, 165, 230};
    int red = (imagePixels.get(0).get(0)).getRed();
    int green = (imagePixels.get(0).get(0)).getGreen();
    int blue = (imagePixels.get(0).get(0)).getBlue();
    int[] actualColors = new int[]{red, green, blue};
    assertArrayEquals(expectedColors, actualColors);

    // 41.457 + 118.008 + 16.606 = 176.071
    this.pngModel.grayscale(ImageUtilModel.Grayscale.Luma, "sixPNG", "lumaSixPNG");
    imagePixels = this.pngModel.getImage("lumaSixPNG");
    expectedColors = new int[]{176, 176, 176};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};
    assertArrayEquals(expectedColors, actualColors);

    // tests jpg
    imagePixels = this.jpgModel.getImage("sixJPG");
    (imagePixels.get(0)).set(0, new Color(195, 165, 230));
    expectedColors = new int[]{195, 165, 230};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};
    assertArrayEquals(expectedColors, actualColors);

    // 41.457 + 118.008 + 16.606 = 176.071
    this.jpgModel.grayscale(ImageUtilModel.Grayscale.Luma, "sixJPG", "lumaSixJPG");
    imagePixels = this.jpgModel.getImage("lumaSixJPG");
    expectedColors = new int[]{176, 176, 176};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};
    assertArrayEquals(expectedColors, actualColors);

    // tests BMP
    imagePixels = this.bmpModel.getImage("sixBMP");
    (imagePixels.get(0)).set(0, new Color(195, 165, 230));
    expectedColors = new int[]{195, 165, 230};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};
    assertArrayEquals(expectedColors, actualColors);

    // 41.457 + 118.008 + 16.606 = 176.071
    this.bmpModel.grayscale(ImageUtilModel.Grayscale.Luma, "sixBMP", "lumaSixBMP");
    imagePixels = this.bmpModel.getImage("lumaSixBMP");
    expectedColors = new int[]{176, 176, 176};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};
    assertArrayEquals(expectedColors, actualColors);

  }

  @Test
  public void testBrighten() {
    // tests png

    // sets pixel 0,0 to a specific color combination
    int[] expectedColors = new int[]{195, 165, 230};


    List<List<Color>> imagePixels = this.pngModel.getImage("sixPNG");
    (imagePixels.get(0)).set(0, new Color(195, 165, 230));

    int red = (imagePixels.get(0).get(0)).getRed();
    int green = (imagePixels.get(0).get(0)).getGreen();
    int blue = (imagePixels.get(0).get(0)).getBlue();
    int[] actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);


    // brightens the original colors
    this.pngModel.brighten(50, "sixPNG", "sixPNGBrighter");
    imagePixels = this.pngModel.getImage("sixPNGBrighter");
    expectedColors = new int[]{245, 215, 255};

    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);


    // darkens the original colors
    this.pngModel.brighten(-100, "sixPNG", "sixPNGDarker");
    imagePixels = this.pngModel.getImage("sixPNGDarker");
    expectedColors = new int[]{145, 115, 155};

    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);


    // tests jpg

    // sets pixel 0,0 to a specific color combination
    expectedColors = new int[]{195, 165, 230};

    imagePixels = this.jpgModel.getImage("sixJPG");
    (imagePixels.get(0)).set(0, new Color(195, 165, 230));

    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);


    // brightens the original colors
    this.jpgModel.brighten(50, "sixJPG", "sixJPGBrighter");
    imagePixels = this.jpgModel.getImage("sixJPGBrighter");
    expectedColors = new int[]{245, 215, 255};

    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);


    // darkens the original colors
    this.jpgModel.brighten(-100, "sixJPG", "sixJPGDarker");
    imagePixels = this.jpgModel.getImage("sixJPGDarker");
    expectedColors = new int[]{145, 115, 155};

    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    // tests BMP

    // sets pixel 0,0 to a specific color combination
    expectedColors = new int[]{195, 165, 230};

    imagePixels = this.bmpModel.getImage("sixBMP");
    (imagePixels.get(0)).set(0, new Color(195, 165, 230));

    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);


    // brightens the original colors
    this.bmpModel.brighten(50, "sixBMP", "sixBMPBrighter");
    imagePixels = this.bmpModel.getImage("sixBMPBrighter");
    expectedColors = new int[]{245, 215, 255};

    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);


    // darkens the original colors
    this.bmpModel.brighten(-100, "sixBMP", "sixBMPDarker");
    imagePixels = this.bmpModel.getImage("sixBMPDarker");
    expectedColors = new int[]{145, 115, 155};

    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);
  }

  @Test
  public void testBlur() {
    // tests png
    List<List<Color>> imagePixels = this.pngModel.getImage("sixPNG");
    (imagePixels.get(0)).set(0, new Color(195, 165, 230));
    int[] expectedColors = new int[]{195, 165, 230};

    int red = (imagePixels.get(0).get(0)).getRed();
    int green = (imagePixels.get(0).get(0)).getGreen();
    int blue = (imagePixels.get(0).get(0)).getBlue();
    int[] actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    BlurMacroCommand blurMacro = new BlurMacroCommand("sixPNG", "blurSixPNG");
    this.pngModel.execute(blurMacro);
    imagePixels = this.pngModel.getImage("blurSixPNG");
    expectedColors = new int[]{125, 41, 57};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    // tests jpg
    imagePixels = this.jpgModel.getImage("sixJPG");
    (imagePixels.get(0)).set(0, new Color(195, 165, 230));
    expectedColors = new int[]{195, 165, 230};

    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    blurMacro = new BlurMacroCommand("sixJPG", "blurSixJPG");
    this.jpgModel.execute(blurMacro);
    imagePixels = this.jpgModel.getImage("blurSixJPG");
    expectedColors = new int[]{125, 41, 57};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    // tests bmp
    imagePixels = this.bmpModel.getImage("sixBMP");
    (imagePixels.get(0)).set(0, new Color(195, 165, 230));
    expectedColors = new int[]{195, 165, 230};

    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    blurMacro = new BlurMacroCommand("sixBMP", "blurSixBMP");
    this.bmpModel.execute(blurMacro);
    imagePixels = this.bmpModel.getImage("blurSixBMP");
    expectedColors = new int[]{125, 41, 57};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);
  }

  @Test
  public void testSharpen() {
    // tests png
    List<List<Color>> imagePixels = this.pngModel.getImage("sixPNG");
    (imagePixels.get(0)).set(0, new Color(195, 165, 230));
    int[] expectedColors = new int[]{195, 165, 230};

    int red = (imagePixels.get(0).get(0)).getRed();
    int green = (imagePixels.get(0).get(0)).getGreen();
    int blue = (imagePixels.get(0).get(0)).getBlue();
    int[] actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    SharpenMacroCommand sharpenMacro = new SharpenMacroCommand("sixPNG", "sharpSixPNG");
    this.pngModel.execute(sharpenMacro);
    imagePixels = this.pngModel.getImage("sharpSixPNG");
    expectedColors = new int[]{255, 165, 230};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    // tests jpg
    imagePixels = this.jpgModel.getImage("sixJPG");
    (imagePixels.get(0)).set(0, new Color(195, 165, 230));
    expectedColors = new int[]{195, 165, 230};

    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    sharpenMacro = new SharpenMacroCommand("sixJPG", "sharpSixJPG");
    this.jpgModel.execute(sharpenMacro);
    imagePixels = this.jpgModel.getImage("sharpSixJPG");
    expectedColors = new int[]{255, 165, 232};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    // tests bmp
    imagePixels = this.bmpModel.getImage("sixBMP");
    (imagePixels.get(0)).set(0, new Color(195, 165, 230));
    expectedColors = new int[]{195, 165, 230};

    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    sharpenMacro = new SharpenMacroCommand("sixBMP", "sharpSixBMP");
    this.bmpModel.execute(sharpenMacro);
    imagePixels = this.bmpModel.getImage("sharpSixBMP");
    expectedColors = new int[]{255, 165, 230};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);
  }

  @Test
  public void testGreyscale() {
    // tests png
    List<List<Color>> imagePixels = this.pngModel.getImage("sixPNG");
    (imagePixels.get(0)).set(0, new Color(195, 165, 230));
    int[] expectedColors = new int[]{195, 165, 230};

    int red = (imagePixels.get(0).get(0)).getRed();
    int green = (imagePixels.get(0).get(0)).getGreen();
    int blue = (imagePixels.get(0).get(0)).getBlue();
    int[] actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    GreyscaleMacro greyscaleMacro = new GreyscaleMacro("sixPNG", "greyscaleSixPNG");
    this.pngModel.execute(greyscaleMacro);
    imagePixels = this.pngModel.getImage("greyscaleSixPNG");
    expectedColors = new int[]{176, 176, 176};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    // tests jpg
    imagePixels = this.jpgModel.getImage("sixJPG");
    (imagePixels.get(0)).set(0, new Color(195, 165, 230));
    expectedColors = new int[]{195, 165, 230};

    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    greyscaleMacro = new GreyscaleMacro("sixJPG", "greyscaleSixJPG");
    this.jpgModel.execute(greyscaleMacro);

    imagePixels = this.jpgModel.getImage("greyscaleSixJPG");
    expectedColors = new int[]{176, 176, 176};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    // tests bmp
    imagePixels = this.bmpModel.getImage("sixBMP");
    (imagePixels.get(0)).set(0, new Color(195, 165, 230));
    expectedColors = new int[]{195, 165, 230};

    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    greyscaleMacro = new GreyscaleMacro("sixBMP", "greyscaleSixBMP");
    this.bmpModel.execute(greyscaleMacro);
    imagePixels = this.bmpModel.getImage("greyscaleSixBMP");
    expectedColors = new int[]{176, 176, 176};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);
  }

  @Test
  public void testSepia() {
    // tests png
    List<List<Color>> imagePixels = this.pngModel.getImage("sixPNG");
    (imagePixels.get(0)).set(0, new Color(195, 165, 230));
    int[] expectedColors = new int[]{195, 165, 230};

    int red = (imagePixels.get(0).get(0)).getRed();
    int green = (imagePixels.get(0).get(0)).getGreen();
    int blue = (imagePixels.get(0).get(0)).getBlue();
    int[] actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    SepiaMacro sepiaMacro = new SepiaMacro("sixPNG", "sepiaSixPNG");
    this.pngModel.execute(sepiaMacro);
    imagePixels = this.pngModel.getImage("sepiaSixPNG");
    expectedColors = new int[]{246, 219, 171};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    // tests jpg
    imagePixels = this.jpgModel.getImage("sixJPG");
    (imagePixels.get(0)).set(0, new Color(195, 165, 230));
    expectedColors = new int[]{195, 165, 230};

    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    sepiaMacro = new SepiaMacro("sixJPG", "sepiaSixJPG");
    this.jpgModel.execute(sepiaMacro);
    imagePixels = this.jpgModel.getImage("sepiaSixJPG");
    expectedColors = new int[]{246, 219, 171};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    // tests bmp
    imagePixels = this.bmpModel.getImage("sixBMP");
    (imagePixels.get(0)).set(0, new Color(195, 165, 230));
    expectedColors = new int[]{195, 165, 230};

    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);

    sepiaMacro = new SepiaMacro("sixBMP", "sepiaSixBMP");
    this.bmpModel.execute(sepiaMacro);
    imagePixels = this.bmpModel.getImage("sepiaSixBMP");
    expectedColors = new int[]{246, 219, 171};
    red = (imagePixels.get(0).get(0)).getRed();
    green = (imagePixels.get(0).get(0)).getGreen();
    blue = (imagePixels.get(0).get(0)).getBlue();
    actualColors = new int[]{red, green, blue};

    assertArrayEquals(expectedColors, actualColors);
  }
}
