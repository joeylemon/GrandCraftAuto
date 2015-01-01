package com.grandcraftauto.game.weapons;

import java.util.List;

import org.bukkit.Material;

import com.grandcraftauto.game.Rank;

public class MeleeWeapon extends Weapon{

	public MeleeWeapon(String weaponName, WeaponType weaponType, List<String> weaponLore, int weaponCost, int weaponMinLevel, Material weaponMaterial, double weaponDamage, Rank rank){
		super(weaponName, weaponType, weaponLore, weaponCost, weaponMinLevel, weaponMaterial, weaponDamage, rank);
	}

}
