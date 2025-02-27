package ca.qc.cvm.dba.dataguard.dao;

import java.io.File;
import java.io.FileOutputStream;
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
		        
        return success;
	}
}
