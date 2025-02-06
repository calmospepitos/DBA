package ca.qc.cvm.dba.jumper.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ca.qc.cvm.dba.jumper.entity.GameLog;

public class GameLogDAO {

	/**
	 * M�thode permettant la sauvegarde d'une partie.
	 * 
	 * @param gameLog - possède les informations de la partie
	 * @return true si la sauvegarde a fonctionné, false autrement
	 */
	public static boolean save(GameLog gameLog) {
		boolean success = false;
				
		return success;
	}
	
	/**
	 * Permet de retourner les informations des meilleurs parties jouées
	 * 
	 * @param limit - Nombre de parties (GameLog) à retourner
	 * @return les meilleurs parties, selon leur score
	 */
	public static List<GameLog> getHighScores(int limit) {
		List<GameLog> scoreList = new ArrayList<GameLog>();
				
		Connection connection = DBConnection.getConnection();

		return scoreList;
	}
}
