package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import model.AbstractUtilModelMacro.FileType;
import model.BlurMacroCommand;
import model.FileTypeUtilModel;
import model.GreyscaleMacro;
import model.ImageUtilModel.Grayscale;
import model.ImageUtilModelMacro;
import model.MacroCommand;
import model.SepiaMacro;
import model.SharpenMacroCommand;
import view.ImageUtilView;


/**
 * A class to implement the controller for the {@code ImageUtil}. Tells the model when to execute
 * the commands, and tells the view what to display when a command either succeeds or fails.
 * Currently, supports loading an image, saving an image, creating grayscale with the red,
 * green, blue, value, intensity, and luma components, flipping an image horizontally or
 * vertically, brightening or darkening an image, and reading a script from a txt file. Calls
 * macros when needed as well as model.execute().
 */
public class ImageUtilControllerMIME implements ImageUtilController {
  private ImageUtilModelMacro model;
  private final ImageUtilView view;
  private final Readable readable;

  /**
   * Creates a controller for the {@code ImageUtil} application. Handles input from the user and
   * delegates tasks to the model and view.
   *
   * @param model    the model that enforces the rules and completes tasks for the controller
   * @param view     the view that displays the output
   * @param readable the input from the user
   * @throws IllegalArgumentException if the model, view, or input is null
   */
  public ImageUtilControllerMIME(ImageUtilModelMacro model, ImageUtilView view, Readable readable)
          throws IllegalArgumentException {
    if (model == null || view == null || readable == null) {
      throw new IllegalArgumentException("The model, view, nor the readable can be null.");
    }
    this.model = model;
    this.view = view;
    this.readable = readable;
  }

  /**
   * Turns the user input into an array using {@code .split( )}.
   *
   * @param scanner the Scanner object with the user input
   * @return the String array of inputs
   * @throws IllegalStateException if the {@code Scanner} runs out of inputs
   */
  private String[] getUserInput(Scanner scanner) throws IllegalStateException {
    try {
      view.writeMessage(System.lineSeparator() + "Enter command or \"q\" to quit: ");
      return scanner.nextLine().split(" ");
    } catch (IOException e) {
      System.out.println(e.getMessage());
      return scanner.nextLine().split(" ");
    } catch (NoSuchElementException e) {
      try {
        view.writeMessage("No more inputs in file.");
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
      throw new IllegalStateException("Out of inputs.");
    }
  }

  /**
   * Uses the view to print the welcome message with a list of all the commands the controller
   * can handle as well as how to use them properly.
   */
  private void printWelcomeMessage() {
    try {
      view.writeMessage("ImageUtil commands: " + System.lineSeparator());
      view.writeMessage("\"load filepath-on-disk reference-name\": Loads an image into the " +
              "editor. It will be referred to as \"reference-name\"." + System.lineSeparator());
      view.writeMessage("\"save-ppm filepath-on-disk reference-name\": Saves the image with the" +
              " name \"reference-name\" to your disk at the location \"filepath-on-disk\" as a " +
              "PPM file." + System.lineSeparator());
      view.writeMessage("\"save-png filepath-on-disk reference-name\": Saves the image with the" +
              " name \"reference-name\" to your disk at the location \"filepath-on-disk\" as a " +
              "PNG file." + System.lineSeparator());
      view.writeMessage("\"red-component reference-name new-reference-name\": Creates a " +
              "grayscale image with the red component of the image with the name " +
              "\"reference name\" and stores it with the name " +
              "\"new-reference-name\"." + System.lineSeparator());
      view.writeMessage("\"green-component reference-name new-reference-name\": Creates a " +
              "grayscale image with the green component of the image with the name " +
              "\"reference name\" and stores it with the name " +
              "\"new-reference-name\"." + System.lineSeparator());
      view.writeMessage("\"blue-component reference-name new-reference-name\": Creates a " +
              "grayscale image with the blue component of the image with the name " +
              "\"reference name\" and stores it with the name " +
              "\"new-reference-name\"." + System.lineSeparator());
      view.writeMessage("\"value-component reference-name new-reference-name\": Creates a " +
              "grayscale image with the value component of the image with the name " +
              "\"reference name\" and stores it with the name " +
              "\"new-reference-name\"." + System.lineSeparator());
      view.writeMessage("\"intensity-component reference-name new-reference-name\": Creates a " +
              "grayscale image with the intensity component of the image with the name " +
              "\"reference name\" and stores it with the name " +
              "\"new-reference-name\"." + System.lineSeparator());
      view.writeMessage("\"luma-component reference-name new-reference-name\": Creates a " +
              "grayscale image with the luma component of the image with the name " +
              "\"reference name\" and stores it with the name " +
              "\"new-reference-name\"." + System.lineSeparator());
      view.writeMessage("\"horizontal-flip reference-name new-reference-name\": Flips the " +
              "image with the name \"reference-name\" horizontally and stores it with the " +
              "name \"new-reference-name\"." + System.lineSeparator());
      view.writeMessage("\"vertical-flip reference-name new-reference-name\": Flips the " +
              "image with the name \"reference-name\" vertically and stores it with the " +
              "name \"new-reference-name\"." + System.lineSeparator());
      view.writeMessage("\"brighten increment reference-name new-reference-name\": " +
              "Brightens the image with the name \"reference-name\" by \"increment\" and " +
              "stores it with the name \"new-reference-name\". Negative increments will darken " +
              "the image." + System.lineSeparator());
      view.writeMessage("\"read-script path-to-script\": Reads the txt file provided and " +
              "runs the commands inside of it." + System.lineSeparator());
      view.writeMessage("\"greyscale reference-name new-reference-name\": Greyscales the image " +
              "with the name \"reference-name\" and stores it with the name " +
              "\"new-reference=name\"." + System.lineSeparator());
      view.writeMessage("\"blur reference-name new-reference-name\": Blurs the image " +
              "with the name \"reference-name\" and stores it with the name " +
              "\"new-reference=name\"." + System.lineSeparator());
      view.writeMessage("\"sepia reference-name new-reference-name\": Sepia filters the image " +
              "with the name \"reference-name\" and stores it with the name " +
              "\"new-reference=name\"." + System.lineSeparator());
      view.writeMessage("\"sharpen reference-name new-reference-name\": Sharpens the image " +
              "with the name \"reference-name\" and stores it with the name " +
              "\"new-reference=name\"." + System.lineSeparator());
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Tries to complete the load command, and prints a message with the view if it was
   * successful or not.
   *
   * @param filepath         the file to load
   * @param newReferenceName the name to call the loaded file
   */
  private void tryLoad(String filepath, String newReferenceName) {
    String fileType = filepath.substring(filepath.length() - 3);
    boolean completedCommand = true;
    FileType ending = null;
    switch (fileType) {
      case "png":
        ending = FileType.PNG;
        break;
      case "jpg":
        ending = FileType.JPG;
        break;
      case "bmp":
        ending = FileType.BMP;
        break;
      case "ppm":
        ending = FileType.PPM;
        break;
      default:
        completedCommand = false;
        // view prints that there was an error
        try {
          view.writeMessage("Illegal filetype to save as.");
        } catch (IOException e) {
          System.out.println(e.getMessage());
        }
        break;
    }
    try {
      this.model = new FileTypeUtilModel(ending);
      model.loadImage(filepath, newReferenceName);
    } catch (IndexOutOfBoundsException e) {
      try {
        completedCommand = false;
        view.writeMessage("Not enough inputs.");
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    } catch (FileNotFoundException | IllegalStateException e) {
      try {
        completedCommand = false;
        view.writeMessage(e.getMessage());
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    }

    if (completedCommand) {
      try {
        view.writeMessage("Loaded " + filepath + " as \"" + newReferenceName + "\"");
      } catch (IOException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * Tries to save the file.
   *
   * @param filePath          the filepath to save to
   * @param filenameReference the file to save
   */
  private void trySave(String filePath, String filenameReference) {
    String fileType = filePath.substring(filePath.length() - 3);
    boolean completedCommand = true;
    FileType ending = null;
    switch (fileType) {
      case "png":
        ending = FileType.PNG;
        break;
      case "jpg":
        ending = FileType.JPG;
        break;
      case "bmp":
        ending = FileType.BMP;
        break;
      case "ppm":
        ending = FileType.PPM;
        break;
      default:
        completedCommand = false;
        // view prints that there was an error
        try {
          view.writeMessage("Illegal filetype to save as.");
        } catch (IOException e) {
          System.out.println(e.getMessage());
        }
        break;
    }

    try {
      ImageUtilModelMacro tempModel = new FileTypeUtilModel(ending);
      tempModel.addImage(this.model.getImage(filenameReference), filenameReference);
      tempModel.saveImage(filePath, filenameReference);
    } catch (IOException e) {
      completedCommand = false;
      try {
        view.writeMessage(e.getMessage());
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    }

    if (completedCommand) {
      try {
        view.writeMessage("Save successful");
      } catch (IOException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * Tries to complete the red-component command.
   *
   * @param filename          the file to greyscale
   * @param filenameReference the name of the greyscaled file
   */
  private void tryRedComponent(String filename, String filenameReference) {
    boolean completedCommand = true;
    try {
      model.grayscale(Grayscale.Red, filename, filenameReference);
    } catch (IndexOutOfBoundsException e) {
      try {
        completedCommand = false;
        view.writeMessage("Not enough inputs.");
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    } catch (IllegalArgumentException e) {
      try {
        completedCommand = false;
        view.writeMessage("That image hasn't been loaded yet.");
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    } catch (IllegalStateException e) {
      try {
        completedCommand = false;
        view.writeMessage(e.getMessage());
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    }

    if (completedCommand) {
      try {
        view.writeMessage("Red component saved as " + filenameReference);
      } catch (IOException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * Tries to complete the green-component command.
   *
   * @param filename          the file to greyscale
   * @param filenameReference the name of the greyscaled file
   */
  private void tryGreenComponent(String filename, String filenameReference) {
    boolean completedCommand = true;
    try {
      model.grayscale(Grayscale.Green, filename, filenameReference);
    } catch (IndexOutOfBoundsException e) {
      try {
        completedCommand = false;
        view.writeMessage("Not enough inputs.");
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    } catch (IllegalArgumentException e) {
      try {
        completedCommand = false;
        view.writeMessage("That image hasn't been loaded yet.");
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    } catch (IllegalStateException e) {
      try {
        completedCommand = false;
        view.writeMessage(e.getMessage());
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    }

    if (completedCommand) {
      try {
        view.writeMessage("Green component saved as " + filenameReference);
      } catch (IOException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * Tries to complete the blue-component command.
   *
   * @param filename          the file to greyscale
   * @param filenameReference the name of the greyscaled file
   */
  private void tryBlueComponent(String filename, String filenameReference) {
    boolean completedCommand = true;
    try {
      model.grayscale(Grayscale.Blue, filename, filenameReference);
    } catch (IndexOutOfBoundsException e) {
      try {
        completedCommand = false;
        view.writeMessage("Not enough inputs.");
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    } catch (IllegalArgumentException e) {
      try {
        completedCommand = false;
        view.writeMessage("That image hasn't been loaded yet.");
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    } catch (IllegalStateException e) {
      try {
        completedCommand = false;
        view.writeMessage(e.getMessage());
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    }

    if (completedCommand) {
      try {
        view.writeMessage("Blue component saved as " + filenameReference);
      } catch (IOException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * Tries to complete the value-component command.
   *
   * @param filename          the file to greyscale
   * @param filenameReference the name of the greyscaled file
   */
  private void tryValueComponent(String filename, String filenameReference) {
    boolean completedCommand = true;
    try {
      model.grayscale(Grayscale.Value, filename, filenameReference);
    } catch (IndexOutOfBoundsException e) {
      try {
        completedCommand = false;
        view.writeMessage("Not enough inputs.");
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    } catch (IllegalArgumentException e) {
      try {
        completedCommand = false;
        view.writeMessage("That image hasn't been loaded yet.");
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    } catch (IllegalStateException e) {
      try {
        completedCommand = false;
        view.writeMessage(e.getMessage());
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    }

    if (completedCommand) {
      try {
        view.writeMessage("Value component saved as " + filenameReference);
      } catch (IOException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * Tries to complete the intensity-component command.
   *
   * @param filename          the file to greyscale
   * @param filenameReference the name of the greyscaled file
   */
  private void tryIntensityComponent(String filename, String filenameReference) {
    boolean completedCommand = true;
    try {
      model.grayscale(Grayscale.Intensity, filename, filenameReference);
    } catch (IndexOutOfBoundsException e) {
      try {
        completedCommand = false;
        view.writeMessage("Not enough inputs.");
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    } catch (IllegalArgumentException e) {
      try {
        completedCommand = false;
        view.writeMessage("That image hasn't been loaded yet.");
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    } catch (IllegalStateException e) {
      try {
        completedCommand = false;
        view.writeMessage(e.getMessage());
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    }

    if (completedCommand) {
      try {
        view.writeMessage("Intensity component saved as " + filenameReference);
      } catch (IOException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * Tries to complete the luma-component command.
   *
   * @param filename          the file to greyscale
   * @param filenameReference the name of the greyscaled file
   */
  private void tryLumaComponent(String filename, String filenameReference) {
    boolean completedCommand = true;
    try {
      model.grayscale(Grayscale.Luma, filename, filenameReference);
    } catch (IndexOutOfBoundsException e) {
      try {
        completedCommand = false;
        view.writeMessage("Not enough inputs.");
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    } catch (IllegalArgumentException e) {
      try {
        completedCommand = false;
        view.writeMessage("That image hasn't been loaded yet.");
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    } catch (IllegalStateException e) {
      try {
        completedCommand = false;
        view.writeMessage(e.getMessage());
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    }

    if (completedCommand) {
      try {
        view.writeMessage("Luma component saved as " + filenameReference);
      } catch (IOException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * Tries to complete the brighten command.
   *
   * @param increment         how much to increase/decrease the brightness by
   * @param filename          the file to brighten or darken
   * @param filenameReference what to call the new file
   */
  private void tryBrighten(String increment, String filename, String filenameReference) {
    boolean completedCommand = true;
    try {
      model.brighten(Integer.parseInt(increment), filename, filenameReference);
    } catch (NumberFormatException e) {
      try {
        completedCommand = false;
        view.writeMessage("Increment is not a number.");
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    } catch (IndexOutOfBoundsException e) {
      try {
        completedCommand = false;
        view.writeMessage("Not enough inputs.");
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    } catch (IllegalArgumentException e) {
      try {
        completedCommand = false;
        view.writeMessage("That image hasn't been loaded yet.");
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    } catch (IllegalStateException e) {
      try {
        completedCommand = false;
        view.writeMessage(e.getMessage());
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    }

    if (completedCommand) {
      try {
        view.writeMessage("Image brightened by " + increment);
      } catch (IOException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * Tries to complete the horizontal-flip command.
   *
   * @param filename          the file to flip
   * @param filenameReference the name of the new file
   */
  private void tryHorizontal(String filename, String filenameReference) {
    boolean completedCommand = true;
    try {
      model.flipHorizontal(filename, filenameReference);
    } catch (IndexOutOfBoundsException e) {
      try {
        completedCommand = false;
        view.writeMessage("Not enough inputs.");
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    } catch (IllegalArgumentException e) {
      try {
        completedCommand = false;
        view.writeMessage("That image hasn't been loaded yet.");
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    } catch (IllegalStateException e) {
      try {
        completedCommand = false;
        view.writeMessage(e.getMessage());
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    }

    if (completedCommand) {
      try {
        view.writeMessage("Horizontal flip completed");
      } catch (IOException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * Tries to complete the vertical-flip command.
   *
   * @param filename          the file to flip
   * @param filenameReference the name of the new file
   */
  private void tryVertical(String filename, String filenameReference) {
    boolean completedCommand = true;
    try {
      model.flipVertical(filename, filenameReference);
    } catch (IndexOutOfBoundsException e) {
      try {
        completedCommand = false;
        view.writeMessage("Not enough inputs.");
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    } catch (IllegalArgumentException e) {
      try {
        completedCommand = false;
        view.writeMessage("That image hasn't been loaded yet.");
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    } catch (IllegalStateException e) {
      try {
        completedCommand = false;
        view.writeMessage(e.getMessage());
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    }

    if (completedCommand) {
      try {
        view.writeMessage("Vertical flip completed");
      } catch (IOException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * Tries to complete the blur command.
   *
   * @param filename          the file to blur
   * @param filenameReference the name of the new file
   */
  private void tryBlur(String filename, String filenameReference) {
    boolean completedCommand = true;
    try {
      MacroCommand blur = new BlurMacroCommand(filename, filenameReference);
      model.execute(blur);
    } catch (IndexOutOfBoundsException e) {
      try {
        completedCommand = false;
        view.writeMessage("Not enough inputs.");
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    } catch (IllegalArgumentException e) {
      try {
        completedCommand = false;
        view.writeMessage("That image hasn't been loaded yet.");
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    } catch (IllegalStateException e) {
      try {
        completedCommand = false;
        view.writeMessage(e.getMessage());
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    }

    if (completedCommand) {
      try {
        view.writeMessage("Blur completed");
      } catch (IOException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * Tries to complete the sharpen command.
   *
   * @param filename          the file to sharpen
   * @param filenameReference the name of the new file
   */
  private void trySharpen(String filename, String filenameReference) {
    boolean completedCommand = true;
    try {
      MacroCommand sharpen = new SharpenMacroCommand(filename, filenameReference);
      model.execute(sharpen);
    } catch (IndexOutOfBoundsException e) {
      try {
        completedCommand = false;
        view.writeMessage("Not enough inputs.");
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    } catch (IllegalArgumentException e) {
      try {
        completedCommand = false;
        view.writeMessage("That image hasn't been loaded yet.");
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    } catch (IllegalStateException e) {
      try {
        completedCommand = false;
        view.writeMessage(e.getMessage());
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    }

    if (completedCommand) {
      try {
        view.writeMessage("Sharpen completed");
      } catch (IOException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * Tries to complete the sepia command.
   *
   * @param filename          the file to color transform
   * @param filenameReference the name of the new file
   */
  private void trySepia(String filename, String filenameReference) {
    boolean completedCommand = true;
    try {
      MacroCommand sepia = new SepiaMacro(filename, filenameReference);
      model.execute(sepia);
    } catch (IndexOutOfBoundsException e) {
      try {
        completedCommand = false;
        view.writeMessage("Not enough inputs.");
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    } catch (IllegalArgumentException e) {
      try {
        completedCommand = false;
        view.writeMessage("That image hasn't been loaded yet.");
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    } catch (IllegalStateException e) {
      try {
        completedCommand = false;
        view.writeMessage(e.getMessage());
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    }

    if (completedCommand) {
      try {
        view.writeMessage("Sepia completed");
      } catch (IOException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * Tries to complete the sepia command.
   *
   * @param filename          the file to color transform
   * @param filenameReference the name of the new file
   */
  private void tryGreyscale(String filename, String filenameReference) {
    boolean completedCommand = true;
    try {
      MacroCommand greyscale = new GreyscaleMacro(filename, filenameReference);
      model.execute(greyscale);
    } catch (IndexOutOfBoundsException e) {
      try {
        completedCommand = false;
        view.writeMessage("Not enough inputs.");
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    } catch (IllegalArgumentException e) {
      try {
        completedCommand = false;
        view.writeMessage("That image hasn't been loaded yet.");
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    } catch (IllegalStateException e) {
      try {
        completedCommand = false;
        view.writeMessage(e.getMessage());
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    }

    if (completedCommand) {
      try {
        view.writeMessage("Greyscale completed");
      } catch (IOException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  @Override
  public void startEditor() throws IllegalArgumentException {
    boolean quitEditor = false;
    Scanner sc = new Scanner(readable);
    this.printWelcomeMessage();

    while (!quitEditor) {
      boolean completedCommand = true;
      String[] input;
      try {
        input = this.getUserInput(sc);
      } catch (IllegalStateException e) {
        break;
      }
      try {
        switch (input[0]) {
          case "load":
            this.tryLoad(input[1], input[2]);
            break;
          case "save":
            this.trySave(input[1], input[2]);
            break;
          case "red-component":
            this.tryRedComponent(input[1], input[2]);
            break;
          case "green-component":
            this.tryGreenComponent(input[1], input[2]);
            break;
          case "blue-component":
            this.tryBlueComponent(input[1], input[2]);
            break;
          case "value-component":
            this.tryValueComponent(input[1], input[2]);
            break;
          case "intensity-component":
            this.tryIntensityComponent(input[1], input[2]);
            break;
          case "luma-component":
            this.tryLumaComponent(input[1], input[2]);
            break;
          case "horizontal-flip":
            this.tryHorizontal(input[1], input[2]);
            break;
          case "vertical-flip":
            this.tryVertical(input[1], input[2]);
            break;
          case "greyscale":
            this.tryGreyscale(input[1], input[2]);
            break;
          case "brighten":
            this.tryBrighten(input[1], input[2], input[3]);
            break;
          case "blur":
            tryBlur(input[1], input[2]);
            break;
          case "sharpen":
            trySharpen(input[1], input[2]);
            break;
          case "sepia":
            trySepia(input[1], input[2]);
            break;
          case "read-script":
            try {
              File file = new File(input[1]);
              sc = new Scanner(file);
            } catch (IndexOutOfBoundsException e) {
              try {
                completedCommand = false;
                view.writeMessage("Not enough inputs.");
              } catch (IOException ex) {
                System.out.println(ex.getMessage());
              }
            } catch (FileNotFoundException e) {
              try {
                completedCommand = false;
                view.writeMessage("Filepath doesn't exist!");
              } catch (IOException ex) {
                System.out.println(ex.getMessage());
              }
            } catch (IllegalStateException e) {
              completedCommand = true;
              sc = new Scanner(readable);
            }

            if (completedCommand) {
              try {
                view.writeMessage("Script file loaded");
              } catch (IOException e) {
                System.out.println(e.getMessage());
              }
            }
            break;
          case "q":
            quitEditor = true;
            try {
              view.writeMessage("Quitting.");
            } catch (IOException e) {
              System.out.println(e.getMessage());
            }
            break;
          default:
            try {
              view.writeMessage("Invalid input, try again.");
            } catch (IOException e) {
              System.out.println(e.getMessage());
            }
            break;
        }
      } catch (IndexOutOfBoundsException e) {
        System.out.println(e.getMessage());
      }
    }
  }
}
