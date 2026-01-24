package com.artigianhair.view.cli;

import java.util.Scanner;

public class GestioneInputCLI {
    private static final Scanner scanner = new Scanner(System.in);

    public static String leggiString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Errore.....");
        }

    }

    public static int leggiInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            return scanner.nextInt();
        }
    }
}
