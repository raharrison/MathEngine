package uk.co.raharrison.mathengine;

import java.util.Arrays;
import java.util.Collections;

public final class Utils
{
	private Utils()
	{
	}

	// Does string have matching set of parenthesis
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

	// Is string null or empty
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

	// Is character numeric
	public static boolean isNumeric(char character)
	{
		return Character.isDigit(character);
	}

	// Is string numeric
	public static boolean isNumeric(String string)
	{
		try
		{
			Double.parseDouble(string);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	// Reverse double array
	public static void reverse(double[] elements)
	{
		int left = 0; // index of leftmost element
		int right = elements.length - 1; // index of rightmost element

		while (left < right)
		{
			// exchange the left and right elements
			double temp = elements[left];
			elements[left] = elements[right];
			elements[right] = temp;

			// move the bounds toward the centre
			left++;
			right--;
		}
	}

	// Reverse T array
	public static <T> void reverse(T[] elements)
	{
		Collections.reverse(Arrays.asList(elements));
	}

	// Remove spaces and to convert to lower
	public static String standardizeString(String string)
	{
		return string.replace(" ", "").trim().toLowerCase();
	}
	
	// Convert a String to Title Case (e.g sample -> Sample)
	public static String toTitleCase(String input)
	{
		StringBuilder titleCase = new StringBuilder();
		boolean nextTitleCase = true;
		
		for(char c : input.toCharArray())
		{
			if(Character.isSpaceChar(c))
			{
				nextTitleCase = true;
			}
			else if(nextTitleCase)
			{
				c = Character.toTitleCase(c);
				nextTitleCase = false;
			}
			
			titleCase.append(c);
		}
		
		return titleCase.toString();		
	}
}
