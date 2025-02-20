package ca.qc.cvm.dba.memos.event;

public class GeneralEvent extends CommonEvent {
	public enum GeneralType {Correct}
	private GeneralType type;
	
	private String text;
	private String text2;
	private String text3;

	public GeneralEvent(GeneralType type) {
		this(type, null, null, null);
	}
		
	public GeneralEvent(GeneralType type, String text, String text2, String text3) {
		super(CommonEvent.Type.General);
		
		this.text = text;
		this.text2 = text2;
		this.text3 = text3;
		this.type = type;
	}
	
	public GeneralType getGeneralType() {
		return type;
	}

	public String getText() {
		return text;
	}

	public String getText2() {
		return text2;
	}
	
	public String getText3() {
		return text3;
	}
}
