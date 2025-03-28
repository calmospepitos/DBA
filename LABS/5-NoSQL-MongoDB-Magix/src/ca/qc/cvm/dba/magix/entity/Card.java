package ca.qc.cvm.dba.magix.entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.ImageIcon;

public abstract class Card {
	public enum CardType {Minion, Spell};
	
	private String name;
	private ImageIcon image;
	private CardType type;
	private int cost;

	private static ImageIcon frame;
	private static ImageIcon coin;
	private static Color disabledOverlay = new Color(0f,0f,0f,0.75f);
	private static Font font = new java.awt.Font("Verdana", java.awt.Font.BOLD, 10);
	
	public Card(String name, CardType type, int cost) {
		this.name = name;
		this.type = type;
		this.cost = cost;
		
		if (frame == null) {
			frame = new ImageIcon("assets/images/frame-card-sm.png");
			coin = new ImageIcon("assets/images/coin.png");
		}

		image = new ImageIcon("assets/images/" + name.toLowerCase() + "-sm.png");
	}
	
	public CardType getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}
	
	public int getCost() {
		return cost;
	}

	public void render(Graphics g, int x, int y, boolean active) {
		g.drawImage(image.getImage(), x, y, null);
		g.drawImage(frame.getImage(), x, y, null);
		
		if (type == CardType.Minion) {
			g.setColor(Color.white);
		}
		else {
			g.setColor(Color.magenta);
		}
		
		g.drawString(name, x + 8, y + 155);
		g.setColor(Color.gray);
		
		if (type == CardType.Minion) {
			g.drawString("Minion", x + 45, y + 210);
		}
		else {
			g.drawString("Spell", x + 50, y + 210);
		}
		
		g.drawImage(coin.getImage(), x + 110, y + 120, null);
		g.setColor(Color.black);
		g.drawString("" + cost, x + 121, y + 140);
		
		Font font = g.getFont();
		g.setFont(font);
		g.setColor(Color.white);
		renderCard(g, x + 8, y + 180);
		g.setFont(font);
		
		if (!active) {
			Color originalColor = g.getColor();
			
			g.setColor(disabledOverlay);
			g.fillRect(x, y, 150, 223); //draw onto the alpha map
			
			g.setColor(originalColor);
		}
	}
	
	protected abstract void renderCard(Graphics g, int x, int y); 
}
