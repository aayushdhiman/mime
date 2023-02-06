package view;

import java.io.IOException;

/**
 * Interface for a view for the {@code ImageUtil}. Has the capability to display a message by
 * appending the message's content to an {@code Appendable} object.
 */
public interface ImageUtilView {
  /**
   * Writes a message to the output of the application.
   *
   * @param message the message to display
   * @throws IOException if the display destination is null
   */
  void writeMessage(String message) throws IOException;
}
