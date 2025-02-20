package ca.qc.cvm.dba.memos.event;

public class DeleteMemoEvent extends CommonEvent {
	private int id;

	public DeleteMemoEvent(int id) {
		super(CommonEvent.Type.DeleteMemo);
		
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
}
