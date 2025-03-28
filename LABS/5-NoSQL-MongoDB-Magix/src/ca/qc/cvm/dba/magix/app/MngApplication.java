package ca.qc.cvm.dba.magix.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import ca.qc.cvm.dba.magix.dao.DBConnection;
import ca.qc.cvm.dba.magix.dao.GameDAO;
import ca.qc.cvm.dba.magix.entity.Card;
import ca.qc.cvm.dba.magix.entity.Dragon;
import ca.qc.cvm.dba.magix.entity.Focus;
import ca.qc.cvm.dba.magix.entity.Giant;
import ca.qc.cvm.dba.magix.entity.Heal;
import ca.qc.cvm.dba.magix.entity.Hunter;
import ca.qc.cvm.dba.magix.entity.Knight;
import ca.qc.cvm.dba.magix.entity.Motivate;
import ca.qc.cvm.dba.magix.entity.Protect;
import ca.qc.cvm.dba.magix.entity.Reflect;
import ca.qc.cvm.dba.magix.entity.Swift;
import ca.qc.cvm.dba.magix.entity.WereWolf;
import ca.qc.cvm.dba.magix.entity.Wolf;
import ca.qc.cvm.dba.magix.event.BatchEvent;
import ca.qc.cvm.dba.magix.event.ChooseCardEvent;
import ca.qc.cvm.dba.magix.event.CommonEvent;
import ca.qc.cvm.dba.magix.event.CorrectionEvent;
import ca.qc.cvm.dba.magix.event.GoToEvent;
import ca.qc.cvm.dba.magix.event.GoToEvent.Destination;
import ca.qc.cvm.dba.magix.event.UIEvent;

public class MngApplication extends Observable {
	private Game game;
	private boolean runningBatchGames = false;
	private List<Card> collection;
        
    public MngApplication() {
    	
    }
    
	public void processEvent(CommonEvent event) {
		if (event.getType() == CommonEvent.Type.GoTo) {
			if (((GoToEvent)event).getDestination() == Destination.Choose) {
				if (!runningBatchGames) {
					game = new Game();
				}
			}
			else if (((GoToEvent)event).getDestination() == Destination.Game) {
				List<Card> cards = game.createDeck(this.getCardCollection());
				
				for (Card card : cards) {
					game.addCardToAI(card);
				}
				
				game.shuffleCards();
				game.start(1000 * 2);
			}
			
			setChanged();
			notifyObservers(new UIEvent(UIEvent.UIType.GoTo, ((GoToEvent)event).getDestination().toString()));
		}
		else if (event.getType() == CommonEvent.Type.Correction) {
			CorrectionEvent evt = (CorrectionEvent)event;
			CorClient c = new CorClient(this.getCardCollection());
			c.start(evt.getIp(), evt.getName(), evt.getPwd());
		}
		else if (event.getType() == CommonEvent.Type.ChooseCard) {
			game.addCardToPlayer(((ChooseCardEvent)event).getCard());

			setChanged();
			notifyObservers(new UIEvent(UIEvent.UIType.Refresh, null));
		}
		else if (event.getType() == CommonEvent.Type.Batch) {
			runBatchGames(((BatchEvent)event).getNumber());
		}
		else if (event.getType() == CommonEvent.Type.Exit) {
			quitGame();
		}
	}
	
	public void testConnection() {
		if (!DBConnection.connectionCredentialsValid()) {
			setChanged();
			notifyObservers(new UIEvent(UIEvent.UIType.ShowMessage, "Connexion impossible à la BD. Est-ce que le serveur est parti?"));
			quitGame();
		}
	}
	
	private void quitGame() {
		DBConnection.releaseConnection();
		System.exit(0);
	}
	
	private void runBatchGames(int gameCount) {
		runningBatchGames = true;
		
		for (int i  = 0; i < gameCount; i++) {
			Game tmpGame = new Game();
			
			for (Card card : tmpGame.createDeck(this.getCardCollection())) {
				tmpGame.addCardToAI(card);
			}
			
			for (Card card : tmpGame.createDeck(this.getCardCollection())) {
				tmpGame.addCardToPlayer(card);
			}
			
			tmpGame.shuffleCards();
			tmpGame.start(-1);
			
			while (!tmpGame.isFinished()) {
				tmpGame.updateGame();
			}
		}
		
		runningBatchGames = false;
		
		setChanged();
		notifyObservers(new UIEvent(UIEvent.UIType.ShowMessage, "Parties complétées"));
	}
	
	public void updateGame() {
		boolean updated = game.updateGame();
		
		if (updated) {
			setChanged();
			notifyObservers(new UIEvent(UIEvent.UIType.Refresh, null));
		}
	}
	
	public boolean isGameFinished() {
		return game.isFinished();
	}
	
	public List<Card> getPlayerCardList() {
		return game.getPlayerCardList();
	}

	public List<Card> getAICardList() {
		return game.getAICardList();
	}
	
	public int getPlayerRemainingCoins() {
		return game.getPlayerRemainingCoins();
	}
	
	public int getPlayerLife() {
		return game.getPlayerLife();
	}
	
	public int getAILife() {
		return game.getAILife();
	}
	
	public int getRound() {
		return game.getRound();
	}
	
	public int getMaximumAllowedCopies() {
		return game.getMaximumAllowedCopies();
	}
	
	public List<Card> getCardCollection() {		
		if (collection == null) {
	    	collection = new ArrayList<Card>();
			
	    	collection.add(new Wolf());
	    	collection.add(new Hunter());
	    	collection.add(new WereWolf());
	    	collection.add(new Knight());
	    	collection.add(new Giant());
	    	collection.add(new Dragon());
			
	    	collection.add(new Swift());
	    	collection.add(new Protect());
	    	collection.add(new Reflect());
	    	collection.add(new Heal());
			collection.add(new Motivate());
			collection.add(new Focus());
		}
		return collection;
	}
	
	public long getGameCount() {
		return GameDAO.getGameCount();
	}
	
	public List<String> getLatestResults(int count) {
		return GameDAO.getLatestGamesResults(count);
	}
	
	public List<Object[]> getCardRankings() {
		return GameDAO.getCardRankings(getCardCollection());
	}
	
	public double getAverageRounds() {
		return GameDAO.getAverageRounds();
	}
}
