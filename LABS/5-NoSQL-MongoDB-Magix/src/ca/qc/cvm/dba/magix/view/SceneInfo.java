package ca.qc.cvm.dba.magix.view;

import java.util.List;

import javax.swing.JTextArea;
import javax.swing.JTextPane;

import ca.qc.cvm.dba.magix.app.Facade;
import ca.qc.cvm.dba.magix.event.GoToEvent;
import ca.qc.cvm.dba.magix.util.ActionHandler;

public class SceneInfo extends Scene {
	private static final long serialVersionUID = 1L;
	private JTextArea text;

	public SceneInfo() throws Exception {
		super(null);
	}
	
	public void init() {		
		super.addButtonSM("Retour", 60, 580, new ActionHandler() {
 		    public void onAction() {
 		    	Facade.getInstance().processEvent(new GoToEvent(GoToEvent.Destination.Splash));
 		    }
 		});

		text = super.addText("");
	}
	

	@Override
	public void resetUI() {
		long current = System.currentTimeMillis();
		String ranking = "";
		
		List<Object[]> rankings = Facade.getInstance().getCardRankings();
		
		for (int i = 0; i < rankings.size(); i++) {
			ranking += rankings.get(i)[0] + "(" + rankings.get(i)[1] + ") ";
		
			if (i == 5) {
				ranking += "\n";
			}
		}
		
		String txt = Facade.getInstance().getGameCount() + " parties ont été jouées au total.\n";
		txt += "Nombre moyen de tours par partie: " + Facade.getInstance().getAverageRounds() + "\n\n";
		txt += "Classement des cartes:\n" + ranking + "\n\n";
		txt += "Les derniers résultats:\n";
		
		for (String result : Facade.getInstance().getLatestResults(15)) {
			txt += "- " + result + "\n";
		}	
		
		double time = (System.currentTimeMillis() - current)/1000.0;
		
		txt += "\n\nPage générée en : " + time + " sec.";
		
		text.setText(txt);
	}
}
