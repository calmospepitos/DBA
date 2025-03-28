package ca.qc.cvm.dba.magix.app;

import java.util.ArrayList;
import java.util.List;

import ca.qc.cvm.dba.magix.dao.GameDAO;
import ca.qc.cvm.dba.magix.entity.Card;
import ca.qc.cvm.dba.magix.entity.Minion;
import ca.qc.cvm.dba.magix.entity.Spell;
import ca.qc.cvm.dba.magix.entity.Card.CardType;
import ca.qc.cvm.dba.magix.entity.Spell.Effect;

public class Game {
	private static final int MAX_ALLOWED_COPIES = 20;
	private static final int INITIAL_COINS = 20;
	private static final int INITIAL_LIFE = 30;

	private int interMoveDelay;
	private long lastUpdatedTime;
	private boolean finished;
	private int round;
	
	private List<Card> playerCards;
	private List<Card> aiCards;
	
	private int playerLife;
	private int aiLife;
	private List<Effect> playerEffects;
	private List<Effect> aiEffects;

	public Game() {
		playerCards = new ArrayList<Card>();
		aiCards = new ArrayList<Card>();
		playerLife = INITIAL_LIFE;
		aiLife = INITIAL_LIFE;
		finished = false;
		round = 0;
		playerEffects = new ArrayList<Effect>();
		aiEffects = new ArrayList<Effect>();
	}
	
	public List<Card> createDeck(List<Card> collection) {
		List<Card> cards = new ArrayList<Card>();
		
		for (int i = 0; i < 3; i++) {
			Card card = null;
			
			while (card == null || card.getType() != Card.CardType.Minion) {
				card = collection.get((int)(Math.random() * collection.size()));
				
				if (card.getCost() > getRemainingCoins(cards)) {
					card = null;
				}
			}
			
			cards.add(card);
		}
		
		while (getRemainingCoins(cards) > 0) {
			Card card = collection.get((int)(Math.random() * collection.size()));
			
			if (card.getCost() <= getRemainingCoins(cards)) {
				cards.add(card);
			}
		}
		
		return cards;
	}
	
	public boolean isFinished() {
		return finished;
	}
	
	public void shuffleCards() {
		playerCards = shuffleCards(playerCards);
		aiCards = shuffleCards(aiCards);
	}
	
	private List<Card> shuffleCards(List<Card> cards) {
		List<Card> shuffledCards = new ArrayList<Card>();
		
		while (cards.size() > 0) {
			Card card = cards.remove((int)(Math.random() * cards.size()));
			shuffledCards.add(card);
		}
		
		return shuffledCards;
	}
	
	public void addCardToAI(Card card) {
		aiCards.add(card);
	}
	
	public void addCardToPlayer(Card card) {
		playerCards.add(card);
	}
	
	public List<Card> getPlayerCardList() {
		return playerCards;
	}

	public List<Card> getAICardList() {
		return aiCards;
	}

	private int getRemainingCoins(List<Card> cards) {
		int coins = INITIAL_COINS;
		
		for (Card card : cards) {
			coins -= card.getCost();
		}
		
		return coins;
	}
	
	public int getPlayerRemainingCoins() {
		return getRemainingCoins(playerCards);
	}

	public int getPlayerLife() {
		return playerLife;
	}
	
	public int getAILife() {
		return aiLife;
	}
	
	public int getRound() {
		return round;
	}
	
	public int getMaximumAllowedCopies() {
		return MAX_ALLOWED_COPIES;
	}
	
	public void start(int interMoveDelay) {
		lastUpdatedTime = System.currentTimeMillis();
		this.interMoveDelay = interMoveDelay;
	}
	
	public boolean updateGame() {
		boolean updated = false;
		
		if (lastUpdatedTime + interMoveDelay < System.currentTimeMillis()) {
			lastUpdatedTime = System.currentTimeMillis();
			updated = true;
			
			playNext();
		}
		
		return updated;
	}
	
	private void playNext() {
		
		if ((round >= aiCards.size() && round >= playerCards.size()) ||
			aiLife <= 0 || playerLife <= 0) {
			finished = true;
			GameDAO.logGame(playerCards, aiCards, playerLife, aiLife, round);
		}
		else {
			if (aiCards.size() > round) {
				playCard(false, aiCards.get(round));
			}			
			
			if (playerCards.size() > round) {
				playCard(true, playerCards.get(round));
			}			
		}

		round++;
	}
	
	private int applyBuffs(int amount, List<Effect> effects) {
		double newAmount = amount;
		
		for (int i = 0; i < effects.size(); i++) {
			Effect effect = effects.get(i);
			
			if (effect == Effect.BuffMinion) {
				newAmount = newAmount * 3;
				effects.remove(i);
				i--;
			}
			else if (effect == Effect.BuffMinions) {
				newAmount = newAmount * 1.5;
			}
			else if (effect == Effect.LowerEnemyMinons) {
				newAmount = newAmount - 1;
			}
		}
		
		if (newAmount < 0) {
			newAmount = 0;
		}
		
		return (int)newAmount;
	}
	
	private void playCard(boolean isPlayer, Card card) {
		if (card.getType() == CardType.Minion) {
			int amount = ((Minion)card).getAttackPoints();
			
			amount = this.applyBuffs(amount, isPlayer ? playerEffects : aiEffects);
			
			if (isPlayer) {
				if (aiEffects.contains(Effect.Reflect)) {
					amount = amount/2;
					playerLife -= amount;
				}
				
				if (aiEffects.contains(Effect.BlockNext)) {
					aiEffects.remove(Effect.BlockNext);
					amount = 0;
				}
				
				aiLife -= amount;
			}
			else {
				if (playerEffects.contains(Effect.Reflect)) {
					playerEffects.remove(Effect.Reflect);
					amount = amount/2;
					aiLife -= amount;
				}
				
				if (playerEffects.contains(Effect.BlockNext)) {
					playerEffects.remove(Effect.BlockNext);
					amount = 0;
				}
				
				playerLife -= amount;
			}
		}
		else {
			Effect effect = ((Spell)card).getEffect();
			
			if (effect == Effect.Heal) {
				int amount = (int)(Math.random() * 4) + 1;
				
				if (isPlayer) {
					playerLife += amount;
					
					if (playerLife > INITIAL_LIFE) {
						playerLife = INITIAL_LIFE;
					}
				}
				else {
					aiLife += amount;
					
					if (aiLife > INITIAL_LIFE) {
						aiLife = INITIAL_LIFE;
					}
				}
			}
			else {
				if (isPlayer) {
					if (effect == Effect.BuffMinion || effect == Effect.BuffMinions || effect == Effect.BlockNext || effect == Effect.Reflect) {
						playerEffects.add(effect);
					}
					else if (effect == Effect.LowerEnemyMinons) {
						aiEffects.add(effect);
					}
				}
			}
		}
	}
}
