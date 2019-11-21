import java.io.*;
import java.rmi.*;

public class Client {
    public static void main(String[] args) {
        final int REGISTRYPORT = 1099;
        String registryHost = null;

        String serviceName = null;
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        if (args.length != 2) {
            System.out.println("Client: Errore di sintassi, utilizzo: java Client RegistryHost ServiceName");
            System.exit(1);
        }

        registryHost = args[0];
        serviceName = args[1];

        System.out.println("Client: Invio richieste a " + registryHost + " per il servizio di nome " + serviceName);

        try {
            String completeName = "//" + registryHost + ":" + REGISTRYPORT + "/" + serviceName;
            RemOp serverRMI = (RemOp) Naming.lookup(completeName);
            System.out.println("Client: Servizio \"" + serviceName + "\" connesso");
            System.out.println("Client: Richieste di servizio fino a fine file");

            String service;

            System.out.print("Client: Digitare 'C/CC' per il servizio di conta parole, 'E' per eliminare una riga o CTRL+D per uscire: ");
            while ((service = stdIn.readLine()) != null) {
                if (service.equals("C") || service.equals("CC")) {
                    String fileName, delim;
                    int numParole;

                    // Ricevo in input il nome del file
                    System.out.print("Client: Inserire il nome del file: ");
                    fileName = stdIn.readLine();
                    // Ricevo l'intero
                    System.out.print("Client: Inserire il numero minimo di parole per riga: ");
                    
		    if((numParole = Integer.parseInt(stdIn.readLine())) < 0) {
			System.out.println("Client: Errore, devi inserire un numero maggiore o uguale a 0");	
                	System.out.print("Client: Digitare 'C/CC' per il servizio di conta parole, 'E' per eliminare una riga o CTRL+D per uscire: ");
			continue;
		    }
                    // Ricevo i separatori
                    System.out.print("Client: Inserire i separatori: ");
                    delim = stdIn.readLine();

                    System.out.println("Client: Conto le righe del file " + fileName + " con piu' di " + numParole + " parole e come delimitatori " + delim);
                    
                    try {
                    	int serv = -1;
                    	if(service.equals("C")){
                    		time = System.nanoTime();
                    		serv = serverRMI.conta_righe(fileName, numParole, delim);
                    	}
                    	if(service.equals("CC")){
                    		time = System.nanoTime();
                    		serv = serverRMI.conta_righe_char(fileName, numParole, delim);
                    	}
                    } catch (RemoteException re) {
                        System.err.println("Client: Possibile errore nel file; " + re.getMessage());
                        re.printStackTrace();
                    }
                } else if (service.equals("E")) {
                    String fileName;
                    int numRiga, ris;

                    // Ricevo in input il nome del file
                    System.out.print("Client: Inserire il nome del file: ");
                    fileName = stdIn.readLine();
                    // Ricevo l'intero
                    System.out.print("Client: Inserire il numero della riga da eliminare: ");
                    if((numRiga = Integer.parseInt(stdIn.readLine())) < 1) {
			System.out.println("Client: Errore, devi inserire un numero positivo");	
                	System.out.print("Client: Digitare 'C' per il servizio di conta parole, 'E' per eliminare una riga o CTRL+D per uscire: ");
			continue;
		    }

                    try {
                        System.out.println("Client: La linea richiesta è stata eliminata e il file ha " + serverRMI.elimina_riga(fileName, numRiga) + " righe");
                    } catch (RemoteException re) {
                        System.err.println("Client: Possibile errore nel file; " + re.getMessage());
                        re.printStackTrace();
                    }
                } else {
                    System.out.println("Client: Servizio non esistente!");
                    System.out.println("'C' per conta parole;");
                    System.out.println("'E' per elimina riga;");
                    System.out.println("CTRL+D per uscire;");
                    System.out.println("");
                }

                System.out.print("Client: Digitare 'C' per il servizio di conta parole, 'E' per eliminare una riga o CTRL+D per uscire: ");
            }
        } catch (NotBoundException nbe) {
            System.err.println("Client: Il nome fornito non risulta nel registry; " + nbe.getMessage());
            System.exit(2);
        } catch (Exception e) {
            System.err.println("Client: " + e.getMessage());
            e.printStackTrace();
            System.exit(3);
        }
    }
}
