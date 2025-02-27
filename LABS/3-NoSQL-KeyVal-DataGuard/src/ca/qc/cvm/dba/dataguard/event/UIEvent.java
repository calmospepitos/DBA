package ca.qc.cvm.dba.dataguard.event;

public class UIEvent extends CommonEvent {
	public enum UIType {ShowMessage, loginSuccessful, GoTo, Refresh, ForceExit}
	
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
