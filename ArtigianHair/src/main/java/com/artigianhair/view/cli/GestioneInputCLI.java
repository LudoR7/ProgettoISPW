package com.artigianhair.view.cli;
import java.util.Scanner;

//Classe per centralizzare la gestione degli Input/Output nella CLI
public class GestioneInputCLI {

    // Oggetto Scanner condiviso per la lettura dello standard input
    private static final Scanner scanner = new Scanner(System.in);
    private GestioneInputCLI() {}

    @SuppressWarnings("java:S106")
    public static int leggiInt(String messaggio) {
        System.out.print(messaggio);
        //Verifica se l'input è un numero
        while (!scanner.hasNextInt()) {
            System.out.println("Errore: inserisci un numero intero.");
            System.out.print(messaggio);
            scanner.next();
        }
        int valore = scanner.nextInt();
        scanner.nextLine(); // Pulisce il buffer (per gli \n)
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
        //Stampa un messaggio senza andare a capo
        System.out.print(s);
    }
}

