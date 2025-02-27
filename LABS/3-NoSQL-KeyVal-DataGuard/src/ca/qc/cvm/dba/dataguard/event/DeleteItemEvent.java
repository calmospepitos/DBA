package ca.qc.cvm.dba.dataguard.event;

public class DeleteItemEvent extends CommonEvent {
	private String name;

	public DeleteItemEvent(String name) {
		super(CommonEvent.Type.DeleteItem);
		
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
