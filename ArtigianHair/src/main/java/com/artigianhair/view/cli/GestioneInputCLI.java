package com.artigianhair.view.cli;
import java.util.Scanner;
import java.util.logging.Logger;

public class GestioneInputCLI {

    private static final Scanner scanner = new Scanner(System.in);
    private GestioneInputCLI() {}
    public static int leggiInt(String messaggio) {
        System.out.print(messaggio);
        while (!scanner.hasNextInt()) {
            System.out.println("Errore: inserisci un numero intero.");
            System.out.print(messaggio);
            scanner.next();
        }
        int valore = scanner.nextInt();
        scanner.nextLine();
        return valore;
    }

    public static String leggiString(String messaggio) {
        System.out.print(messaggio);
        return scanner.nextLine();
    }
}

