package model;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Represents the model for an {@code ImageUtil} program. Enforce rules and actually performs the
 * commands that are supported by the model. The implemented commands are load, save,
 * grayscale, flip horizontal or vertical, and brighten.
 */
public interface ImageUtilModel {

  /**
   * An enumeration that represents a value of the RGB color model.
   */
  enum Grayscale { Red, Green, Blue, Value, Intensity, Luma }

  /**
   * Loads an image from the given path and assigns it the given destination file name.
   *
   * @throws FileNotFoundException if the filename is not found
   */
  void loadImage(String filename, String destFilename) throws FileNotFoundException;

  /**
   * Saves an image to the provided path.
   *
   * @param filepath          the filepath to save the image to.
   * @param filenameReference the name that was previously given to reference the image.
   * @throws IOException if the filepath doesn't exist
   */
  void saveImage(String filepath, String filenameReference) throws IOException;

  /**
   * Displays only the given color component of an image. Sets the RGB value of each pixel to only
   * the value of the given component.
   *
   * @param g                 the desired component to be made into grayscale
   * @param filenameReference the name that the image is referred to as
   * @param newReferenceName  the name to save the image reference as
   * @throws IllegalArgumentException if the filenameReference is not already loaded
   */
  void grayscale(Grayscale g, String filenameReference, String newReferenceName)
          throws IllegalArgumentException;

  /**
   * Flips an image horizontally and loads it with a new reference name.
   *
   * @param filenameReference the reference name of the image to flip
   * @param newReferenceName  the name to reference the new image as
   * @throws IllegalArgumentException if the filenameReference is not already loaded
   */
  void flipHorizontal(String filenameReference, String newReferenceName)
          throws IllegalArgumentException;

  /**
   * Flips an image vertically and loads it with a new reference name.
   *
   * @param filenameReference the reference name of the image to flip
   * @param newReferenceName  the name to reference the new image as
   * @throws IllegalArgumentException if the filenameReference is not already loaded
   */
  void flipVertical(String filenameReference, String newReferenceName)
          throws IllegalArgumentException;

  /**
   * Brightens or darkens an image by the given increment and loads it with a new reference name.
   *
   * @param increment         the amount to brighten (if positive) or darken (if negative)
   *                          an image by
   * @param filenameReference the reference name of the image to brighten
   * @param newReferenceName  the name to reference the new image as
   * @throws IllegalArgumentException if the filenameReference is not already loaded
   */
  void brighten(int increment, String filenameReference, String newReferenceName)
          throws IllegalArgumentException;

  /**
   * Returns the image that the reference name refers to as a {@code List} of {@code List} of
   * {@code Color}s.
   *
   * @param referenceName the name that the image is referred to as
   * @return the {@code List} of {@code List} of {@code Color}s that represent the image
   * @throws IllegalArgumentException if the referenceName is not already loaded
   */
  List<List<Color>> getImage(String referenceName) throws IllegalArgumentException;

  /**
   * Gets the height of the specified image.
   *
   * @param referenceName the name of the image to get height of
   * @return the height of the image in pixels.
   * @throws IllegalArgumentException if the referenceName is not already loaded
   */
  int getHeight(String referenceName) throws IllegalArgumentException;

  /**
   * Gets the width of the specified image.
   *
   * @param referenceName the name of the image to get width of
   * @return the width of the image in pixels.
   * @throws IllegalArgumentException if the referenceName is not already loaded
   */
  int getWidth(String referenceName) throws IllegalArgumentException;
}
