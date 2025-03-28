package ca.qc.cvm.dba.magix.event;

public class GoToEvent extends CommonEvent {
	public enum Destination {Splash, About, Choose, Game, Info}

	private Destination destination;
	
	public GoToEvent(Destination destination) {
		super(CommonEvent.Type.GoTo);
		
		this.destination = destination;
	}
	
	public Destination getDestination() {
		return destination;
	}
}
