package ca.qc.cvm.dba.doyouknow;

import ca.qc.cvm.dba.doyouknow.app.Facade;
import ca.qc.cvm.dba.doyouknow.dao.UserDAO;
import ca.qc.cvm.dba.doyouknow.event.GeneralEvent;
import ca.qc.cvm.dba.doyouknow.event.GeneralEvent.General;
import ca.qc.cvm.dba.doyouknow.view.FrameMain;

public class Main {

	public static void main(String[] args) {
		
		try {
			FrameMain frame = new FrameMain();
			frame.setVisible(true);
			Facade.getInstance().processEvent(new GeneralEvent(General.LOAD_DATABASE));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
				
	}
}
