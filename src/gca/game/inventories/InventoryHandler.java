package gca.game.inventories;

import gca.game.weapons.Ammo;
import gca.game.weapons.MeleeWeapon;
import gca.game.weapons.Weapon;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class InventoryHandler {
	
	//private static Main main = Main.getInstance();
	private static Integer[] invslots = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 18, 19, 20, 21, 22};
	private static Integer[] unallowed = {14, 15, 16, 17, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35};
	private static List<Material> misc = null;
	
	/**
	 * Get the allowed materials for a specific slot
	 * @param slot - The slot to get the allowed materials for
	 * @return A list of materials that are allowed in that slot
	 */
	public static final List<Material> getAllowedMaterialsForSlot(int slot){
		List<Material> mats = new ArrayList<Material>();
		if(slot == 0 || (slot >= 9 && slot <= 13)){
			for(Weapon w : Weapon.list()){
				mats.add(w.getItemStack(false).getType());
			}
		}else if(slot >= 18 && slot <= 22){
			for(Ammo a : Ammo.values()){
				mats.add(a.getItemStack(false).getType());
			}
		}else if(slot >= 1 && slot <= 6){
			mats = getMiscAllowedMaterials();
		}else if(slot == Slot.CAR.getSlot()){
			mats.add(Material.MINECART);
		}else if(slot == Slot.PHONE.getSlot()){
			mats.add(Material.PAPER);
		}else if(slot == Slot.GPS.getSlot()){
			mats.add(Material.COMPASS);
		}
		return mats;
	}
	
	/**
	 * Get miscellaneous materials that are allowed
	 * @return A list of miscellaneous materials that are allowed
	 */
	public static final List<Material> getMiscAllowedMaterials(){
		if(misc == null){
			misc = new ArrayList<Material>();
			misc.add(Material.SUGAR);
			misc.add(Material.COOKED_BEEF);
			misc.add(Material.COOKIE);
			misc.add(Material.BAKED_POTATO);
			misc.add(Material.GRILLED_PORK);
			misc.add(Material.POTION);
			misc.add(Material.TRIPWIRE_HOOK);
			misc.add(Material.PAPER);
			misc.add(Material.FERMENTED_SPIDER_EYE);
		}
		return misc;
	}
	
	/**
	 * Get a list of open slots the player has
	 * @param player - The player to get the open slots of
	 * @return A list of open slots the player has
	 */
	public static final List<Integer> getOpenSlots(Player player){
		List<Integer> slots = new ArrayList<Integer>();
		for(int i : invslots){
			if(player.getInventory().getItem(i) == null){
				slots.add(i);
			}
		}
		return slots;
	}
	
	/**
	 * Get a list of open slots the player has
	 * @param player - The player to get the open slots of
	 * @return A list of open slots the player has
	 */
	public static final List<Integer> getSlotsWithMaterial(Player player, Material mat){
		List<Integer> slots = new ArrayList<Integer>();
		for(int i : invslots){
			if(player.getInventory().getItem(i) != null){
				if(player.getInventory().getItem(i).getType() == mat){
					slots.add(i);
				}
			}
		}
		return slots;
	}
	
	/**
	 * Add the given itemstack to the appropriate slot for the player
	 * @param player - The player to add the itemstack to
	 * @param item - The itemstack to give to the player
	 * @return True or false depending on if the item was successfully added or not
	 */
	@SuppressWarnings("deprecation")
	public static final boolean addItemToAppropriateSlot(Player player, ItemStack item){
		boolean alreadyHad = false;
		boolean allow = false;
		boolean change = true;
		int slot = 0;
		if(InventoryHandler.getSlotsWithMaterial(player, item.getType()).size() > 0){
			alreadyHad = false;
			boolean found = false;
			for(int i : InventoryHandler.getSlotsWithMaterial(player, item.getType())){
				ItemStack it = player.getInventory().getItem(i);
				if((it.getAmount() + item.getAmount()) <= getItemMaxStackSize(player.getInventory().getItem(i)) && it.getData().getData() == item.getData().getData()){
					allow = true;
					found = true;
					change = false;
					alreadyHad = true;
					slot = i;
					break;
				}
			}
			if(found == false){
				change = true;
			}
		}
		if(change == true){
			for(int i : InventoryHandler.getOpenSlots(player)){
				if(InventoryHandler.getAllowedMaterialsForSlot(i).contains(item.getType())){
					allow = true;
					slot = i;
					break;
				}
			}
		}
		if(item.getType() == Material.MINECART){
			allow = true;
			slot = Slot.CAR.getSlot();
		}
		if(allow == true){
			if(alreadyHad == false){
				player.getInventory().setItem(slot, item);
			}else{
				player.getInventory().getItem(slot).setAmount(player.getInventory().getItem(slot).getAmount() + item.getAmount());
			}
		}
		return allow;
	}
	
	/**
	 * Convert a raw slot number into a regular slot number
	 * @param rawSlot - The raw slot number to convert
	 * @return The convert slot number
	 */
	public static final int convertRawSlot(int rawSlot){
		int regSlot = 0;
		int slot = rawSlot - 27;
		if(slot >= 0 && slot <= 26){
			regSlot = slot + 9;
		}else if(slot >= 27 && slot <= 35){
			regSlot = slot - 27;
		}
		return regSlot;
	}
	
	/**
	 * Remove items in the player's inventory that are in a slot they shouldn't be
	 * @param p - The player to remove the illegal items from
	 */
	public static final boolean removeIllegalItems(Player p){
		boolean removed = false;
		for(int x : unallowed){
			if(p.getInventory().getItem(x) != null){
				p.getInventory().setItem(x, null);
				removed = true;
			}
		}
		return removed;
	}
	
	/**
	 * Get the max stack size of an item
	 * @param item - The item to get the max stack size of
	 * @return The max stack size of the item
	 */
	@SuppressWarnings("deprecation")
	public static final int getItemMaxStackSize(ItemStack item){
		int max = item.getMaxStackSize();
		if(item.hasItemMeta() == true){
			for(Weapon w : Weapon.list()){
				if(item.getItemMeta().getDisplayName().contains(w.getName()) && w.getItemStack().getType() == item.getType() && w.getItemStack().getData().getData() == item.getData().getData()){
					if(w instanceof MeleeWeapon){
						max = 1;
						break;
					}
				}
			}
		}
		if(item.getType() == Material.LEVER){
			max = 16;
		}else if(item.getType() == Material.AIR){
			max = 64;
		}else if(item.getType() == Material.PAPER){
			max = 1;
		}else if(item.getType() == Material.FERMENTED_SPIDER_EYE){
			max = 1;
		}
		return max;
	}
}
