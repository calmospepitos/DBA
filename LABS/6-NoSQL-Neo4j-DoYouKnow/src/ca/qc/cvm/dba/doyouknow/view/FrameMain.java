package ca.qc.cvm.dba.doyouknow.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import ca.qc.cvm.dba.correctionserver.lib.CorrectionDialog;
import ca.qc.cvm.dba.doyouknow.app.Facade;
import ca.qc.cvm.dba.doyouknow.event.ButtonEvent;
import ca.qc.cvm.dba.doyouknow.event.UIEvent;

public class FrameMain extends JFrame implements Observer {
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 700;
	private static final int HEIGHT = 525;
	
	private JScrollPane scrollBox;
	private JTextPane textBox;
	private List<JButton> buttonList = new ArrayList<JButton>();
	
	public FrameMain() throws Exception {
		Facade.getInstance().addObserverClass(this);
		jbInit();
	}
	
	private void jbInit() throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
        this.setResizable(false);
		this.setSize(WIDTH, HEIGHT);
		
		this.setTitle("Connais-tu cette personne?");
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {}

			@Override
			public void windowClosing(WindowEvent e) {
				Facade.getInstance().exit();
			}

			@Override
			public void windowClosed(WindowEvent e) {}

			@Override
			public void windowIconified(WindowEvent e) {}

			@Override
			public void windowDeiconified(WindowEvent e) {}

			@Override
			public void windowActivated(WindowEvent e) {}

			@Override
			public void windowDeactivated(WindowEvent e) {	}
        	
        });

		Toolkit g = this.getToolkit();
		int x = (g.getScreenSize().width / 2) - WIDTH/2;
		int y = (g.getScreenSize().height / 2) - HEIGHT/2;
		this.setLocation(x, y);
		
		BufferedImage myImage = ImageIO.read(new File("assets/images/background.jpg"));
		ImagePanel panel = new ImagePanel(myImage);
		panel.setLayout(null);
		
		JButton btn = new JButton("1 - Afficher toutes les personnes");
		btn.setBounds(10, 10, 250, 30);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Facade.getInstance().processEvent(new ButtonEvent(1));
			}
		});
		buttonList.add(btn);
		panel.add(btn);
		
		btn = new JButton("2 - Les connexions de...");
		btn.setBounds(10, 50, 250, 30);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String name = JOptionPane.showInputDialog(FrameMain.this, "Entrez le nom de la personne");
				
				if (name != null && name.trim().length() > 0) {
					Facade.getInstance().processEvent(new ButtonEvent(2, name));
				}
			}
		});
		buttonList.add(btn);
		panel.add(btn);
		
		btn = new JButton("3 - Les populaires");
		btn.setBounds(10, 90, 250, 30);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Facade.getInstance().processEvent(new ButtonEvent(3));
			}
		});
		buttonList.add(btn);
		panel.add(btn);

		btn = new JButton("4 - Proposition de connexions...");
		btn.setBounds(10, 130, 250, 30);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String name = JOptionPane.showInputDialog(FrameMain.this, "Entrez le nom de la personne");
				
				if (name != null && name.trim().length() > 0) {
					Facade.getInstance().processEvent(new ButtonEvent(4, name));
				}
			}
		});
		buttonList.add(btn);
		panel.add(btn);

		btn = new JButton("5 - Les inconnues");
		btn.setBounds(10, 170, 250, 30);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Facade.getInstance().processEvent(new ButtonEvent(5));
			}
		});
		buttonList.add(btn);
		panel.add(btn);

		btn = new JButton("6 - Le plus agé");
		btn.setBounds(10, 210, 250, 30);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Facade.getInstance().processEvent(new ButtonEvent(6));
			}
		});
		buttonList.add(btn);
		panel.add(btn);

		btn = new JButton("x - Démarrer la correction");
		btn.setBounds(10, 450, 250, 30);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String[] data = CorrectionDialog.getData(FrameMain.this);
				
				if (data != null) {
					FrameMain.this.setWorking(true);
					Facade.getInstance().processEvent(new ButtonEvent(10, data[0], data[1], data[2]));
				}
			}
		});
		buttonList.add(btn);
		panel.add(btn);
		
		textBox = new JTextPane();
		textBox.setEditable(false);
		scrollBox  = new JScrollPane(textBox);
		scrollBox.setBounds(300, 10, 350, 475);
		scrollBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panel.add(scrollBox);

		this.setContentPane(panel);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		UIEvent event = ((UIEvent)arg);
		
		if (event.getUIType() == UIEvent.UIType.ShowMessage) {
			Document doc = textBox.getDocument();
			
			try {
				SimpleAttributeSet style = null;
				
				if (event.isSpecial()) {
					style = new SimpleAttributeSet();
					StyleConstants.setForeground(style, Color.BLUE);
					StyleConstants.setBold(style, true);
					setWorking(false);
				}
				else {
					setWorking(true);
				}
				
				doc.insertString(doc.getLength(), event.getText() + (event.getText().endsWith("\n") ? "" : "\n"), style);
				
				textBox.setCaretPosition(doc.getLength());
			} 
			catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void setWorking(boolean working) {
		for(JButton btn : this.buttonList) {
			btn.setEnabled(!working);
		}
	}
	
	class ImagePanel extends JComponent {
		private static final long serialVersionUID = 1L;
		private Image image;
	    
	    public ImagePanel(Image image) {
	        this.image = image;
	    }
	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        g.drawImage(image, 0, 0, this);
	    }
	}
}
