package ca.qc.cvm.dba.dataguard;

import ca.qc.cvm.dba.dataguard.view.FrameMain;
import ca.qc.cvm.dba.dataguard.app.Facade;
 
public class Main {
 
    public static void main(String[] args) throws Exception {
		FrameMain mainFrame = new FrameMain();
		mainFrame.setVisible(true);

		Facade.getInstance().testConnection();
    }
}