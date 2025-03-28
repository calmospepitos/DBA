package ca.qc.cvm.dba.magix.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.Border;

import ca.qc.cvm.dba.magix.util.ActionHandler;

public abstract class Scene extends JPanel{
	private static final long serialVersionUID = 1L;
	protected static final int LINE_HEIGHT = 25;
	protected int width;
	protected int height;
	public static final Font bigFont = new Font("Verdana",0, 20);
	public static final Font mediumFont = new Font("Verdana",0, 15);
	private String backgroundPath;
	private JPanel backgroundPanel;

	public Scene(String backgroundPath) throws Exception {
		this.width = 950;
		this.height = 650;
		this.backgroundPath = backgroundPath;
		initUI();
	}
	
	protected void initUI() throws Exception {
		this.setPreferredSize(new Dimension(width, height));
		this.setBackground(Color.WHITE);
		
		this.setLayout(null);
		
		init();
		
		if (backgroundPath != null) {
			backgroundPanel = new BackgroundPanel(backgroundPath, width, height);
			this.add(backgroundPanel, new Dimension(0, 0));
		}
		else {
			this.setBackground(Color.BLACK);
		}
	}
	
	protected abstract void init() throws Exception;
	
	protected JTextArea addText(String text) {
		JTextArea label = new JTextArea();
		label.setText(text);
		label.setFont(mediumFont);
		label.setForeground(Color.WHITE);
		label.setOpaque(false);
		label.setBounds(10, 10, 910, 600);
		label.setEditable(false);
		this.add(label);
		
		return label;
	}
	
	protected JLabel addLabel(String text, int x, int y, int w, int h) {
		JLabel label = new JLabel(text);
		label.setFont(mediumFont);
		label.setForeground(Color.WHITE);
		this.add(label);
		label.setBounds(x, y, w, h);
		
		return label;
	}
	
	protected JButton addButtonSM(String text, int x, int y, final ActionHandler action) {
		JButton btn = new JButton(text);
		btn.setOpaque(false);
		btn.setContentAreaFilled(false);
		btn.setForeground(Color.WHITE);
		btn.setBorderPainted(false);
		btn.setIcon(new ImageIcon("assets/images/button-sm.png"));
		btn.setPressedIcon(new ImageIcon("assets/images/button-over-sm.png"));
		btn.setFont(new Font("Verdana", Font.PLAIN, 10));
		btn.setHorizontalTextPosition(JButton.CENTER);
		btn.setVerticalTextPosition(JButton.CENTER);		
		this.add(btn);
		btn.setBounds(x - 50, y - 14, 100, 28);
		btn.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				action.onAction();
			}
		});
		
		return btn;
	}
	
	protected JButton addButton(String text, int x, int y, final ActionHandler action) {
		JButton btn = new JButton(text);
		btn.setOpaque(false);
		btn.setContentAreaFilled(false);
		btn.setForeground(Color.WHITE);
		btn.setBorderPainted(false);
		btn.setIcon(new ImageIcon("assets/images/button.png"));
		btn.setPressedIcon(new ImageIcon("assets/images/button-over.png"));
		btn.setFont(bigFont);
		btn.setHorizontalTextPosition(JButton.CENTER);
		btn.setVerticalTextPosition(JButton.CENTER);		
		this.add(btn);
		btn.setBounds(x - 125, y - 35, 249, 69);
		btn.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				action.onAction();
			}
		});
		
		return btn;
	}
	
	protected JLabel addImage(String name, int x, int y, int w, int h) {
		JLabel label = new JLabel(new ImageIcon("assets/images/" + name));
		label.setBounds(x, y, w, h);
		this.add(label);
		
		return label;
	}
	
	protected abstract void resetUI();
	
	public void resetView() {		
		resetUI();
	}
	
	class BackgroundPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private Image backgroundImage;
		
		public BackgroundPanel(String fileName, int width, int height) throws IOException {
			backgroundImage = ImageIO.read(new File("assets/images/" + fileName));
			this.setSize(width, height);
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(backgroundImage, 0, 0, this);
		}
	}
}
