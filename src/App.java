// CPSC 323-06 19665
// Project 2 - Syntax Analyzer
// Team Members:
// 1. Guido Asbun
// 2. Cade Duncan
// 3. Briyana Verdugo

import java.util.Scanner;

public class App {
    public static void main(String[] args){
        LRParser parser = new LRParser();

        System.out.println("Parsing test for: (id+id)*id$");
        parser.parseString("(id+id)*id$");
        System.out.println("\n\nParsing test for: id*id$");
        parser.parseString("id*id$");
        System.out.println("\n\nParsing test for: (id*)$");
        parser.parseString("(id*)$");

        Scanner scanner = new Scanner(System.in);
        System.out.println("\n\nEnter a string to parse: ");System.out.println("LR Parser Testing");
        System.out.println("Enter strings to parse or 'q' to quit:");

        while (true) {
            System.out.print("Input: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("q")) {
                System.out.println("Exiting...");
                break;
            }

            // Parse the string if it's not the quit command
            if (!input.isEmpty()) {
                parser.parseString(input);
            } else {
                System.out.println("No input detected. Please enter a valid string.");
            }
        }
        scanner.close();
    }
}
