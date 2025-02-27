package ca.qc.cvm.dba.dataguard.dao;

public class UserDAO {
	private static final String password = "AAAaaa111";

	/**
	 * Méthode vérifier si l'usager a spécifié le bon mot de passe
	 * @param password 
	 * @return vrai si le mot de passe correspond, sinon faux
	 */
	public static boolean checkPassword(char[] password) {
		boolean success = false;
		
		success = UserDAO.password.equals(String.valueOf(password));
		
		return success;
	}
}
