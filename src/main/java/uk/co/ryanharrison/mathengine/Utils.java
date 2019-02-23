package uk.co.ryanharrison.mathengine;

import java.util.Arrays;
import java.util.Collections;

/**
 * Various helpful utility methods for use throughout the library
 * 
 * @author Ryan Harrison
 * 
 */
public final class Utils
{
	/**
	 * Not permitted to create an instance of this class
	 */
	private Utils()
	{
	}

	/**
	 * Get the index of any of the characters from anyOf in str
	 * 
	 * @param str
	 *            The string to search in
	 * @param anyOf
	 *            The array of characters to search for
	 * @return The index of any of the characters in str. -1 if none present
	 */
	public static int indexOfAny(String str, char[] anyOf)
	{
		for (int i = 0; i < anyOf.length; i++)
		{
			int index = str.indexOf(anyOf[i]);
			if (index != -1)
				return index;
		}
		return -1;
	}

	/**
	 * Determines whether or not a string has matching parenthesis
	 * 
	 * @param string
	 *            The string to test
	 * @return True if the string has matching parenthesis. Otherwise false
	 */
	public static boolean isMatchingParenthesis(String string)
	{
		int count = 0;

		for (int i = 0; i < string.length(); i++)
		{
			if (string.charAt(i) == '(')
				count++;

			if (string.charAt(i) == ')')
				count--;
		}

		return count == 0;
	}

	/**
	 * Determines if a string is null of empty
	 * 
	 * @param string
	 *            The string to test
	 * @return True if the string is null or empty. Otherwise false.
	 */
	public static boolean isNullOrEmpty(String string)
	{
		if (string != null)
		{
			if (!string.equals(""))
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Determines whether a string can be considered to be numeric
	 * 
	 * @param string
	 *            The string to test
	 * @return True if the string is numeric, otherwise false
	 */
	public static boolean isNumeric(String string)
	{
		// Double.parseDouble will accept strings with 'd' or 'f' in
		if (string.contains("d") || string.contains("f"))
			return false;

		try
		{
			Double.parseDouble(removeSpaces(string));
			return true;
		}
		catch (NumberFormatException e)
		{
			return false;
		}
	}

	/**
	 * Join together an array of elements separated by a delimiter
	 * 
	 * @param elements
	 *            The elements to join
	 * @param delimiter
	 *            The delimiter to use as a separator
	 * @return The elements joined using the delimiter as a separator
	 */
	public static <T> String join(T[] elements, String delimiter)
	{
		if (elements == null)
			return "";

		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < elements.length; i++)
		{
			builder.append(elements[i]).append(delimiter);
		}

		String result = builder.toString();
		if (result.length() > delimiter.length())
			return result.substring(0, result.length() - delimiter.length());
		else
			return result;
	}

	/**
	 * Get the index of the matching character end to begin starting at index
	 * 
	 * @param expression
	 *            The string to search in
	 * @param index
	 *            The index to start at
	 * @param begin
	 *            The beginning character
	 * @param end
	 *            The ending character which is considering a match to the
	 *            beginning character
	 * @return The index of the matching character. index of not present
	 */
	public static int matchingCharacterIndex(String expression, int index, char begin, char end)
	{
		int len = expression.length();
		int i = index;
		int count = 0;

		while (i < len)
		{
			if (expression.charAt(i) == begin)
			{
				count++;
			}
			else if (expression.charAt(i) == end)
			{
				count--;
			}

			if (count == 0)
				return i;

			i++;
		}

		return index;
	}

	/**
	 * Reverse an array of elements
	 * 
	 * @param elements
	 *            The elements to reverse
	 * @return The reversed elements
	 */
	public static <T> T[] reverse(T[] elements)
	{
		Collections.reverse(Arrays.asList(elements));
		return elements;
	}

	/**
	 * Remove an outer set of parenthesis from a string if present. That is if
	 * the first character is a '( and last is a ')'
	 * 
	 * @param source
	 *            The string to use
	 * @return source with outer parenthesis removed if present
	 */
	public static String removeOuterParenthesis(String source)
	{
		int i = Utils.matchingCharacterIndex(source, 0, '(', ')');
		if (source.charAt(0) == '(' && i == source.length() - 1)
		{
			return source.substring(1, source.length() - 1);
		}
		else
			return source;
	}

	/**
	 * Remove all the spaces in a string
	 * 
	 * @param string
	 *            The string to remove the spaces of
	 * @return string with all spaces removed
	 */
	public static String removeSpaces(String string)
	{
		if (string == null || string.length() == 0)
			return new String(string);

		int sz = string.length();
		char[] chs = new char[sz];
		int count = 0;
		for (int i = 0; i < sz; i++)
		{
			char c = string.charAt(i);
			if (!Character.isWhitespace(c))
			{
				chs[count++] = c;
			}
		}
		if (count == sz)
			return new String(string);

		return new String(chs, 0, count);
	}

	/**
	 * Remove the spaces in a string, trim the ends and convert to lower case
	 * 
	 * @param string
	 *            The string to standardise
	 * @return string with no spaces, trimmed and in lower case
	 */
	public static String standardiseString(String string)
	{
		return removeSpaces(string).trim().toLowerCase();
	}

	/**
	 * Convert a string to a number.
	 * 
	 * @param str
	 *            The string to convert
	 * @return The sum of the ASCII characters in str
	 */
	public static int stringToNum(String str)
	{
		int result = 0;

		for (int i = 0; i < str.length(); i++)
		{
			result += str.charAt(i);
		}

		return result;
	}

	/**
	 * Convert a string to title case (e.g sample -> Sample)
	 * 
	 * @param input
	 *            The input string to convert
	 * @return input in title case
	 */
	public static String toTitleCase(String input)
	{
		StringBuilder titleCase = new StringBuilder();
		boolean nextTitleCase = true;

		for (char c : input.toCharArray())
		{
			if (Character.isSpaceChar(c))
			{
				nextTitleCase = true;
			}
			else if (nextTitleCase)
			{
				c = Character.toTitleCase(c);
				nextTitleCase = false;
			}

			titleCase.append(c);
		}

		return titleCase.toString();
	}

	/**
	 * Trim the end of a string with a character
	 * 
	 * @param str
	 *            The string to trim
	 * @param character
	 *            The character to remove from the end of str
	 * @return The string with all instances of character removed from the end
	 */
	public static String trimEnd(String str, char character)
	{
		if (str == null)
			return null;
		char[] arr = str.toCharArray();
		char[] newArr = new char[1];
		int pos = 0;

		for (int i = 0; i < arr.length; i++)
		{
			pos = (arr.length - 1) - i;
			if (arr[pos] != character)
			{
				newArr = new char[pos + 1];
				System.arraycopy(arr, 0, newArr, 0, pos + 1);
				break;
			}
		}
		return new String(newArr);
	}
}
