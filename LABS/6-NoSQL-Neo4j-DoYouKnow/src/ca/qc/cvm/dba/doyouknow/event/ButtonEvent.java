package ca.qc.cvm.dba.doyouknow.event;

public class ButtonEvent extends CommonEvent {
	private int no;
	private String text;
	private String text2;
	private String text3;
	
	public ButtonEvent(int no) {
		this(no, null);
	}
	
	public ButtonEvent(int no, String text) {
		this(no, text, null);
	}
	
	public ButtonEvent(int no, String text, String text2) {
		this(no, text, text2, null);
	}
	
	public ButtonEvent(int no, String text, String text2, String text3) {
		super(CommonEvent.Type.BUTTON_EVENT);
		
		this.text2 = text2;
		this.text3 = text3;
		this.text = text;
		this.no = no;		
	}
	
	public String getText() {
		return  text;
	}
	
	public int getButtonNo() {
		return no;
	}
	
	public String getText2() {
		return text2;
	}
	
	public String getText3() {
		return text3;
	}
}
