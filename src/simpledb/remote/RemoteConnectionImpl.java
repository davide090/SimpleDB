package simpledb.remote;

import simpledb.tx.Transaction;
import simpledb.stats.BasicFileStats;
import simpledb.server.SimpleDB;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

/**
 * The RMI server-side implementation of RemoteConnection.
 * @author Edward Sciore
 */
@SuppressWarnings("serial") 
class RemoteConnectionImpl extends UnicastRemoteObject implements RemoteConnection {
	private static Transaction tx;

	/**
	 * Creates a remote connection
	 * and begins a new transaction for it.
	 * @throws RemoteException
	 */
	RemoteConnectionImpl() throws RemoteException {
		tx = new Transaction();

		printAllBlockStats();
//		SimpleDB.fileMgr().resetMapStats();
	}

	/**
	 * Creates a new RemoteStatement for this connection.
	 * @see simpledb.remote.RemoteConnection#createStatement()
	 */
	public RemoteStatement createStatement() throws RemoteException {
		return new RemoteStatementImpl(this);
	}

	/**
	 * Closes the connection.
	 * The current transaction is committed.
	 * @see simpledb.remote.RemoteConnection#close()
	 */
	public void close() throws RemoteException {
		tx.commit();
	}

	// The following methods are used by the server-side classes.

	/**
	 * Returns the transaction currently associated with
	 * this connection.
	 * @return the transaction associated with this connection
	 */
	Transaction getTransaction() {  
		return tx;
	}

	/**
	 * Commits the current transaction,
	 * and begins a new one.
	 */
	void commit() {
		printAllBlockStats();
		tx.commit();
		tx = new Transaction();
	}

	/**
	 * Rolls back the current transaction,
	 * and begins a new one.
	 */
	void rollback() {
		tx.rollback();
		tx = new Transaction();
	}

	//DAVIDE
	private static void printBlockStats(String fileName, BasicFileStats fileStats){

		System.out.printf("\n%-18s%-12s%s\n","File Name","Read","Write");
		System.out.printf("%-18s%-12d%-12d\n",fileName, fileStats.getBlockRead(), fileStats.getBlockWritten());

	}

	//DAVIDE
	public void printAllBlockStats(){

		Map<String,BasicFileStats> mappa =  SimpleDB.fileMgr().getBlockStatsFile();

		for(String chiave : mappa.keySet()){

			printBlockStats(chiave, mappa.get(chiave));

		}

		System.out.println("\n\n\n");
	}
}
