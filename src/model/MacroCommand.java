package model;

/**
 * Command interface for the macros in order to add more commands easily.
 */
public interface MacroCommand {
  /**
   * Completes the command on the given model.
   * @param model the model that the command should be executed on
   */
  void doCommand(ImageUtilModelMacro model);
}
