import java.io.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.StringTokenizer;

public class ServerImpl extends UnicastRemoteObject implements RemOp {

    public ServerImpl() throws RemoteException {
        super();
    }

    @Override
    public int conta_righe(String fileName, int threshold, String delim) throws RemoteException {
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
                    System.out.println("Ho letto il carattere " + read_char);
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

    @Override
    public int elimina_riga(String fileName, int rowNum) throws RemoteException {
        return 0;

    }

    // Avvio del Server RMI
    public static void main(String[] args) {
        final int REGISTRYPORT = 1099;
        String registryHost = "localhost";
        String serviceName = "Server";

        // Registrazione del servizio RMI
        String completeName = "//" + registryHost + ":" + REGISTRYPORT + "/" + serviceName;

        try {
            ServerImpl serverRMI = new ServerImpl();
            Naming.rebind(completeName, serverRMI);
            System.out.println("Server RMI: Servizio \"" + serviceName + "\" registrato");
        } catch (Exception e) {
            System.err.println("Server RMI \"" + serviceName + "\": " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}