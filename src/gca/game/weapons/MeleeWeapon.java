package gca.game.weapons;

import gca.core.Rank;

import java.util.List;

import org.bukkit.Material;

public class MeleeWeapon extends Weapon{

	/**
	 * Create a new melee weapon
	 * @param weaponName - The name of the weapon
	 * @param weaponType - The type of weapon it is
	 * @param weaponLore - Some lore for the weapon
	 * @param weaponCost - The cost of the weapon
	 * @param weaponMinLevel - The minimum level needed to buy the weapon
	 * @param weaponMaterial - The material of the weapon
	 * @param weaponDamage - The damage of the weapon's hits
	 */
	public MeleeWeapon(String weaponName, WeaponType weaponType, List<String> weaponLore, int weaponCost, int weaponMinLevel, Material weaponMaterial, double weaponDamage, Rank rank){
		super(weaponName, weaponType, weaponLore, weaponCost, weaponMinLevel, weaponMaterial, weaponDamage, rank);
	}

}
