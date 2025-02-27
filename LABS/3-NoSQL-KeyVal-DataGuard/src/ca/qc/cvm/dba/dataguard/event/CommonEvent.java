package ca.qc.cvm.dba.dataguard.event;

public abstract class CommonEvent {
	public enum Type {UI, GoTo, Login, AddItem, DeleteItem, RestoreItem, General};
	
	private Type type;
	
	public CommonEvent(Type type) {
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}
}
