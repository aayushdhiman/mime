package model;

import java.awt.Color;
import java.util.List;

/**
 * Interface that allows the macro to interact with the model.
 */
public interface ImageUtilModelMacro extends ImageUtilModel {
  /**
   * Executes the given macro command on this model.
   * @param macro the macro to be completed.
   */
  void execute(MacroCommand macro);

  /**
   * Adds an image to the Map of imageReferences.
   * @param pixels the {@code List<List<Color>>} to add to the map
   * @param filenameReference the name to call that image
   */
  void addImage(List<List<Color>> pixels, String filenameReference);
}
