package ca.qc.cvm.dba.memos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ca.qc.cvm.dba.memos.entity.User;
import ca.qc.cvm.dba.memos.util.BCrypt;

public class UserDAO {

	/**
	 * En fonction d'un nom d'usager et mot de passe, vérifier si l'usager existe bel et bien dans la BD
	 * 
	 * @param username nom d'usager
	 * @param password mot de passe
	 * @return l'usager ou null si les informations sont erronées
	 */
	public static User login(String username, char[] password) {
		User user = null;
		Connection connection = DBConnection.getConnection();

		try {
			PreparedStatement statement;
			statement = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
			statement.setString(1, username);
			ResultSet result = statement.executeQuery();

			if (result.first()) {
				String pwd = result.getString("password");

				if (BCrypt.checkpw(String.valueOf(password), pwd)) {
					// Succès
					user = new User(result.getInt("id"), username);
				}
				else {
					// Mauvais mot de passe
					return null;
				}
			}

			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return user;
	}
	
	/**
	 * M�thode qui permet d'enregistrer un nouveau membre
	 * 
	 * @param username nom d'usager
	 * @param password son mot de passe
	 * @return
	 */
	public static boolean register(String username, char[] password) {
		boolean success = false;
		Connection connection = DBConnection.getConnection();

		PreparedStatement statement;
		String motDePasse = BCrypt.hashpw(String.valueOf(password), BCrypt.gensalt());
		// Doit retourner success à "true" lorsque l'enregistrement a fonctionné
		try {
			statement = connection.prepareStatement("INSERT INTO users(username, password) VALUES(?, ?)");
			statement.setString(1, username);
			statement.setString(2, motDePasse);
			statement.execute();
			statement.close();
			success = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return success;
	}
}
