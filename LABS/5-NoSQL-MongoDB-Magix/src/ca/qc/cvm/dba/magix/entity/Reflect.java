package ca.qc.cvm.dba.magix.entity;

import java.awt.Graphics;

import ca.qc.cvm.dba.magix.entity.Spell.Effect;

public class Reflect extends Spell {
	
	public Reflect() {
		super("Reflect", 3);
	}
	
	@Override
	protected void renderSpell(Graphics g, int x, int y) {
		g.drawString("50% de la prochaine", x, y);
		g.drawString("attaque sera retournée", x, y + 15);
	}

	@Override
	public Effect getEffect() {
		return Effect.Reflect;
	}
}
