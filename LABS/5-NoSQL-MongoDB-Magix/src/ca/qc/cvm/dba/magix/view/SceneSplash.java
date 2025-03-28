package ca.qc.cvm.dba.magix.view;

import javax.swing.JOptionPane;

import ca.qc.cvm.dba.correctionserver.lib.CorrectionDialog;
import ca.qc.cvm.dba.magix.app.Facade;
import ca.qc.cvm.dba.magix.event.BatchEvent;
import ca.qc.cvm.dba.magix.event.CorrectionEvent;
import ca.qc.cvm.dba.magix.event.ExitEvent;
import ca.qc.cvm.dba.magix.event.GoToEvent;
import ca.qc.cvm.dba.magix.util.ActionHandler;

public class SceneSplash extends Scene {
	private static final long serialVersionUID = 1L;

	public SceneSplash() throws Exception {
		super("background-landscape.png");
	}
	
	public void init() {
		super.addImage("title.png", 475 - 170, 200 - 60 , 336, 124);
		super.addButton("Démarrer", 475, 320, new ActionHandler() {
 		    public void onAction() {
 		    	Facade.getInstance().processEvent(new GoToEvent(GoToEvent.Destination.Choose));
 		    }
 		});
		
		super.addButton("À propos de", 475, 390, new ActionHandler() {
 		    public void onAction() {
 		    	Facade.getInstance().processEvent(new GoToEvent(GoToEvent.Destination.About));
 		    }
 		});
		
		super.addButton("Quitter", 475, 460, new ActionHandler() {
 		    public void onAction() {
 		    	Facade.getInstance().processEvent(new ExitEvent());
 		    }
 		});
		
		super.addButtonSM("Batch", 60, 580, new ActionHandler() {
 		    public void onAction() {
 		    	String number = JOptionPane.showInputDialog("Nombre de parties?", "10");
 		    	
 		    	if (number != null) {
	 		    	try {
	 		    		int num = Integer.parseInt(number);
	 		    		Facade.getInstance().processEvent(new BatchEvent(num));
	 		    	}
	 		    	catch (Exception e){}
 		    	}
 		    }
 		});
		
		super.addButtonSM("Info", 870, 580, new ActionHandler() {
 		    public void onAction() {
 		    	Facade.getInstance().processEvent(new GoToEvent(GoToEvent.Destination.Info));
 		    }
		});
		
		super.addButtonSM("Correct", 60, 30, new ActionHandler() {
 		    public void onAction() {
 		    	String[] data = CorrectionDialog.getData(null);
				
				if (data != null) {
					Facade.getInstance().processEvent(new CorrectionEvent(data[0], data[1], data[2]));
				}
 		    }
		});
	}

	@Override
	public void resetUI() {
	}
}
