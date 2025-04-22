package ca.qc.cvm.dba.grumpy.event;

public class UIEvent extends CommonEvent {
	public enum UIType {ShowMessage, TriggerDance, Nope}
	
	private UIType uiType;
	private Object data;
	
	public UIEvent(UIType type, Object data) {
		super(CommonEvent.Type.UI);
		
		uiType = type;
		this.data = data;
	}
	
	public UIType getUIType() {
		return uiType;
	}
	
	public Object getData() {
		return data;
	}
}
