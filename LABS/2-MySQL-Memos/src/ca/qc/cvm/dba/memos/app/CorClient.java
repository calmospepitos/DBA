package ca.qc.cvm.dba.memos.app;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;

import ca.qc.cvm.dba.correctionserver.lib.BaseCorClient;
import ca.qc.cvm.dba.memos.dao.CategoryDAO;
import ca.qc.cvm.dba.memos.dao.DBConnection;
import ca.qc.cvm.dba.memos.dao.MemoDAO;
import ca.qc.cvm.dba.memos.dao.UserDAO;
import ca.qc.cvm.dba.memos.entity.Category;
import ca.qc.cvm.dba.memos.entity.Memo;
import ca.qc.cvm.dba.memos.entity.User;

import com.mysql.cj.jdbc.DatabaseMetaData;
import com.objectif8.libraries.common.network.ClientAdapter;
import com.objectif8.libraries.common.network.NetworkAdapter;
import com.objectif8.libraries.common.network.NetworkEvent;
import com.objectif8.libraries.common.network.StringListMessage;
import com.objectif8.libraries.common.util.FileUtil;

public class CorClient extends BaseCorClient {
	
	public CorClient() {
	}	

	@Override
	protected void executeTests(List<String> res, List<String> info) {			
		for (String line : info) {
			if (line.startsWith("RUN;")) {
				line = line.replace("RUN;", "");
				
				StringTokenizer tokenizer = new StringTokenizer(line, "-");
				String p1 = tokenizer.nextElement().toString() + (int)(Math.random() * 10000);
				String p2 = tokenizer.nextElement().toString();
				String p3 = tokenizer.nextElement().toString() + (int)(Math.random() * 10000);
				String p4 = tokenizer.nextElement().toString();
				Boolean r1 = null;
				User u = null;
				Boolean r2 = null;
				List<Category> lc = null;
				Boolean r3 = null;
				List<Memo> m = null;
				
				try {
					r1 = UserDAO.register(p1, p2.toCharArray());
				}
				catch (Exception e) {}

				try {
					if (r1 != null && r1) {
						u = UserDAO.login(p1, p2.toCharArray());
					}
				}
				catch (Exception e) {}
				
				try {
					if (u != null) {
						r2 = CategoryDAO.addCategory(u.getId(), p3);
					}
				}
				catch (Exception e) {}

				try {
					if (u != null && r2 != null && r2) {
						lc = CategoryDAO.getCategoryList(u.getId());
					}
				}
				catch (Exception e) {}

				try {
					if (u != null && r2 != null && r2 && lc != null && lc.size() > 0) {
						r3 = MemoDAO.addMemo(lc.get(0).getId(), p4);
					}
				}
				catch (Exception e) {}

				try {
					if (u != null && r3 != null && r3) {
						m = MemoDAO.getMemoList(u.getId());
					}
				}
				catch (Exception e) {}
				
				res.add(r1 + "/" + (u != null ? u.toString() : "n") + "/" + r2 + "/" + lc + "/" + r3 + "/" + m);
				
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
					
					res.add(addContent("src/ca/qc/cvm/dba/memos/dao/UserDAO.java", "User login", "return user;"));
					res.add(addContent("src/ca/qc/cvm/dba/memos/dao/UserDAO.java", "boolean register", null));
					res.add(addContent("src/ca/qc/cvm/dba/memos/dao/CategoryDAO.java", "boolean addCategory", "return success;"));
					res.add(addContent("src/ca/qc/cvm/dba/memos/dao/CategoryDAO.java", "boolean deleteCategory", "return success;"));
					res.add(addContent("src/ca/qc/cvm/dba/memos/dao/CategoryDAO.java", "List<Category> getCategoryList", null));
					res.add(addContent("src/ca/qc/cvm/dba/memos/dao/MemoDAO.java", "List<Memo> getMemoList", "return memos;"));					
				}
				catch (Exception e) { e.printStackTrace(); }
			}
		}
	}
}
