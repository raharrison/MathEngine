package uk.co.raharrison.mathengine.unitconversion;

import java.util.Scanner;

public class Converter
{
	public static void main(String[] args)
	{
		ConversionEngine engine = new ConversionEngine();
		engine.updateTimeZones();
		Scanner input = new Scanner(System.in);
		long start, end;

		try
		{
			System.out.println("Enter a conversion - ");
			String inputString = input.nextLine();

			start = System.currentTimeMillis();
			
			String result = engine.convertToString(inputString);

			end = System.currentTimeMillis();

			System.out.println(result);
			System.out.println("Time taken = " + (end - start) + "ms");
		}
		catch (Exception e)
		{
			e.printStackTrace();

		}
		finally
		{
			input.close();
		}
	}
}
