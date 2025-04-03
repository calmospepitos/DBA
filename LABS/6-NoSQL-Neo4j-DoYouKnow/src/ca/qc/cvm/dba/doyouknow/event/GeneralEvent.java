package ca.qc.cvm.dba.doyouknow.event;

public class GeneralEvent extends CommonEvent {
	public enum General {LOAD_DATABASE}
	private General type;

	public GeneralEvent(General type) {
		super(CommonEvent.Type.GENERAL_EVENT);
		
		this.type = type;
	}
	
	public General getGeneralType() {
		return type;
	}
}
