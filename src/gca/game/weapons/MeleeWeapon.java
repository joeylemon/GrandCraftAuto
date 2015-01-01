package gca.game.weapons;

import gca.game.Rank;

import java.util.List;

import org.bukkit.Material;

public class MeleeWeapon extends Weapon{

	public MeleeWeapon(String weaponName, WeaponType weaponType, List<String> weaponLore, int weaponCost, int weaponMinLevel, Material weaponMaterial, double weaponDamage, Rank rank){
		super(weaponName, weaponType, weaponLore, weaponCost, weaponMinLevel, weaponMaterial, weaponDamage, rank);
	}

}
