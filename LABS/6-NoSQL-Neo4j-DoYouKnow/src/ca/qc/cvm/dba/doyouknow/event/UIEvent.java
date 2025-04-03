package ca.qc.cvm.dba.doyouknow.event;

public class UIEvent extends CommonEvent {
	public enum UIType {ShowMessage}
	
	private UIType uiType;
	private String text;
	private boolean special;
	
	public UIEvent(UIType type, String text, boolean special) {
		super(CommonEvent.Type.UI);
		
		uiType = type;
		
		this.text = text;
		this.special = special;
	}
	
	public UIType getUIType() {
		return uiType;
	}
	
	public String getText() {
		return text;
	}
	
	public boolean isSpecial() {
		return special;
	}
}
