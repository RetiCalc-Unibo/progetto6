// Interfaccia remota di servizio

import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemOp extends Remote {
    public int conta_righe(String fileName, int threshold, String delim) throws RemoteException, FileNotFoundException;
    public int elimina_riga(String fileName, int rowNum) throws RemoteException;
}