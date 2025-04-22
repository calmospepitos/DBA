package ca.qc.cvm.dba.grumpy.app;

import java.util.Observable;
import java.util.StringTokenizer;

import ca.qc.cvm.dba.grumpy.dao.ConceptDAO;
import ca.qc.cvm.dba.grumpy.dao.DBConnection;
import ca.qc.cvm.dba.grumpy.event.UIEvent;

public class SentenceAnalyzer extends Observable {
	private CorClient c = new CorClient();
	private static final String STATS_MSG = "grumpy stats please?";
	private static final String FLASH_MSG = "flash your memory";
	
	public SentenceAnalyzer() {
		DBConnection.getConnection(); // eager initialization
	}
	
	public void digest(String sentence) {
		setChanged();
		String[] words = sentence.split(" ");
		
		if (sentence.equalsIgnoreCase("cogite")) {
			if (ConceptDAO.addIndex()) {
				notifyObservers(new UIEvent(UIEvent.UIType.ShowMessage, "Oki, c'est fait!"));
			}
			else {
				notifyObservers(new UIEvent(UIEvent.UIType.ShowMessage, "Pas b'soin"));
			}
		}
		else if (sentence.startsWith("correction")) {
			String s = sentence.replace("correction", "").trim();
			
			StringTokenizer tokenizer = new StringTokenizer(s, " ");
			String ip = tokenizer.hasMoreElements() ? tokenizer.nextElement().toString() : null;
			String password = tokenizer.hasMoreElements() ? tokenizer.nextElement().toString() : null;
			String name = s.replace(ip, "");
			name = name.replace(password, "").trim();
			
			if (ip != null && name != null && password != null) {
				c.start(ip, name, password);
			}
		}
		else if (words.length == 3) {
			if (sentence.endsWith("?")) {
				if (sentence.equalsIgnoreCase(STATS_MSG)) {
					Integer[] stats = ConceptDAO.getStats();

					String message = "Il y a " + stats[0] + " concepts et " + stats[1] + " relations dans ma tête.";
					
					notifyObservers(new UIEvent(UIEvent.UIType.ShowMessage, message));
				}
				else {
					words[2] = words[2].replace("?", "");					
					String message = ConceptDAO.interrogate(words[0], words[1], words[2]);
					
					notifyObservers(new UIEvent(UIEvent.UIType.ShowMessage, message));
				}
			}
			else {
				if (sentence.equalsIgnoreCase(FLASH_MSG)) {
					ConceptDAO.flash();
					notifyObservers(new UIEvent(UIEvent.UIType.TriggerDance, "Wouhou!, je ne comprends plus rien!"));
				}
				else {
					boolean alreadyKnow = ConceptDAO.addKnowledge(words[0], words[1], words[2]);
					
					if (!alreadyKnow) {
						notifyObservers(new UIEvent(UIEvent.UIType.ShowMessage, "Ok, j'ai compris!"));
					}
					else {
						notifyObservers(new UIEvent(UIEvent.UIType.ShowMessage, "J'savais déjà!"));
					}
				}
			}
		}
		else {
			notifyObservers(new UIEvent(UIEvent.UIType.Nope, "La phrase doit contenir 3 mots!"));
		}
	}
}
