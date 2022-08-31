package mc.carlton.freerpg.commands;

import mc.carlton.freerpg.utils.game.LanguageSelector;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHelper {

  /*
   * This class perform simple tests on common "properties" of a command to see if the user's input is valid.
   * It also provides feedback to the user via message when the inputs are incorrect,
   */
  private CommandSender sender;
  private String[] args;
  private int minArguments;
  private int maxArguments;
  private String permissionName;
  private boolean isPlayerOnlyCommand = false;
  private boolean checkInBed = false;
  private String improperArgumentsMessage;

  /**
   * Constructor for CommandHelper
   *
   * @param sender                   CommandSender instance
   * @param args                     Arguments of command
   * @param minArguments             The minimum  number of allowed arguments
   * @param maxArguments             The maximum number of allowed arguments (set to 0 for no
   *                                 restriciton)
   * @param improperArgumentsMessage The message to be displayed if the user fails a validity test
   *                                 of the command. The message string can be formatted such that
   *                                 it can be decoded by
   * @see LanguageSelector#translateMessage(String)
   */
  public CommandHelper(CommandSender sender, String[] args, int minArguments, int maxArguments,
      String improperArgumentsMessage) {
    this.sender = sender;
    this.args = args;
    this.minArguments = minArguments;
    this.maxArguments = maxArguments;
    this.improperArgumentsMessage = improperArgumentsMessage;
  }

  /**
   * Constructor for CommandHelper
   *
   * @param sender                   CommandSender instance
   * @param args                     Arguments of command
   * @param numberOfArguments        The maximum number of allowed arguments (set to 0 for no
   *                                 restriciton)
   * @param improperArgumentsMessage The message to be displayed if the user fails a validity test
   *                                 of the command. The message string can be formatted such that
   *                                 it can be decoded by
   * @see LanguageSelector#translateMessage(String)
   */
  public CommandHelper(CommandSender sender, String[] args, int numberOfArguments,
      String improperArgumentsMessage) {
    this.sender = sender;
    this.args = args;
    this.minArguments = numberOfArguments;
    this.maxArguments = numberOfArguments;
    this.improperArgumentsMessage = improperArgumentsMessage;
  }

  public void setPermissionName(String permissionName) {
    this.permissionName = permissionName;
  }

  public void setPlayerOnlyCommand(boolean isPlayerOnlyCommand) {
    this.isPlayerOnlyCommand = isPlayerOnlyCommand;
  }

  public void setCheckInBed(boolean checkInBed) {
    this.checkInBed = checkInBed;
  }

  /**
   * Checks if a command is properly sent
   *
   * @return true if the command is "proper", i.e has a proper number of arguments, is cast by an
   * allowed sender, the sender has the proper permissions, and the sender is not in a bed (if
   * restricted).
   */
  public boolean isProperCommand() {
    if (maxArguments > 0) { //Ignore argument restrictions if argument is less than 0
      if (!hasProperNumberOfArguments()) {
        return false; //Improper arguments
      }
    }
    if (isPlayerOnlyCommand) {
      if (!isPlayer()) {
        return false; //Not a player (player only command)
      }
    }
    if (!hasPermission()) {
      return false; //No permission
    }
    if (checkInBed) {
      if (isInBed()) {
        return false; //Cannot be in bed
      }
    }
    return true; //Passed all tests
  }

  /**
   * Checks if a player has permission to use a command and sends the player a message if they do
   * not have permission
   *
   * @return true if player has permission, false otherwise
   */
  public boolean hasPermission() {
    if (sender instanceof Player) {
      Player p = (Player) sender;
      if (!p.hasPermission("freeRPG." + permissionName)) {
        LanguageSelector lang = new LanguageSelector(p);
        p.sendMessage(ChatColor.RED + lang.getString("noPermission"));
        return false;
      }
    }
    return true;
  }

  /**
   * Checks if a player is in bed and sends the player a message if they are
   *
   * @return true if player is in a bed
   */
  public boolean isInBed() {
    if (sender instanceof Player) {
      Player p = (Player) sender;
      LanguageSelector lang = new LanguageSelector(p);
      if (p.isSleeping()) {
        p.sendMessage(ChatColor.RED + lang.getString("bedGUI"));
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if the CommandSender is a player and sends them a message if not
   *
   * @return true if commandSender is a player
   */
  public boolean isPlayer() {
    if (sender instanceof Player) {
      return true;
    }
    sender.sendMessage("You must be a player to cast this command!");
    return false;
  }

  /**
   * Checks if the command has the proper number of arguments and sends a message to the sender if
   * not
   *
   * @return true if the command has a proper number of arguments
   */
  public boolean hasProperNumberOfArguments() {
    if (minArguments <= args.length && args.length <= maxArguments) {
      return true;
    } else {
      sendImproperArgumentsMessage();
      return false;
    }
  }

  /**
   * Sends an "Improper Arguments" message to the player.
   */
  public void sendImproperArgumentsMessage() {
    if (sender instanceof Player) {
      Player p = (Player) sender;
      LanguageSelector lang = new LanguageSelector(p);
      p.sendMessage(ChatColor.RED + lang.getString("improperArguments") + lang.translateMessage(
          improperArgumentsMessage));
    } else {
      sender.sendMessage("Improper Arguments, try " + LanguageSelector.getEnglishMessage(
          improperArgumentsMessage));
    }
  }
}
