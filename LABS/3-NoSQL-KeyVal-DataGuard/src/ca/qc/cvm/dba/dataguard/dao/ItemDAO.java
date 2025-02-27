package ca.qc.cvm.dba.dataguard.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

import ca.qc.cvm.dba.dataguard.entity.Item;

public class ItemDAO {

	/**
	 * M�thode permettant d'ajouter un item/fichier dans la base de données
	 * 
	 * @param key (ou clé)
	 * @param fileData (ou valeur)
	 * @return vrai/faux, selon si c'est un succès ou échec
	 */
	public static boolean addItem(String key, byte[] fileData) {
		boolean success = false;
		Database connection = DBConnection.getConnection();

		try {
			DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
			DatabaseEntry theData = new DatabaseEntry(fileData);
			connection.put(null, theKey, theData);
			success = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return success;
	}

	/**
	 * Méthode utilisée pour supprimer un item/fichier
	 * 
	 * @param key
	 * @return vrai/faux, selon si c'est un succès ou échec
	 */
	public static boolean deleteItem(String key) {
		boolean success = false;
		Database connection = DBConnection.getConnection();

		try {
			DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
			connection.delete(null, theKey);
			success = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return success;
	}
	
	/**
	 * Permet d'avoir accès à la liste des items/fichiers de la base de données
	 * 
	 * @return la liste des Item de la base de données (leur clé, pas leur valeur)
	 */
	public static List<Item> getItemList() {
		List<Item> items = new ArrayList<Item>();
		Database connection = DBConnection.getConnection();
		Cursor myCursor = null;

		try {
			myCursor = connection.openCursor(null, null);

			DatabaseEntry foundKey = new DatabaseEntry();
			DatabaseEntry foundData = new DatabaseEntry();

			while (myCursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
				String keyString = new String(foundKey.getData(), "UTF-8");
				// String dataString = new String(foundData.getData(), "UTF-8");
				// System.out.println("Clé|Valeur: " + keyString + " | " + dataString + "");
				Item listItem = new Item(keyString);
				items.add(listItem);
			}
		}
		catch (DatabaseException de) {
			System.err.println("Erreur de lecture de la base de données: " + de);
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (myCursor != null) {
					myCursor.close();
				}
			}
			catch(DatabaseException dbe) {
				System.err.println("Erreur de fermeture du curseur: " + dbe.toString());
			}
		}

		return items;
	}
	
	/**
	 * Puisque le programme possède des fichiers, cette méthode permet de
	 * récupérer un élément de la base de données afin de recréer le fichier à l'endroit voulu
	 * 
	 * @param key 
	 * @param destinationFile endroit de destination
	 * @return vrai/faux, selon si c'est un succès ou échec
	 */
	public static boolean restoreItem(String key, File destinationFile) {
		boolean success = false;
		Database connection = DBConnection.getConnection();

		try {
			DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
			DatabaseEntry theData = new DatabaseEntry();

			if (connection.get(null, theKey, theData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
				byte[] retData = theData.getData();
				String foundData = new String(retData, "UTF-8");

				FileOutputStream fos = new FileOutputStream(destinationFile);
				fos.write(retData);
				fos.close();

				System.out.println("Clé: '" + key + "' donnée: '" + foundData + "'.");
				success = true;
			}
			else {
				System.out.println("Element inexistant");
			}
		}
		catch (Exception e) {
		}

		return success;
	}
}
