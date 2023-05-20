package com.wileyedge.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.wileyedge.exceptions.InsufficientBalanceException;

public class InputUtilitiesNoLoop {
	private static Scanner scanner = new Scanner(System.in);
	
	public static double getInputAsDouble(String prompt, double min, double max) throws InsufficientBalanceException {
	    double input = 0;
	        try {
	            System.out.println(prompt);
	            input = scanner.nextDouble();
	            scanner.nextLine();

	            if (input < min || input > max) {
	                System.out.printf("Input must be between %.2f and %.2f%n", min, max);
	                throw new InsufficientBalanceException("Amount entered is less than the required minimum amount");
	            }

	        } catch (InputMismatchException e) {
	            System.out.println("Input must be a valid double - try again.");
	            scanner.nextLine();
	        }
	    return input;
	}
	
}
