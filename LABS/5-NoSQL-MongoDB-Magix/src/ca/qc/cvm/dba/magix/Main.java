package ca.qc.cvm.dba.magix;

import ca.qc.cvm.dba.magix.view.FrameMain;

/**
 * Classe principale.
 */
public class Main {

	public static void main(String[] args) {
		try {
			FrameMain mainFrame = new FrameMain();
			mainFrame.setVisible(true);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
