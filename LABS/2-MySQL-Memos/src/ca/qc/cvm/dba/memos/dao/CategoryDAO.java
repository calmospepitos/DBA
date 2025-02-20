package ca.qc.cvm.dba.memos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ca.qc.cvm.dba.memos.entity.Category;
import ca.qc.cvm.dba.memos.entity.User;
import ca.qc.cvm.dba.memos.util.BCrypt;

public class CategoryDAO {

	/**
	 * Méthode qui permet d'ajouter une catégorie.
	 * Note : Le nom de la catégorie doit être unique et non vide!
	 * 
	 * @param id L'id de l'usager
	 * @param name Le nom de la catégorie
	 * @return vrai/faux, selon si l'opération a fonctionnée ou pas
	 */
	public static boolean addCategory(int userId, String name) {
		boolean success = false;
		Connection connection = DBConnection.getConnection();

		PreparedStatement statement;
		try {
			statement = connection.prepareStatement("INSERT INTO category(id_user, name) VALUES(?, ?)");
			statement.setInt(1, userId);
			statement.setString(2, name);
			statement.execute();
			statement.close();
			success = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return success;
	}
	
	/**
	 * M�thode qui permet de supprimer une catégorie et les mémos associés
	 * 
	 * @param id de la catégorie
	 * @return vrai/faux, selon si l'opération a fonctionnée ou pas
	 */
	public static boolean deleteCategory(int id) {
		boolean success = false;
		Connection connection = DBConnection.getConnection();

		try {
			PreparedStatement statement = connection.prepareStatement("DELETE FROM category WHERE id = ?");
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
	 * Méthode qui retourne l'ensemble des catégories
	 * 
	 * @param id L'id de l'usager
	 * @return une liste de catégories (si aucune catégorie, retourner une liste vide)
	 */
	public static List<Category> getCategoryList(int userId) {
		List<Category> categories = new ArrayList<Category>();
		Connection connection = DBConnection.getConnection();

		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM category WHERE id_user = ?");
			statement.setInt(1, userId);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				categories.add(new Category(result.getInt("id"), result.getInt("id_user"), result.getString("name")));
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return categories;
	}
}
