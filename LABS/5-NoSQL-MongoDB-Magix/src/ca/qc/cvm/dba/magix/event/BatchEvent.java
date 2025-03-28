package ca.qc.cvm.dba.magix.event;

public class BatchEvent extends CommonEvent {
	private int number;
	
	public BatchEvent(int number) {
		super(CommonEvent.Type.Batch);
		this.number = number;
	}
	
	public int getNumber() {
		return number;
	}
}
