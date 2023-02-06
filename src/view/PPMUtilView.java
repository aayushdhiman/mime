package view;

import java.io.IOException;

/**
 * The implementation of the view for a PPM file. Can currently display a message at the
 * output, which is represented by an {@code Appendable} object.
 */
public class PPMUtilView implements ImageUtilView {
  Appendable appendable;

  /**
   * Creates an {@code PPMUtilView} object that can display output to the user.
   *
   * @param appendable the location to display the output
   * @throws IllegalArgumentException if the {@code Appendable} object is {@code null}
   */
  public PPMUtilView(Appendable appendable) throws IllegalArgumentException {
    if (appendable == null) {
      throw new IllegalArgumentException("Model nor appendable can be null.");
    }
    this.appendable = appendable;
  }

  /**
   * Writes the message out to the display.
   *
   * @param message the message to be displayed.
   * @throws IOException if the object to append to doesn't exist (it is {@code null})
   */
  public void writeMessage(String message) throws IOException {
    try {
      this.appendable.append(message);
    } catch (Exception e) {
      throw new IOException("Error in writing message to appendable.");
    }
  }
}
