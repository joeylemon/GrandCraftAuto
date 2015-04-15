package com.grandcraftauto.game.weapons;

public final class WeaponUtils {
	
	private static double maxDamage = 0;
	private static double maxAccuracy = 0;
	private static double maxFiringRate = 0;
	private static double maxRange = 0;
	private static double maxClipSize = 0;
	
	/**
	 * Get the highest damage of the weapons
	 * @return The highest damage of the weapons
	 */
	public static final double getMaxDamage(){
		if(maxDamage <= 0){
			double max = 0;
			for(Weapon w : Weapon.list()){
				if(w.getDamage() > max && w.getType() != WeaponType.GRENADE){
					max = w.getDamage();
				}
			}
			maxDamage = max;
			return max;
		}else{
			return maxDamage;
		}
	}
	
	/**
	 * Get the highest accuracy of the weapons
	 * @return The highest accuracy of the weapons
	 */
	public static final double getMaxAccuracy(){
		if(maxAccuracy <= 0){
			double max = 0;
			for(Weapon w : Weapon.list()){
				if(w instanceof Gun){
					Gun g = (Gun) w;
					if(g.getRealAccuracy() > max){
						max = g.getRealAccuracy();
					}
				}
			}
			maxAccuracy = max;
			return max;
		}else{
			return maxAccuracy;
		}
	}
	
	/**
	 * Get the highest firing rate of the weapons
	 * @return The highest firing rate of the weapons
	 */
	public static final double getMaxFiringRate(){
		if(maxFiringRate <= 0){
			double max = 0;
			for(Weapon w : Weapon.list()){
				if(w instanceof Gun){
					Gun g = (Gun) w;
					if(g.getFiringRate() > max){
						max = g.getFiringRate();
					}
				}
			}
			maxFiringRate = max;
			return max;
		}else{
			return maxFiringRate;
		}
	}
	
	/**
	 * Get the longest range of the weapons
	 * @return The longest range of the weapons
	 */
	public static final double getMaxRange(){
		if(maxRange <= 0){
			double max = 0;
			for(Weapon w : Weapon.list()){
				if(w instanceof Gun){
					Gun g = (Gun) w;
					if(g.getRange() > max){
						max = g.getRange();
					}
				}
			}
			maxRange = max;
			return max;
		}else{
			return maxRange;
		}
	}
	
	/**
	 * Get the largest clip size of the weapons
	 * @return The largest clip size of the weapons
	 */
	public static final double getMaxClipSize(){
		if(maxClipSize <= 0){
			double max = 0;
			for(Weapon w : Weapon.list()){
				if(w instanceof Gun){
					Gun g = (Gun) w;
					if(g.getClipSize() > max){
						max = g.getClipSize();
					}
				}
			}
			maxClipSize = max;
			return max;
		}else{
			return maxClipSize;
		}
	}
}
