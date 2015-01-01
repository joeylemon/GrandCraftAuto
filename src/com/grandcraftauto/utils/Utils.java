package com.grandcraftauto.utils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import net.minecraft.server.v1_8_R1.EntityInsentient;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.Vector;

import com.gmail.filoghost.holograms.api.Hologram;
import com.gmail.filoghost.holograms.api.HolographicDisplaysAPI;
import com.gmail.filoghost.holograms.object.HologramBase;
import com.gmail.filoghost.holograms.utils.VisibilityManager;
import com.grandcraftauto.core.Main;
import com.grandcraftauto.game.Skill;
import com.grandcraftauto.game.VillagerType;
import com.grandcraftauto.game.garage.Garage;
import com.grandcraftauto.game.gps.PathEffect;
import com.grandcraftauto.game.pathfinding.Tile;
import com.grandcraftauto.game.player.GPlayer;
import com.grandcraftauto.game.weapons.Gun;
import com.grandcraftauto.game.weapons.Shotgun;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;

public final class Utils {
	
	private static Main main = Main.getInstance();
	private static World world = null;
	private static final String gray = ChatColor.GRAY + "";
	private static final String gold = ChatColor.GOLD + "";
	private static final Random rand = new Random();
	private static final String[] blockedcmds = {"/me"};
	private static final int maxY = 80;
	private static final int minY = 67;
	
	
	
	/**
	 * Get the WorldGuardPlugin
	 * @return - The WorldGuardPlugin
	 */
	public static final WorldGuardPlugin getWorldGuard() {
	    Plugin plugin = main.getServer().getPluginManager().getPlugin("WorldGuard");
	    if(plugin == null || !(plugin instanceof WorldGuardPlugin)) {
	    	return null;
	    }
	    return (WorldGuardPlugin) plugin;
	}
	
	/**
	 * Check if the location is a garage
	 * @param loc - The location to check for a garage
	 * @return True or false depending on if the location is a garage or not
	 */
	@SuppressWarnings("deprecation")
	public static final boolean isGarage(Location loc){
		boolean is = false;
		ApplicableRegionSet set = getWorldGuard().getRegionManager(loc.getWorld()).getApplicableRegions(loc);
        if(set.allows(DefaultFlag.PISTONS) == false){
        	is = true;
        }
        return is;
	}
	
	/**
	 * Get the entity hider instance
	 * @return The entity hider instance
	 */
	public static final EntityHider getEntityHider(){
		return Main.entityHider;
	}
	
	/**
	 * Get the scoreboard manager instance
	 * @return The scoreboard manager instance
	 */
	public static final ScoreboardManager getScoreboardManager(){
		return Main.manager;
	}
	
	/**
	 * Check if the server is whitelisted
	 * @return True if the server is whitelisted, false if not
	 */
	public static final boolean isServerWhitelisted(){
		boolean whitelisted = false;
		if(main.whitelist == -1){
			boolean conf = main.getConfig().getBoolean("whitelisted");
			if(conf == true){
				main.whitelist = 1;
			}else if(conf == false){
				main.whitelist = 0;
			}
			whitelisted = conf;
		}else{
			if(main.whitelist == 1){
				whitelisted = true;
			}
		}
		return whitelisted;
	}
	
	/**
	 * Set the server as whitelisted
	 * @param whitelisted - True to set the server as whitelisted, false to not
	 */
	public static final void setServerWhitelisted(boolean whitelisted){
		main.getConfig().set("whitelisted", whitelisted);
		main.saveConfig();
		if(whitelisted == true){
			main.whitelist = 1;
		}else if(whitelisted == false){
			main.whitelist = 0;
		}
	}
	
	/**
	 * Check if a player is whitelisted
	 * @param player - The player to check
	 * @return True if the player is whitelisted, false if not
	 */
	public static final boolean isPlayerWhitelisted(String player){
		boolean whitelisted = false;
		List<String> conf = main.getConfig().getStringList("whitelistedPlayers");
		if(conf != null){
			if(conf.contains(player) == true){
				whitelisted = true;
			}
		}
		return whitelisted;
	}
	
	/**
	 * Set a player as whitelisted
	 * @param player - The player to whitelist
	 * @param whitelisted - True to whitelist the player, false to un-whitelist
	 */
	public static final void setPlayerWhitelisted(String player, boolean whitelisted){
		List<String> conf = main.getConfig().getStringList("whitelistedPlayers");
		if(whitelisted == true){
			if(conf.contains(player) == false){
				conf.add(player);
			}
		}else if(whitelisted == false){
			if(conf.contains(player) == true){
				conf.remove(player);
			}
		}
		main.getConfig().set("whitelistedPlayers", conf);
		main.saveConfig();
	}
	
	/**
	 * Check if a string contains symbols
	 * @param name - The string to check
	 * @return True or false depending on if the string has symbols or not
	 */
	public static final boolean containsSymbols(String name){
		boolean symbols = false;
		if(name.contains("!") || name.contains("@") || name.contains("#") || name.contains("$") || name.contains("%") || name.contains("^") || name.contains("*") ||
				name.contains("(") || name.contains(")") || name.contains("+") || name.contains("-") || name.contains("?") || name.contains("<") ||
				name.contains(">") || name.contains("|") || name.contains("[") || name.contains("]") || name.contains("{") || name.contains("}") ||
				name.contains("/") || name.contains(":") || name.contains(";") || name.contains("\"") || name.contains("\\") || name.contains("'") || name.contains("&")){
			symbols = true;
		}
		return symbols;
	}
	
	/**
	 * Check if a string contains curse words
	 * @param name - The string to check
	 * @return True or false depending on if the string has curse words or not
	 */
	public static final boolean containsCurses(String name){
		name = name.toLowerCase();
		boolean curses = false;
		if(name.contains("bitch") || name.contains("fuck") || name.contains("ass") || name.contains("nig") || name.contains("cunt") || name.contains("whore") || name.contains("puss") ||
				 name.contains("dam") || name.contains("fuk") || name.contains("shit") || name.contains("cock") || name.contains("dick") || name.contains("dik") || name.contains("cunt") ||
				 name.contains("vagina") || name.contains("penis") || name.contains("pen15") || name.contains("tit") || name.contains("boob") || name.contains("anus") || name.contains("bastard") || 
				 (name.contains("blow") && name.contains("job")) || name.contains("boner") || name.contains("clit") || name.contains("cum") || name.contains("fag") || name.contains("gay") ||
				 name.contains("homo") || name.contains("fuk") || name.contains("queer") || name.contains("queef") || name.contains("slut") || name.contains("testicle")){
			curses = true;
		}
		return curses;
	}
	
	/**
	 * Get the maximum amount of consumption a player can have
	 * @return The maximum amount of consumption a player can have
	 */
	public static final int getMaxConsumptionLevel(){
		return 20;
	}
	
	/**
	 * Get a random statement when someone is sick
	 * @return A random sick statement
	 */
	public static final String getRandomSickStatement(){
		String statement = "";
		int random = randInt(1, 3);
		if(random == 1){
			statement = "You're not feeling too well...";
		}else if(random == 2){
			statement = "Your stomach begins to violently growl.";
		}else if(random == 3){
			statement = "You feel like you're about to puke.";
		}
		return statement;
	}
	
	/**
	 * Get the chat color from a wool data
	 * @param wool - The data of the wool
	 * @return The chat color of the wool
	 */
	public static final ChatColor getChatColorFromWool(int wool){
		ChatColor color = ChatColor.WHITE;
		if(wool == 1){
			color = ChatColor.GOLD;
		}else if(wool == 2){
			color = ChatColor.LIGHT_PURPLE;
		}else if(wool == 3){
			color = ChatColor.BLUE;
		}else if(wool == 4){
			color = ChatColor.YELLOW;
		}else if(wool == 5){
			color = ChatColor.GREEN;
		}else if(wool == 6){
			color = ChatColor.LIGHT_PURPLE;
		}else if(wool == 7){
			color = ChatColor.DARK_GRAY;
		}else if(wool == 8){
			color = ChatColor.GRAY;
		}else if(wool == 9){
			color = ChatColor.DARK_AQUA;
		}else if(wool == 10){
			color = ChatColor.DARK_PURPLE;
		}else if(wool == 11){
			color = ChatColor.DARK_BLUE;
		}else if(wool == 12){
			color = ChatColor.DARK_GRAY;
		}else if(wool == 13){
			color = ChatColor.DARK_GREEN;
		}else if(wool == 14){
			color = ChatColor.DARK_RED;
		}else if(wool == 15){
			color = ChatColor.DARK_GRAY;
		}
		return color;
	}
	
	/**
	 * Get the color from a wool data
	 * @param wool - The data of the wool
	 * @return The color of the wool
	 */
	public static final Color getColorFromWool(int wool){
		Color color = Color.WHITE;
		if(wool == 1){
			color = Color.ORANGE;
		}else if(wool == 2){
			color = Color.FUCHSIA;
		}else if(wool == 3){
			color = Color.fromRGB(1, 169, 219);
		}else if(wool == 4){
			color = Color.YELLOW;
		}else if(wool == 5){
			color = Color.LIME;
		}else if(wool == 6){
			color = Color.fromRGB(247, 129, 243);
		}else if(wool == 7){
			color = Color.GRAY;
		}else if(wool == 8){
			color = Color.SILVER;
		}else if(wool == 9){
			color = Color.fromRGB(8, 106, 135);
		}else if(wool == 10){
			color = Color.PURPLE;
		}else if(wool == 11){
			color = Color.NAVY;
		}else if(wool == 12){
			color = Color.fromRGB(112, 60, 4);
		}else if(wool == 13){
			color = Color.fromRGB(11, 97, 33);
		}else if(wool == 14){
			color = Color.fromRGB(143, 4, 4);
		}else if(wool == 15){
			color = Color.BLACK;
		}
		return color;
	}
	
	/**
	 * 
	 * @param wool
	 * @return
	 */
	public static final String getColorNameFromWool(int wool){
		String color = "White";
		if(wool == 1){
			color = "Orange";
		}else if(wool == 2){
			color = "Magenta";
		}else if(wool == 3){
			color = "Light Blue";
		}else if(wool == 4){
			color = "Yellow";
		}else if(wool == 5){
			color = "Lime";
		}else if(wool == 6){
			color = "Pink";
		}else if(wool == 7){
			color = "Gray";
		}else if(wool == 8){
			color = "Light Gray";
		}else if(wool == 9){
			color = "Cyan";
		}else if(wool == 10){
			color = "Purple";
		}else if(wool == 11){
			color = "Blue";
		}else if(wool == 12){
			color = "Brown";
		}else if(wool == 13){
			color = "Green";
		}else if(wool == 14){
			color = "Red";
		}else if(wool == 15){
			color = "Black";
		}
		color = getChatColorFromWool(wool) + color;
		return color;
	}
	
	/**
	 * Round a number to two decimal points
	 * @param d - The number to round
	 * @param decimalPlaces - The amount of decimal places to round to
	 * @return The rounded number
	 */
	public static final double round(double d, int decimalPlaces){
        String format = "#.";
        for(int x = 1; x <= decimalPlaces; x++){
        	format = format + "#";
        }
        DecimalFormat form = new DecimalFormat(format);
        return Double.valueOf(form.format(d));
    }
	
	/**
	 * Round a number up
	 */
	public static final long roundUp(long n, long m) {
	    return n >= 0 ? ((n + m - 1) / m) * m : (n / m) * m;
	}
	
	/**
	 * Get a random number in the range
	 * @param min - The lowest possible number
	 * @param max - The highest possible number
	 * @return A random number between the minimum and maximum values
	 */
	public static final int randInt(int min, int max){
	    return rand.nextInt((max - min) + 1) + min;
	}
	
	/**
	 * Check if a string is also an integer
	 * @param isIt - The string to check
	 * @return True or false depending on if the string is an integer or not
	 */
	public static final boolean isInteger(String isIt){
		try{
			Integer.parseInt(isIt);
			return true;
		}catch (Exception ex){
			return false;
		}
	}
	
	/**
	 * Check if a string is also a double
	 * @param isIt - The string to check
	 * @return True or false depending on if the string is a double or not
	 */
	public static final boolean isDouble(String isIt){
		try{
			Double.parseDouble(isIt);
			return true;
		}catch (Exception ex){
			return false;
		}
	}
	
	/**
	 * Get the percent of two integers
	 * @param n - The first integer
	 * @param v - The second integer
	 * @return The percent out of 100
	 */
	public static final double getPercent(double n, double v){
		return round(((n * 100) / v), 1);
	}
	
	/**
	 * Check if a player is online
	 * @param player - The name of the player
	 * @return True or false depending on if the player is online or not
	 */
	@SuppressWarnings("deprecation")
	public static final boolean isOnline(String player){
		if(Bukkit.getPlayer(player) != null && Bukkit.getPlayer(player).isOnline() == true){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Round a number up to be an inventory size
	 * @param number - The number to round up
	 * @return The inventory size
	 */
	public static final int getRoundedInventorySize(int number){
		int size = 9;
		if(number > 9 && number <= 18){
			size = 18;
		}else if(number > 18 && number <= 27){
			size = 27;
		}else if(number > 27 && number <= 36){
			size = 36;
		}else if(number > 36 && number <= 45){
			size = 45;
		}else if(number > 45 && number <= 54){
			size = 54;
		}
		return size;
	}
	
	/**
	 * Convert seconds into a days, hours, minutes, and seconds format
	 * @param seconds - The seconds to convert
	 * @return The converted value in a String
	 */
	public static final String convertSeconds(int seconds){
		String converted = "";
		
		int d = (int)TimeUnit.SECONDS.toDays(seconds);        
		long h = TimeUnit.SECONDS.toHours(seconds) - (d *24);
		long m = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
		long s = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);
		if(d > 0){
			converted = d + "d " + h + "h " + m + "m " + s + "s";
		}else if(h > 0){
			converted = h + "h " + m + "m " + s + "s";
		}else if(m > 0){
			converted = m + "m " + s + "s";
		}else if(s > 0){
			converted = s + "s";
		}
		
		return converted;
	}
	
	/**
	 * Set the color of leather armor
	 * @param armor - The armor itemstack to color
	 * @param color - The color to set the armor to
	 * @return The dyed armor itemstack
	 */
	public static final ItemStack dyeArmor(ItemStack armor, Color color){
    	LeatherArmorMeta lam = (LeatherArmorMeta) armor.getItemMeta();
    	lam.setColor(color);
    	armor.setItemMeta(lam);
    	return armor;
	}
	
	/**
	 * Set an entity's navigation
	 * @param l - The location to navigate to
	 * @param le - The entity to navigate to the location
	 */
	public static final void setNavigation(Location l, LivingEntity le){
	    ((EntityInsentient)((CraftLivingEntity) le).getHandle()).getNavigation().a(l.getX(), l.getY(), l.getZ(), 1.25F);
	}
	
	/**
	 * Get the vector from one location to another
	 * @param from - The origin location
	 * @param to - The destination of the vector
	 * @return The vector from the origin to the destination
	 */
	public static final Vector getDifferentialVector(Location from, Location to){
        return new Vector((to.getX() - from.getX()), 0, (to.getZ() - from.getZ()));
    }
	
	/**
	 * Spawn a villager at the location with the given name
	 * @param loc - The location to spawn the villager at
	 * @param name - The name of the villager to spawn
	 */
	public static final Villager spawnVillager(VillagerType type, Location loc, String name){
		Villager villager = (Villager) getGCAWorld().spawnEntity(loc, EntityType.VILLAGER);
		if(name != null){
			villager.setCustomName(name);
			villager.setCustomNameVisible(true);
		}
		villager.setProfession(type.getProfession());
		villager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2147483647, 2));
		return villager;
	}
	
	/**
	 * Spawn a random type of villager at a random location
	 * @return The villager that was spawned
	 */
	public static final Villager spawnRandomVillager(){
		Villager villager = null;
		while(villager == null){
			int x = Utils.randInt(0, 200);
			int z = Utils.randInt(0, 200);
			int y = Utils.getGCAWorld().getHighestBlockYAt(new Location(Utils.getGCAWorld(), x, 0, z));
			if(y <= maxY && y >= minY){
				Location loc = new Location(Utils.getGCAWorld(), x, y, z);
				if(loc.getBlock().getLightLevel() > 2){
					int chance = Utils.randInt(1, 7);
					if(chance == 1){
						villager = Utils.spawnCop(loc);
					}else{
						villager = Utils.spawnVillager(VillagerType.REGULAR, loc, null);
					}
				}
			}
		}
		return villager;
	}
	
	/**
	 * Spawn a cop at the location
	 * @param loc - The location to spawn the cop
	 * @return The cop entity
	 */
	public static final Villager spawnCop(Location loc){
		return spawnVillager(VillagerType.COP, loc, ChatColor.AQUA + "Cop");
	}
	
	/**
	 * Drop money at the location
	 * @param loc - The location to drop money at
	 */
	public static final Item dropMoney(Location loc){
		return dropMoney(loc, 8, 17);
	}
	
	/**
	 * Drop money at the location with the given values
	 * @param loc - The location to drop money at
	 */
	public static final Item dropMoney(Location loc, int min, int max){
		Item item = getGCAWorld().dropItem(loc, new ItemStack(Material.GOLD_NUGGET));
		item.setMetadata("value", new FixedMetadataValue(main, randInt(min, max)));
		return item;
	}
	
	/**
	 * Drop money at the location with the given value
	 * @param loc - The location to drop money at
	 */
	public static final Item dropMoney(Location loc, int amount){
		Item item = getGCAWorld().dropItem(loc, new ItemStack(Material.GOLD_NUGGET));
		item.setMetadata("value", new FixedMetadataValue(main, amount));
		return item;
	}
	
	/**
	 * Set an entity's metadata
	 * @param entity - The entity to set metadata of
	 * @param metadata - The metadata to set for the entity
	 * @param object - The value of the metadata to set
	 */
	public static final void setMetadata(Entity entity, String metadata, Object object){
		entity.setMetadata(metadata, new FixedMetadataValue(Main.getInstance(), object));
	}
	
	/**
	 * Get an entity's metadata
	 * @param entity - The entity to get the metadata of
	 * @param metadata - The metadata to get
	 * @param type - The type of object the metadata is
	 * @return The metadata object
	 */
	public static final Object getMetadata(Entity entity, String metadata, ObjectType type){
		Object obj = null;
		for(MetadataValue v : entity.getMetadata(metadata)){
			if(type == ObjectType.STRING){
				obj = v.asString();
			}else if(type == ObjectType.INT){
				obj = v.asInt();
			}else if(type == ObjectType.BOOLEAN){
				obj = v.asBoolean();
			}
			break;
		}
		return obj;
	}
	
	/**
	 * Get the cocaine item
	 * @param amount - The amount of cocaine to get
	 * @return The cocaine item
	 */
	public static final ItemStack getCocaineItem(int amount){
		return renameItem(new ItemStack(Material.SUGAR, amount), ChatColor.DARK_AQUA + "Cocaine");
	}
	
	/**
	 * Get the kidney item
	 * @return The kidney item
	 */
	public static final ItemStack getKidneyItem(){
		return Utils.renameItem(new ItemStack(Material.FERMENTED_SPIDER_EYE), ChatColor.DARK_GREEN + "Kidney", ChatColor.GRAY + "" + ChatColor.ITALIC + "A kidney harvested from a human.");
	}
	
	/**
	 * Get the kidney sell price
	 * @return The kidney sell price
	 */
	public static final int getKidneySellPrice(){
		return 400;
	}
	
	/**
	 * Get the phone inventory
	 * @return The phone inventory
	 */
	public static final Inventory getPhoneInventory(){
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "iFruit Cell Phone");
		inv.setItem(3, Utils.renameItem(new ItemStack(Material.ENCHANTED_BOOK), ChatColor.AQUA + "Skills"));
		inv.setItem(5, Utils.renameItem(new ItemStack(Material.SKULL_ITEM, 1, (short) 3), ChatColor.RED + "Bounty Placer"));
		return inv;
	}
	
	/**
	 * Get the prostitute inventory
	 * @return The prostitute inventory
	 */
	public static final Inventory getProstituteInventory(Villager villager){
		String arrow = gold + TextUtils.getArrow() + " ";
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "How long, baby?");
		inv.setMaxStackSize(villager.getEntityId());
		inv.setItem(2, Utils.renameItem(new ItemStack(Material.GOLD_NUGGET), gold + "30 " + gray + "seconds", arrow + ChatColor.YELLOW + "Price: " + gray + "$15"));
		inv.setItem(4, Utils.renameItem(new ItemStack(Material.GOLD_NUGGET), gold + "1 " + gray + "minute", arrow + ChatColor.YELLOW + "Price: " + gray + "$30"));
		inv.setItem(6, Utils.renameItem(new ItemStack(Material.GOLD_NUGGET), gold + "2 " + gray + "minutes", arrow + ChatColor.YELLOW + "Price: " + gray + "$60"));
		return inv;
	}
	
	/**
	 * Check if a player is an NPC
	 * @param player - The player to check
	 * @return True if the player is an NPC, false if not
	 */
	public static final boolean isNPC(Player player){
		if(ChatColor.getLastColors(player.getName()).length() == 0){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * Check if a block is a road block
	 * @param block - The block to check
	 * @return True or false depending on if the block is a road block or not
	 */
	public static final boolean isRoadBlock(Block block){
		if(block.getType() == Material.WOOL){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Check if a command is blocked
	 * @param command - The command to check
	 * @return True if the command is blocked, false if not
	 */
	public static final boolean isCommandBlocked(String command){
		boolean blocked = false;
		for(int x = 0; x <= (blockedcmds.length - 1); x++){
			if(command.contains(blockedcmds[x])){
				blocked = true;
				break;
			}
		}
		return blocked;
	}
	
	/**
	 * Get the Grand Craft Auto world
	 * @return The GCA world
	 */
	public static final World getGCAWorld(){
		if(world == null){
			world = Bukkit.getWorld("loscraftos");
		}
		return world;
	}
	
	/**
	 * Hide a player from everyone else
	 * @param player - The player to hide
	 */
	public static final void hidePlayer(Player player){
		for(Player p : getGCAWorld().getPlayers()){
			p.hidePlayer(player);
		}
	}
	
	/**
	 * Show a hidden player again
	 * @param player - The player to show
	 */
	public static final void showPlayer(Player player){
		for(Player p : getGCAWorld().getPlayers()){
			p.showPlayer(player);
		}
	}
	
	/**
	 * Rename an itemstack
	 * @param item - The itemstack to rename
	 * @param name - The new name of the itemstack
	 * @param desc - A single line of lore for the itemstack
	 * @return The renamed itemstack
	 */
	public static final ItemStack renameItem(ItemStack item, String name, String... lore){
	    ItemMeta meta = (ItemMeta) item.getItemMeta();
    	meta.setDisplayName(name);
    	List<String> desc = new ArrayList<String>();
    	for(int x = 0; x <= (lore.length - 1); x++){
    		desc.add(lore[x]);
    	}
	    meta.setLore(desc);
	    item.setItemMeta(meta);
	    return item;
	}
	
	/**
	 * Rename an itemstack
	 * @param item - The itemstack to rename
	 * @param name - The new name of the itemstack
	 * @param desc - A single line of lore for the itemstack
	 * @return The renamed itemstack
	 */
	public static final ItemStack renameItem(ItemStack item, String name, List<String> lore){
	    ItemMeta meta = (ItemMeta) item.getItemMeta();
    	meta.setDisplayName(name);
	    meta.setLore(lore);
	    item.setItemMeta(meta);
	    return item;
	}
	
	/**
	 * Rename an itemstack
	 * @param item - The itemstack to rename
	 * @param name - The new name of the itemstack
	 * @return The renamed itemstack
	 */
	public static final ItemStack renameItem(ItemStack item, String name){
	    ItemMeta meta = (ItemMeta) item.getItemMeta();
    	meta.setDisplayName(name);
	    item.setItemMeta(meta);
	    return item;
	}
	
	/**
	 * Shoot a bullet from an entity
	 * @param entity - The entity to shoot a bullet from
	 */
	public static final void shootBullet(Player player){
		GPlayer gplayer = new GPlayer(player);
		Projectile proj = player.getWorld().spawn(player.getEyeLocation().subtract(0, 0.3, 0), Egg.class);
		proj.setShooter(player);
		double rV = attemptSetAsNegative(getRandomDouble(.2));
		double rV2 = attemptSetAsNegative(getRandomDouble(.2));
		Vector velocity = player.getEyeLocation().getDirection().multiply(4.0);
		if(gplayer.hasWeaponInHand() == true){
			if(gplayer.getWeaponInHand() instanceof Gun){
				Gun gun = (Gun) gplayer.getWeaponInHand();
				double accuracy = gun.getAccuracy();
				if(main.zoomed.contains(player.getName())){
					accuracy = accuracy / 2;
				}
				accuracy -= (gplayer.getSkillLevel(Skill.SHOOTING) * .001);
				rV = attemptSetAsNegative(getRandomDouble(accuracy));
				rV2 = attemptSetAsNegative(getRandomDouble(accuracy));
				
				double range = gun.getRange();
				if(range < 3.5){
					range += 1;
				}
				velocity = player.getEyeLocation().getDirection().multiply(range);
			}
		}
		proj.setVelocity(velocity);
		double rV3 = proj.getVelocity().getY();
		if(gplayer.hasWeaponInHand() == true){
			if(gplayer.getWeaponInHand() instanceof Shotgun){
				Gun gun = (Gun) gplayer.getWeaponInHand();
				rV3 = (proj.getVelocity().getY() + attemptSetAsNegative(getRandomDouble(gun.getAccuracy())));
			}
		}
		proj.setVelocity(new Vector(proj.getVelocity().getX() + rV, rV3, proj.getVelocity().getZ() + rV2));
		EffectUtils.playShotSound(player.getEyeLocation());
		EffectUtils.playSmokeEffect(player.getEyeLocation());
	}
	
	/**
	 * Shoot a bullet from an entity to another entity
	 * @param origin - The entity who shot the bullet
	 * @param dest - The entity who will be hit by the bullet
	 */
	public static final void shootBullet(LivingEntity origin, LivingEntity dest){
		Projectile proj = origin.getLocation().getWorld().spawn(origin.getEyeLocation(), Egg.class);
		proj.setShooter(origin);
		Vector vec = new Vector(getDifferentialVector(origin.getLocation(), dest.getLocation()).getX(), proj.getVelocity().getY(), getDifferentialVector(origin.getLocation(), dest.getLocation()).getZ());
		double rV = attemptSetAsNegative(getRandomDouble(.2));
		double rV2 = attemptSetAsNegative(getRandomDouble(.2));
		vec.setX(vec.getX() + rV);
		vec.setZ(vec.getZ() + rV2);
		proj.setVelocity(vec.multiply(0.25));
		EffectUtils.playShotSound(origin.getEyeLocation());
		EffectUtils.playSmokeEffect(origin.getEyeLocation());
	}
	
	/**
	 * Create a GPS path effect for the player on the given route
	 * @param player - The player to send the effect to
	 * @param start - The start of the path
	 * @param tiles - The list of tiles in the path
	 */
	@SuppressWarnings("deprecation")
	public static final void createPathEffect(Player player, Location start, Location end, ArrayList<Tile> tiles){
		List<Hologram> holograms = new ArrayList<Hologram>();
		int blocks = 0;
		int holoCount = 0;
		for(Tile t : tiles){
			Location tloc = t.getLocation(start);
			blocks++;
			if(blocks == 1){
				ChatColor color = ChatColor.GOLD;
				double distance = tloc.distance(end);
				if(distance < 20){
					color = ChatColor.GREEN;
				}else if(distance >= 20 && distance < 60){
					color = ChatColor.YELLOW;
				}else if(distance >= 60 && distance < 120){
					color = ChatColor.GOLD;
				}else if(distance >= 120 && distance < 185){
					color = ChatColor.RED;
				}else if(distance >= 185){
					color = ChatColor.DARK_RED;
				}
				holoCount++;
				if(holoCount == 1){
					Hologram hologram = HolographicDisplaysAPI.createHologram(main, tloc.add(0.5, 2.5, 0.5), color + "" + (int)distance + "m");
					holograms.add(hologram);
				}else if(holoCount > 1){
					Hologram hologram = HolographicDisplaysAPI.createHologram(main, tloc.add(0.5, 2.5, 0.5), color + TextUtils.getSeperator());
					holograms.add(hologram);
				}
				if(holoCount > 3){
					holoCount = 0;
				}
			}else if(blocks >= 3){
				blocks = 0;
			}
		}
		for(Entity e : Utils.getGCAWorld().getEntities()){
			if(e instanceof LivingEntity){
				LivingEntity le = (LivingEntity) e;
				if(HolographicDisplaysAPI.isHologramEntity(le) == true){
					HologramBase h = HolographicDisplaysAPI.getNmsManager().getParentHologram(le);
					for(Hologram holo : holograms){
						if(h.getLocation().distance(holo.getLocation()) < 1){
							Set<String> whoCanSee = new HashSet<String>();
							whoCanSee.add(player.getName());
							h.setVisibilityManager(new VisibilityManager(whoCanSee));
							break;
						}
					}
				}
			}
		}
		PathEffect effect = new PathEffect(player, holograms, end);
		main.pathEffect.put(player.getName(), effect);
	}
	
	/**
	 * Launch a firework at the specified location with the given speed
	 * @param loc - The location to launch the firework at
	 * @param speed - The speed at which to launch the firework
	 */
	public static final void launchFirework(Location loc){
		Firework fw = (Firework) getGCAWorld().spawn(loc, Firework.class);
		FireworkMeta meta = fw.getFireworkMeta();
		meta.addEffect(FireworkEffect.builder().withColor(Color.LIME).withColor(Color.BLUE).flicker(true).with(Type.BALL_LARGE).build());
		fw.setFireworkMeta(meta);
		fw.setVelocity(new Vector(fw.getVelocity().getX(), 0.5, fw.getVelocity().getZ()));
	}
	
	/**
	 * Get a random double
	 * @return A random double
	 */
	public static final double getRandomDouble(double max){
		Random r = new Random();
		return 0 + (max - 0) * r.nextDouble();
	}
	
	/**
	 * A 50% chance of setting the double as a negative
	 * @param d - The double to attempt to set as negative
	 * @return The new double which could either be the same positive double or a negative double
	 */
	public static final double attemptSetAsNegative(double d){
		int random = 1 + (int)(Math.random() * ((2 - 1) + 1));
		if(random == 1){
			d = d - (d*2);
		}
		return d;
	}
	
	/**
	 * Get the level's xp required to level up
	 * @return The xp required to level up
	 */
	public static final int getXPForLevel(int level){
		return (int) ((Math.pow(level, 2)) + 9);
	}
	
	/**
	 * Return a string of bars with the given percent greened out
	 * @param percent - The percent to fill the bars up to
	 * @return The string of bars
	 */
	public static final String asBars(double percent){
		String bars = "";
		int totalGreen = (int) ((percent * 0.1) * 3);
		int currentGreen = 0;
		for(int x = 1; x <= 30; x++){
			if(currentGreen < totalGreen){
				bars = bars + ChatColor.GREEN + TextUtils.getBar();
				currentGreen++;
			}else{
				bars = bars + ChatColor.DARK_GRAY + TextUtils.getBar();
			}
		}
		return bars;
	}
	
	/**
	 * Broadcast a server-wide message
	 * @param message - The message to broadcast
	 */
	public static final void broadcastMessage(String message){
		for(Player p : Bukkit.getOnlinePlayers()){
			GPlayer gp = new GPlayer(p);
			gp.sendMessage(message);
		}
	}
	
	/**
	 * Get the amount of villagers alive
	 * @return The amount of villagers alive
	 */
	public static final int getAliveCitizens(){
		int alive = 0;
		for(Entity e : getGCAWorld().getEntities()){
			if(e instanceof Villager){
				alive++;
			}
		}
		return alive;
	}
	
	/**
	 * Get the amount of villagers allowed to be alive
	 * @return The amount of villagers allowed to be alive
	 */
	public static final int getMaxAllowedCitizens(){
		return 600;
	}
	
	/**
	 * Get the world border size
	 * @return The world border size
	 */
	public static final int getWorldBorder(){
		return 200;
	}
	
	/**
	 * Get the total player spawn points
	 * @return The total player spawn points
	 */
	public static final int getTotalSpawnPoints(){
		return main.getConfig().getInt("spawns.totalplayerspawns");
	}
	
	/**
	 * Set the total player spawn points
	 * @param spawns - The total player spawn points
	 */
	public static final void setTotalSpawnPoints(int spawns){
		main.getConfig().set("spawns.totalplayerspawns", spawns);
		main.saveConfig();
	}
	
	/**
	 * Create a spawn point at the location
	 * @param loc - The location to create a spawn point at
	 */
	public static final void addSpawnPoint(Location loc){
		int spawn = getTotalSpawnPoints() + 1;
		setTotalSpawnPoints(spawn);
		main.getConfig().set("spawns." + spawn + ".x", loc.getX());
		main.getConfig().set("spawns." + spawn + ".y", loc.getY());
		main.getConfig().set("spawns." + spawn + ".z", loc.getZ());
		main.getConfig().set("spawns." + spawn + ".yaw", loc.getYaw());
		main.getConfig().set("spawns." + spawn + ".pitch", loc.getPitch());
		main.saveConfig();
	}
	
	/**
	 * Get a random spawn point
	 * @return A random spawn point
	 */
	public static final Location getSpawnPoint(){
		int spawn = randInt(1, getTotalSpawnPoints());
		
		double x,y,z;
		x = main.getConfig().getDouble("spawns." + spawn + ".x");
		y = main.getConfig().getDouble("spawns." + spawn + ".y");
		z = main.getConfig().getDouble("spawns." + spawn + ".z");
		int yaw,pitch;
		yaw = main.getConfig().getInt("spawns." + spawn + ".yaw");
		pitch = main.getConfig().getInt("spawns." + spawn + ".pitch");
		
		return new Location(getGCAWorld(), x, y, z, yaw, pitch);
	}
	
	/**
	 * Get the next apartment ID
	 * @return The next apartment ID
	 */
	public static final int getNextApartmentID(){
		File folder = new File(main.getDataFolder() + File.separator + "apartments");
		if(folder.exists() == true){
			try{
				return (folder.listFiles().length + 1);
			}catch (Exception ex){
				return 1;
			}
		}else{
			return 1;
		}
	}
	
	/**
	 * Get the next garage ID
	 * @return The next garage ID
	 */
	public static final int getNextGarageID(){
		return (Garage.getTotalGarages() + 1);
	}
	
	/**
	 * Get the next crew ID
	 * @return The next crew ID
	 */
	public static final int getNextCrewID(){
		int next = 1;
		for(int x = 1; x <= 5000; x++){
			File f = new File(main.getDataFolder() + File.separator + "crews" + File.separator + x + ".yml");
			if(f.exists() == false){
				next = x;
				break;
			}
		}
		return next;
	}
}
