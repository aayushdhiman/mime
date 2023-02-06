package hw05;


import org.junit.Before;
import org.junit.Test;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.List;

import model.BlurMacroCommand;
import model.FileTypeUtilModel;
import model.ImageUtilModel;
import model.ImageUtilModelMacro;
import model.MacroCommand;
import model.SepiaMacro;
import model.SharpenMacroCommand;

import static model.AbstractUtilModelMacro.FileType.PNG;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


/**
 * A Junit test class for macros with filters.
 */
public class FilterMacroTest {

  ImageUtilModel model;
  ImageUtilModelMacro macroModel;
  MacroCommand blur;
  MacroCommand sharpen;
  MacroCommand sepia;

  @Before
  public void init() {
    this.model = new FileTypeUtilModel(PNG);
    try {
      this.model.loadImage("res/6ColorRGB.png", "ogSix");
    } catch (FileNotFoundException e) {
      fail("Unable to load b, got message " + e.getMessage());
    }
    this.macroModel = new FileTypeUtilModel(PNG);
    try {
      this.macroModel.loadImage("res/6ColorRGB.png", "six");
    } catch (FileNotFoundException e) {
      fail("Unable to load b, got message " + e.getMessage());
    }

    this.blur = new BlurMacroCommand("res/6ColorRGB.png", "six");
    this.sharpen = new SharpenMacroCommand("res/6ColorRGB.png", "six");
    this.sepia = new SepiaMacro("res/6ColorRGB.png", "six");

  }

  @Test
  public void testSharpenDoCommand() {
    List<List<Color>> ogImagePixels = this.model.getImage("ogSix");
    List<List<Color>> imagePixels = this.macroModel.getImage("six");
    for (int i = 0; i < ogImagePixels.size(); i++) {
      for (int j = 0; j < ogImagePixels.get(0).size(); j++) {
        assertEquals(ogImagePixels.get(i).get(j), imagePixels.get(i).get(j));
      }
    }

    this.sharpen.doCommand(this.macroModel);

    ImageUtilModelMacro sharpenModel = new FileTypeUtilModel(PNG);
    try {
      this.model.loadImage("res/sixSharpen.png", "sixSharpen");
    } catch (FileNotFoundException e) {
      fail("Unable to load b, got message " + e.getMessage());
    }

    List<List<Color>> sepiaImagePixels = sharpenModel.getImage("sixSharpen");

    for (int i = 0; i < sepiaImagePixels.size(); i++) {
      for (int j = 0; j < sepiaImagePixels.get(0).size(); j++) {
        assertEquals(sepiaImagePixels.get(i).get(j), imagePixels.get(i).get(j));
      }
    }

  }

  @Test
  public void testBlurDoCommand() {
    List<List<Color>> ogImagePixels = this.model.getImage("ogSix");
    List<List<Color>> imagePixels = this.macroModel.getImage("six");
    for (int i = 0; i < ogImagePixels.size(); i++) {
      for (int j = 0; j < ogImagePixels.get(0).size(); j++) {
        assertEquals(ogImagePixels.get(i).get(j), imagePixels.get(i).get(j));
      }
    }

    this.blur.doCommand(this.macroModel);

    ImageUtilModelMacro blurModel = new FileTypeUtilModel(PNG);
    try {
      this.model.loadImage("res/sixBlur.png", "sixBlur");
    } catch (FileNotFoundException e) {
      fail("Unable to load b, got message " + e.getMessage());
    }

    List<List<Color>> sepiaImagePixels = blurModel.getImage("sixBlur");

    for (int i = 0; i < sepiaImagePixels.size(); i++) {
      for (int j = 0; j < sepiaImagePixels.get(0).size(); j++) {
        assertEquals(sepiaImagePixels.get(i).get(j), imagePixels.get(i).get(j));
      }
    }

  }


  @Test
  public void testSepiaDoCommand() {
    List<List<Color>> ogImagePixels = this.model.getImage("ogSix");
    List<List<Color>> imagePixels = this.macroModel.getImage("six");
    for (int i = 0; i < ogImagePixels.size(); i++) {
      for (int j = 0; j < ogImagePixels.get(0).size(); j++) {
        assertEquals(ogImagePixels.get(i).get(j), imagePixels.get(i).get(j));
      }
    }
    this.sepia.doCommand(this.macroModel);

    ImageUtilModelMacro sepiaModel = new FileTypeUtilModel(PNG);
    try {
      this.model.loadImage("res/sixSepia.png", "sixSepia");
    } catch (FileNotFoundException e) {
      fail("Unable to load b, got message " + e.getMessage());
    }

    List<List<Color>> sepiaImagePixels = sepiaModel.getImage("sixSepia");

    for (int i = 0; i < sepiaImagePixels.size(); i++) {
      for (int j = 0; j < sepiaImagePixels.get(0).size(); j++) {
        assertEquals(sepiaImagePixels.get(i).get(j), imagePixels.get(i).get(j));
      }
    }


  }
}
