package ca.qc.cvm.dba.memos.event;

public class AddMemoEvent extends CommonEvent {
	private String text;
	private int categoryId;

	public AddMemoEvent(int categoryId, String text) {
		super(CommonEvent.Type.AddMemo);
		
		this.categoryId = categoryId;
		this.text = text;
	}
	
	public int getCategoryId() {
		return categoryId;
	}
	
	public String getText() {
		return text;
	}
}
