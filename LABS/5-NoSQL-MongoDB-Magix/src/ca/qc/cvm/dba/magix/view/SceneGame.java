package ca.qc.cvm.dba.magix.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ca.qc.cvm.dba.magix.app.Facade;
import ca.qc.cvm.dba.magix.entity.Card;
import ca.qc.cvm.dba.magix.event.GoToEvent;
import ca.qc.cvm.dba.magix.util.ActionHandler;

public class SceneGame extends Scene {
	private static final long serialVersionUID = 1L;
	private JLabel playerLife;
	private JLabel aiLife;
	private JLabel resultText;
	private JButton startOverButton;
	private JPanel cardContainer; 

	private List<Card> playerCardList;
	private List<Card> aiCardList;
	private Thread start;
	private boolean running = false;
	
	public SceneGame() throws Exception {
		super("background-forest.jpg");
	}
	
	public void init()  {		
		resultText = super.addLabel("", 280, 240, 400, 40);
		resultText.setHorizontalAlignment(JLabel.CENTER);
		resultText.setFont(new Font("Verdana", java.awt.Font.BOLD, 30));
		
		startOverButton = super.addButton("Rejouer", 475, 330, new ActionHandler() {
 		    public void onAction() {
				Facade.getInstance().processEvent(new GoToEvent(GoToEvent.Destination.Choose));
 		    }
 		});
		startOverButton.setDisabledIcon(new ImageIcon("assets/images/button-over.png"));
		startOverButton.setVisible(false);
		
		playerLife = super.addLabel("", 10, 510, 100, 100);
		playerLife.setIcon(new ImageIcon("assets/images/heart.png"));
		playerLife.setFont(new Font("Verdana", Font.BOLD, 18));
		playerLife.setHorizontalTextPosition(JLabel.CENTER);
		playerLife.setVerticalTextPosition(JLabel.CENTER);

		aiLife = super.addLabel("", 10, 10, 100, 100);
		aiLife.setIcon(new ImageIcon("assets/images/heart.png"));
		aiLife.setFont(new Font("Verdana", Font.BOLD, 18));
		aiLife.setHorizontalTextPosition(JLabel.CENTER);
		aiLife.setVerticalTextPosition(JLabel.CENTER);
		
		super.addLabel("VOUS", 40, 490, 100, 20);
		super.addLabel("ADVERSAIRE", 20, 110, 100, 20);

		cardContainer = new JPanel() {			
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

				drawCards(g, aiCardList, 10);
				drawCards(g, playerCardList, 380);

				if (Facade.getInstance().isGameFinished()) {					
					resultText.setVisible(true);
				}
			}
		};
		cardContainer.setBounds(0, 0, 930, 620);
		cardContainer.setOpaque(false);
		this.add(cardContainer);		

	}
	
	
	private void drawCards(Graphics g, List<Card> cards, int y) {
		int round = Facade.getInstance().getRound();
		int x = 150;
		
		for (int i = (cards.size() > 8 && round > 8 ? round - 8 : 0 ); i < round && i < cards.size(); i++) {
			cards.get(i).render(g, x, y, true);
			x += 90;
		}
	}
	
	@Override
	public void resetUI() {
		if (!running && this.isVisible()) {
			start = new Thread(new Updater());
			start.start();
		}
		
		int playerLifeTmp = Facade.getInstance().getPlayerLife();
		int aiLifeTmp = Facade.getInstance().getAILife();
		
		playerLife.setText(playerLifeTmp + "");
		aiLife.setText(aiLifeTmp + "");
		
		playerCardList = Facade.getInstance().getPlayerCardList();
		aiCardList = Facade.getInstance().getAICardList();
		
		startOverButton.setVisible(Facade.getInstance().isGameFinished());
		resultText.setVisible(Facade.getInstance().isGameFinished());
		
		if (Facade.getInstance().isGameFinished()) {
			if (aiLifeTmp < playerLifeTmp) {
				resultText.setForeground(Color.magenta);
				resultText.setText("Vous avez gagné!");
			}
			else if (aiLifeTmp > playerLifeTmp) {
				resultText.setForeground(Color.red);
				resultText.setText("Vous avez perdu...");
			}
			else {
				resultText.setForeground(Color.white);
				resultText.setText("La partie est nulle");
			}
		}
		
		cardContainer.repaint();
	}
	
	class Updater implements Runnable {
		public void run() {
			while (!Facade.getInstance().isGameFinished()) {
				running = true;
				Facade.getInstance().updateGame();
				
				try {
					Thread.currentThread().sleep(1000);
				}
				catch (Exception e) {}
				
				start = null;
			}
			
			running = false;
		}
	}
}
