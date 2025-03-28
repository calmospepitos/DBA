package ca.qc.cvm.dba.magix.entity;

import java.awt.Graphics;

import ca.qc.cvm.dba.magix.entity.Spell.Effect;

public class Swift extends Spell {
	
	public Swift() {
		super("Swift", 3);
	}
	
	@Override
	protected void renderSpell(Graphics g, int x, int y) {
		g.drawString("Les minions de l'ennemi", x, y);
		g.drawString("ont -1 d'attaque", x, y + 15);
	}

	@Override
	public Effect getEffect() {
		return Effect.LowerEnemyMinons;
	}
}
