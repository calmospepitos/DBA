package ca.qc.cvm.dba.jumper.event;

import ca.qc.cvm.dba.jumper.entity.GameLog;
import ca.qc.cvm.dba.jumper.view.FrameMain.Views;

/**
 * Événement pour passer d'un panel à un autre
 */
public class GoToEvent extends CommonEvent {
	private Views destination;
	private GameLog currentPerson;
	
	public GoToEvent(Views destination) {
		super(CommonEvent.Type.GoTo);
		
		this.destination = destination;
	}
	
	public GoToEvent(Views destination, GameLog person) {
		super(CommonEvent.Type.GoTo);
		
		this.destination = destination;
		this.currentPerson = person;
	}
	
	public GameLog getPerson() {
		return currentPerson;
	}
	
	public Views getDestination() {
		return destination;
	}
}
