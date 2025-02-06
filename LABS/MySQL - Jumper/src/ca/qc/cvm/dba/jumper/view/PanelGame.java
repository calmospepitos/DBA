package ca.qc.cvm.dba.jumper.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import ca.qc.cvm.dba.jumper.app.Facade;
import ca.qc.cvm.dba.jumper.entity.GameLog;
import ca.qc.cvm.dba.jumper.event.BackEvent;
import ca.qc.cvm.dba.jumper.event.SaveEvent;

public class PanelGame extends CommonPanel {
	private static final long serialVersionUID = 1L;
	
	private Font bigFont = new Font("Arial", Font.ITALIC, 40);
	private Font inGameFont = new Font("Arial", 0, 15);
	
	private long previousTime;
	private int gameState;
	private int gameCounter;
	
	private double gameSpeed;
	private int jumperX;
	private int jumperY;
	private int jumperSpeed;
	private long bombDelay;
	private List<Integer[]> bombWalls;
	
	private BufferedImage bombImage;
	private BufferedImage cityImage;
	private BufferedImage nightImage;
	private BufferedImage jumperImage;
	
	private boolean upPushed;
	private boolean downPushed;
	private boolean leftPushed;
	private boolean rightPushed;
	
	private boolean redrawCompleted;

	public PanelGame(int width, int height) throws Exception {
		super(width, height, false, "assets/images/space.jpg");
	}
	
	@Override
	protected void initUI() throws Exception {
		this.setPreferredSize(new Dimension(width, height));
		this.setBackground(Color.WHITE);
		
		this.setLayout(null);				
		this.setOpaque(true);

		this.setFocusable(true);
		
		try { 
			cityImage = ImageIO.read(new File("assets/images/city.png"));
			nightImage = ImageIO.read(new File("assets/images/night.png"));
			jumperImage = ImageIO.read(new File("assets/images/jumper.png"));
			bombImage = ImageIO.read(new File("assets/images/bomb.png"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		this.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_W) upPushed = true;
				if (arg0.getKeyCode() == KeyEvent.VK_S) downPushed = true;
				if (arg0.getKeyCode() == KeyEvent.VK_A) leftPushed = true;
				if (arg0.getKeyCode() == KeyEvent.VK_D) rightPushed = true;
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_W) upPushed = false;
				if (arg0.getKeyCode() == KeyEvent.VK_S) downPushed = false;
				if (arg0.getKeyCode() == KeyEvent.VK_A) leftPushed = false;
				if (arg0.getKeyCode() == KeyEvent.VK_D) rightPushed = false;
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	@Override
	public void jbInit() throws Exception {}
	
	private void drawString(Graphics2D g2, String text, int x, int y, Font font, boolean centered) {
		g2.setColor(Color.WHITE);
		g2.setFont(font);
		int titleLength = 0;
		
		if (centered) {
			TextLayout textLayout = new TextLayout(text, g2.getFont(), new FontRenderContext(null, false, false));
			titleLength = (int)(textLayout.getBounds().getWidth());
		}
		
		g2.drawString(text, x - titleLength/2, y);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		if (!redrawCompleted) {
			Graphics2D g2 = (Graphics2D)g;
			g2.setColor(Color.BLACK);
			g2.fillRect(0, 0, width, height);
			
			if (gameState == 0) {
				if (gameCounter/1000 > 0) {
					drawString(g2, "Attention!!! ... " + (gameCounter/1000), width/2 - 40, 150, bigFont, true);
				}
				else {
					drawString(g2, "Go !!", width/2 - 40, 150, bigFont, true);
				}
			}
			else if (gameState == 1) {
				g2.drawImage(nightImage, 0, 0, null);
				drawString(g2, "Score : " + gameCounter, 10, 20, inGameFont, false);
				g2.drawImage(cityImage, -((int)gameSpeed % 2937), 100, null);
				g2.drawImage(cityImage, 2937 -((int)gameSpeed % 2937), 100, null);
				g2.drawImage(jumperImage, jumperX, jumperY, null);
				
				for (Integer[] wall : bombWalls) {
					for (int i = 0; i < height; i += 55) {
						if (Math.abs(i - wall[1]) > 75) {
							g2.drawImage(bombImage, wall[0], i, null);
							
							if (Math.abs(wall[0] + 20 - (jumperX + 35)) < 35 &&
								Math.abs(i + 20 - (jumperY + 35)) < 35) {
								gameState = 2;
							}
						}
					}
					
					wall[0] = wall[0] - wall[2];
				}
			}
			
			g.dispose();
			redrawCompleted = true;
		}		
	}
	
	private void processGameOver() {
		String playerName = JOptionPane.showInputDialog(PanelGame.this, "Entrez votre nom");
		
		if (playerName != null) {
			Facade.getInstance().processEvent(new SaveEvent(new GameLog(playerName, gameCounter)));	
		}
		else {					
			Facade.getInstance().processEvent(new BackEvent());
		}
	}
	
	private void addBombWall() {
		if (bombWalls.size() > 0 && bombWalls.get(0)[0] < 0) bombWalls.remove(0);
		
		bombWalls.add(new Integer[]{width, (int)(Math.random() * (height - 150)), 3 + (int)gameSpeed/1500});
	}
	
	/**
	 * Cette méthode est appelée automatiquement à chaque fois qu'un panel est affiché (lorsqu'on arrive sur la page)
	 */
	@Override
	public void resetUI() {
		gameState = 0;
		previousTime = 0;
		gameCounter = 4000;
		
		gameSpeed = 1500;
		jumperX = 100;
		jumperY = 450;
		previousTime = System.currentTimeMillis();
		jumperSpeed = 3;
		bombWalls = new ArrayList<Integer[]>();
		
		bombDelay = 1000;
		
		upPushed = false;
		downPushed = false;
		leftPushed = false;
		rightPushed = false;
		
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				
				while (true) {
					long newTime = System.currentTimeMillis();
					long delta = newTime - previousTime;
					
					if (gameState == 0) {
						gameCounter -= delta;
						
						if (gameCounter < 0) {
							gameState = 1;
							gameCounter = 0;
							requestFocusInWindow();
						}
					}
					else if (gameState == 1) {
						gameCounter += delta;
						gameSpeed *= 1.00075;
						
						bombDelay -= delta;
						
						if (bombDelay < 0) {
							addBombWall();
							bombDelay = 5000 - (int)gameSpeed;
							if (bombDelay < 1000) bombDelay = 1000; 
						}
						
						jumperSpeed = 4 + gameCounter/10000;
						
						if (upPushed && jumperY - jumperSpeed > 10) jumperY -= jumperSpeed;
						if (downPushed && jumperY + jumperSpeed < height - 100) jumperY += jumperSpeed;
						if (leftPushed && jumperX - jumperSpeed > 10) jumperX -= jumperSpeed;
						if (rightPushed && jumperX + jumperSpeed < width - 60) jumperX += jumperSpeed;
					}
					else if (gameState == 2) {
						processGameOver();
						break;
					}
					
					long paintTime = System.currentTimeMillis();
					redrawCompleted = false;
					paintImmediately(0, 0, width, height);
					
					try {
						while (!redrawCompleted) {
							Thread.sleep(5);
						}
						
						paintTime = System.currentTimeMillis() - paintTime;
						if (paintTime < 15) Thread.sleep(15 - paintTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}					
					
					previousTime = newTime;
				}
			}
			
		});
		
		t.start();
	}

}
