package de.yottaflops.distancechat;

public class Helper {
    /**
     * Replaces a character an index in a string
     *
     * @param index The character to be replaced
     * @param with The character that will be placed
     * @param input The input string
     * @return The string where the character is replaced
     */
    public static String replaceCharacterAt(int index, char with, String input) {
        if (index >= 0 && index < input.length()) {
            if (index == 0) {
                return with + input.substring(1, input.length());
            } else if (index == input.length() - 1) {
                return input.substring(0, index) + with;
            } else {
                return input.substring(0, index) + with + input.substring(index + 1, input.length());
            }
        } else {
            return input;
        }
    }
}
