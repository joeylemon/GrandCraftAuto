package com.grandcraftauto.game.weapons;

import java.util.List;

import org.bukkit.Material;

import com.grandcraftauto.game.Rank;

public class Grenade extends Weapon {

	private int blastRadius;
	
	public Grenade(String name, WeaponType type, List<String> lore, int cost, int minLevel, Material material, double damage, int blastRadius, Rank rank){
		super(name, type, lore, cost, minLevel, material, damage, rank);
		this.blastRadius = blastRadius;
	}
	
	public int getBlastRadius(){
		return blastRadius;
	}
}
