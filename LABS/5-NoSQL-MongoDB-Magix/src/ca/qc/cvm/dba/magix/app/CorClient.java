package ca.qc.cvm.dba.magix.app;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.bson.Document;

import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import ca.qc.cvm.dba.correctionserver.lib.BaseCorClient;
import ca.qc.cvm.dba.magix.dao.DBConnection;
import ca.qc.cvm.dba.magix.dao.GameDAO;
import ca.qc.cvm.dba.magix.entity.Card;

public class CorClient extends BaseCorClient  {
	private List<Card> cardCollection;
	
	public CorClient(List<Card> cardCollection) {
		this.cardCollection = cardCollection;
	}
	
	@Override
	protected void executeTests(final List<String> res, List<String> info) {
		for (String line : info) {
			if (line.startsWith("RUN;")) {
				line = line.replace("RUN;", "");
				
				StringTokenizer tokenizer = new StringTokenizer(line, "-");
				String p1 = tokenizer.nextElement().toString();
				long st = System.currentTimeMillis();
				
				long gameCount = 0;
				
				try {
					gameCount = GameDAO.getGameCount();
				} catch (Exception e) {}
				
				List<String> results = new ArrayList<String>();
				
				try {
					results = GameDAO.getLatestGamesResults(Integer.parseInt(p1));
				} catch (Exception e) {}
				
				List<Object[]> ranking = new ArrayList<Object[]>();
				try {
					ranking = GameDAO.getCardRankings(cardCollection);
				} catch (Exception e) {}
				
				double avg = 0;
				try {
					avg = GameDAO.getAverageRounds();
				} catch (Exception e) {}
				
				long en = System.currentTimeMillis();
				
				res.add(gameCount + "/" + avg);
				
				for (String s : results) {
					res.add(s);
				}
				
				for (Object[] obj : ranking) {
					res.add(obj[0] + "/" + obj[1]);
				}
				
				res.add((en - st) + "msec");
				
				try {
					MongoDatabase connection = DBConnection.getConnection();
					
					for (String s : connection.listCollectionNames()) {
						MongoCollection<Document> c = connection.getCollection(s);
						res.add("Collection : " + s);
						
						ListIndexesIterable<Document> i = c.listIndexes();
						MongoCursor<Document> cursor = i.cursor();
						
						while (cursor.hasNext()) {
							res.add(cursor.next().toJson());
						}
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		res.add(addContent("src/ca/qc/cvm/dba/magix/dao/GameDAO.java", "List<String> getLatestGamesResults", "return results;"));
		res.add(addContent("src/ca/qc/cvm/dba/magix/dao/GameDAO.java", "void logGame", "List<Object[]> getCardRankings"));
	}
}
