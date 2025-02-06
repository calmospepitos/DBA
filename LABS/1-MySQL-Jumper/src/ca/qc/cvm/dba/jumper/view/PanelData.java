package ca.qc.cvm.dba.jumper.view;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import ca.qc.cvm.dba.jumper.app.Facade;
import ca.qc.cvm.dba.jumper.entity.GameLog;

public class PanelData extends CommonPanel {
	private static final long serialVersionUID = 1L;
	
	private JTextArea highScores;
	
	public PanelData(int width, int height) throws Exception {
		super(width, height, true, "assets/images/space.jpg");
		jbInit();
	}
	
	@Override
	protected void jbInit() throws Exception {
		JLabel logo = this.addLabel("", this.width/2 - 150, 10, 300, 200);
		logo.setIcon(new ImageIcon("assets/images/title-high-scores.png"));
		
		highScores = new JTextArea();
		highScores.setOpaque(false);
		highScores.setForeground(Color.WHITE);
		highScores.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		super.addField(highScores, this.width/2 - 200, 230, 400, 240);
	}
	
	/**
	 * Cette méthode est appelée automatiquement à chaque fois qu'un panel est affiché (lorsqu'on arrive sur la page)
	 */
	@Override
	public void resetUI() {
		StringBuilder scores = new StringBuilder();
		int position = 0;
		
		for (GameLog log : Facade.getInstance().getHighScores(10)) {
			position++;
			scores.append("" + position + " - " + log.getPlayerName() + " (" + log.getScore() + ")\n");
		}
		
		highScores.setText(scores.toString());
	}

}
