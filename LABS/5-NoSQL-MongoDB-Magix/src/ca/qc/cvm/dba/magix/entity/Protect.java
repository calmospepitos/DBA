package ca.qc.cvm.dba.magix.entity;

import java.awt.Graphics;

import ca.qc.cvm.dba.magix.entity.Spell.Effect;

public class Protect extends Spell {
	
	public Protect() {
		super("Protect", 3);
	}
	
	@Override
	protected void renderSpell(Graphics g, int x, int y) {
		g.drawString("Vous bloquerez la", x, y);
		g.drawString("prochaine attaque", x, y + 15);
	}

	@Override
	public Effect getEffect() {
		return Effect.BlockNext;
	}
}
