package ca.qc.cvm.dba.dataguard.app;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;

import ca.qc.cvm.dba.correctionserver.lib.BaseCorClient;
import ca.qc.cvm.dba.dataguard.dao.ItemDAO;
import ca.qc.cvm.dba.dataguard.entity.Item;

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
				String p1 = tokenizer.nextElement().toString();
				String p2 = tokenizer.hasMoreElements() ? tokenizer.nextElement().toString() : null; 
				String p3 = tokenizer.hasMoreElements() ? tokenizer.nextElement().toString() : null;
				String p4 = tokenizer.hasMoreElements() ? tokenizer.nextElement().toString() : null;
				
				if (p1.equals("da")) {
					int size = -1;
					try {
						for (Item i : ItemDAO.getItemList()) {
							ItemDAO.deleteItem(i.getName());
						}
						
						size = ItemDAO.getItemList().size();
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					
					res.add(size + "");
				}
				else if (p1.equals("a")) {
					int size = -1;
					
					try {
						byte[] data = Files.readAllBytes(new File(p2).toPath());
						ItemDAO.addItem(p2, data);
						size = ItemDAO.getItemList().size();
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					
					res.add(size + "");
				}
				else if (p1.equals("r")) {
					try {
						ItemDAO.restoreItem(p2, new File(p3));
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					
					res.add(new File(p3).exists() + "");
					new File(p3).delete();
				}
				else if (p1.equals("d")) {
					int size = -1;
					
					try {
						ItemDAO.deleteItem(p2);
						size = ItemDAO.getItemList().size();
					}
					catch (Exception e) {
						e.printStackTrace();
					}

					res.add(size + "");
				}
			}
		}
	}
}
