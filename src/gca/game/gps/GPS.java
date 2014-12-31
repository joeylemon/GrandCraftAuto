package gca.game.gps;

import gca.core.GPlayer;
import gca.game.apartment.Apartment;
import gca.game.missions.objectives.ReachDestinationObjective;
import gca.utils.Utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class GPS {
	
	/**
	 * Get the GPS inventory
	 * @return The GPS inventory
	 */
	public static final Inventory getInventory(Player player){
		GPlayer gplayer = new GPlayer(player);
		Inventory inv = Bukkit.createInventory(null, 18, ChatColor.DARK_GRAY + "GPS");
		inv.setItem(2, Utils.renameItem(new ItemStack(Material.IRON_HOE), ChatColor.DARK_RED + "Ammu" + ChatColor.WHITE + "Nation"));
		inv.setItem(3, Utils.renameItem(new ItemStack(Material.COOKED_BEEF), ChatColor.DARK_AQUA + "Burgershot"));
		inv.setItem(4, Utils.renameItem(new ItemStack(Material.MINECART), ChatColor.GRAY + "Car Dealership"));
		inv.setItem(5, Utils.renameItem(new ItemStack(Material.SKULL_ITEM), ChatColor.DARK_GRAY + "Black Market"));
		inv.setItem(6, Utils.renameItem(new ItemStack(Material.GOLD_NUGGET), ChatColor.GOLD + "Bank"));
		if(gplayer.hasApartment() == false){
			inv.setItem(13, Utils.renameItem(new ItemStack(Material.PAPER), ChatColor.DARK_RED + "Cancel Route"));
		}
		if(gplayer.hasMission() == true && gplayer.getObjective() instanceof ReachDestinationObjective){
			if(gplayer.hasApartment() == true){
				inv.setItem(12, Utils.renameItem(new ItemStack(Material.WOOD_DOOR), ChatColor.GOLD + "Apartments"));
				inv.setItem(13, Utils.renameItem(new ItemStack(Material.COMPASS), ChatColor.GOLD + "Mission Objective"));
				inv.setItem(14, Utils.renameItem(new ItemStack(Material.PAPER), ChatColor.DARK_RED + "Cancel Route"));
			}else{
				inv.setItem(12, Utils.renameItem(new ItemStack(Material.COMPASS), ChatColor.GOLD + "Mission Objective"));
				inv.setItem(13, null);
				inv.setItem(14, Utils.renameItem(new ItemStack(Material.PAPER), ChatColor.DARK_RED + "Cancel Route"));
			}
		}
		return inv;
	}
	
	/**
	 * Get a player's apartment GPS inventory
	 * @param player - The player to get the inventory of
	 * @return The apartment GPS inventory for the player
	 */
	public static final Inventory getApartmentInventory(Player player){
		GPlayer gplayer = new GPlayer(player);
		List<Apartment> apartments = gplayer.getApartments();
		Inventory inv = Bukkit.createInventory(null, Utils.getRoundedInventorySize(apartments.size()), ChatColor.DARK_GRAY + "GPS - Apartments");
		int slot = 0;
		for(Apartment a : apartments){
			inv.setItem(slot, Utils.renameItem(new ItemStack(Material.PAPER), ChatColor.GOLD + a.getName()));
			slot++;
		}
		return inv;
	}
	
	/**
	 * Get the compass itemstack
	 * @return The compass itemstack
	 */
	public static final ItemStack getCompass(){
		return Utils.renameItem(new ItemStack(Material.COMPASS), ChatColor.YELLOW + "GPS");
	}
	
	/**
	 * Get the nearest NPC of the store inventory
	 * @param store - The store type to get the NPC of
	 * @return The location of the NPC
	 */
	public static final Location getNearest(Player player, Destination dest){
		Location nearest = null;
		for(Entity e : Utils.getGCAWorld().getEntities()){
			if(e instanceof Player){
				Player p = (Player) e;
				if(ChatColor.stripColor(p.getName()).toLowerCase().contains(dest.toString().toLowerCase().replaceAll("_", " "))){
					boolean replace = true;
					if(nearest != null){
						if(nearest.distance(player.getLocation()) < e.getLocation().distance(player.getLocation())){
							replace = false;
						}
					}
					if(replace == true){
						nearest = e.getLocation();
					}
				}
			}
		}
		return nearest;
	}
}
