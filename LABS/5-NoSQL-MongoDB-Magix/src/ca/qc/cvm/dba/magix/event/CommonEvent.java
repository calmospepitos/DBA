package ca.qc.cvm.dba.magix.event;

public abstract class CommonEvent {
	public enum Type {UI, GoTo, ChooseCard, Batch, Exit, Correction};
	
	private Type type;
	
	public CommonEvent(Type type) {
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}
}
