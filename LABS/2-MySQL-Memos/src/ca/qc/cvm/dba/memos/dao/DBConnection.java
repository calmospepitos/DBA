package ca.qc.cvm.dba.memos.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.mysql.cj.jdbc.MysqlXADataSource;

public class DBConnection {
	private static Connection connection;
	
	/**
	 * Méthode qui permet de retourner une connexion à la base de données MySQL
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		if (connection == null) {
			MysqlXADataSource dataSource = new MysqlXADataSource();
			dataSource.setUser("memos_user");
			dataSource.setPassword("AAAaaa111");
			dataSource.setServerName("localhost");
			dataSource.setDatabaseName("memos_db");
			
			try {
				dataSource.setUseSSL(false);
				connection = dataSource.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return connection;
	}
	
	/**
	 * Méthode permettant de tester la connexion
	 * 
	 * @return si la connexion est ouverte ou pas
	 */
	public static boolean connectionCredentialsValid() {
		Connection c = getConnection();
		boolean valid = c != null;
		releaseConnection();
		
		return valid;
	}
	
	public static void releaseConnection() {
		if (connection != null) {
			try {
				connection.close();
				connection = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
