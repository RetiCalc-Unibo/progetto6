import java.io.*;
import java.rmi.*;

public class Client{
	public static void main(String[] args){
		final int REGISTRYPORT = 1099;
		String registryHost = null;

		String serviceName = "";
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

		if(args.length != 2){
			System.out.println("[C] Syntax: java Client RegistryHost [RegistryPort]");
			System.exit(1);
		}

		registryHost = args[0];
		serviceName = args[1];

		System.out.println("[C] Invio richieste a " + registryHost + " per il servizio di nome " + serviceName);

		try {
			String completeName = "//" + registryHost + ":" + REGISTRYPORT + "/" + serviceName;
			ServerImpl serverRMI = (ServerImpl) Naming.lookup(completeName);
			System.out.println("[C] Servizio \"" + serviceName + "\" connesso");
			System.out.println("[C] Richieste di servizio fino a fine file");

			String service;

			System.out.print("[C] Servizio (C = Conta Parole - E = Elimina Riga): ");

			while((service = stdIn.readLine()) != null){
				if(service.equals("C")){
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

					 if(serverRMI.conta_righe(fileName, numParole, delim) == 0)
					 	System.out.println("[C] Conto le righe del file " + fileName + " con uno threshold di " + numParole + " e come delimitatori " + delim);
					 else
					 	System.out.println("[C] Servizio attualmente non disponibile");
				} else if(service.equals("E")){
					 String fileName;
					 int numRiga;

					 // Ricevo in input il nome del file
					 System.out.print("[C] Inserisci il nome del file: ");
					 fileName = stdIn.readLine();
					 // Ricevo l'intero
					 System.out.print("[C] Inserisci il la riga da eliminare: ");
					 numRiga = Integer.parseInt(stdIn.readLine());

					 if(serverRMI.elimina_riga(fileName, numRiga) == 0)
					 	System.out.println("[C] Elimino la riga " + numRiga + " dal file " + fileName);
					 else
					 	System.out.println("[C] Servizio attualmente non disponibile");
				} else {
					System.out.println("[C] Servizio non esistente!");
					System.out.println("[C] Servizi disponibili: C -> Conta Parole; E -> Elimina Riga");
				}
			}
		} catch(RemoteException re){
			System.err.println("[C] Possibile errore nel file; " + re.getMessage());
			re.printStackTrace();
		} catch(NotBoundException nbe){
			System.err.println("[C] Il nome fornito non risulta nel resgistry; " + nbe.getMessage());
			System.exit(2);
		} catch(Exception e){
			System.err.println("[C] " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}
}