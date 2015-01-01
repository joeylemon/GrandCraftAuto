package com.grandcraftauto.game.inventories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.grandcraftauto.game.cars.Car;
import com.grandcraftauto.game.player.GPlayer;
import com.grandcraftauto.game.weapons.Ammo;
import com.grandcraftauto.game.weapons.Grenade;
import com.grandcraftauto.game.weapons.Gun;
import com.grandcraftauto.game.weapons.MeleeWeapon;
import com.grandcraftauto.game.weapons.Shotgun;
import com.grandcraftauto.game.weapons.Weapon;
import com.grandcraftauto.utils.TextUtils;
import com.grandcraftauto.utils.Utils;

public class Store {
	
	static List<Store> stores = new ArrayList<Store>();
	private static String gray = ChatColor.GRAY + "";
	private static String gold = ChatColor.GOLD + "";
	
	private String name;
	private List<Inventory> inv;
	
	/**
	 * Create a store
	 * @param storeName - The name of the store
	 * @param storeVendor - The store vendor's name
	 * @param storeInv - The inventory of the store
	 */
	public Store(String storeName, List<Inventory> storeInv){
		name = storeName;
		inv = storeInv;
	}
	
	/**
	 * Get the name of the store
	 * @return The name of the store
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Get the store vendor's name
	 * @return The store vendor's name
	 */
	public String getVendor(){
		return name;
	}
	
	/**
	 * Get a page of the store
	 * @return A page of the store
	 */
	public Inventory getPage(int page){
		return inv.get(page - 1);
	}
	
	/**
	 * Get the total pages of the store
	 * @return The total pages of the store
	 */
	public int getTotalPages(){
		return inv.size();
	}
	
	@SuppressWarnings("deprecation")
	public void purchaseItem(Player player, ItemStack item){
		GPlayer gplayer = new GPlayer(player);
		for(int x = 1; x <= this.getTotalPages(); x++){
			if(this.getPage(x).contains(item.getType())){
				Inventory inv = this.getPage(x);
				for(int i = 0; i <= (inv.getContents().length - 1); i++){
					if(inv.getItem(i) != null){
						if(inv.getItem(i).getItemMeta() != null){
							if(inv.getItem(i).getItemMeta().hasDisplayName() == true){
								if(inv.getItem(i).getItemMeta().getDisplayName().equalsIgnoreCase(item.getItemMeta().getDisplayName()) && inv.getItem(i).getAmount() == item.getAmount()){
									boolean enufLevel = true;
									boolean enufMoney = true;
									int price = 0;
									if(inv.getItem(i).getItemMeta().hasLore() == true){
										for(String s : inv.getItem(i).getItemMeta().getLore()){
											String lore = ChatColor.stripColor(s);
											if(lore.contains("Price")){
												String split[] = lore.split("\\$");
												price = Integer.parseInt(ChatColor.stripColor(split[1]));
												if(gplayer.getWalletBalance() >= price){
													enufMoney = true;
												}else{
													enufMoney = false;
												}
											}
											if(lore.contains("Level")){
												String split[] = lore.split(":");
												int level = Integer.parseInt(ChatColor.stripColor(split[1].replaceAll(" ", "")));
												if(gplayer.getLevel() >= level){
													enufLevel = true;
												}else{
													enufLevel = false;
												}
											}
										}
									}
									boolean carCanBuy = true;
									Car car = null;
									for(Car c : Car.values()){
										if(ChatColor.stripColor(inv.getItem(i).getItemMeta().getDisplayName()).contains(c.getName())){
											car = c;
											break;
										}
									}
									if(car != null){
										if(gplayer.ownsCar(car) == true){
											carCanBuy = false;
										}
									}
									boolean weaponCanBuy = true;
									Weapon wep = null;
									for(Weapon w : Weapon.list()){
										if(ChatColor.stripColor(inv.getItem(i).getItemMeta().getDisplayName()).contains(w.getName())){
											wep = w;
											break;
										}
									}
									if(wep != null){
										if(wep.getRequiredRank() != null){
											if(gplayer.getRankLadder() < wep.getRequiredRank().getLadder()){
												weaponCanBuy = false;
											}
										}
									}
									if(enufMoney == true && enufLevel == true && carCanBuy == true && weaponCanBuy == true){
										if(car != null){
											player.getInventory().setItem(Slot.CAR.getSlot(), null);
										}
										List<String> lore = new ArrayList<String>();
										for(String s : inv.getItem(i).getItemMeta().getLore()){
											String name = ChatColor.stripColor(s);
											if(!name.contains("Price") && !name.contains("Level") && !name.contains("required")){
												lore.add(s);
											}
										}
										ItemStack it = Utils.renameItem(new ItemStack(inv.getItem(i).getType(), inv.getItem(i).getAmount(), inv.getItem(i).getData().getData()), 
												inv.getItem(i).getItemMeta().getDisplayName(), lore);
										if(InventoryHandler.addItemToAppropriateSlot(player, it) == true){
											gplayer.setWalletBalance(gplayer.getWalletBalance() - price);
											String name = ChatColor.stripColor(it.getItemMeta().getDisplayName());
											gplayer.sendMessage("You have purchased a " + gold + name + gray + "!");
											gplayer.refreshScoreboard();
											if(car != null){
												gplayer.giveCar(car);
											}
										}else{
											gplayer.sendError("You do not have any open slots to buy this!");
										}
									}else{
										if(enufMoney == false){
											gplayer.sendError("You do not have enough money to buy this!");
										}else if(enufLevel == false){
											gplayer.sendError("You do not have a high enough level for this!");
										}else if(carCanBuy == false){
											gplayer.sendError("You already own this car!");
										}else if(weaponCanBuy == false){
											gplayer.sendError("You do not own the required rank for this weapon!");
										}
									}
									break;
								}
							}
						}
					}
				}
				break;
			}
		}
	}
	
	public static final Inventory getInventory(StoreInventory type, int page){
		String arrow = gold + TextUtils.getArrow() + " ";
		Inventory inv = null;
		if(type == StoreInventory.AMMUNATION){
			String ammunation = ChatColor.DARK_RED + "Ammu" + ChatColor.GRAY + "Nation";
			if(page == 1){
				inv = Bukkit.createInventory(null, 9, ammunation + ChatColor.DARK_GRAY + " - Categories");
				inv.setItem(0, Utils.renameItem(new ItemStack(Material.PAPER), ChatColor.YELLOW + "Melee Weapons"));
				inv.setItem(2, Utils.renameItem(new ItemStack(Material.PAPER), ChatColor.YELLOW + "Guns"));
				inv.setItem(4, Utils.renameItem(new ItemStack(Material.PAPER), ChatColor.YELLOW + "Shotguns"));
				inv.setItem(6, Utils.renameItem(new ItemStack(Material.PAPER), ChatColor.YELLOW + "Ammunition"));
				inv.setItem(8, Utils.renameItem(new ItemStack(Material.PAPER), ChatColor.YELLOW + "Explosives"));
			}else if(page == 2){
				List<Gun> weapons = Weapon.gunList();
				int size = Utils.getRoundedInventorySize((weapons.size() + 1));
				
				inv = Bukkit.createInventory(null, size, ammunation + ChatColor.DARK_GRAY + " - Guns");
				
				inv.setItem((size - 1), Utils.renameItem(new ItemStack(Material.PAPER), ChatColor.YELLOW + TextUtils.getBackwardsArrow() + " Go Back"));
				
				int slot = 0;
				for(Weapon w : weapons){
					inv.setItem(slot, w.getItemStack(true));
					slot++;
				}
			}else if(page == 3){
				List<MeleeWeapon> weapons = Weapon.meleeList();
				int size = Utils.getRoundedInventorySize((weapons.size() + 1));
				
				inv = Bukkit.createInventory(null, size, ammunation + ChatColor.DARK_GRAY + " - Melee");
				
				inv.setItem((size - 1), Utils.renameItem(new ItemStack(Material.PAPER), ChatColor.YELLOW + TextUtils.getBackwardsArrow() + " Go Back"));
				
				int slot = 0;
				for(Weapon w : weapons){
					inv.setItem(slot, w.getItemStack(true));
					slot++;
				}
			}else if(page == 4){
				Ammo[] ammo = Ammo.values();
				int size = Utils.getRoundedInventorySize((ammo.length + 1));
				
				inv = Bukkit.createInventory(null, size, ammunation + ChatColor.DARK_GRAY + " - Ammo");
				
				inv.setItem((size - 1), Utils.renameItem(new ItemStack(Material.PAPER), ChatColor.YELLOW + TextUtils.getBackwardsArrow() + " Go Back"));
				
				int slot = 0;
				for(Ammo a : ammo){
					inv.setItem(slot, a.getItemStack(true));
					slot++;
				}
			}else if(page == 5){
				List<Shotgun> weapons = Weapon.shotgunList();
				int size = Utils.getRoundedInventorySize((weapons.size() + 1));
				
				inv = Bukkit.createInventory(null, size, ammunation + ChatColor.DARK_GRAY + " - Shotguns");
				
				inv.setItem((size - 1), Utils.renameItem(new ItemStack(Material.PAPER), ChatColor.YELLOW + TextUtils.getBackwardsArrow() + " Go Back"));
				
				int slot = 0;
				for(Weapon w : weapons){
					inv.setItem(slot, w.getItemStack(true));
					slot++;
				}
			}else if(page == 6){
				List<Grenade> weapons = Weapon.explosiveList();
				int size = Utils.getRoundedInventorySize((weapons.size() + 1));
				
				inv = Bukkit.createInventory(null, size, ammunation + ChatColor.DARK_GRAY + " - Explosives");
				
				inv.setItem((size - 1), Utils.renameItem(new ItemStack(Material.PAPER), ChatColor.YELLOW + TextUtils.getBackwardsArrow() + " Go Back"));
				
				int slot = 0;
				for(Weapon w : weapons){
					inv.setItem(slot, w.getItemStack(true));
					slot++;
				}
			}
		}else if(type == StoreInventory.CAR_DEALERSHIP){
			if(page == 1){
				Car[] cars = Car.values();
				int size = Utils.getRoundedInventorySize(cars.length);
				inv = Bukkit.createInventory(null, size, ChatColor.DARK_GRAY + "Car Dealership");
				for(Car c : cars){
					inv.addItem(c.getItemStack(true));
				}
			}
		}else if(type == StoreInventory.BURGERSHOT){
			if(page == 1){
				inv = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "Burgershot");
				int amount = 1;
				for(int x = 1; x <= 7; x++){
					inv.setItem(x, Utils.renameItem(new ItemStack(Material.COOKED_BEEF, amount), ChatColor.DARK_AQUA + "Burger", Arrays.asList(arrow + ChatColor.YELLOW + "Price: " + gray + "$" + 
							(amount * 5))));
					amount = amount * 2;
				}
			}
		}else if(type == StoreInventory.BLACK_MARKET){
			if(page == 1){
				inv = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "Black Market");
				inv.setItem(0, Utils.renameItem(new ItemStack(Material.TRIPWIRE_HOOK), ChatColor.DARK_GREEN + "Lockpick", Arrays.asList(
						gray + ChatColor.ITALIC + "Gain access to locked vehicles... illegally.",
						arrow + ChatColor.YELLOW + "Price: " + gray + "$17")));
				inv.setItem(1, Utils.renameItem(new ItemStack(Material.PAPER), ChatColor.DARK_GREEN + "Chloroform Rag", Arrays.asList(
						gray + ChatColor.ITALIC + "Place firmly against victim's mouth and watch them sleep.",
						arrow + ChatColor.YELLOW + "Price: " + gray + "$100")));
			}
		}else if(type == StoreInventory.GAS_STATION){
			if(page == 1){
				inv = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "Gas Station");
				inv.setItem(0, Utils.renameItem(new ItemStack(Material.COOKED_BEEF, 1), ChatColor.DARK_AQUA + "Burger", Arrays.asList(arrow + ChatColor.YELLOW + "Price: " + gray + "$5")));
				inv.setItem(1, Utils.renameItem(new ItemStack(Material.COOKIE, 1), ChatColor.DARK_AQUA + "Cookie", Arrays.asList(arrow + ChatColor.YELLOW + "Price: " + gray + "$1")));
				inv.setItem(2, Utils.renameItem(new ItemStack(Material.BAKED_POTATO, 1), ChatColor.DARK_AQUA + "Hot Dog", Arrays.asList(arrow + ChatColor.YELLOW + "Price: " + gray + "$3")));
				inv.setItem(3, Utils.renameItem(new ItemStack(Material.GRILLED_PORK, 1), ChatColor.DARK_AQUA + "Pizza", Arrays.asList(arrow + ChatColor.YELLOW + "Price: " + gray + "$6")));
				inv.setItem(5, Utils.renameItem(new ItemStack(Material.POTION, 1, (byte) 0), ChatColor.DARK_AQUA + "Beer", Arrays.asList(arrow + ChatColor.YELLOW + "Price: " + gray + "$8")));
				inv.setItem(6, Utils.renameItem(new ItemStack(Material.POTION, 1, (byte) 0), ChatColor.DARK_AQUA + "Soda", Arrays.asList(arrow + ChatColor.YELLOW + "Price: " + gray + "$4")));
				inv.setItem(7, Utils.renameItem(new ItemStack(Material.POTION, 1, (byte) 0), ChatColor.DARK_AQUA + "Wine", Arrays.asList(arrow + ChatColor.YELLOW + "Price: " + gray + "$5")));
				inv.setItem(8, Utils.renameItem(new ItemStack(Material.POTION, 1, (byte) 0), ChatColor.DARK_AQUA + "Whiskey", Arrays.asList(arrow + ChatColor.YELLOW + "Price: " + gray + "$11")));
			}
		}
		return inv;
	}
	
	/**
	 * Initialize the stores list for usage
	 */
	public static final void initializeList(){
		stores.add(new Store("AmmuNation", Arrays.asList(getInventory(StoreInventory.AMMUNATION, 1), getInventory(StoreInventory.AMMUNATION, 2), getInventory(StoreInventory.AMMUNATION, 3), 
				getInventory(StoreInventory.AMMUNATION, 4), getInventory(StoreInventory.AMMUNATION, 5), getInventory(StoreInventory.AMMUNATION, 6))));
		stores.add(new Store("Car Dealership", Arrays.asList(getInventory(StoreInventory.CAR_DEALERSHIP, 1))));
		stores.add(new Store("Burgershot", Arrays.asList(getInventory(StoreInventory.BURGERSHOT, 1))));
		stores.add(new Store("Black Market", Arrays.asList(getInventory(StoreInventory.BLACK_MARKET, 1))));
		stores.add(new Store("Gas Station", Arrays.asList(getInventory(StoreInventory.GAS_STATION, 1))));
	}
	
	/**
	 * Get the missions list
	 * @return The missions list
	 */
	public static final List<Store> list(){
		return stores;
	}
}
