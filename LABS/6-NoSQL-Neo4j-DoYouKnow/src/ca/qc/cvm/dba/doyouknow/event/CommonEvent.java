package ca.qc.cvm.dba.doyouknow.event;

public abstract class CommonEvent {
	public enum Type {UI, GENERAL_EVENT, BUTTON_EVENT};
	
	private Type type;
	
	public CommonEvent(Type type) {
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}
}
