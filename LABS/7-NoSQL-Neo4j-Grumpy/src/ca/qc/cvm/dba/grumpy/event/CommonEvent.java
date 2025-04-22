package ca.qc.cvm.dba.grumpy.event;

public class CommonEvent {
	public enum Type {UI, Exit};
	
	private Type type;
	
	public CommonEvent(Type type) {
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}
}
