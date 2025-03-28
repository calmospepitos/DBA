package ca.qc.cvm.dba.magix.view;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import ca.qc.cvm.dba.magix.app.Facade;
import ca.qc.cvm.dba.magix.event.GoToEvent;
import ca.qc.cvm.dba.magix.util.ActionHandler;

public class SceneAbout extends Scene {
	private static final long serialVersionUID = 1L;

	public SceneAbout() throws Exception {
		super(null);
	}
	
	public void init() {
		super.addButtonSM("Retour", 60, 580, new ActionHandler() {
 		    public void onAction() {
 		    	Facade.getInstance().processEvent(new GoToEvent(GoToEvent.Destination.Splash));
 		    }
 		});

		try {
			super.addText(new String(Files.readAllBytes(new File("assets/text/about.txt").toPath())));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}


	@Override
	public void resetUI() {
	}

}
