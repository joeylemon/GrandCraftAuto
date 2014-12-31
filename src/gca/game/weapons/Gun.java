package gca.game.weapons;

import gca.core.Rank;

import java.util.List;

import org.bukkit.Material;

public class Gun extends Weapon {
	
	private Ammo ammoType;
	private int clipSize;
	private int roundsPerShot;
	private int firingRate;
	private int cooldown;
	private double accuracy;
	private double range;
	
	public Gun(String name, WeaponType type, List<String> lore, int cost, int minLevel, Material material, Ammo ammoType, double damage,
			int clipSize, int roundsPerShot, int firingRate, int cooldown, double accuracy, double range, Rank rank){
		super(name, type, lore, cost, minLevel, material, damage, rank);
		this.ammoType = ammoType;
		this.clipSize = clipSize;
		this.roundsPerShot = roundsPerShot;
		this.firingRate = firingRate;
		this.cooldown = cooldown;
		this.accuracy = accuracy;
		this.range = range;
	}
	
	/**
	 * Get the weapon's bullet material
	 * @return The weapon's bullet material
	 */
	public Ammo getAmmoType(){
		return ammoType;
	}
	
	/**
	 * Get the weapon's clip size
	 * @return The weapon's clip size
	 */
	public int getClipSize(){
		return clipSize;
	}
	
	/**
	 * Get the weapon's rounds per shot
	 * @return The weapon's rounds per shot
	 */
	public int getRoundsPerShot(){
		return roundsPerShot;
	}
	
	/**
	 * Get the weapon's firing rate
	 * @return The weapon's firing rate
	 */
	public int getFiringRate(){
		return firingRate;
	}
	
	/**
	 * Get the weapon's reload rate
	 * @return The weapon's reload rate
	 */
	public int getReloadRate(){
		return 75/this.getClipSize();
	}
	
	/**
	 * Get the weapon's cooldown time
	 * @return The weapon's cooldown time
	 */
	public int getCooldown(){
		return cooldown;
	}
	
	/**
	 * Get the weapon's accuracy
	 * @return The weapon's accuracy
	 */
	public double getRealAccuracy(){
		return accuracy;
	}
	
	/**
	 * Get the weapon's accuracy
	 * @return The weapon's accuracy
	 */
	public double getAccuracy(){
		double a = (1 - (accuracy * .1)) * .4;
		return a;
	}
	
	/**
	 * Get the weapon's range
	 * @return The weapon's range
	 */
	public double getRange(){
		return range;
	}
}