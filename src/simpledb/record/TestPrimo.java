package simpledb.record;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;

public class TestPrimo {



	public static String generateRandomString() {

		char[] chars = "abcdefghijklmnopqrstu".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 20; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		String output = sb.toString();


		return output;
	}



	public static int generateRandomInt() {


		int randomNum = ThreadLocalRandom.current().nextInt(0, 500000 + 1);

		return randomNum;
	}


	public static void deleteall(RecordFile rf) {

		rf.beforeFirst();
		while(rf.next())
			rf.delete();

		rf.beforeFirst();
		if(rf.next())
			while(rf.next())
				System.out.println(rf.getInt("matricola"));

		else System.out.println("deleted all");
	}


	public static void main(String[] args) throws FileNotFoundException {

		PrintWriter writer = new PrintWriter("/home/davide/studentdb/studenti.tbl");
		writer.print("");
		writer.close();
		SimpleDB.init("studentdb");
		Transaction tx = new Transaction();


		Schema sc = new Schema();

		sc.addIntField("matricola");
		sc.addStringField("nome", 20);
		sc.addStringField("cognome", 20);

		TableInfo ti = new TableInfo("studenti", sc);

		System.out.println("table info creata");


		RecordFile rf = new RecordFile(ti, tx);


		deleteall(rf);

		//inserimento record
		for(int i=0; i<10; i++) {

			rf.insert();

			rf.setInt("matricola", generateRandomInt());
			rf.setString("nome", generateRandomString() );
			rf.setString("cognome", generateRandomString() );

			System.out.println("-");

			
		}
		

		
		rf.beforeFirst();

		//stampa record
		while(rf.next()) {


			System.out.println("matricola: " + rf.getInt("matricola") + ", nome: " +
					rf.getString("nome") + ", cognome:  " +
					rf.getString("cognome") + ", RID =  " + rf.currentRid() + ",  stats: " + rf.getStatsRecord().get(rf.currentRid()).toString());


		}


		rf.beforeFirst();


		//eliminazione record con nomi il cui primo carattere è compreso fra a e i
		while(rf.next()) {

			char c = rf.getString("nome").charAt(0);

			if (c >= 'a' && c <= 'i') {
				System.out.println("\nelimino ---> " + rf.getString("nome"));
				rf.delete();
			}
		}


		rf.beforeFirst();

		int max = 0;

		// Valore massimo matricola
		while(rf.next()) {

			if(rf.getInt("matricola") > max) max = rf.getInt("matricola");

		}

		System.out.println("\nvalore massimo matricola = " + max);


		//inserimento altri record
		for(int i=0; i<7; i++) {

			rf.insert();

			rf.setInt("matricola", generateRandomInt());
			rf.setString("nome", generateRandomString() );
			rf.setString("cognome", generateRandomString() );

			System.out.println("-");

		}



		rf.beforeFirst();

		//stampa record
		while(rf.next()) {


			System.out.println("matricola: " + rf.getInt("matricola") + ", nome: " +
					rf.getString("nome") + ", cognome:  " +
					rf.getString("cognome") + ", RID =  " + rf.currentRid() + ", stats: " + rf.getStatsRecord().get(rf.currentRid()).toString());


		}


		tx.commit();
	}


}