package uk.co.ryanharrison.mathengine.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Various helpful utility methods for use throughout the library
 *
 * @author Ryan Harrison
 */
public final class Utils {
    /**
     * Not permitted to create an instance of this class
     */
    private Utils() {
    }

    /**
     * Determines whether a string can be considered to be numeric
     *
     * @param string The string to test
     * @return True if the string is numeric, otherwise false
     */
    public static boolean isNumeric(String string) {
        return NumberUtils.isCreatable(StringUtils.deleteWhitespace(string));
    }

    /**
     * Get the index of the matching character end to begin starting at index
     *
     * @param expression The string to search in
     * @param index      The index to start at
     * @param begin      The beginning character
     * @param end        The ending character which is considering a match to the
     *                   beginning character
     * @return The index of the matching character. -1 if not present
     */
    public static int matchingCharacterIndex(String expression, int index, char begin, char end) {
        int len = expression.length();
        int i = index;
        int count = 0;

        while (i < len) {
            if (expression.charAt(i) == begin) {
                count++;
            } else if (expression.charAt(i) == end) {
                count--;
            }

            if (count == 0)
                return i;

            i++;
        }

        return index;
    }

    /**
     * Remove an outer set of parenthesis from a string if present. That is if
     * the first character is a '( and last is a ')'
     *
     * @param source The string to use
     * @return source with outer parenthesis removed if present
     */
    public static String removeOuterParenthesis(String source) {
        if (!source.startsWith("(")) return source;
        int i;
        while ((i = Utils.matchingCharacterIndex(source, 0, '(', ')')) == source.length() - 1) {
            if (source.charAt(0) == '(' && i == source.length() - 1) {
                source = source.substring(1, source.length() - 1);
            }
        }
        return source;
    }

    /**
     * Remove the spaces in a string, trim the ends and convert to lower case
     *
     * @param string The string to standardise
     * @return string with no spaces, trimmed and in lower case
     */
    public static String standardiseString(String string) {
        if (string == null) return null;
        return StringUtils.deleteWhitespace(string).toLowerCase();
    }

    /**
     * Convert a string to title case (e.g sample -> Sample)
     *
     * @param input The input string to convert
     * @return input in title case
     */
    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }

}
