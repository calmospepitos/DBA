package ca.qc.cvm.dba.magix.app;

import java.util.List;
import java.util.Observer;

import ca.qc.cvm.dba.magix.dao.GameDAO;
import ca.qc.cvm.dba.magix.entity.Card;
import ca.qc.cvm.dba.magix.event.CommonEvent;

public class Facade {
	private static Facade instance;
	
	private MngApplication app;
	
	private Facade() {
		app = new MngApplication();
	}
	
	public static Facade getInstance() {
		if (instance == null) {
			instance = new Facade();
		}
		
		return instance;
	}
	
	public void processEvent(CommonEvent event) {
		app.processEvent(event);
	}
	
	public void addObserverClass(Observer o) {
		app.addObserver(o);
	}
	
	public void testConnection() {
		app.testConnection();
	}
	
	public List<Card> getCardCollection() {
		return app.getCardCollection();
	}

	public int getMaximumAllowedCopies() {
		return app.getMaximumAllowedCopies();
	}
	
	public List<Card> getPlayerCardList() {
		return app.getPlayerCardList();
	}
	
	public List<Card> getAICardList() {
		return app.getAICardList();
	}
	
	public int getPlayerRemainingCoins() {
		return app.getPlayerRemainingCoins();
	}
	
	public int getPlayerLife() {
		return app.getPlayerLife();
	}
	
	public int getRound() {
		return app.getRound();
	}
	
	public int getAILife() {
		return app.getAILife();
	}
	
	public void updateGame() {
		app.updateGame();
	}
	
	public boolean isGameFinished() {
		return app.isGameFinished();
	}
	
	public long getGameCount() {
		return app.getGameCount();
	}
	
	public List<String> getLatestResults(int count) {
		return app.getLatestResults(count);
	}
	
	public List<Object[]> getCardRankings() {
		return app.getCardRankings();
	}
	
	public double getAverageRounds() {
		return app.getAverageRounds();
	}
}
