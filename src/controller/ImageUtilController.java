package controller;

/**
 * Interface for a controller for the {@code ImageUtil}. Delegate tasks between the
 * model and the view.
 * Reads and processes inputs from the user.
 */
public interface ImageUtilController {
  /**
   * Starts the editor, and allows the controller to tell the model and view what to do.
   *
   * @throws IllegalArgumentException if the controller is unable to successfully read input or
   *                                  transmit output.
   */
  void startEditor() throws IllegalArgumentException;
}
