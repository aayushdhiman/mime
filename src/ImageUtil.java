import java.io.InputStreamReader;
import java.io.StringReader;

import controller.ImageUtilController;
import controller.ImageUtilControllerMIME;
import model.AbstractUtilModelMacro;
import model.FileTypeUtilModel;
import model.ImageUtilModelMacro;
import view.ImageUtilView;
import view.PPMUtilView;


/**
 * Class that has the {@code main} method for the {@code ImageUtil class.} Creates the
 * controller and runs it.
 */
public class ImageUtil {
  /**
   * Run file for {@code ImageUtil class.} Creates a {@code Readable} for the input, an
   * {@code Appendable} for the output, a model, a view, and a controller, and runs the controller.
   *
   * @param args the user input/command line arguments.
   */
  public static void main(String[] args) {
    Appendable appendable = System.out;
    ImageUtilModelMacro model = new FileTypeUtilModel(AbstractUtilModelMacro.FileType.PPM);
    ImageUtilView view = new PPMUtilView(appendable);
    if (args.length == 0) {
      Readable readable = new InputStreamReader(System.in);
      ImageUtilController controller = new ImageUtilControllerMIME(model, view, readable);
      controller.startEditor();
    } else if (args.length == 2) {
      if (args[0].equals("-file")) {
        Readable readable = new StringReader("read-script " + args[1]);
        ImageUtilController controller = new ImageUtilControllerMIME(model, view, readable);
        controller.startEditor();
      } else {
        throw new IllegalArgumentException("Improper command line input.");
      }
    } else {
      throw new IllegalArgumentException("Unable to interpret command line input.");
    }
  }
}

