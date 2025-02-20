package ca.qc.cvm.dba.memos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ca.qc.cvm.dba.memos.entity.Category;
import ca.qc.cvm.dba.memos.entity.Memo;

public class MemoDAO {
	/**
	 * Méthode qui permet d'ajouter un mémo
	 * 
	 * @param categoryId l'ID de la catégorie
	 * @param memo Le texte du mémo
	 * @return vrai/faux, selon si l'opération a fonctionnée ou pas
	 */
	public static boolean addMemo(int categoryId, String memo) {
		boolean success = false;
		Connection connection = DBConnection.getConnection();

		PreparedStatement statement;
		try {
			statement = connection.prepareStatement("INSERT INTO memos(id_category, memo, created) VALUES (?, ?, NOW())");
			statement.setInt(1, categoryId);
			statement.setString(2, memo);
			statement.execute();
			statement.close();
			success = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return success;
	}
	
	/**
	 * Méthode qui permet de supprimer un mémo
	 * 
	 * @param id du mémo
	 * @return vrai/faux, selon si l'opération a fonctionnée ou pas
	 */
	public static boolean deleteMemo(int id) {
		boolean success = false;
		Connection connection = DBConnection.getConnection();

		try {
			PreparedStatement statement = connection.prepareStatement("DELETE FROM memos WHERE id = ?");
			statement.setInt(1, id);
			statement.execute();
			statement.close();
			success = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return success;
	}
	
	/**
	 * Méthode qui retourne l'ensemble des mémos
	 * 
	 * @param id L'id de l'usager
	 * @return une liste de mémos (si aucun mémo, retourner une liste vide)
	 */
	public static List<Memo> getMemoList(int userId) {
		List<Memo> memos = new ArrayList<Memo>();
		Connection connection = DBConnection.getConnection();

		try {
			PreparedStatement statement = connection.prepareStatement("SELECT m.*, c.id_user, c.name FROM memos m, category c WHERE m.id_category = c.id AND c.id_user = ? ORDER BY m.created DESC");
			statement.setInt(1, userId);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				memos.add(new Memo(result.getInt("id"), result.getString("name"), result.getString("memo"), result.getDate("created")));
			}

			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return memos;
	}

	/**
	 * Méthode pour faire une recherche FULL TEXT
	 * 
	 * ELLE NE FAIT PAS PARTIE DE CE QUI DOIT ÊTRE FAIT DANS LE PROJET!
	 * 
	 * @param id L'id de l'usager
	 * @param categoryId l'ID de la catégorie (peut être nulle!!!)
	 * @param text texte à considérer pour la recherche
	 * @return une liste de mémos (si aucun résultat, retourner une liste vide)
	 */
	public static List<Memo> searchMemos(int userId, Integer categoryId, String text) {
		List<Memo> memos = new ArrayList<Memo>();
		Connection connection = DBConnection.getConnection();
				
		return memos;
	}
}
