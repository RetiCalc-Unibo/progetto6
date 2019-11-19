import java.io.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.StringTokenizer;

public class ServerImpl implements RemOp {


    @Override
    public int conta_righe(String fileName, int threshold, String delim) throws RemoteException {
        if (!fileName.endsWith(".txt")) {
            throw new RemoteException(fileName + " isn't a text file");
        }
        File file = new File(fileName);
        /*if (!file.isFile()) {
            throw new RemoteException(fileName + " doesn't exist");
        }*/

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            int occ = 0;
            while ((line = br.readLine()) != null) {
                StringTokenizer tk = new StringTokenizer(line, delim);
                if (tk.countTokens() > threshold) {
                    occ++;
                }
            }
            return occ;
        } catch (FileNotFoundException e) {
            throw new RemoteException(fileName + " doesn't exist");
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
        String serviceName = "Server";        //lookup name...

        // Registrazione del servizio RMI
        String completeName = "//" + registryHost + ":" + REGISTRYPORT + "/"
                + serviceName;
        try {
            ServerImpl serverRMI = new ServerImpl();
            Naming.rebind(completeName, serverRMI);
            System.out.println("Server RMI: Servizio \"" + serviceName
                    + "\" registrato");
        } catch (Exception e) {
            System.err.println("Server RMI \"" + serviceName + "\": "
                    + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}