import java.io.*;
import java.rmi.*;

public class Client {
    public static void main(String[] args) {
        final int REGISTRYPORT = 1099;
        String registryHost = null;

        String serviceName = null;
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        if (args.length != 2) {
            System.out.println("[C] Syntax: java Client RegistryHost ServiceName");
            System.exit(1);
        }

        registryHost = args[0];
        serviceName = args[1];

        System.out.println("[C] Invio richieste a " + registryHost + " per il servizio di nome " + serviceName);

        try {
            String completeName = "//" + registryHost + ":" + REGISTRYPORT + "/" + serviceName;
            RemOp serverRMI = (RemOp) Naming.lookup(completeName);
            System.out.println("[C] Servizio \"" + serviceName + "\" connesso");
            System.out.println("[C] Richieste di servizio fino a fine file");

            String service;

            System.out.print("[C] Servizio (C = Conta Parole - E = Elimina Riga), CTRL+D per uscire:  ");
            while ((service = stdIn.readLine()) != null) {
                if (service.equals("C")) {
                    String fileName, delim;
                    int numParole;

                    // Ricevo in input il nome del file
                    System.out.print("[C] Inserisci il nome del file: ");
                    fileName = stdIn.readLine();
                    // Ricevo l'intero
                    System.out.print("[C] Inserisci il numero di parole per riga da ricercare: ");
                    numParole = Integer.parseInt(stdIn.readLine());
                    // Ricevo i separatori
                    System.out.print("[C] Inserisci i separatori: ");
                    delim = stdIn.readLine();

                    System.out.println("[C] Conto le righe del file " + fileName + " con un threshold di " + numParole + " e come delimitatori " + delim);
                    
                    try {
                        System.out.println("[C] Nel file sono presenti " + serverRMI.conta_righe(fileName, numParole, delim) + " righe con più di " + numParole + " parole");
                    } catch (RemoteException re) {
                        System.err.println("[C] Possibile errore nel file; " + re.getMessage());
                        re.printStackTrace();
                    }

                } else if (service.equals("E")) {
                    String fileName;
                    int numRiga, ris;

                    // Ricevo in input il nome del file
                    System.out.print("[C] Inserisci il nome del file: ");
                    fileName = stdIn.readLine();
                    // Ricevo l'intero
                    System.out.print("[C] Inserisci il riga da eliminare: ");
                    numRiga = Integer.parseInt(stdIn.readLine());

                    try {
                        System.out.println("[C] La linea richiesta è stata eliminata e il file ha " + serverRMI.elimina_riga(fileName, numRiga) + " righe");
                    } catch (RemoteException re) {
                        System.err.println("[C] Possibile errore nel file; " + re.getMessage());
                        re.printStackTrace();
                    }
                } else {
                    System.out.println("[C] Servizio non esistente!");
                    System.out.println("[C] Servizi disponibili: C -> Conta Parole; E -> Elimina Riga, CTRL+D per uscire: ");
                }
                System.out.print("[C] Servizio (C = Conta Parole - E = Elimina Riga), CTRL+D per uscire: ");

            }
        } catch (NotBoundException nbe) {
            System.err.println("[C] Il nome fornito non risulta nel registry; " + nbe.getMessage());
            System.exit(2);
        } catch (Exception e) {
            System.err.println("[C] " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}