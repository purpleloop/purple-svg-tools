package io.github.purpleloop.tools.svg.model;

import java.util.Optional;

/**
 * Models path commands. Letters are in lower case for relative commands and in
 * upper case for absolute commands.
 */
public enum PathCommand {

    /** Move. */
    MOVE("m"),

    /** Draw a line */
    LINETO("l"),

    /** Draw an horizontal line. */
    HORIZONTAL_LINETO("h"),

    /** Draw a vertical line. */
    VERTICAL_LINETO("v"),

    /** Draw a curve. */
    CURVE("c"),

    /** Draw an arch. */
    ARCH("a"),

    /** Close the path. */
    CLOSE("z");

    /** The command letter (for relative commands). */
    private String relativeCommandLetter;

    /**
     * Path command constructor.
     * 
     * @param letter the letter for relative command
     */
    private PathCommand(String letter) {
        this.relativeCommandLetter = letter;
    }

    /**
     * Get the path command (relative or absolute) for the given path command
     * string.
     * 
     * @param pathCommandString the path command string
     * @return optional of path command
     */
    static Optional<PathCommand> getCommand(String pathCommandString) {
        for (PathCommand command : PathCommand.values()) {
            if (command.relativeCommandLetter.equalsIgnoreCase(pathCommandString)) {
                return Optional.of(command);
            }
        }

        return Optional.empty();
    }

    /**
     * Tests if the command is relative.
     * 
     * @param pathCommandString the path command to check
     * @return relative if case of the command is strictly equals to the
     *         relative command letter, false otherwise
     */
    boolean isRelative(String pathCommandString) {
        return relativeCommandLetter.equals(pathCommandString);
    }
}
