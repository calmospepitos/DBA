package ca.qc.cvm.dba.jumper.event;

import ca.qc.cvm.dba.jumper.entity.GameLog;

/**
 * Événement utilisé lorsque l'on veut sauvegarder une nouvelle
 * entrée 
 */
public class SaveEvent extends CommonEvent {
	private GameLog gameLog;
	
	public SaveEvent(GameLog gameLog) {
		super(CommonEvent.Type.Save);
		
		this.gameLog = gameLog;
	}
	
	public GameLog getGameLog() {
		return gameLog;
	}
}
