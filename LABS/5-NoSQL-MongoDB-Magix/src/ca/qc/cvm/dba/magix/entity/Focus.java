package ca.qc.cvm.dba.magix.entity;

import java.awt.Graphics;

import ca.qc.cvm.dba.magix.entity.Spell.Effect;

public class Focus extends Spell {
	public Focus() {
		super("Focus", 5);
	}
	
	@Override
	protected void renderSpell(Graphics g, int x, int y) {
		g.drawString("Le prochain minion", x, y);
		g.drawString("fera 300% d'attaque", x, y + 15);
	}

	@Override
	public Effect getEffect() {
		return Effect.BuffMinion;
	}
}
