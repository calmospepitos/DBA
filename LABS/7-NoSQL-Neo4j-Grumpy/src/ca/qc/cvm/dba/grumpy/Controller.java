package ca.qc.cvm.dba.grumpy;

import ca.qc.cvm.dba.grumpy.app.SentenceAnalyzer;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class Controller implements ScreenController {
	private TextField textField;
	private SentenceAnalyzer analyzer;

	private static MainFrame main;
	private static Element messageLabel;

	public void bind(Nifty nifty, Screen screen) {
		textField = screen.findNiftyControl("text", TextField.class);
		messageLabel = screen.findElementByName("message");
		analyzer = new SentenceAnalyzer();
		analyzer.addObserver(main);
	}
	
	public static void setMainFrame(MainFrame m) {
		main = m;
	}
	
	public static void setMessage(String msg) {
		messageLabel.getRenderer(TextRenderer.class).setText(msg);
	}

	@Override
	public void onEndScreen() {
	}

	@Override
	public void onStartScreen() {
		
	}
		
	public void doneClicked() {
		String sentence = textField.getText();
		textField.setText("");
		textField.setFocus();
		analyzer.digest(sentence.trim());
	}
}
