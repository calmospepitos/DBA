package ca.qc.cvm.dba.magix.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ca.qc.cvm.dba.magix.app.Facade;
import ca.qc.cvm.dba.magix.entity.Card;
import ca.qc.cvm.dba.magix.event.ChooseCardEvent;
import ca.qc.cvm.dba.magix.event.GoToEvent;
import ca.qc.cvm.dba.magix.util.ActionHandler;

public class SceneChoose extends Scene {
	private static final long serialVersionUID = 1L;
	private List<Card> cardList;
	private int maxAllowedCopies;
	private JButton playButton;
	private JLabel coin;
	private JLabel textNum;
	private JLabel cardNames;
	private JPanel cardContainer; 

	public SceneChoose() throws Exception {
		super("background-fort.png");
	}
	
	public void init() {
		playButton = super.addButton("Jouer!", 800, 570, new ActionHandler() {
 		    public void onAction() {
 		    	Facade.getInstance().processEvent(new GoToEvent(GoToEvent.Destination.Game));
 		    }
 		});
		playButton.setDisabledIcon(new ImageIcon("assets/images/button-over.png"));

		super.addLabel("jetons restants", 90, 545, 200, 20);
		coin = super.addLabel("20", 45, 540, 40, 32);
		coin.setForeground(Color.orange);
		coin.setHorizontalAlignment(JLabel.RIGHT);
		
		textNum = super.addLabel("", 45, 580, 40, 20);
		textNum.setForeground(Color.orange);
		textNum.setHorizontalAlignment(JLabel.RIGHT);
		
		
		super.addLabel("carte(s) dans votre deck", 90, 580, 200, 20);
		cardNames = super.addLabel("", 45, 500, 850, 20);
		
		cardContainer = new JPanel() {			
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				int x = 5;
				int y = 10;
				int remainingCoins = Facade.getInstance().getPlayerRemainingCoins();
				
				for (Card card : cardList) {
					card.render(g, x, y, remainingCoins >= card.getCost() && countCopies(card) < maxAllowedCopies);
					x += 155;
					
					if (x > 800) {
						x = 10;
						y += 235;
					}
				}
			}
		};
		
		cardContainer.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int x = 5;
				int y = 10;
				
				for (Card card : cardList) {
					if (e.getX() > x && e.getX() < x + 150 &&
						e.getY() > y && e.getY() < y + 225) {
						
						if (Facade.getInstance().getPlayerRemainingCoins() >= card.getCost() && countCopies(card) < maxAllowedCopies) {
							Facade.getInstance().processEvent(new ChooseCardEvent(card));
						}
					}
					
					x += 155;
					
					if (x > 800) {
						x = 10;
						y += 235;
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}			
		});
		cardContainer.setBounds(0, 0, 930, 490);
		cardContainer.setOpaque(false);
		this.add(cardContainer);
	}
		
	public int countCopies(Card card) {
		int copies = 0;
		
		for (Card c : Facade.getInstance().getPlayerCardList()) {
			if (c.getName().equals(card.getName())) {
				copies++;
			}
		}
		
		return copies;
	}


	@Override
	public void resetUI() {		
		cardList = Facade.getInstance().getCardCollection();
		coin.setText(Facade.getInstance().getPlayerRemainingCoins() + "");
		playButton.setEnabled(Facade.getInstance().getPlayerRemainingCoins() == 0);
		textNum.setText(Facade.getInstance().getPlayerCardList().size() + "");
		maxAllowedCopies = Facade.getInstance().getMaximumAllowedCopies();
		
		String text = "";
		
		for (Card card : Facade.getInstance().getPlayerCardList()) {
			text += card.getName() + ", "; 
		}
		
		if (text.length() > 0) {
			text = text.substring(0, text.length() - 2);
		}
		
		cardNames.setText(text);
		this.repaint();
	}
}
