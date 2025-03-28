package ca.qc.cvm.dba.magix.entity;

import java.awt.Graphics;

public abstract class Minion extends Card {
	private int attackPoints;
	
	public Minion(String name, int cost, int attackPoints) {
		super(name, CardType.Minion, cost);
		this.attackPoints = attackPoints;
	}
	
	public int getAttackPoints() {
		return attackPoints;
	}
	
	@Override
	protected void renderCard(Graphics g, int x, int y) {
		g.drawString("Attaque : " + attackPoints, x, y);
		
		this.renderMinion(g, x, y);
	}
	
	protected abstract void renderMinion(Graphics g, int x, int y);
}
