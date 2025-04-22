package ca.qc.cvm.dba.grumpy.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;

import org.neo4j.driver.types.Relationship;

import com.objectif8.libraries.common.network.ClientAdapter;
import com.objectif8.libraries.common.network.NetworkAdapter;
import com.objectif8.libraries.common.network.NetworkEvent;
import com.objectif8.libraries.common.network.StringListMessage;
import com.objectif8.libraries.common.util.FileUtil;

import ca.qc.cvm.dba.correctionserver.lib.BaseCorClient;
import ca.qc.cvm.dba.grumpy.dao.ConceptDAO;

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
				String p2 = tokenizer.nextElement().toString();
				String p3 = tokenizer.nextElement().toString();
				String p4 = tokenizer.nextElement().toString();
				String p5 = tokenizer.nextElement().toString();

				try {
					ConceptDAO.flash();
				}
				catch (Exception e) {}
				boolean knew = false;
				Integer[] s = null;
				Integer[] s2 = null;
				Integer[] s3 = null;
				Integer[] s5 = null;
				boolean i = false;
				boolean i2 = false;
				String s4 = null;
				
				try {
					Thread.currentThread().sleep(50);
					s = ConceptDAO.getStats();
				} catch (Exception e) {}
				
				try {
					Thread.currentThread().sleep(50);
					ConceptDAO.addKnowledge(p1, p2, p3);
				} catch (Exception e) {}

				try {
					Thread.currentThread().sleep(50);
					knew = ConceptDAO.addKnowledge(p1, p2, p3);
				} catch (Exception e) {}
				
				try {
					Thread.currentThread().sleep(50);
					s2 = ConceptDAO.getStats();
				} catch (Exception e) {}
			
				try {
					Thread.currentThread().sleep(50);
					ConceptDAO.addKnowledge(p1, p2, p3);
				} catch (Exception e) {}
				
				try {
					Thread.currentThread().sleep(50);
					s3 = ConceptDAO.getStats();
				} catch (Exception e) {}
				
				try {
					Thread.currentThread().sleep(50);
					i = ConceptDAO.indexExists();
				} catch (Exception e) {}
				
				try {
					Thread.currentThread().sleep(50);
					ConceptDAO.addIndex();
				} catch (Exception e) {}
				
				try {
					Thread.currentThread().sleep(50);
					i2 = ConceptDAO.indexExists();
				} catch (Exception e) {}
				
				try {
					Thread.currentThread().sleep(50);
					ConceptDAO.addKnowledge(p3, p4, p5);
				} catch (Exception e) {}
				
				try {
					Thread.currentThread().sleep(50);
					s4 = ConceptDAO.interrogate(p1, p2, p5);
				} catch (Exception e) {}
				
				try {
					Thread.currentThread().sleep(50);
					ConceptDAO.flash();
				} catch (Exception e) {}
				
				try {
					Thread.currentThread().sleep(50);
					s5 = ConceptDAO.getStats();
				} catch (Exception e) {}
				
				res.add((s != null ? (s[0].intValue() + "," + s[1].intValue()) : "-") + "/" +
						knew + "/" +
						(s2 != null ? (s2[0].intValue() + "," + s2[1].intValue()) : "-") + "/" +
						(s3 != null ? (s3[0].intValue() + "," + s3[1].intValue()) : "-") + "/" +
						i + "/" + i2 + "/" +
						s4 + "/" + 
						(s5 != null ? (s5[0].intValue() + "," + s5[1].intValue()) : "-"));
			}
		}
		

		res.add(addContent("src/ca/qc/cvm/dba/grumpy/dao/ConceptDAO.java", "boolean indexExists()", "return exists;"));
		res.add(addContent("src/ca/qc/cvm/dba/grumpy/dao/ConceptDAO.java", "boolean addIndex()", "return added;"));
		res.add(addContent("src/ca/qc/cvm/dba/grumpy/dao/ConceptDAO.java", "boolean addKnowledge", "return alreadyKnow;"));
		res.add(addContent("src/ca/qc/cvm/dba/grumpy/dao/ConceptDAO.java", "Relationship getRelationship", "void flash("));
		res.add(addContent("src/ca/qc/cvm/dba/grumpy/dao/ConceptDAO.java", "void flash(", "static Integer[]"));
		res.add(addContent("src/ca/qc/cvm/dba/grumpy/dao/ConceptDAO.java", "Integer[] getStats()", "return new Integer"));
	}
}
