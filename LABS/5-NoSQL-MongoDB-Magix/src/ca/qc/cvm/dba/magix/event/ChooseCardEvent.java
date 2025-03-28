package ca.qc.cvm.dba.magix.event;

import ca.qc.cvm.dba.magix.entity.Card;

public class ChooseCardEvent extends CommonEvent {
	private Card card;
	
	public ChooseCardEvent(Card card) {
		super(CommonEvent.Type.ChooseCard);
		this.card = card;
	}
	
	public Card getCard() {
		return card;
	}
}
