package ca.qc.cvm.dba.memos.event;

public class SearchMemosEvent extends CommonEvent {
	private String text;
	private Integer categoryId;

	public SearchMemosEvent(Integer categoryId, String text) {
		super(CommonEvent.Type.SearchMemos);
		
		this.categoryId = categoryId;
		this.text = text;
	}
	
	public Integer getCategoryId() {
		return categoryId;
	}
	
	public String getText() {
		return text;
	}
}
