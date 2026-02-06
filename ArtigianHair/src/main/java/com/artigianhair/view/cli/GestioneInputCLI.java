package com.artigianhair.view.cli;
import java.util.Scanner;


public class GestioneInputCLI {

    private static final Scanner scanner = new Scanner(System.in);
    private GestioneInputCLI() {}

    @SuppressWarnings("java:S106")
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

    @SuppressWarnings("java:S106")
    public static String leggiString(String messaggio) {
        System.out.print(messaggio);
        return scanner.nextLine();
    }

    @SuppressWarnings("java:S106")
    public static void print(String messaggio) {
        System.out.println(messaggio);
    }

    @SuppressWarnings("java:S106")
    public static void print2(String s) {
        System.out.print(s);
    }
}

