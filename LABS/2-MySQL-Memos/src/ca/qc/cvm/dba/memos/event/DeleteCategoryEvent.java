package ca.qc.cvm.dba.memos.event;

public class DeleteCategoryEvent extends CommonEvent {
	private int id;

	public DeleteCategoryEvent(int id) {
		super(CommonEvent.Type.DeleteCategory);
		
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
}
