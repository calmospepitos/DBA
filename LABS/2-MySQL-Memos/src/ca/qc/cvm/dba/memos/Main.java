package ca.qc.cvm.dba.memos;

import ca.qc.cvm.dba.memos.app.Facade;
import ca.qc.cvm.dba.memos.view.FrameMain;

public class Main {

	public static void main(String[] args) {
		try {
			FrameMain mainFrame = new FrameMain();
			mainFrame.setVisible(true);
			
			Facade.getInstance().testConnection();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
