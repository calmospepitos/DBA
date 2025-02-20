package ca.qc.cvm.dba.memos.event;

public class AddCategoryEvent extends CommonEvent {
	private String name;

	public AddCategoryEvent(String name) {
		super(CommonEvent.Type.AddCategory);
		
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
}
