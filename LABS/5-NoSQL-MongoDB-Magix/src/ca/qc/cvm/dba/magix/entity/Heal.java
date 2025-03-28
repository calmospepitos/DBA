package ca.qc.cvm.dba.magix.entity;

import java.awt.Graphics;

import ca.qc.cvm.dba.magix.entity.Spell.Effect;

public class Heal extends Spell {
	
	public Heal() {
		super("Heal", 4);
	}
	
	@Override
	protected void renderSpell(Graphics g, int x, int y) {
		g.drawString("Restauration de", x, y);
		g.drawString("1 à 5 points de vie", x, y + 15);
	}

	@Override
	public Effect getEffect() {
		return Effect.Heal;
	}
}
