import java.io.*;
import java.time.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.StringTokenizer;

public class ServerImpl extends UnicastRemoteObject implements RemOp {

    public ServerImpl() throws RemoteException {
        super();
    }

    public int conta_righe(String fileName, int threshold, String delim) throws RemoteException {
        if (!fileName.endsWith(".txt")) {
            throw new RemoteException("Errore!" + fileName + " non e' un file di testo.");
        }

        File file = new File(fileName);
        //long startTime = 0, elapsed = 0;

        try {
            long start = System.nanoTime();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            int occ = 0;

            //startTime = System.nanoTime();

            while ((line = br.readLine()) != null) {
                StringTokenizer tk = new StringTokenizer(line, delim);
                if (tk.countTokens() > threshold) {
                    occ++;
                }
            }
            br.close();

            //long end = System.nanoTime();
            //long elapsed = end - start;
            //System.out.println("\nTEMPO TOK - " + threshold + " = " + elapsed + " ms\n");

            return occ;
        } catch (FileNotFoundException e) {
            throw new RemoteException("Errore! Il file " + fileName + " non esiste.");
        } catch (IOException e) {
            throw new RemoteException("Errore inaspettato di I/O!");
        }
    }

    public int conta_righe_char(String fileName, int threshold, String delim) throws RemoteException {
        int nread;
        char read_char;
        int occ;
        int readDelim;

        if (!fileName.endsWith(".txt")) {
            throw new RemoteException(fileName + " is not a text file");
        }

        File file = new File(fileName);

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            occ = 0;
            readDelim = 0;
            boolean newLine = true;

            while ((nread = br.read()) >= 0) {
                    read_char = (char) nread;
                    //System.out.println("Ho letto il carattere " + read_char);
                    for (int i = 0; i < delim.length(); i++) {
                        if (read_char == delim.charAt(i)) {
                            readDelim++;
                        }
                    }

                    if (readDelim > threshold) {
                        occ++;
                    }
                    if(read_char == '\n'){
                        readDelim = 0;
                    }
               
            }

            return occ;
        } catch (FileNotFoundException e) {
            throw new RemoteException(fileName + " does not exist");
        } catch (IOException e) {
            throw new RemoteException("Unexpected read error!");
        }
    }

    public int elimina_riga(String fileName, int rowNum) throws RemoteException {
        if (!fileName.endsWith(".txt")) {
            throw new RemoteException("Errore!" + fileName + " non e' un file di testo.");
        }

        File file = new File(fileName);

        try {
            String line;

            int numRowNew = 0;
            //Si parte da 1 nell'indicizzazione delle righe
            int index = 1;

            boolean checkRow = false;

            BufferedReader br = new BufferedReader(new FileReader(file));
            File fileR = new File("alias");
            PrintWriter pw = new PrintWriter(fileR);

            while ((line = br.readLine()) != null) {
                if (index != rowNum) {
                    pw.println(line);
                    numRowNew++;
                } else {
                    checkRow = true;
                }
                index++;
            }

            pw.close();
            br.close();

            if (!checkRow) {
                throw new RemoteException("La riga " + rowNum + " non esiste!");
            }

            file.delete();
            fileR.renameTo(file);

            return numRowNew;
        } catch (FileNotFoundException e) {
            throw new RemoteException("Errore! Il file " + fileName + " non esiste.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RemoteException("Errore inaspettato di I/O!");
        }
    }

    // Avvio del Server RMI
    public static void main(String[] args) {
        final int REGISTRYPORT = 1099;
        String registryHost;
        String serviceName;

        if (args.length != 2) {
            System.out.println("Server: Errore di sintassi, utilizzo: java ServerImpl serviceHost serviceName");
            System.exit(1);
        }

        registryHost = args[0];
        serviceName = args[1];

        // Registrazione del servizio RMI
        String completeName = "//" + registryHost + ":" + REGISTRYPORT + "/" + serviceName;

        try {
            ServerImpl serverRMI = new ServerImpl();
            Naming.rebind(completeName, serverRMI);
            System.out.println("Server: Servizio \"" + serviceName + "\" registrato");
        } catch (Exception e) {
            System.err.println("Server: \"" + serviceName + "\": " + e.getMessage());
            e.printStackTrace();
            System.exit(2);
        }
    }
}