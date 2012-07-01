package uk.co.raharrison.mathengine.unitconversion;

import java.util.Scanner;

public class Converter
{
	public static void main(String[] args)
	{
		ConversionEngine engine = new ConversionEngine();
		Scanner input = new Scanner(System.in);

		try
		{
			System.out.println("Enter a conversion - ");
			System.out.println(engine.convertToString(input.nextLine()));
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		finally
		{
			input.close();
		}
	}
}
