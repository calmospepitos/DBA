package ca.qc.cvm.dba.magix.entity;

import java.awt.Graphics;

public abstract class Spell extends Card {
	public enum Effect {Heal, LowerEnemyMinons, BlockNext, BuffMinion, BuffMinions, Reflect}
	
	public Spell(String name, int cost) {
		super(name, CardType.Spell, cost);
	}

	@Override
	protected void renderCard(Graphics g, int x, int y) {
		this.renderSpell(g, x, y);
	}
	
	protected abstract void renderSpell(Graphics g, int x, int y);
	public abstract Effect getEffect();
}
