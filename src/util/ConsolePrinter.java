package util;

import java.util.List;
import java.util.Scanner;

/**
 * ConsolePrinter utility class for formatted console output and input.
 * Provides methods for displaying formatted information and handling user input.
 * 
 * @author MediConnect+ Development Team
 * @version 1.0
 */
public class ConsolePrinter {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String SEPARATOR = "=".repeat(50);
    private static final String DASH_SEPARATOR = "-".repeat(50);
    private static final String THICK_SEPARATOR = "=".repeat(60);
    
    // Color codes for console output (ANSI escape codes)
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    
    // Background colors
    public static final String BG_RED = "\u001B[41m";
    public static final String BG_GREEN = "\u001B[42m";
    public static final String BG_YELLOW = "\u001B[43m";
    public static final String BG_BLUE = "\u001B[44m";
    public static final String BG_PURPLE = "\u001B[45m";
    public static final String BG_CYAN = "\u001B[46m";
    
    // Text styles
    public static final String BOLD = "\u001B[1m";
    public static final String ITALIC = "\u001B[3m";
    public static final String UNDERLINE = "\u001B[4m";
    
    /**
     * Print a header with title
     * @param title title to display
     */
    public static void printHeader(String title) {
        System.out.println();
        System.out.println(CYAN + THICK_SEPARATOR + RESET);
        System.out.println(CYAN + BOLD + centerText(title, 60) + RESET);
        System.out.println(CYAN + THICK_SEPARATOR + RESET);
        System.out.println();
    }
    
    /**
     * Print a subheader
     * @param title title to display
     */
    public static void printSubHeader(String title) {
        System.out.println();
        System.out.println(BLUE + SEPARATOR + RESET);
        System.out.println(BLUE + BOLD + centerText(title, 50) + RESET);
        System.out.println(BLUE + SEPARATOR + RESET);
        System.out.println();
    }
    
    /**
     * Print a section separator
     */
    public static void printSeparator() {
        System.out.println(DASH_SEPARATOR);
    }
    
    /**
     * Print a thick separator
     */
    public static void printThickSeparator() {
        System.out.println(THICK_SEPARATOR);
    }
    
    /**
     * Print success message
     * @param message success message
     */
    public static void printSuccess(String message) {
        System.out.println(GREEN + "✓ " + message + RESET);
    }
    
    /**
     * Print error message
     * @param message error message
     */
    public static void printError(String message) {
        System.out.println(RED + "✗ " + message + RESET);
    }
    
    /**
     * Print warning message
     * @param message warning message
     */
    public static void printWarning(String message) {
        System.out.println(YELLOW + "⚠ " + message + RESET);
    }
    
    /**
     * Print info message
     * @param message info message
     */
    public static void printInfo(String message) {
        System.out.println(CYAN + "ℹ " + message + RESET);
    }
    
    /**
     * Print a formatted menu
     * @param title menu title
     * @param options menu options
     */
    public static void printMenu(String title, String[] options) {
        printSubHeader(title);
        for (int i = 0; i < options.length; i++) {
            System.out.println(BOLD + "[" + (i + 1) + "] " + RESET + options[i]);
        }
        System.out.println();
    }
    
    /**
     * Print a formatted menu with custom numbering
     * @param title menu title
     * @param options menu options
     * @param startNumber starting number for options
     */
    public static void printMenu(String title, String[] options, int startNumber) {
        printSubHeader(title);
        for (int i = 0; i < options.length; i++) {
            System.out.println(BOLD + "[" + (startNumber + i) + "] " + RESET + options[i]);
        }
        System.out.println();
    }
    
    /**
     * Print a list of items with numbering
     * @param title list title
     * @param items list of items
     */
    public static void printList(String title, List<String> items) {
        printSubHeader(title);
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1) + ". " + items.get(i));
        }
        System.out.println();
    }
    
    /**
     * Print a table with headers and data
     * @param headers table headers
     * @param data table data rows
     */
    public static void printTable(String[] headers, List<String[]> data) {
        if (headers.length == 0 || data.isEmpty()) {
            printWarning("No data to display");
            return;
        }
        
        // Calculate column widths
        int[] columnWidths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            columnWidths[i] = headers[i].length();
        }
        
        for (String[] row : data) {
            for (int i = 0; i < row.length && i < headers.length; i++) {
                columnWidths[i] = Math.max(columnWidths[i], row[i].length());
            }
        }
        
        // Print table
        printTableRow(headers, columnWidths, true);
        printTableSeparator(columnWidths);
        
        for (String[] row : data) {
            printTableRow(row, columnWidths, false);
        }
        System.out.println();
    }
    
    /**
     * Print a table row
     * @param row row data
     * @param columnWidths column widths
     * @param isHeader whether this is a header row
     */
    private static void printTableRow(String[] row, int[] columnWidths, boolean isHeader) {
        System.out.print("|");
        for (int i = 0; i < row.length && i < columnWidths.length; i++) {
            String cell = row[i];
            if (cell.length() > columnWidths[i]) {
                cell = cell.substring(0, columnWidths[i]);
            }
            String formattedCell = String.format(" %-" + columnWidths[i] + "s ", cell);
            if (isHeader) {
                System.out.print(BOLD + BLUE + formattedCell + RESET);
            } else {
                System.out.print(formattedCell);
            }
            System.out.print("|");
        }
        System.out.println();
    }
    
    /**
     * Print table separator
     * @param columnWidths column widths
     */
    private static void printTableSeparator(int[] columnWidths) {
        System.out.print("+");
        for (int width : columnWidths) {
            System.out.print("-".repeat(width + 2) + "+");
        }
        System.out.println();
    }
    
    /**
     * Print a confirmation dialog
     * @param message confirmation message
     * @return true if user confirms, false otherwise
     */
    public static boolean printConfirmation(String message) {
        System.out.print(YELLOW + message + " (y/n): " + RESET);
        String input = scanner.nextLine().trim().toLowerCase();
        return input.equals("y") || input.equals("yes");
    }
    
    /**
     * Get user input with prompt
     * @param prompt input prompt
     * @return user input
     */
    public static String getInput(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine().trim();
    }
    
    /**
     * Get user input with prompt and validation
     * @param prompt input prompt
     * @param validator input validator
     * @return validated user input
     */
    public static String getInput(String prompt, java.util.function.Predicate<String> validator) {
        String input;
        do {
            input = getInput(prompt);
            if (!validator.test(input)) {
                printError("Invalid input. Please try again.");
            }
        } while (!validator.test(input));
        return input;
    }
    
    /**
     * Get integer input with prompt
     * @param prompt input prompt
     * @return integer input
     */
    public static int getIntInput(String prompt) {
        int value;
        do {
            try {
                value = Integer.parseInt(getInput(prompt));
                break;
            } catch (NumberFormatException e) {
                printError("Please enter a valid integer.");
            }
        } while (true);
        return value;
    }
    
    /**
     * Get integer input with range validation
     * @param prompt input prompt
     * @param min minimum value
     * @param max maximum value
     * @return validated integer input
     */
    public static int getIntInput(String prompt, int min, int max) {
        int value;
        do {
            value = getIntInput(prompt);
            if (value < min || value > max) {
                printError("Please enter a number between " + min + " and " + max + ".");
            }
        } while (value < min || value > max);
        return value;
    }
    
    /**
     * Get password input (hidden)
     * @param prompt input prompt
     * @return password input
     */
    public static String getPasswordInput(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine().trim();
    }
    
    /**
     * Print a loading message
     * @param message loading message
     */
    public static void printLoading(String message) {
        System.out.print(CYAN + message + "..." + RESET);
        try {
            Thread.sleep(1000); // Simulate loading
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(GREEN + " Done!" + RESET);
    }
    
    /**
     * Print a progress bar
     * @param current current progress
     * @param total total progress
     * @param message progress message
     */
    public static void printProgress(int current, int total, String message) {
        int percentage = (current * 100) / total;
        int barLength = 30;
        int filledLength = (current * barLength) / total;
        
        String bar = "█".repeat(filledLength) + "░".repeat(barLength - filledLength);
        System.out.printf("\r%s: [%s] %d%% (%d/%d)", message, bar, percentage, current, total);
        
        if (current == total) {
            System.out.println();
        }
    }
    
    /**
     * Print a formatted card
     * @param title card title
     * @param content card content
     */
    public static void printCard(String title, String content) {
        System.out.println();
        System.out.println(BOLD + BLUE + "+- " + title + " -+" + RESET);
        System.out.println(BLUE + "|" + RESET + " " + content + " " + BLUE + "|" + RESET);
        System.out.println(BLUE + "+" + "-".repeat(title.length() + 4) + "+" + RESET);
        System.out.println();
    }
    
    /**
     * Print a formatted alert
     * @param type alert type (success, error, warning, info)
     * @param message alert message
     */
    public static void printAlert(String type, String message) {
        String icon;
        String color;
        
        switch (type.toLowerCase()) {
            case "success":
                icon = "✓";
                color = GREEN;
                break;
            case "error":
                icon = "✗";
                color = RED;
                break;
            case "warning":
                icon = "⚠";
                color = YELLOW;
                break;
            case "info":
                icon = "ℹ";
                color = CYAN;
                break;
            default:
                icon = "•";
                color = WHITE;
        }
        
        System.out.println(color + BOLD + icon + " " + message + RESET);
    }
    
    /**
     * Clear the console screen
     */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    /**
     * Pause execution and wait for user input
     * @param message pause message
     */
    public static void pause(String message) {
        System.out.println();
        System.out.print(message + " Press Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Center text within specified width
     * @param text text to center
     * @param width total width
     * @return centered text
     */
    private static String centerText(String text, int width) {
        if (text.length() >= width) {
            return text;
        }
        
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text + " ".repeat(width - text.length() - padding);
    }
    
    /**
     * Print a welcome message
     * @param appName application name
     * @param version application version
     */
    public static void printWelcome(String appName, String version) {
        clearScreen();
        printHeader("Welcome to " + appName);
        System.out.println(CYAN + BOLD + "Version: " + version + RESET);
        System.out.println(CYAN + "Smart Medical Appointment Management System" + RESET);
        System.out.println();
        printInfo("Please log in to continue...");
        System.out.println();
    }
    
    /**
     * Print a goodbye message
     * @param appName application name
     */
    public static void printGoodbye(String appName) {
        System.out.println();
        printHeader("Thank you for using " + appName);
        System.out.println(GREEN + "Have a great day! 👋" + RESET);
        System.out.println();
    }
    
    /**
     * Close the scanner
     */
    public static void close() {
        scanner.close();
    }
}
