package uk.co.ryanharrison.mathengine.unitconversion;

import java.util.Scanner;

public final class Converter {

    static void main() {
        ConversionEngine engine = ConversionEngine.loadDefaults();
        engine.updateTimeZones();

        try (Scanner input = new Scanner(System.in)) {
            System.out.println("Unit Converter - Enter conversion in format: <amount> <unit> to <unit>");
            System.out.println("Example: 100 meters to feet");
            System.out.println("Type 'exit' to quit\n");

            while (true) {
                System.out.print("Enter conversion: ");
                String inputString = input.nextLine().trim();

                if ("exit".equalsIgnoreCase(inputString) || "quit".equalsIgnoreCase(inputString)) {
                    System.out.println("Goodbye!");
                    break;
                }

                if (inputString.isEmpty()) {
                    continue;
                }

                try {
                    long startTime = System.currentTimeMillis();
                    ConversionResult result = engine.convertFromString(inputString);
                    long endTime = System.currentTimeMillis();

                    System.out.println("Result: " + result);
                    System.out.println("Time taken: " + (endTime - startTime) + "ms\n");
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage() + "\n");
                }
            }
        }
    }
}
