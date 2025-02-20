package ca.qc.cvm.dba.memos.event;

public abstract class CommonEvent {
	public enum Type {General, UI, GoTo, Login, AddCategory, DeleteCategory, AddMemo, DeleteMemo, SearchMemos, Register};
	
	private Type type;
	
	public CommonEvent(Type type) {
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}
}
