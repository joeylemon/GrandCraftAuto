package gca.game.weapons;

import gca.game.Rank;
import gca.game.missions.Reward;
import gca.utils.TextUtils;
import gca.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Weapon implements Reward {
	
	static List<Weapon> weapons = new ArrayList<Weapon>();
	private static String gray = ChatColor.GRAY + "";
	private static String gold = ChatColor.GOLD + "";
	private static String italic = ChatColor.ITALIC + "";
	
	private String name;
	private WeaponType type;
	private List<String> lore;
	private int cost;
	private int minLevel;
	private Material material;
	private double damage;
	private Rank rank;
	
	public Weapon(String name, WeaponType type, List<String> lore, int cost, int minLevel, Material material, double damage, Rank rank){
		this.name = name;
		this.type = type;
		this.lore = lore;
		this.cost = cost;
		this.minLevel = minLevel;
		this.material = material;
		this.damage = damage;
		this.rank = rank;
	}
	
	/**
	 * Get the weapon's name
	 * @return The weapon's name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Get the weapon's type
	 * @return The weapon's type
	 */
	public WeaponType getType(){
		return type;
	}
	
	/**
	 * Get the weapon's lore
	 * @return The weapon's lore
	 */
	public List<String> getLore(){
		List<String> gunlore = new ArrayList<String>();
		gunlore.addAll(lore);
		if(this instanceof Gun){
			Gun g = (Gun) this;
			gunlore.add(gold + TextUtils.getArrow() + " Ammo: " + gray + g.getAmmoType().getName());
			gunlore.add(gold + TextUtils.getArrow() + " Damage: " + gray + Utils.asBars(Utils.getPercent(this.getDamage(), WeaponUtils.getMaxDamage())));
			gunlore.add(gold + TextUtils.getArrow() + " Accuracy: " + gray + Utils.asBars(Utils.getPercent(g.getRealAccuracy(), WeaponUtils.getMaxAccuracy())));
			gunlore.add(gold + TextUtils.getArrow() + " Firing Rate: " + gray + Utils.asBars(Utils.getPercent((6 - g.getFiringRate()), (WeaponUtils.getMaxFiringRate() + 1))));
			gunlore.add(gold + TextUtils.getArrow() + " Range: " + gray + Utils.asBars(Utils.getPercent(g.getRange() - 1, WeaponUtils.getMaxRange())));
			gunlore.add(gold + TextUtils.getArrow() + " Clip Size: " + gray + Utils.asBars(Utils.getPercent(g.getClipSize() - 4, WeaponUtils.getMaxClipSize())));
		}else{
			gunlore.add(gold + TextUtils.getArrow() + " Damage: " + gray + Utils.asBars(Utils.getPercent(this.getDamage(), WeaponUtils.getMaxDamage())));
		}
		return gunlore;
	}
	
	/**
	 * Get the weapon's price
	 * @return The weapon's price
	 */
	public int getCost(){
		return cost;
	}
	
	/**
	 * Get the weapon's minimum level required
	 * @return The weapon's minimum level required
	 */
	public int getMinimumLevel(){
		return minLevel;
	}
	
	/**
	 * Get the weapon's material
	 * @return The weapon's material
	 */
	public Material getMaterial(){
		return material;
	}
	
	/**
	 * Get the item stack of the weapon
	 * @return The item stack of the weapon
	 */
	public ItemStack getItemStack(boolean showPrice){
		if(showPrice == false){
			return Utils.renameItem(new ItemStack(this.getMaterial()), ChatColor.GOLD + this.getName(), this.getLore());
		}else{
			List<String> lore = new ArrayList<String>();
			lore.addAll(this.getLore());
			lore.add(gold + TextUtils.getArrow() + ChatColor.YELLOW + " Price: " + gray + "$" + this.getCost());
			lore.add(gold + TextUtils.getArrow() + ChatColor.YELLOW + " Required Level: " + gray + this.getMinimumLevel());
			return Utils.renameItem(new ItemStack(this.getMaterial()), ChatColor.GOLD + this.getName(), lore);
		}
	}
	
	/**
	 * Get the weapon's damage
	 * @return The weapon's damage
	 */
	public double getDamage(){
		return damage;
	}
	
	/**
	 * Get the weapon's required rank
	 * @return The weapon's required rank
	 */
	public Rank getRequiredRank(){
		return rank;
	}
	
	/**
	 * Get the weapon as a reward
	 * @return The weapon as a reward
	 */
	public Reward asReward(){
		return (Reward) this;
	}
	
	/**
	 * Initialize the weapons list for usage
	 */
	public static final void initializeList(){
		weapons.add(new MeleeWeapon("Knife", WeaponType.KNIFE, Arrays.asList(gray + italic + "Perfect for shanking."), 
				25, 1, Material.ARROW, 1.1, null));
		weapons.add(new MeleeWeapon("Golf Club", WeaponType.GOLF_CLUB, Arrays.asList(gray + italic + "Fore!"), 
				40, 2, Material.STICK, 1.3, null));
		weapons.add(new MeleeWeapon("Nightstick", WeaponType.NIGHTSTICK, Arrays.asList(gray + italic + "Easily found off of a dead cop's body."), 
				50, 4, Material.BLAZE_ROD, 1.38, null));
		weapons.add(new MeleeWeapon("Baseball Bat", WeaponType.BASEBALL_BAT, Arrays.asList(gray + italic + "Used for more than just hitting baseballs."), 
				65, 7, Material.STICK, 1.5, null));
		weapons.add(new MeleeWeapon("Crowbar", WeaponType.CROWBAR, Arrays.asList(gray + italic + "High quality head smasher."), 
				95, 12, Material.LEVER, 1.75, null));
		
		weapons.add(new Gun("Pistol", WeaponType.PISTOL, Arrays.asList(gray + italic + "A well-rounded weapon for beginners."), 
				350, 1, Material.WOOD_HOE, Ammo._45ACP, 5.2, 12, 1, 5, 10, 4, 2.5, null));
		weapons.add(new Gun("Combat Pistol", WeaponType.COMBAT_PISTOL, Arrays.asList(gray + italic + "A ranged, accurate, and powerful pistol."), 
				600, 3, Material.WOOD_HOE, Ammo._45ACP, 5.4, 12, 1, 5, 8, 5, 3, null));
		weapons.add(new Gun("AP Pistol", WeaponType.AP_PISTOL, Arrays.asList(gray + italic + "A fully automatic and powerful handgun."), 
				1000, 5, Material.WOOD_HOE, Ammo._9MM, 5.6, 18, 2, 3, 1, 4.5, 3, null));
		weapons.add(new Gun("Micro SMG", WeaponType.MICRO_SMG, Arrays.asList(gray + italic + "A fairly rounded SMG, especially in close-combat."), 
				850, 7, Material.STONE_HOE, Ammo._22, 2.1, 16, 2, 2, 0, 3, 2.5, null));
		weapons.add(new Gun("SMG", WeaponType.SMG, Arrays.asList(gray + italic + "Just your average submachine gun."), 
				1300, 10, Material.STONE_HOE, Ammo._22, 2.2, 30, 3, 3, 0, 4, 3.5, null));
		weapons.add(new Gun("SNS Pistol", WeaponType.SNS_PISTOL, Arrays.asList(gray + italic + "Features fewer but meatier bullets."), 
				1250, 12, Material.WOOD_HOE, Ammo._9MM, 6.4, 6, 1, 5, 10, 4, 1.5, null));
		weapons.add(new Gun("Assault SMG", WeaponType.ASSAULT_SMG, Arrays.asList(gray + italic + "A fast-firing model of the SMG."), 
				1450, 14, Material.STONE_HOE, Ammo._22, 2.2, 30, 2, 2, 0, 4, 3.5, null));
		weapons.add(new Gun("Assault Rifle", WeaponType.ASSAULT_RIFLE, Arrays.asList(gray + italic + "Just your average assault rifle."), 
				1650, 18, Material.IRON_HOE, Ammo._556, 3.0, 30, 2, 3, 0, 4.5, 4.5, null));
		weapons.add(new Gun("Carbine Rifle", WeaponType.CARBINE_RIFLE, Arrays.asList(gray + italic + "An improved model of the Assault Rifle."), 
				2100, 19, Material.IRON_HOE, Ammo._556, 3.2, 30, 2, 2, 0, 5.5, 4.5, null));
		weapons.add(new Gun("Advanced Rifle", WeaponType.ADVANCED_RIFLE, Arrays.asList(gray + italic + "A superior assault rifle with insane firing speed."), 
				3500, 22, Material.IRON_HOE, Ammo._762, 3.4, 30, 2, 1, 0, 5, 4.5, null));
		weapons.add(new Gun("Special Carbine", WeaponType.SPECIAL_CARBINE, Arrays.asList(gray + italic + "The highest damaging assault rifle you can find.", 
				ChatColor.GREEN + "Thug rank required."), 
				4250, 29, Material.IRON_HOE, Ammo._762, 5.7, 30, 2, 3, 0, 6, 4.5, Rank.THUG));
		
		weapons.add(new Shotgun("Pump Shotgun", WeaponType.PUMP_SHOTGUN, Arrays.asList(gray + italic + "Perfect for close-quarters with its destructive damage."), 
				2000, 10, Material.WOOD_PICKAXE, Ammo._12_GAUGE, 5.8, 8, 5, 6, 25, 0.1, 1.5, null));
		weapons.add(new Shotgun("Sawed-Off Shotgun", WeaponType.SAWED_OFF_SHOTGUN, Arrays.asList(gray + italic + "Amazing at point-blank range, but otherwise useless."), 
				4600, 26, Material.WOOD_PICKAXE, Ammo._12_GAUGE, 6.6, 6, 6, 6, 25, 0.05, 1, null));
		weapons.add(new Shotgun("Assault Shotgun", WeaponType.ASSAULT_SHOTGUN, Arrays.asList(gray + italic + "Fully automatic design with accuracy and range.", 
				ChatColor.GREEN + "Godfather rank required."), 
				6500, 35, Material.WOOD_PICKAXE, Ammo._12_GAUGE, 6.2, 8, 5, 4, 10, 0.2, 2.0, Rank.GODFATHER));
		
		weapons.add(new Grenade("Grenade", WeaponType.GRENADE, Arrays.asList(gray + italic + "Obliterate the masses in a single explosion."),
				1000, 20, Material.FIREWORK_CHARGE, 20, 5, null));
	}
	
	/**
	 * Get the list of weapons
	 * @return The list of weapons
	 */
	public static final List<Weapon> list(){
		return weapons;
	}
	
	/**
	 * Get the list of guns
	 * @return The list of guns
	 */
	public static final List<Gun> gunList(){
		List<Gun> guns = new ArrayList<Gun>();
		for(Weapon w : weapons){
			if(w instanceof Gun && !(w instanceof Shotgun)){
				guns.add((Gun)w);
			}
		}
		return guns;
	}
	
	/**
	 * Get the list of shotguns
	 * @return The list of shotguns
	 */
	public static final List<Shotgun> shotgunList(){
		List<Shotgun> shotguns = new ArrayList<Shotgun>();
		for(Weapon w : weapons){
			if(w instanceof Shotgun){
				shotguns.add((Shotgun)w);
			}
		}
		return shotguns;
	}
	
	/**
	 * Get the list of melee weapons
	 * @return The list of melee weapons
	 */
	public static final List<MeleeWeapon> meleeList(){
		List<MeleeWeapon> melees = new ArrayList<MeleeWeapon>();
		for(Weapon w : weapons){
			if(w instanceof MeleeWeapon){
				melees.add((MeleeWeapon)w);
			}
		}
		return melees;
	}
	
	/**
	 * Get the list of explosive weapons
	 * @return The list of explosive weapons
	 */
	public static final List<Grenade> explosiveList(){
		List<Grenade> explosives = new ArrayList<Grenade>();
		for(Weapon w : weapons){
			if(w instanceof Grenade){
				explosives.add((Grenade)w);
			}
		}
		return explosives;
	}
	
	/**
	 * Get a weapon through it's weapon type
	 * @param type - The weapon type
	 * @return The weapon
	 */
	public static final Weapon get(WeaponType type){
		Weapon weapon = null;
		for(Weapon w : Weapon.list()){
			if(w.getType() == type){
				weapon = w;
				break;
			}
		}
		return weapon;
	}
	
	/**
	 * Check if a material is a weapon
	 * @param material - The material to check
	 * @return True or false depending on if the material is a weapon or not
	 */
	public static final boolean isWeapon(Material material){
		boolean is = false;
		for(Weapon w : Weapon.list()){
			if(w.getItemStack().getType() == material){
				is = true;
				break;
			}
		}
		return is;
	}

	/**
	 * Get the reward itemstack
	 */
	public ItemStack getItemStack(){
		return this.getItemStack(false);
	}
	
	/**
	 * Get the reward's cash payout
	 */
	public int getCashReward(){
		return 0;
	}
}
