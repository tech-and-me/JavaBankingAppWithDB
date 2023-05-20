package com.wileyedge.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class InputUtilities {
	private static Scanner scanner = new Scanner(System.in);

	public static int getInputAsInteger(String prompt, int min, int max) {
	    int input = 0;
	    boolean validInput = false;

	    do {
	        try {
	            System.out.println(prompt);
	            input = scanner.nextInt();
	            scanner.nextLine();

	            if (input < min || input > max) {
	                System.out.printf("Input must be between %d and %d%n", min, max);
	                continue;
	            }
	            validInput = true;

	        } catch (InputMismatchException e) {
	            System.out.println("Input must be an integer.");
	            scanner.nextLine(); // consume invalid input
	        } catch (Exception e) {
	            System.out.println("Oops! Something went wrong.");
	        }

	    } while (!validInput);

	    return input;
	}
	
	public static double getInputAsDouble(String prompt, double min, double max) {
	    boolean validInput = false;
	    double input = 0;

	    do {
	        try {
	            System.out.println(prompt);
	            input = scanner.nextDouble();
	            scanner.nextLine();

	            if (input < min || input > max) {
	                System.out.printf("Input must be between %.2f and %.2f%n", min, max);
	                continue;
	            }

	            validInput = true;
	        } catch (InputMismatchException e) {
	            System.out.println("Input must be a valid double - try again.");
	            scanner.nextLine();
	        }
	    } while (!validInput);

	    return input;
	}


	
	
	public static long getInputAsLong(String prompt, long min, long max) {
	    boolean validInput = false;
	    long input = 0;
	    
	    do {
	        try {
	            System.out.println(prompt);
	            input = scanner.nextLong();
	            scanner.nextLine();

	            if (input < min || input > max) {
	            	System.out.printf("Input must be between %d and %d%n", min, max);
	                continue;
	            }

	            validInput = true;
	        } catch (InputMismatchException e) {
	            System.out.println("Input must be a valid integer - try again.");
	            scanner.nextLine();
	        } 
	    } while (!validInput);

	    return input;
	}
	
	public static String getInputAsString(String prompt, int minLength, int maxLength) {
	    boolean validInput = false;
	    String input = null;

	    do {
	        try {
	            System.out.println(prompt);
	            input = scanner.nextLine();

	            if (input.length() < minLength || input.length() > maxLength) {
	                System.out.printf("Input must be between %d and %d characters long.%n", minLength, maxLength);
	                continue;
	            }

	            validInput = true;
	        } catch (Exception e) {
	            System.out.println("Ooops! Something went wrong. Please try again.");
	            System.out.println(e.getMessage());
	            e.printStackTrace();
	        }
	    } while (!validInput);

	    return input;
	}
	
	public static String getInputAsDate(String prompt, LocalDate minDate, LocalDate maxDate) {
	    boolean validInput = false;
	    String input = null;

	    do {
	        try {
	            System.out.println(prompt);
	            input = scanner.nextLine();

	            // Parse the input string into a LocalDate object
	            LocalDate date = LocalDate.parse(input, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

	            // Check that the date is between today and 80 years ago
	            if (date.isBefore(minDate) || date.isAfter(maxDate)) {
	                System.out.println("Input must be a date between " + minDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
	                        " and " + maxDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
	                continue;
	            }

	            validInput = true;
	        } catch (DateTimeParseException e) {
	            System.out.println("Input must be a valid date in the format DD/MM/YYYY - try again.");
	        } catch (Exception e) {
	            System.out.println("Oops! Something went wrong.");
	            e.printStackTrace();
	        }
	    } while (!validInput);

	    // Return the date as a string in the desired format
	    return input;
	}


}
