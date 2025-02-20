package ca.qc.cvm.dba.memos.event;

public class GoToEvent extends CommonEvent {
	public enum Destination {Menu, Categories, Memos, Register, Login}

	private Destination destination;
	
	public GoToEvent(Destination destination) {
		super(CommonEvent.Type.GoTo);
		
		this.destination = destination;
	}
	
	public Destination getDestination() {
		return destination;
	}
}
