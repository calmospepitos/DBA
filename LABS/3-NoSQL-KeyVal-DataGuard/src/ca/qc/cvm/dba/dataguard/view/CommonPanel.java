package ca.qc.cvm.dba.dataguard.view;

import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class CommonPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	protected static final int LINE_HEIGHT = 25;
	protected int width;
	protected int height;

	public CommonPanel(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	protected Component addField(Component component, int x, int y, int w, int h) {
		this.add(component);
		component.setBounds(x, y, w, h);
		
		return component;
	}
	
	protected JLabel addLabel(String text, int x, int y, int w, int h) {
		JLabel label = new JLabel(text);
		this.add(label);
		label.setBounds(x, y, w, h);
		
		return label;
	}
	
	protected JButton addButton(String text, int x, int y, int w, int h, ActionListener actionListener) {
		JButton btn = new JButton(text);
		this.add(btn);
		btn.setBounds(x, y, w, h);
		btn.addActionListener(actionListener);
		
		return btn;
	}
	
	public abstract void resetUI();
}
