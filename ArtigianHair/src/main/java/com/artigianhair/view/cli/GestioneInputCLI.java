/*Quando usi scanner.nextInt(), il sistema legge solo il numero, ma lascia nel "buffer" di input il tasto Invio (\n)
che hai premuto. Quando subito dopo chiami scanner.nextLine(), questa vede l'Invio rimasto lì e pensa
che tu abbia già finito di scrivere, restituendo una stringa vuota e passando alla riga successiva.*/

package com.artigianhair.view.cli;
import java.util.Scanner;
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
        scanner.nextLine(); //Pulisce il buffer
        return valore;
    }

    public static String leggiString(String messaggio) {
        System.out.print(messaggio);
        return scanner.nextLine();
    }
}

