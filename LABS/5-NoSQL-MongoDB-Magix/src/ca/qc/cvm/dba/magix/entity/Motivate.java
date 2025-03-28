package ca.qc.cvm.dba.magix.entity;

import java.awt.Graphics;

import ca.qc.cvm.dba.magix.entity.Spell.Effect;

public class Motivate extends Spell {
	
	public Motivate() {
		super("Motivate", 4);
	}
	
	@Override
	protected void renderSpell(Graphics g, int x, int y) {
		g.drawString("Tous les minions font", x, y);
		g.drawString("150% de leur attaque", x, y + 15);
	}

	@Override
	public Effect getEffect() {
		return Effect.BuffMinions;
	}
}
