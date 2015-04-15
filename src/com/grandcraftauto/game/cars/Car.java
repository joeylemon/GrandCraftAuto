package com.grandcraftauto.game.cars;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import com.grandcraftauto.game.missions.Reward;
import com.grandcraftauto.utils.ObjectType;
import com.grandcraftauto.utils.TextUtils;
import com.grandcraftauto.utils.Utils;

public enum Car implements Reward {
	
	UBERMACHT_ORACLE("Ubermacht Oracle", 1.0,
			25, 1100, 3),
	UBERMACHT_SENTINEL("Ubermacht Sentinel", 1.1,
			31, 1600, 5),
	BRAVADO_BUFFALO("Bravado Buffalo", 1.14,
			45, 2000, 8),
	BRAVADO_BANSHEE("Bravado Banshee", 1.2,
			36, 2750, 11),
	BENEFACTOR_SURANO("Benefactor Surano", 1.275,
			18, 3000, 15),
	ANNIS_ELEGY_RH8("Annis Elegy RH8", 1.32,
			28, 3650, 19),
	PFISTER_COMET("Pfister Comet", 1.37,
			48, 4275, 22),
	INVERTO_COQUETTE("Inverto Coquette", 1.41,
			55, 4925, 25),
	DEWBAUCHEE_RAPID_GT("Dewbauchee Rapid GT", 1.48,
			38, 6150, 28),
	GROTTI_CARBONIZZARE("Grotti Carbonizzare", 1.55,
			65, 6975, 32),
	OBEY_9F("Obey 9F", 1.64,
			53, 8200, 35),
	COIL_VOLTIC("Coil Voltic", 1.7,
			63, 8850, 39),
	PEGASSI_INFERNUS("Pegassi Infernus", 1.775,
			49, 10100, 42),
	VAPID_BULLET("Vapid Bullet", 1.84,
			65, 10975, 46),
	PEGASSI_VACCA("Pegassi Vacca", 2,
			73, 13100, 51),
	GROTTI_CHEETAH("Grotti Cheetah", 2.1,
			59, 14700, 53),
	OVERFLOD_ENTITY_XF("Overflod Entity XF", 2.2,
			86, 16350, 56),
	TRUFFADE_ADDER("Truffade Adder", 2.5,
			78, 21500, 64);
	
	private static String gray = ChatColor.GRAY + "";
	private static String gold = ChatColor.GOLD + "";
	
	private String name;
	private double speed;
	private int armor;
	private int price;
	private int minlevel;
	
	/**
	 * Create a new car
	 * @param carName - The name of the car
	 * @param carSpeed - The speed of the car
	 * @param carPrice - The price of the car
	 */
	Car(String name, double speed, int armor, int price, int minimumLevel){
		this.name = name;
		this.speed = speed;
		this.armor = armor;
		this.price = price;
		this.minlevel = minimumLevel;
	}
	
	/**
	 * Get the car's name
	 * @return The car's name
	 */
	public String getName(){
		return name;
	}

	/**
	 * Get the car's speed
	 * @return The car's speed
	 */
	public double getSpeed(){
		return speed;
	}
	
	/**
	 * Get the car's speed
	 * @return The car's speed
	 */
	public int getArmor(){
		return armor;
	}
	
	/**
	 * Get the car's bar speed
	 * @return The car's bar speed
	 */
	public String getBarSpeed(){
		return Utils.asBars(Utils.getPercent((this.getSpeed() - 1), 1.5));
	}
	
	public String getBarArmor(){
		return Utils.asBars(Utils.getPercent(this.getArmor(), 100));
	}
	
	/**
	 * Get the car's horsepower
	 * @return The car's horsepower
	 */
	public int getHorsepower(){
		return (int)(this.getSpeed() * 227);
	}
	
	/**
	 * Get the car's price
	 * @return The car's price
	 */
	public int getPrice(){
		return price;
	}
	
	/**
	 * Get the car's minimum level
	 * @return The car's minimum level
	 */
	public int getMinimumLevel(){
		return minlevel;
	}
	
	/**
	 * Get the car's minimum speed
	 * @return The car's minimum speed
	 */
	public double getMinimumSpeed(){
		return (this.getSpeed() / 0.117646);
	}
	
	/**
	 * Get the car's maximum speed
	 * @return The car's maximum speed
	 */
	public double getMaximumSpeed(){
		return (this.getSpeed() / 0.116655);
	}
	
	/**
	 * Get the itemstack of the car
	 * @return The itemstack of the car
	 */
	public ItemStack getItemStack(boolean showPrice){
		String arrow = gold + TextUtils.getArrow() + " ";
		if(showPrice == false){
			return Utils.renameItem(new ItemStack(Material.MINECART), ChatColor.YELLOW + this.getName(), gray + ChatColor.ITALIC + "Shift + right-click to lock/unlock.", 
					arrow + "Speed: " + gray + this.getBarSpeed(), 
					arrow + "Armor: " + gray + this.getBarArmor(),
					arrow + "Horsepower: " + gray + this.getHorsepower());
		}else{
			return Utils.renameItem(new ItemStack(Material.MINECART), ChatColor.YELLOW + this.getName(), arrow + "Speed: " + gray + this.getBarSpeed(), 
					arrow + "Armor: " + gray + this.getBarArmor(),
					arrow + "Horsepower: " + gray + (int)(this.getSpeed() * 227),
					arrow + ChatColor.YELLOW + "Price: " + gray + "$" + this.getPrice(), 
					arrow + ChatColor.YELLOW + "Required Level: " + gray + this.getMinimumLevel());
		}
	}
	
	/**
	 * Get the car as a reward
	 * @return The car as a reward
	 */
	public Reward asReward(){
		return (Reward) this;
	}
	
	/**
	 * Check if a car is locked
	 * @param cart - The minecart of the car
	 * @return True or false depending on if the car is locked or not
	 */
	public static final boolean isLocked(Entity cart){
		boolean isLocked = false;
		if(cart.hasMetadata("locked") == true){
			if(Utils.getMetadata(cart, "locked", ObjectType.BOOLEAN) != null){
				isLocked = (boolean)Utils.getMetadata(cart, "locked", ObjectType.BOOLEAN);
			}
		}
		return isLocked;
	}
	
	/**
	 * Get the owner of a minecart
	 * @param cart - The minecart to get the owner of
	 * @return The owner of the minecart
	 */
	public static final String getOwner(Entity cart){
		String owner = null;
		if(cart.hasMetadata("owner") == true){
			if(Utils.getMetadata(cart, "owner", ObjectType.STRING) != null){
				owner = (String)Utils.getMetadata(cart, "owner", ObjectType.STRING);
			}
		}
		return owner;
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
