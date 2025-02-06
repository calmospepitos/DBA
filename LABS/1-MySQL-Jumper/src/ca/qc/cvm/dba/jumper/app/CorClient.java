package ca.qc.cvm.dba.jumper.app;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.jdbc.DatabaseMetaData;

import ca.qc.cvm.dba.correctionserver.lib.BaseCorClient;
import ca.qc.cvm.dba.jumper.dao.DBConnection;
import ca.qc.cvm.dba.jumper.dao.GameLogDAO;
import ca.qc.cvm.dba.jumper.entity.GameLog;

public class CorClient extends BaseCorClient  {
	
	public CorClient() {
	}
	
	@Override
	protected void executeTests(final List<String> res, List<String> info) {
		GameLogDAO.save(new GameLog("F1", 11111111));
		GameLogDAO.save(new GameLog("F2", 22222222));
		GameLogDAO.save(new GameLog("F3", 33333333));
		GameLogDAO.save(new GameLog("F4", 44444444));
		
		List<GameLog> logs = GameLogDAO.getHighScores(5);
		
		for (GameLog l : logs) {
			res.add(l.getPlayerName() + "/" + l.getScore());
		}
		
		try {
			DatabaseMetaData md = (DatabaseMetaData) DBConnection.getConnection().getMetaData();
			ResultSet rs = (ResultSet) md.getTables(null, null, "%", null);
			List<String> t = new ArrayList<String>();
			
			while (rs.next()) {
				if (rs.getString(3).equals(rs.getString(3).toLowerCase())) {
					t.add(rs.getString(3));
				}
			}
			
			for (String tb : t) {
				rs = (ResultSet) md.getColumns(null, null, tb, null);
				res.add("========================");
				res.add(tb);
				res.add("------------------------");
				
				while (rs.next()) {
					res.add(rs.getString("COLUMN_NAME") + " " + rs.getString("TYPE_NAME") + " " + rs.getInt("COLUMN_SIZE"));
				}

				rs = (ResultSet) md.getIndexInfo(null, null, tb, false, false);
				while (rs.next()) {
					res.add(rs.getString("INDEX_NAME") + " " + rs.getString("COLUMN_NAME") + " ");
				}
				
				res.add(" ");
			}
			
			res.add(addContent("src/ca/qc/cvm/dba/jumper/dao/GameLogDAO.java", "boolean save", "return success;"));
			res.add(addContent("src/ca/qc/cvm/dba/jumper/dao/GameLogDAO.java", "getHighScores", null));
		}
		catch (Exception e) { e.printStackTrace(); }
	}
}
