import java.io.*;
import java.rmi.*;

public class Client{
	public static void main(String[] args){
		final int REGISTRYPORT = 1099;
		String registryHost = null;

		String serviceName = "";
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

		if(args.length != 2){
			System.out.println("Client: Syntax: java Client RegistryHost [RegistryPort]");
			System.exit(1);
		}

		registryHost = args[0];
		serviceName = args[1];

		System.out.println("Client: Invio richieste a " + registryHost + " per il servizio di nome " + serviceName);

		try {
			String completeName = "//" + registryHost + ":" + REGISTRYPORT + "/" + serviceName;
			ServerImpl serverRMI = (ServerImpl) Naming.lookup(completeName);
			System.out.println("Client: Servizio \"" + serviceName + "\" connesso");
			System.out.println("Client: Richieste di servizio fino a fine file");

			String service;

			System.out.print("Client: Servizio (C = Conta Parole - E = Elimina Riga): ");

			while((serivce = stdIn.readLine()) != null){
				if(service.equals("C")){
					 // Ricevo in input il nome del file
					 String fileName, delim;
					 int numParole;
					 System.out.print("Client: Inserisci il nome del file: ");
					 fileName = stdIn.readLine();
					 // Ricevo l'intero
					 System.out.print("Client: Inserisci il numero di parole per riga da ricercare: ");
					 numParole = Integer.parseInt(stdIn.readLine());
					 // Ricevo i separatori
					 System.out.print("Client: Inserisci i separatori: ");
					 delim = stdIn.readLine();

					 if(serverRMI.conta_righe(fileName, numParole, delim) == 0)
					 	System.out.println("Client: Conto le righe del file " + fileName + " con uno threshold di " + numParole + " e come delimitatori " + delim);
					 else
					 	System.out.println("Client: 
				}
			}
		} catch(RemoteException re){
			System.err.println("Client: Possibile errore nel file; " + re.getMessage());
			re.printStackTrace;
		} catch(NotBountException nbe){
			System.err.println("Client: Il nome fornito non risulta nel resgistry; " + nbe.getMessage());
			System.exit(2);
		} catch(Exception e){
			System.err.println("Client: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}
}