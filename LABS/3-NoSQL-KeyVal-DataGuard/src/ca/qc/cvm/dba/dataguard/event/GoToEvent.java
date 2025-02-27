package ca.qc.cvm.dba.dataguard.event;

public class GoToEvent extends CommonEvent {
	public enum Destination {Main, Login}

	private Destination destination;
	
	public GoToEvent(Destination destination) {
		super(CommonEvent.Type.GoTo);
		
		this.destination = destination;
	}
	
	public Destination getDestination() {
		return destination;
	}
}
