package com.grandcraftauto.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.ScoreboardManager;

import com.grandcraftauto.game.Creator;
import com.grandcraftauto.game.Events;
import com.grandcraftauto.game.Gang;
import com.grandcraftauto.game.ItemSpawner;
import com.grandcraftauto.game.Rank;
import com.grandcraftauto.game.Skill;
import com.grandcraftauto.game.Tutorial;
import com.grandcraftauto.game.VehicleSpawner;
import com.grandcraftauto.game.apartment.Apartment;
import com.grandcraftauto.game.apartment.ApartmentCreator;
import com.grandcraftauto.game.cars.Car;
import com.grandcraftauto.game.cars.SpeedModifier;
import com.grandcraftauto.game.crew.Crew;
import com.grandcraftauto.game.crew.CrewRank;
import com.grandcraftauto.game.districts.District;
import com.grandcraftauto.game.districts.DistrictCreator;
import com.grandcraftauto.game.districts.DistrictUtils;
import com.grandcraftauto.game.garage.GarageCreator;
import com.grandcraftauto.game.garage.GarageInstance;
import com.grandcraftauto.game.gps.PathEffect;
import com.grandcraftauto.game.inventories.InventoryHandler;
import com.grandcraftauto.game.inventories.Slot;
import com.grandcraftauto.game.inventories.Store;
import com.grandcraftauto.game.jobs.FreeForAll;
import com.grandcraftauto.game.jobs.FreeForAllCreator;
import com.grandcraftauto.game.jobs.Job;
import com.grandcraftauto.game.jobs.JobInstance;
import com.grandcraftauto.game.jobs.JobJoiner;
import com.grandcraftauto.game.jobs.Race;
import com.grandcraftauto.game.jobs.RaceCreator;
import com.grandcraftauto.game.jobs.TeamDeathmatch;
import com.grandcraftauto.game.jobs.TeamDeathmatchCreator;
import com.grandcraftauto.game.leaderboards.LeaderboardType;
import com.grandcraftauto.game.missions.MissionUtils;
import com.grandcraftauto.game.missions.SideMission;
import com.grandcraftauto.game.missions.SideMissionType;
import com.grandcraftauto.game.player.GPlayer;
import com.grandcraftauto.game.player.PlayerStats;
import com.grandcraftauto.game.weapons.Ammo;
import com.grandcraftauto.game.weapons.Weapon;
import com.grandcraftauto.tasks.FriendRequestTask;
import com.grandcraftauto.tasks.InviteTask;
import com.grandcraftauto.tasks.InvokedTask;
import com.grandcraftauto.tasks.KnockOutTask;
import com.grandcraftauto.tasks.LockpickTask;
import com.grandcraftauto.tasks.ProstituteFollowTask;
import com.grandcraftauto.tasks.ReloadTask;
import com.grandcraftauto.tasks.RobTask;
import com.grandcraftauto.tasks.TeleportTask;
import com.grandcraftauto.utils.EntityHider;
import com.grandcraftauto.utils.Help;
import com.grandcraftauto.utils.ObjectType;
import com.grandcraftauto.utils.ParticleEffect;
import com.grandcraftauto.utils.ParticleUtils;
import com.grandcraftauto.utils.TextUtils;
import com.grandcraftauto.utils.Utils;
import com.grandcraftauto.utils.EntityHider.Policy;
import com.useful.uCarsAPI.uCarsAPI;

public class Main extends JavaPlugin{
	
	private static Main instance;
	public static EntityHider entityHider;
	public static ScoreboardManager manager;
	private String gray = ChatColor.GRAY + "";
	private String gold = ChatColor.GOLD + "";
	
	public int grenadeID = 0;
	public int whitelist = -1;
	
	public List<String> zoomed = new ArrayList<String>();
	public List<String> spawning = new ArrayList<String>();
	public List<String> crewChat = new ArrayList<String>();
	public List<String> knockout = new ArrayList<String>();
	public List<String> cancelDrop = new ArrayList<String>();
	public List<String> harvesting = new ArrayList<String>();
	public List<String> messageTask = new ArrayList<String>();
	public List<JobInstance> jobs = new ArrayList<JobInstance>();
	public List<String> killedProstitute = new ArrayList<String>();
	
	public List<String> robCooldown = new ArrayList<String>();
	public List<String> killCooldown = new ArrayList<String>();
	public List<String> shootCooldown = new ArrayList<String>();
	public List<String> reloadCooldown = new ArrayList<String>();
	public List<Integer> collisionCooldown = new ArrayList<Integer>();
	
	public HashMap<String, Car> currentcar = new HashMap<String, Car>();
	public HashMap<String, Minecart> car = new HashMap<String, Minecart>();
	public HashMap<String, RobTask> robbing = new HashMap<String, RobTask>();
	public HashMap<String, Creator> creating = new HashMap<String, Creator>();
	public HashMap<String, Integer> bounties = new HashMap<String, Integer>();
	public HashMap<String, Integer> playtime = new HashMap<String, Integer>();
	public HashMap<String, Double> consumption = new HashMap<String, Double>();
	public HashMap<String, Tutorial> tutorial = new HashMap<String, Tutorial>();
	public HashMap<String, District> districts = new HashMap<String, District>();
	public HashMap<String, PlayerStats> stats = new HashMap<String, PlayerStats>();
	public HashMap<String, InviteTask> invited = new HashMap<String, InviteTask>();
	public HashMap<String, ReloadTask> reloading = new HashMap<String, ReloadTask>();
	public HashMap<String, InvokedTask> invoked = new HashMap<String, InvokedTask>();
	public HashMap<String, PathEffect> pathEffect = new HashMap<String, PathEffect>();
	public HashMap<String, JobInstance> playerJobs = new HashMap<String, JobInstance>();
	public HashMap<String, LockpickTask> lockpicking = new HashMap<String, LockpickTask>();
	public HashMap<String, TeleportTask> teleporting = new HashMap<String, TeleportTask>();
	public HashMap<String, KnockOutTask> knockingout = new HashMap<String, KnockOutTask>();
	public HashMap<String, GarageInstance> inGarage = new HashMap<String, GarageInstance>();
	public HashMap<String, LivingEntity> playerkiller = new HashMap<String, LivingEntity>();
	public HashMap<String, FriendRequestTask> friendRequest = new HashMap<String, FriendRequestTask>();
	public HashMap<String, ProstituteFollowTask> prostitute = new HashMap<String, ProstituteFollowTask>();
	
	/**
	 * Get this class's instance
	 * @return The class's instance
	 */
	public static final Main getInstance(){
		return instance;
	}
	
	/*
	 * -=-(*)-=- TO DO -=-(*)-=-
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	@SuppressWarnings("deprecation")
	public void onEnable(){
		instance = this;
		entityHider = new EntityHider(this, Policy.BLACKLIST);
		manager = Bukkit.getScoreboardManager();
		uCarsAPI.getAPI().hookPlugin(this);
		uCarsAPI.getAPI().registerSpeedMod(this, new SpeedModifier());
		Weapon.initializeList();
		Store.initializeList();
		Job.initializeList();
		this.getServer().getPluginManager().registerEvents(new Events(), this);
		for(Entity e : Utils.getGCAWorld().getEntities()){
			if(!(e instanceof Player)){
				e.remove();
			}else{
				Player p = (Player) e;
				if(p.hasMetadata("NPC") == true){
					e.remove();
				}
			}
		}
		
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			public void run(){
				killCooldown.clear();
			}
		}, 0, 5);
		
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			public void run(){
				if(Utils.getAliveCitizens() < Utils.getMaxAllowedCitizens()){
					Utils.spawnRandomVillager();
				}
				for(Player p : Utils.getGCAWorld().getPlayers()){
					GPlayer gp = new GPlayer(p);
					
					int time = 0;
					if(playtime.containsKey(p.getName())){
						time = playtime.get(p.getName());
						playtime.remove(p.getName());
					}
					playtime.put(p.getName(), (time + 1));
					
					if(p.getFoodLevel() >= 20 && p.getHealth() < 20){
						double toAdd = 0.35;
						toAdd += (gp.getSkillLevel(Skill.HEALTH) * .004);
						double newhealth = p.getHealth() + toAdd;
						if(newhealth <= 20){
							p.setHealth(newhealth);
						}else{
							p.setHealth(20);
						}
					}
					
					p.setWalkSpeed((0.2F + (gp.getSkillLevel(Skill.STAMINA) * .0015F)));
					
					if(!tutorial.containsKey(p.getName())){
						District d = null;
						District currentDistrict = DistrictUtils.getDistrict(p.getLocation());
						if(districts.containsKey(p.getName())){
							d = districts.get(p.getName());
						}
						boolean districtChange = false;
						if(d != null){
							if(currentDistrict != null){
								if(!currentDistrict.getName().equalsIgnoreCase(d.getName())){
									districtChange = true;
								}
							}else{
								districtChange = true;
							}
						}else{
							if(currentDistrict != null){
								districtChange = true;
							}
						}
						if(districtChange == true){
							if(districts.containsKey(p.getName())){
								districts.remove(p.getName());
							}
							districts.put(p.getName(), currentDistrict);
							if(currentDistrict != null){
								gp.sendMessage("Entering the " + gold + currentDistrict.getName() + gray + " district!");
							}else if(d != null){
								gp.sendMessage("Leaving the " + gold + d.getName() + gray + " district!");
							}
						}
					}
				}
				Utils.getGCAWorld().setWeatherDuration(0);
			}
		}, 0, 20);
		
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			public void run(){
				for(Gang g : Gang.values()){
					for(int x = 1; x <= g.getTotalHideoutLocations(); x++){
						int toSpawn = g.getMaxAliveMembers() - g.getAliveMembers(x).size();
						if(toSpawn <= g.getMaxAliveMembers()){
							for(int z = 1; z <= toSpawn; z++){
								Villager v = Utils.spawnVillager(g.getVillagerType(), g.getHideoutLocation(x).add(Utils.randInt(-3, 3), 0, Utils.randInt(-3, 3)), g.getTag());
								Utils.setMetadata(v, "hideout", x);
								g.members.add(v.getEntityId());
							}
						}
					}
				}
				for(Entity e : Utils.getGCAWorld().getEntities()){
					if(e instanceof Minecart){
						Minecart m = (Minecart) e;
						if(m.hasMetadata("owner") == true && Utils.getMetadata(m, "owner", ObjectType.STRING) != null){
							if(Bukkit.getPlayer((String)Utils.getMetadata(m, "owner", ObjectType.STRING)) == null || 
									Bukkit.getPlayer((String)Utils.getMetadata(m, "owner", ObjectType.STRING)).isOnline() == false){
								m.remove();
							}
						}
					}
				}
				ItemSpawner.refreshItems();
				VehicleSpawner.refreshVehicles();
				JobJoiner.refreshJobJoiners();
				for(Player p : Bukkit.getOnlinePlayers()){
					GPlayer gp = new GPlayer(p);
					if(InventoryHandler.removeIllegalItems(p) == true){
						gp.sendError("Items in your inventory were removed because you glitched them there.");
					}
					if(gp.hasApartment() == true){
						for(Apartment a : gp.getApartments()){
							if(gp.hasToPayRent(a) == true){
								if(gp.getWarnedOfRent(a) == true){
									if(gp.canForecloseApartment(a) == true){
										gp.forecloseApartment(a);
									}
								}
							}
						}
					}
					if(gp.getThugPoints() > 0){
						gp.sendMessage("You have unspent thug points! Use your phone to upgrade skills!");
					}
					gp.removeMentalState(2.5);
					if(consumption.containsKey(p.getName())){
						double consumed = consumption.get(p.getName());
						consumption.remove(p.getName());
						double newconsumed = consumed - 5;
						if(newconsumed > 0){
							consumption.put(p.getName(), newconsumed);
						}
					}
				}
				if(SideMission.anySideMissions() == true){
					for(SideMissionType t : SideMissionType.values()){
						if(SideMission.canBeRandomized(t) == true){
							SideMission.randomizeSideMission(t);
						}
					}
				}
			}
		}, 0, 800);
	}
	
	public void onDisable(){
		for(Player p : Utils.getGCAWorld().getPlayers()){
			if(Utils.isNPC(p) == false){
				GPlayer gp = new GPlayer(p);
				gp.logout();
			}
		}
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender instanceof Player){
			Player player = (Player) sender;
			GPlayer gplayer = new GPlayer(player);
			if(cmd.getName().equalsIgnoreCase("gca") || cmd.getName().equalsIgnoreCase("grandcraftauto")){
				if(args.length == 0){
					gplayer.sendCommandHelp(Help.GENERAL, 1);
				}else if(args.length > 0){
					if(args[0].equalsIgnoreCase("addplayerspawn")){
						if(player.isOp() == true){
							Utils.addSpawnPoint(player.getLocation());
							gplayer.sendMessage(gold + gray + "You have added a new spawn point on your location!");
						}else{
							gplayer.sendError("You may not perform this command!");
						}
					}else if(args[0].equalsIgnoreCase("addganghideout")){
						if(player.isOp() == true){
							if(args.length == 2){
								String name = args[1];
								Gang gang = null;
								for(Gang g : Gang.values()){
									if(g.getName().equalsIgnoreCase(name)){
										gang = g;
									}
								}
								if(gang != null){
									int id = gang.getTotalHideoutLocations() + 1;
									gang.setTotalHideoutLocations(id);
									gang.setHideoutLocation(id, player.getLocation());
									gplayer.sendMessage("You have set the " + gold + gang.getName() + gray + "'s " + gold + "#" + id + gray + " hideout location!");
								}else{
									gplayer.sendError("That gang does not exist!");
								}
							}else{
								gplayer.sendError("Usage: /" + cmd.getName() + " addganghideout <gang>");
							}
						}else{
							gplayer.sendError("You may not perform this command!");
						}
					}else if(args[0].equalsIgnoreCase("additemspawn")){
						if(player.isOp() == true){
							if(args.length == 2){
								if(Utils.isInteger(args[1]) == true){
									int spawnID = Integer.parseInt(args[1]);
									ItemSpawner.addLocationToSpawn(spawnID, player.getTargetBlock(null, 7).getLocation());
									gplayer.sendMessage("You have added an item spawn to spawn " + gold + spawnID + gray + "!");
								}else{
									gplayer.sendError("You have entered an invalid number!");
								}
							}else{
								gplayer.sendError("Usage: /" + cmd.getName() + " additemspawn <spawn id>");
							}
						}else{
							gplayer.sendError("You may not perform this command!");
						}
					}else if(args[0].equalsIgnoreCase("addvehiclespawn")){
						if(player.isOp() == true){
							if(args.length == 1){
								VehicleSpawner.addLocationToSpawn(player.getTargetBlock(null, 7).getLocation(), null, null, false);
								gplayer.sendMessage("You have added a vehicle spawn!");
							}else if(args.length == 2){
								Car type = null;
								for(Car t : Car.values()){
									if(t.toString().equalsIgnoreCase(args[1])){
										type = t;
									}
								}
								if(type != null){
									VehicleSpawner.addLocationToSpawn(player.getTargetBlock(null, 7).getLocation(), type, null, false);
									gplayer.sendMessage("You have added a vehicle spawn with the " + gold + type.toString().toUpperCase() + gray + " car!");
								}else{
									gplayer.sendError("That is an invalid car type!");
								}
							}else if(args.length == 3){
								Car type = null;
								for(Car t : Car.values()){
									if(t.toString().equalsIgnoreCase(args[1])){
										type = t;
									}
								}
								if(type != null){
									VehicleSpawner.addLocationToSpawn(player.getTargetBlock(null, 7).getLocation(), type, args[2], false);
									gplayer.sendMessage("You have added a vehicle spawn with the " + gold + type.toString().toUpperCase() + gray + "car and owner " + gold + args[2] + gray + "!");
								}else{
									gplayer.sendError("That is an invalid car type!");
								}
							}else if(args.length == 4){
								Car type = null;
								for(Car t : Car.values()){
									if(t.toString().equalsIgnoreCase(args[1])){
										type = t;
									}
								}
								if(type != null){
									boolean locked = false;
									if(args[3].equalsIgnoreCase("true")){
										locked = true;
									}
									VehicleSpawner.addLocationToSpawn(player.getTargetBlock(null, 7).getLocation(), type, args[2], locked);
									gplayer.sendMessage("You have added a vehicle spawn with the " + gold + type.toString().toUpperCase() + gray + " car, owner " + gold + args[2] + gray + 
											", and locked state of " + gold + locked + gray + "!");
								}else{
									gplayer.sendError("That is an invalid car type!");
								}
							}else{
								gplayer.sendError("Usage: /" + cmd.getName() + " addvehiclespawn [car type] [car owner] [is car locked]");
							}
						}else{
							gplayer.sendError("You may not perform this command!");
						}
					}else if(args[0].equalsIgnoreCase("addmissionloc")){
						if(player.isOp() == true){
							if(args.length == 2){
								MissionUtils.setMissionLocation(args[1], player.getLocation());
								gplayer.sendMessage("You have created a mission location titled " + gold + args[1] + gray + "!");
							}else{
								gplayer.sendError("Usage: /" + cmd.getName() + " addmissionloc <loc id>");
							}
						}else{
							gplayer.sendError("You may not perform this command!");
						}
					}else if(args[0].equalsIgnoreCase("settutloc")){
						if(player.isOp() == true){
							if(args.length == 2){
								if(Utils.isInteger(args[1]) == true){
									int step = Integer.parseInt(args[1]);
									Tutorial.setStepLocation(step, player.getLocation());
									gplayer.sendMessage("You have set step #" + gold + step + gray + "'s location!");
								}else{
									gplayer.sendError("You have entered an invalid step!");
								}
							}else{
								gplayer.sendError("Usage: /" + cmd.getName() + " settutloc <tutorial step>");
							}
						}else{
							gplayer.sendError("You may not perform this command!");
						}
					}else if(args[0].equalsIgnoreCase("addxp")){
						if(player.isOp() == true){
							if(args.length == 2){
								if(Utils.isInteger(args[1]) == true){
									int xp = Integer.parseInt(args[1]);
									gplayer.addXP(xp);
								}else{
									gplayer.sendError("You have entered an invalid number!");
								}
							}else{
								gplayer.sendError("Usage: /" + cmd.getName() + " addxp <amount>");
							}
						}else{
							gplayer.sendError("You may not perform this command!");
						}
					}else if(args[0].equalsIgnoreCase("setmaxapartments")){
						if(player.isOp() == true){
							if(args.length == 3){
								GPlayer gtarget = new GPlayer(args[1]);
								if(Utils.isInteger(args[2]) == true){
									int max = Integer.parseInt(args[2]);
									gtarget.setMaximumApartments(max);
									gplayer.sendMessage("You have set " + args[1] + "'s maximum apartments to " + max + "!");
								}else{
									gplayer.sendMessage("You have entered an invalid maximum value!");
								}
							}else{
								gplayer.sendError("Usage: /" + cmd.getName() + " setmaxapartments <player> <amount>");
							}
						}else{
							gplayer.sendError("You may not perform this command!");
						}
					}else if(args[0].equalsIgnoreCase("givemoney")){
						if(player.isOp() == true){
							if(args.length == 3){
								if(Utils.isOnline(args[1]) == true){
									GPlayer gtarget = new GPlayer(Bukkit.getPlayer(args[1]));
									if(Utils.isInteger(args[2]) == true){
										int money = Integer.parseInt(args[2]);
										gtarget.setWalletBalance(gtarget.getWalletBalance() + money);
										gplayer.sendMessage("You have given " + gold + args[1] + gray + " $" + money + "!");
									}else{
										gplayer.sendMessage("You have entered an invalid amount of money!");
									}
								}else{
									gplayer.sendError("That player is not online!");
								}
							}else{
								gplayer.sendError("Usage: /" + cmd.getName() + " givemoney <player> <amount>");
							}
						}else{
							gplayer.sendError("You may not perform this command!");
						}
					}else if(args[0].equalsIgnoreCase("addlevels")){
						if(player.isOp() == true){
							if(args.length == 3){
								boolean online = false;
								GPlayer gtarget = new GPlayer(args[1]);
								if(Utils.isOnline(args[1]) == true){
									gtarget = new GPlayer(Bukkit.getPlayer(args[1]));
									online = true;
								}
								if(Utils.isInteger(args[2]) == true){
									int level = Integer.parseInt(args[2]);
									if(online == true){
										gtarget.levelUp(level);
									}else{
										gtarget.setLevel(gtarget.getLevel() + level);
									}
									gplayer.sendMessage("You have added " + gold + level + gray+ " level(s) to " + gold + args[1] + gray + "!");
								}else{
									gplayer.sendMessage("You have entered an invalid amount of money!");
								}
							}else{
								gplayer.sendError("Usage: /" + cmd.getName() + " addlevels <player> <amount>");
							}
						}else{
							gplayer.sendError("You may not perform this command!");
						}
					}else if(args[0].equalsIgnoreCase("setrank")){
						if(gplayer.getRank() == Rank.OWNER || gplayer.getRank() == Rank.ADMIN || player.isOp() == true){
							if(args.length == 3){
								GPlayer gtarget = new GPlayer(args[1]);
								if(Bukkit.getPlayer(args[1]) != null && Bukkit.getPlayer(args[1]).isOnline() == true){
									gtarget = new GPlayer(Bukkit.getPlayer(args[1]));
								}
								Rank rank = null;
								for(Rank r : Rank.values()){
									if(r.toString().equalsIgnoreCase(args[2])){
										rank = r;
										break;
									}
								}
								if(rank != null){
									gtarget.setRank(rank);
									if(rank.getLadder() > gtarget.getRankLadder()){
										gtarget.setRankLadder(rank.getLadder());
									}
									if(rank == Rank.THUG){
										gtarget.setMaximumApartments(3);
									}else if(rank == Rank.PIMP){
										gtarget.setMaximumApartments(6);
									}else if(rank == Rank.GODFATHER){
										gtarget.setMaximumApartments(10);
									}
									gplayer.sendMessage("You have set " + gold + args[1] + gray + "'s rank to " + gold + WordUtils.capitalizeFully(rank.toString()) + gray + "!");
									gtarget.sendMessage("You are now a " + gold + WordUtils.capitalizeFully(rank.toString()) + gray + "!");
								}else{
									gplayer.sendError("You have entered an invalid rank!");
								}
							}else{
								gplayer.sendError("Usage: /" + cmd.getName() + " setrank <player> <rank>");
							}
						}else{
							gplayer.sendError("You may not perform this command!");
						}
					}else if(args[0].equalsIgnoreCase("whitelist")){
						if(player.isOp() == true){
							if(args.length == 2){
								String target = args[1];
								Utils.setPlayerWhitelisted(target, true);
								gplayer.sendMessage("You have whitelisted " + gold + target + gray + "!");
							}else{
								gplayer.sendError("Usage: /" + cmd.getName() + " whitelist <player>");
							}
						}else{
							gplayer.sendError("You may not perform this command!");
						}
					}else if(args[0].equalsIgnoreCase("unwhitelist")){
						if(player.isOp() == true){
							if(args.length == 2){
								String target = args[1];
								Utils.setPlayerWhitelisted(target, false);
								gplayer.sendMessage("You have un-whitelisted " + gold + target + gray + "!");
							}else{
								gplayer.sendError("Usage: /" + cmd.getName() + " unwhitelist <player>");
							}
						}else{
							gplayer.sendError("You may not perform this command!");
						}
					}else if(args[0].equalsIgnoreCase("setwhitelisted")){
						if(player.isOp() == true){
							if(args.length == 2){
								if(args[1].equalsIgnoreCase("true")){
									Utils.setServerWhitelisted(true);
									gplayer.sendMessage("You have whitelisted the server!");
								}else if(args[1].equalsIgnoreCase("false")){
									Utils.setServerWhitelisted(false);
									gplayer.sendMessage("You have un-whitelisted the server!");
								}
							}else{
								gplayer.sendError("Usage: /" + cmd.getName() + " setwhitelisted <true/false>");
							}
						}else{
							gplayer.sendError("You may not perform this command!");
						}
					}else if(args[0].equalsIgnoreCase("weapons")){
						if(player.isOp() == true){
							Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GRAY + "Apartment Chest");
							for(Weapon w : Weapon.list()){
								inv.addItem(w.getItemStack(false));
							}
							for(Ammo a : Ammo.values()){
								inv.addItem(a.getItemStack(false));
							}
							player.openInventory(inv);
						}else{
							gplayer.sendError("You may not perform this command!");
						}
					}else if(args[0].equalsIgnoreCase("test")){
						if(player.isOp() == true){
							gplayer.refreshScoreboard();
							gplayer.refreshNametag();
						}else{
							gplayer.sendError("You may not perform this command!");
						}
					}else if(args[0].equalsIgnoreCase("tpvillager")){
						if(player.isOp() == true){
							for(Entity e : player.getNearbyEntities(300, 300, 300)){
								if(e instanceof Villager){
									player.teleport(e.getLocation());
									gplayer.sendMessage("Teleported to the nearest villager.");
									break;
								}
							}
						}else{
							gplayer.sendError("You may not perform this command!");
						}
					}else if(args[0].equalsIgnoreCase("effects")){
						if(player.isOp() == true){
							int page = 1;
							ParticleEffect effect = null;
							if(args.length == 2){
								if(Utils.isInteger(args[1]) == true){
									page = Integer.parseInt(args[1]);
								}else{
									try{
										effect = ParticleEffect.valueOf(args[1].toUpperCase());
										ParticleUtils.sendToLocation(effect, player.getEyeLocation(), 0.5F, 0.5F, 0.5F, 0, 10);
									}catch (Exception ex){
										gplayer.sendError("You have entered an invalid effect!");
									}
								}
							}
							if(effect == null){
								List<ParticleEffect> effects = new ArrayList<ParticleEffect>();
								for(ParticleEffect e : ParticleEffect.values()){
									effects.add(e);
								}
								if(effects.size() > 0){
									int sizePerPage=7;
									if(effects.size() < sizePerPage){
										sizePerPage = effects.size();
									}
									double totalpages = (Utils.roundUp(effects.size(), sizePerPage)) / sizePerPage;
									if(page > (int)totalpages){
										page = (int)totalpages;
									}
									gplayer.sendMessageHeader("Effects - Page " + page + "/" + (int)totalpages);
									page--;
									
									int from = Math.max(0,page*sizePerPage);
									int to = Math.min(effects.size(),(page+1)*sizePerPage);
									
									List<ParticleEffect> result = effects.subList(from,to);
									for(ParticleEffect e : result){
										gplayer.sendMessage(gold + e.toString());
									}
									player.sendMessage(" ");
									gplayer.sendMessage("Type " + gold + "/gca effects " + (page + 2) + gray + " to see the next page!");
								}else{
									gplayer.sendError("There are currently no effects!");
								}
							}
						}else{
							gplayer.sendError("You may not perform this command!");
						}
					}else if(args[0].equalsIgnoreCase("createdistrict")){
						if(player.isOp() == true){
							if(!creating.containsKey(player.getName())){
								DistrictCreator creator = new DistrictCreator(player);
								creating.put(player.getName(), creator);
								gplayer.sendMessage("You are now creating a district!");
								creator.sendStep(creator.getStep());
							}else{
								gplayer.sendError("You are already creating something!");
							}
						}else{
							gplayer.sendError("You may not perform this command!");
						}
					}else if(args[0].equalsIgnoreCase("createjob")){
						if(player.isOp() == true){
							if(args.length == 3){
								if(args[1].equalsIgnoreCase("race")){
									if(Utils.isInteger(args[2]) == true){
										int id = Integer.parseInt(args[2]);
										if(!creating.containsKey(player.getName())){
											RaceCreator creator = new RaceCreator(player, new Race(id));
											creating.put(player.getName(), creator);
											gplayer.sendMessage("You are now creating a race!");
											creator.sendStep(creator.getStep());
										}else{
											gplayer.sendError("You are already creating something!");
										}
									}else{
										gplayer.sendError("That is an invalid job ID!");
									}
								}else if(args[1].equalsIgnoreCase("ffa")){
									if(Utils.isInteger(args[2]) == true){
										int id = Integer.parseInt(args[2]);
										if(!creating.containsKey(player.getName())){
											FreeForAllCreator creator = new FreeForAllCreator(player, new FreeForAll(id));
											creating.put(player.getName(), creator);
											gplayer.sendMessage("You are now creating a free for all!");
											creator.sendStep(creator.getStep());
										}else{
											gplayer.sendError("You are already creating something!");
										}
									}else{
										gplayer.sendError("That is an invalid job ID!");
									}
								}else if(args[1].equalsIgnoreCase("tdm")){
									if(Utils.isInteger(args[2]) == true){
										int id = Integer.parseInt(args[2]);
										if(!creating.containsKey(player.getName())){
											TeamDeathmatchCreator creator = new TeamDeathmatchCreator(player, new TeamDeathmatch(id));
											creating.put(player.getName(), creator);
											gplayer.sendMessage("You are now creating a team deathmatch!");
											creator.sendStep(creator.getStep());
										}else{
											gplayer.sendError("You are already creating something!");
										}
									}else{
										gplayer.sendError("That is an invalid job ID!");
									}
								}else{
									gplayer.sendError("That is an invalid job type!");
								}
							}else{
								gplayer.sendError("Usage: /" + cmd.getName() + " createjob <job type> <job id>");
							}
						}else{
							gplayer.sendError("You may not perform this command!");
						}
					}else if(args[0].equalsIgnoreCase("createjobjoiner")){
						if(player.isOp() == true){
							if(args.length == 2){
								if(Utils.isInteger(args[1]) == true){
									int id = Integer.parseInt(args[1]);
									Job job = Job.getJob(id);
									JobJoiner.addJobJoiner(job, player.getLocation());
									gplayer.sendMessage("You have created a job joiner for " + gold + job.getName() + gray + "!");
								}else{
									gplayer.sendError("That is an invalid job ID!");
								}
							}else{
								gplayer.sendError("Usage: /" + cmd.getName() + " createjobjoiner <job id>");
							}
						}else{
							gplayer.sendError("You may not perform this command!");
						}
					}else if(args[0].equalsIgnoreCase("car")){
						if(player.isOp() == true){
							gplayer.setCurrentCar(Car.VAPID_BULLET);
							player.getInventory().setItem(Slot.CAR.getSlot(), gplayer.getCurrentCar().getItemStack(false));
						}else{
							gplayer.sendError("You may not perform this command!");
						}
					}else if(args[0].equalsIgnoreCase("alive")){
						if(player.isOp() == true){
							gplayer.sendMessage("Alive Citizens: " + gold + Utils.getAliveCitizens() + gray + "/" + gold + Utils.getMaxAllowedCitizens());
						}else{
							gplayer.sendError("You may not perform this command!");
						}
					}else if(args[0].equalsIgnoreCase("getuuid")){
						if(player.isOp() == true){
							if(args.length == 2){
								gplayer.sendMessage("UUID: " + gold + Bukkit.getOfflinePlayer(args[1]).getUniqueId());
							}else{
								gplayer.sendError("Usage: /" + cmd.getName() + " getuuid <player>");
							}
						}else{
							gplayer.sendError("You may not perform this command!");
						}
					}else if(args[0].equalsIgnoreCase("killall")){
						if(player.isOp() == true){
							for(Entity e : Utils.getGCAWorld().getEntities()){
								if(e instanceof LivingEntity){
									e.remove();
								}
							}
						}else{
							gplayer.sendError("You may not perform this command!");
						}
					}else if(args[0].equalsIgnoreCase("spawncop")){
						if(player.isOp() == true){
							Utils.spawnCop(player.getLocation());
						}else{
							gplayer.sendError("You may not perform this command!");
						}
					}else if(args[0].equalsIgnoreCase("apartment")){
						if(player.isOp() == true){
							if(args.length == 2){
								if(args[1].equalsIgnoreCase("create")){
									if(!creating.containsKey(player.getName())){
										ApartmentCreator creator = new ApartmentCreator(player, Utils.getNextApartmentID());
										creating.put(player.getName(), creator);
										gplayer.sendMessage("You are now creating an apartment!");
										creator.sendStep(creator.getStep());
									}else{
										gplayer.sendError("You are already creating something!");
									}
								}else{
									gplayer.sendError("Usage: /" + cmd.getName() + " apartment create");
								}
							}else{
								gplayer.sendError("Usage: /" + cmd.getName() + " apartment create");
							}
						}else{
							gplayer.sendError("You may not perform this command!");
						}
					}else if(args[0].equalsIgnoreCase("garage")){
						if(player.isOp() == true){
							if(args.length == 2){
								if(args[1].equalsIgnoreCase("create")){
									if(!creating.containsKey(player.getName())){
										GarageCreator creator = new GarageCreator(player, Utils.getNextGarageID());
										creating.put(player.getName(), creator);
										gplayer.sendMessage("You are now creating a garage!");
										creator.sendStep(creator.getStep());
									}else{
										gplayer.sendError("You are already creating something!");
									}
								}else{
									gplayer.sendError("Usage: /" + cmd.getName() + " garage create");
								}
							}else{
								gplayer.sendError("Usage: /" + cmd.getName() + " garage create");
							}
						}else{
							gplayer.sendError("You may not perform this command!");
						}
					}
				}
			}else if(cmd.getName().equalsIgnoreCase("stats")){
				if(args.length == 0){
					gplayer.sendStatistics(player);
				}else if(args.length == 1){
					if(Utils.isOnline(args[0]) == true){
						gplayer.sendStatistics(Bukkit.getPlayer(args[0]));
					}else{
						gplayer.sendStatistics(args[0]);
					}
				}else{
					gplayer.sendError("Usage: /" + cmd.getName() + " [player]");
				}
			}else if(cmd.getName().equalsIgnoreCase("mission") || cmd.getName().equalsIgnoreCase("m")){
				if(args.length == 0){
					if(gplayer.hasMission() == true){
						gplayer.sendMessageHeader("Mission Overview");
						gplayer.sendMissionOverview(true);
					}else{
						gplayer.sendAvailableMissions();
					}
				}else if(args[0].equalsIgnoreCase("cancel") || args[0].equalsIgnoreCase("quit")){
					if(gplayer.hasMission() == true && gplayer.getMission().getID() == 1){
						gplayer.sendError("You may not cancel this mission.");
					}else{
						gplayer.setMission(0);
						gplayer.setCurrentObjectiveID(0);
						gplayer.sendMessage("You have cancelled your current mission!");
					}
				}
			}else if(cmd.getName().equalsIgnoreCase("job") || cmd.getName().equalsIgnoreCase("j") || cmd.getName().equalsIgnoreCase("race")){
				if(gplayer.hasJob() == true){
					if(args.length > 0){
						if(args[0].equalsIgnoreCase("quit")){
							gplayer.getJobInstance().removePlayer(player);
							gplayer.sendMessage("You have quit your current job!");
						}else{
							gplayer.sendCommandHelp(Help.JOB, 1);
						}
					}else{
						gplayer.sendCommandHelp(Help.JOB, 1);
					}
				}else{
					gplayer.sendError("You don't have a job currently!");
				}
			}else if(cmd.getName().equalsIgnoreCase("wallet") || cmd.getName().equalsIgnoreCase("balance") || cmd.getName().equalsIgnoreCase("bal")){
				gplayer.sendMessage("Wallet Balance: " + gold + "$" + (int)gplayer.getWalletBalance());
			}else if(cmd.getName().equalsIgnoreCase("apartment") || cmd.getName().equalsIgnoreCase("a")){
				if(args.length != 0){
					if(args[0].equalsIgnoreCase("set")){
						if(args.length == 2){
							if(args[1].equalsIgnoreCase("primary")){
								if(gplayer.hasApartment() == true){
									Apartment apt = null;
									for(Apartment a : gplayer.getApartments()){
										if(a.getSpawn().distance(player.getLocation()) <= 15){
											apt = a;
											break;
										}
									}
									if(apt != null){
										gplayer.setPrimaryApartment(apt);
										gplayer.sendMessage("You have set your primary apartment to " + gold + apt.getName() + gray + "!");
									}else{
										gplayer.sendError("You must be standing in the apartment to make primary.");
									}
								}else{
									gplayer.sendError("You don't have an apartment!");
								}
							}else{
								gplayer.sendError("Usage: /" + cmd.getName() + " set primary");
							}
						}else{
							gplayer.sendError("Usage: /" + cmd.getName() + " set primary");
						}
					}else if(args[0].equalsIgnoreCase("list")){
						if(gplayer.hasApartment() == true){
							gplayer.sendMessageHeader("Apartments");
							for(Apartment a : gplayer.getApartments()){
								gplayer.sendMessage(gold + a.getName());
								player.sendMessage("  " + gold + "-" + gray + ChatColor.ITALIC + " Location: "
										+ "(" + a.getSpawn().getBlockX() + ", " + a.getSpawn().getBlockY() + ", " + a.getSpawn().getBlockZ() + ")");
							}
						}else{
							gplayer.sendError("You do not own any apartments!");
						}
					}else if(args[0].equalsIgnoreCase("pay")){
						if(args.length == 2){
							if(args[1].equalsIgnoreCase("rent")){
								if(gplayer.hasApartment() == true){
									List<Apartment> rent = new ArrayList<Apartment>();
									int total = 0;
									for(Apartment a : gplayer.getApartments()){
										if(gplayer.hasToPayRent(a) == true){
											rent.add(a);
											total += a.getRent();
										}
									}
									if(rent.size() > 0){
										if(gplayer.getWalletBalance() >= total){
											for(Apartment a : rent){
												gplayer.setLastRentPayment(a);
											}
											gplayer.setWalletBalance(gplayer.getWalletBalance() - total);
											gplayer.refreshScoreboard();
											gplayer.sendMessage("You have paid your rent for the next week!");
										}else if(gplayer.getBankBalance() >= total){
											for(Apartment a : rent){
												gplayer.setLastRentPayment(a);
											}
											gplayer.setBankBalance(gplayer.getBankBalance() - total);
											gplayer.refreshScoreboard();
											gplayer.sendMessage("You have paid your rent for the next week!");
										}else{
											gplayer.sendError("You don't have enough money to pay rent!");
										}
									}else{
										gplayer.sendError("You don't have to pay rent for any apartments!");
									}
								}else{
									gplayer.sendError("You don't have an apartment!");
								}
							}else{
								gplayer.sendError("Usage: /" + cmd.getName() + " pay rent");
							}
						}else{
							gplayer.sendError("Usage: /" + cmd.getName() + " pay rent");
						}
					}else if(args[0].equalsIgnoreCase("allow")){
						if(args.length == 2){
							if(args[1].equalsIgnoreCase("crew")){
								if(gplayer.hasApartment() == true){
									if(gplayer.allowsApartmentForCrew() == true){
										gplayer.setAllowsApartmentForCrew(false);
										gplayer.sendMessage("You are no longer allowing crew into your apartment!");
									}else{
										gplayer.setAllowsApartmentForCrew(true);
										gplayer.sendMessage("You are now allowing crew into your apartment!");
									}
								}else{
									gplayer.sendError("You do not own any apartments!");
								}
							}else{
								gplayer.sendError("Usage: /" + cmd.getName() + " allow crew");
							}
						}else{
							gplayer.sendError("Usage: /" + cmd.getName() + " allow crew");
						}
					}else{
						gplayer.sendCommandHelp(Help.APARTMENT, 1);
					}
				}else{
					gplayer.sendCommandHelp(Help.APARTMENT, 1);
				}
			}else if(cmd.getName().equalsIgnoreCase("crew") || cmd.getName().equalsIgnoreCase("c")){
				if(args.length > 0){
					if(args[0].equalsIgnoreCase("list")){
						if(args.length == 1){
							gplayer.sendCrewList(1);
						}else if(args.length == 2){
							if(Utils.isInteger(args[1]) == true){
								gplayer.sendCrewList(Integer.parseInt(args[1]));
							}else{
								gplayer.sendError("You must enter a valid page number!");
							}
						}else{
							gplayer.sendError("Usage: /" + cmd.getName() + " list [page]");
						}
					}else if(args[0].equalsIgnoreCase("create")){
						if(args.length == 2){
							if(gplayer.hasCrew() == false){
								int cost = Crew.getCrewCreationCost();
								if(gplayer.getWalletBalance() >= cost){
									String name = args[1];
									if(name.length() >= 4 && name.length() <= 16){
										if(Utils.containsSymbols(name) == false){
											if(Utils.containsCurses(name) == false){
												if(Crew.doesCrewExist(name) == false){
													Crew crew = new Crew(Utils.getNextCrewID());
													crew.setName(name);
													crew.addMember(player.getName());
													gplayer.setRank(CrewRank.LEADER);
													crew.setPrimaryColor(Utils.randInt(1, 15));
													crew.setSecondaryColor(Utils.randInt(1, 15));
													crew.giveCrewArmor(player);
													gplayer.setWalletBalance(gplayer.getWalletBalance() - cost);
													gplayer.sendMessage("You have created a new crew named " + gold + name + gray + "!");
												}else{
													gplayer.sendError("There is already a crew by that name!");
												}
											}else{
												gplayer.sendError("The crew name may not contain vulgarites.");
											}
										}else{
											gplayer.sendError("The crew name may not contain symbols.");
										}
									}else{
										gplayer.sendError("The crew name must be 4-16 characters!");
									}
								}else{
									gplayer.sendError("You need at least $" + cost + " to create a crew!");
								}
							}else{
								gplayer.sendError("You must leave your current crew to create a new one!");
							}
						}else{
							gplayer.sendError("Usage: /" + cmd.getName() + " create <name>");
						}
					}else if(args[0].equalsIgnoreCase("rename")){
						if(args.length == 2){
							if(gplayer.hasCrew() == true){
								if(gplayer.getCrewRank() == CrewRank.LEADER){
									String name = args[1];
									if(name.length() >= 4 && name.length() <= 16){
										if(Utils.containsSymbols(name) == false){
											if(Utils.containsCurses(name) == false){
												if(Crew.doesCrewExist(name) == false){
													gplayer.getCrew().setName(name);
													gplayer.getCrew().broadcastMessage(gold + player.getName() + gray + " has renamed the gang to " + gold + name + gray + "!");
												}else{
													gplayer.sendError("There is already a crew by that name!");
												}
											}else{
												gplayer.sendError("The crew name may not contain vulgarites.");
											}
										}else{
											gplayer.sendError("The crew name may not contain symbols.");
										}
									}else{
										gplayer.sendError("The crew name must be 4-16 characters!");
									}
								}else{
									gplayer.sendError("You must be the leader to do this!");
								}
							}else{
								gplayer.sendError("You don't have a crew!");
							}
						}else{
							gplayer.sendError("Usage: /" + cmd.getName() + " rename <name>");
						}
					}else if(args[0].equalsIgnoreCase("disband")){
						if(gplayer.hasCrew() == true){
							if(gplayer.getCrewRank() == CrewRank.LEADER){
								gplayer.getCrew().broadcastMessage(gold + player.getName() + gray + " has disbanded the crew!");
								gplayer.getCrew().removeCrewArmor();
								gplayer.getCrew().disband();
							}else{
								gplayer.sendError("You must be the crew leader to do this!");
							}
						}else{
							gplayer.sendError("You don't have a crew!");
						}
					}else if(args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("inv")){
						if(args.length == 2){
							if(gplayer.hasCrew() == true){
								if(gplayer.getCrewRank() == CrewRank.LEADER || gplayer.getCrewRank() == CrewRank.MOD){
									if(gplayer.getCrew().getMembers().size() < 5){
										if(Bukkit.getPlayer(args[1]) != null && Bukkit.getPlayer(args[1]).isOnline() == true){
											Player invitedPlayer = Bukkit.getPlayer(args[1]);
											if(!invited.containsKey(invitedPlayer.getName())){
												GPlayer invitedGPlayer = new GPlayer(invitedPlayer);
												if(invitedGPlayer.hasCrew() == false){
													InviteTask task = new InviteTask(gplayer, invitedGPlayer);
													task.runTaskTimer(this, 0, 20);
													invited.put(invitedPlayer.getName(), task);
													gplayer.sendNotification("Crew", gray + "You have invited " + gold + invitedPlayer.getName() + gray + " to your crew!");
													invitedGPlayer.sendNotification("Crew", "You have been invited to join " + gold + gplayer.getCrew().getName() + gray + "!");
													invitedPlayer.sendMessage(gold + "  " + TextUtils.getArrow() + gray + " Type " + gold + "/crew join " + gplayer.getCrew().getName() + gray + " to accept!");
												}else{
													gplayer.sendError("That player already has a crew!");
												}
											}else{
												gplayer.sendError("That player already has a pending crew invitation!");
											}
										}else{
											if(args[1].equalsIgnoreCase("Lamar")){
												gplayer.sendMessage(gold + "Lamar: " + gray + "Yo crew seem lame, bruh. I'ma pass.");
											}else{
												gplayer.sendError("That player is currently not online!");
											}
										}
									}else{
										gplayer.sendError("You may not invite any more players to your crew!");
									}
								}else{
									gplayer.sendError("You must be a crew moderator to do this!");
								}
							}else{
								gplayer.sendError("You don't have a crew!");
							}
						}else{
							gplayer.sendError("Usage: /" + cmd.getName() + " invite <player>");
						}
					}else if(args[0].equalsIgnoreCase("join")){
						if(args.length == 2){
							if(gplayer.hasCrew() == false){
								if(invited.containsKey(player.getName())){
									InviteTask task = invited.get(player.getName());
									if(task.getSender().getCrew().getName().equalsIgnoreCase(args[1])){
										if(task.getSender().getCrew().getMembers().size() < 5){
											task.getSender().getCrew().addMember(player.getName());
											gplayer.sendNotification("Crew", "You have joined " + gold + task.getSender().getCrew().getName() + gray + "!");
											gplayer.getCrew().giveCrewArmor(player);
											task.getSender().getCrew().broadcastMessage(gold + player.getName() + gray + " has joined the crew!");
											task.cancel();
											invited.remove(player.getName());
										}else{
											gplayer.sendError("That crew has reached its maximum amount of members!");
											task.cancel();
											invited.remove(player.getName());
										}
									}else{
										gplayer.sendError("That crew has not invited you to join them!");
									}
								}else{
									gplayer.sendError("You haven't been invited to join a crew!");
								}
							}else{
								gplayer.sendError("You already have a crew!");
							}
						}else{
							gplayer.sendError("Usage: /" + cmd.getName() + " join <name>");
						}
					}else if(args[0].equalsIgnoreCase("leave")){
						if(args.length == 1){
							if(gplayer.hasCrew() == true){
								if(gplayer.getCrewRank() != CrewRank.LEADER){
									gplayer.getCrew().broadcastMessage(gold + player.getName() + gray + " has left the crew!");
									gplayer.getCrew().removeMember(player.getName());
									gplayer.clearArmor();
									gplayer.sendNotification("Crew", "You have left your crew!");
								}else{
									gplayer.sendError("You must give leadership to another member or disband the crew to do this!");
								}
							}else{
								gplayer.sendError("You don't have a crew!");
							}
						}else{
							gplayer.sendError("Usage: /" + cmd.getName() + " leave");
						}
					}else if(args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("i")){
						if(args.length == 2){
							if(Crew.doesCrewExist(args[1]) == true){
								gplayer.sendCrewInformation(Crew.getCrew(args[1]));
							}else{
								GPlayer gp = new GPlayer(args[1]);
								if(Bukkit.getPlayer(args[1]) != null && Bukkit.getPlayer(args[1]).isOnline() == true){
									gp = new GPlayer(Bukkit.getPlayer(args[1]));
								}
								if(gp.getName() != null){
									gplayer.sendCrewInformation(gp.getCrew());
								}else{
									gplayer.sendError("That crew does not exist!");
								}
							}
						}else if(args.length == 1){
							if(gplayer.hasCrew() == true){
								gplayer.sendCrewInformation(gplayer.getCrew());
							}else{
								gplayer.sendError("Usage: /" + cmd.getName() + " info <name>");
							}
						}else{
							gplayer.sendError("Usage: /" + cmd.getName() + " info <name>");
						}
					}else if(args[0].equalsIgnoreCase("chat") || args[0].equalsIgnoreCase("c")){
						if(args.length == 1){
							if(gplayer.hasCrew() == true){
								if(!crewChat.contains(player.getName())){
									crewChat.add(player.getName());
									gplayer.sendNotification("Crew", "You are now talking in " + gold + "crew " + gray + "chat!");
								}else{
									crewChat.remove(player.getName());
									gplayer.sendNotification("Crew", "You are now talking in " + gold + "public " + gray + "chat!");
								}
							}else{
								gplayer.sendError("You don't have a crew!");
							}
						}else{
							gplayer.sendError("Usage: /" + cmd.getName() + " chat");
						}
					}else if(args[0].equalsIgnoreCase("colors")){
						if(args.length == 1){
							if(gplayer.hasCrew() == true){
								if(gplayer.getCrewRank() == CrewRank.LEADER){
									player.openInventory(Crew.getColorsInventory(1));
								}else{
									gplayer.sendError("You must be the leader to do this!");
								}
							}else{
								gplayer.sendError("You don't have a crew!");
							}
						}else{
							gplayer.sendError("Usage: /" + cmd.getName() + " colors");
						}
					}else if(args[0].equalsIgnoreCase("promote")){
						if(args.length == 2){
							if(gplayer.hasCrew() == true){
								if(gplayer.getCrewRank() == CrewRank.LEADER){
									GPlayer gp = new GPlayer(args[1]);
									if(Bukkit.getPlayer(args[1]) != null && Bukkit.getPlayer(args[1]).isOnline() == true){
										gp = new GPlayer(Bukkit.getPlayer(args[1]));
									}
									if(gp.getName() != null){
										if(gp.hasCrew() == true && gp.getCrew().getName().equalsIgnoreCase(gplayer.getCrew().getName())){
											if(gp.getCrewRank() != CrewRank.MOD){
												gp.setRank(CrewRank.MOD);
												if(Bukkit.getPlayer(args[1]) != null && Bukkit.getPlayer(args[1]).isOnline() == true){
													gp.sendNotification("Crew", "You have been promoted to " + gold + WordUtils.capitalizeFully(gp.getCrewRank().toString()) + gray + "!");
												}
												gplayer.sendNotification("Crew", "You have promoted " + gold + gp.getName() + gray + " to " + gold + WordUtils.capitalizeFully(gp.getCrewRank().toString()) + 
														gray + 
														"!");
											}else{
												gplayer.sendError("You may not promote this player!");
											}
										}else{
											gplayer.sendError("That player is not in your crew!");
										}
									}else{
										gplayer.sendError("That player does not exist!");
									}
								}else{
									gplayer.sendError("You must be the leader to do this!");
								}
							}else{
								gplayer.sendError("You don't have a crew!");
							}
						}else{
							gplayer.sendError("Usage: /" + cmd.getName() + " promote <player>");
						}
					}else if(args[0].equalsIgnoreCase("demote")){
						if(args.length == 2){
							if(gplayer.hasCrew() == true){
								if(gplayer.getCrewRank() == CrewRank.LEADER){
									GPlayer gp = new GPlayer(args[1]);
									if(Bukkit.getPlayer(args[1]) != null && Bukkit.getPlayer(args[1]).isOnline() == true){
										gp = new GPlayer(Bukkit.getPlayer(args[1]));
									}
									if(gp.getName() != null){
										if(gp.hasCrew() == true && gp.getCrew().getName().equalsIgnoreCase(gplayer.getCrew().getName())){
											if(gp.getCrewRank() == CrewRank.MOD){
												gp.setRank(CrewRank.MEMBER);
												if(Bukkit.getPlayer(args[1]) != null && Bukkit.getPlayer(args[1]).isOnline() == true){
													gp.sendNotification("Crew", "You have been demoted to " + gold + WordUtils.capitalizeFully(gp.getCrewRank().toString()) + gray + "!");
												}
												gplayer.sendNotification("Crew", "You have demoted " + gold + gp.getName() + gray + " to " + gold + WordUtils.capitalizeFully(gp.getCrewRank().toString()) + 
														gray + 
														"!");
											}else{
												gplayer.sendError("You may not demote this player!");
											}
										}else{
											gplayer.sendError("That player is not in your crew!");
										}
									}else{
										gplayer.sendError("That player does not exist!");
									}
								}else{
									gplayer.sendError("You must be the leader to do this!");
								}
							}else{
								gplayer.sendError("You don't have a crew!");
							}
						}else{
							gplayer.sendError("Usage: /" + cmd.getName() + " promote <player>");
						}
					}else if(args[0].equalsIgnoreCase("kick")){
						if(args.length == 2){
							if(gplayer.hasCrew() == true){
								if(gplayer.getCrewRank() == CrewRank.LEADER || gplayer.getCrewRank() == CrewRank.MOD){
									GPlayer gp = new GPlayer(args[1]);
									if(Bukkit.getPlayer(args[1]) != null && Bukkit.getPlayer(args[1]).isOnline() == true){
										gp = new GPlayer(Bukkit.getPlayer(args[1]));
									}
									if(gp.getName() != null){
										if(gp.hasCrew() == true && gp.getCrew().getName().equalsIgnoreCase(gplayer.getCrew().getName())){
											if(gp.getCrewRank() != gplayer.getCrewRank()){
												gplayer.getCrew().removeMember(gp.getName());
												gplayer.getCrew().broadcastMessage(gold + gp.getName() + gray + " has been kicked from the crew!");
											}else{
												gplayer.sendError("You may not kick this player!");
											}
										}else{
											gplayer.sendError("That player is not in your crew!");
										}
									}else{
										gplayer.sendError("That player does not exist!");
									}
								}else{
									gplayer.sendError("You must be a crew moderator to do this!");
								}
							}else{
								gplayer.sendError("You don't have a crew!");
							}
						}else{
							gplayer.sendError("Usage: /" + cmd.getName() + " kick <player>");
						}
					}else if(args[0].equalsIgnoreCase("setleader")){
						if(args.length == 2){
							if(gplayer.hasCrew() == true){
								if(gplayer.getCrewRank() == CrewRank.LEADER){
									GPlayer gp = new GPlayer(args[1]);
									if(Bukkit.getPlayer(args[1]) != null && Bukkit.getPlayer(args[1]).isOnline() == true){
										gp = new GPlayer(Bukkit.getPlayer(args[1]));
									}
									if(gp.getName() != null){
										if(gp.hasCrew() == true && gp.getCrew().getName().equalsIgnoreCase(gplayer.getCrew().getName())){
											gplayer.setRank(CrewRank.MOD);
											gp.setRank(CrewRank.LEADER);
											gplayer.getCrew().broadcastMessage(gold + gp.getName() + gray + " has been made the crew leader!");
										}else{
											gplayer.sendError("That player is not in your crew!");
										}
									}else{
										gplayer.sendError("That player does not exist!");
									}
								}else{
									gplayer.sendError("You must be the leader to do this!");
								}
							}else{
								gplayer.sendError("You don't have a crew!");
							}
						}else{
							gplayer.sendError("Usage: /" + cmd.getName() + " setleader <player>");
						}
					}else if(args[0].equalsIgnoreCase("help")){
						if(args.length == 1){
							gplayer.sendCommandHelp(Help.CREW, 1);
						}else if(args.length == 2){
							if(Utils.isInteger(args[1]) == true){
								gplayer.sendCommandHelp(Help.CREW, Integer.parseInt(args[1]));
							}else{
								gplayer.sendError("You must enter a valid page number!");
							}
						}else{
							gplayer.sendError("Usage: /" + cmd.getName() + " help [page]");
						}
					}
				}else{
					if(gplayer.hasCrew() == true){
						gplayer.sendCrewInformation(gplayer.getCrew());
					}else{
						gplayer.sendCommandHelp(Help.CREW, 1);
					}
				}
			}else if(cmd.getName().equalsIgnoreCase("friends") || cmd.getName().equalsIgnoreCase("f") || cmd.getName().equalsIgnoreCase("friend")){
				if(args.length == 0){
					gplayer.sendCommandHelp(Help.FRIENDS, 1);
				}else if(args.length > 0){
					if(args[0].equalsIgnoreCase("list")){
						if(gplayer.getFriends().size() > 0){
							if(args.length == 1){
								gplayer.sendFriendsList(1);
							}else if(args.length == 2){
								if(Utils.isInteger(args[1]) == true){
									gplayer.sendFriendsList(Integer.parseInt(args[1]));
								}else{
									gplayer.sendError("You must enter a valid page number!");
								}
							}else{
								gplayer.sendError("Usage: /" + cmd.getName() + " list [page]");
							}
						}else{
							gplayer.sendError("You currently have no friends!");
						}
					}else if(args[0].equalsIgnoreCase("request")){
						if(args.length == 2){
							if(!args[1].equalsIgnoreCase("accept")){
								if(Utils.isOnline(args[1]) == true){
									if(args[1].equalsIgnoreCase(player.getName()) == false){
										Player receiverPlayer = Bukkit.getPlayer(args[1]);
										if(gplayer.isFriend(receiverPlayer.getName()) == false){
											if(!friendRequest.containsKey(receiverPlayer.getName())){
												GPlayer receiver = new GPlayer(receiverPlayer);
												FriendRequestTask task = new FriendRequestTask(gplayer, receiver);
												task.runTaskTimer(this, 0, 20);
												friendRequest.put(receiver.getName(), task);
												gplayer.sendNotification("Friends", "You have requested " + gold + receiver.getName() + gray + " to be your friend!");
												receiver.sendNotification("Friends", "You have been requested to be " + gold + player.getName() + gray + "'s friend!");
												receiverPlayer.sendMessage(gold + "  " + TextUtils.getArrow() + gray + " Type " + gold + "/friends request accept " + gray + "to accept!");
											}else{
												gplayer.sendError("That player already has a pending friend request!");
											}
										}else{
											gplayer.sendError("You are already friends with that player!");
										}
									}else{
										gplayer.sendError("You may not add yourself as a friend!");
									}
								}else{
									gplayer.sendError("That player is not online!");
								}
							}else{
								if(friendRequest.containsKey(player.getName())){
									FriendRequestTask task = friendRequest.get(player.getName());
									task.getSender().addFriend(player.getName());
									gplayer.addFriend(task.getSender().getName());
									task.cancel();
									friendRequest.remove(player.getName());
									
									task.getSender().sendNotification("Friends", gold + player.getName() + gray + " has accepted your friend request!");
									gplayer.sendNotification("Friends", "You have accepted " + gold + task.getSender().getName() + gray + "'s friend request!");
								}else{
									gplayer.sendError("You don't have a pending friend request!");
								}
							}
						}else{
							gplayer.sendError("Usage: /" + cmd.getName() + " request <player>");
						}
					}else if(args[0].equalsIgnoreCase("remove")){
						if(args.length == 2){
							if(gplayer.getFriends().contains(args[1])){
								GPlayer toRemove = new GPlayer(args[1]);
								if(Utils.isOnline(args[1]) == true){
									toRemove = new GPlayer(Bukkit.getPlayer(args[1]));
									toRemove.sendNotification("Friends", gold + player.getName() + gray + " has unfriended you!");
								}
								toRemove.removeFriend(player.getName());
								gplayer.removeFriend(toRemove.getName());
								gplayer.sendNotification("Friends", "You have unfriended " + gold + toRemove.getName() + gray + "!");
							}else{
								gplayer.sendError("That player is not your friend!");
							}
						}else{
							gplayer.sendError("Usage: /" + cmd.getName() + " remove <player>");
						}
					}else if(args[0].equalsIgnoreCase("tp")){
						if(args.length == 2){
							if(gplayer.getFriends().contains(args[1])){
								if(Utils.isOnline(args[1]) == true){
									GPlayer toTeleportTo = new GPlayer(Bukkit.getPlayer(args[1]));
									toTeleportTo.sendNotification("Friends", gold + player.getName() + gray + " is teleporting to you!");
									gplayer.sendNotification("Friends", "You are teleporting to " + gold + toTeleportTo.getName() + gray + "! Don't move.");
									TeleportTask task = new TeleportTask(player, Bukkit.getPlayer(args[1]));
									task.runTaskTimer(this, 0, 20);
									teleporting.put(player.getName(), task);
								}else{
									gplayer.sendError("That friend is not online!");
								}
							}else{
								gplayer.sendError("That player is not your friend!");
							}
						}else{
							gplayer.sendError("Usage: /" + cmd.getName() + " remove <player>");
						}
					}else{
						gplayer.sendCommandHelp(Help.FRIENDS, 1);
					}
				}
			}else if(cmd.getName().equalsIgnoreCase("kick")){
				if(gplayer.getRank().isStaff() == true){
					if(args.length >= 1){
						String reason = "You have been kicked!";
						if(args.length > 1){
							String newReason = "";
							for(int i = 1; i <= (args.length - 1); i++){
								if(i != 1){
									newReason = newReason + " " + args[i];
								}else{
									newReason = newReason + args[i];
								}
							}
							reason = newReason;
						}
						if(Utils.isOnline(args[0]) == true){
							Player target = Bukkit.getPlayer(args[0]);
							GPlayer gtarget = new GPlayer(target);
							if(gplayer.getRank().getLadder() < gtarget.getRank().getLadder()){
								target.kickPlayer(ChatColor.DARK_RED + "You have been kicked!\n\n"
										+ gold + "Staff: " + gray + player.getName() + "\n"
										+ gold + "Reason: " + gray + reason);
								gplayer.sendMessage("You have kicked " + gold + target.getName() + gray + "!");
							}else{
								gplayer.sendError("You may not kick this player!");
							}
						}else{
							gplayer.sendError("That player is not online!");
						}
					}else{
						gplayer.sendError("Usage: /" + cmd.getName() + " <player>");
					}
				}else{
					gplayer.sendError("You may not perform this command!");
				}
			}else if(cmd.getName().equalsIgnoreCase("top")){
				if(args.length == 0){
					gplayer.sendCommandHelp(Help.TOP, 1);
				}else if(args.length > 0){
					LeaderboardType type = null;
					for(LeaderboardType t : LeaderboardType.values()){
						if(args[0].equalsIgnoreCase(t.toString())){
							type = t;
							break;
						}
					}
					if(type != null){
						if(args.length == 1){
							gplayer.sendLeaderboard(type, 1);
						}else if(args.length == 2){
							if(Utils.isInteger(args[1]) == true){
								int page = Integer.parseInt(args[1]);
								gplayer.sendLeaderboard(type, page);
							}
						}
					}else{
						gplayer.sendError("That is an invalid leaderboard!");
					}
				}
			}else if(cmd.getName().equalsIgnoreCase("help") || cmd.getName().equalsIgnoreCase("?")){
				gplayer.sendCommandHelp(Help.GENERAL, 1);
			}
		}else{
			if(cmd.getName().equalsIgnoreCase("gca")){
				if(args.length == 3){
					if(args[0].equalsIgnoreCase("setmaxapartments")){
						GPlayer gtarget = new GPlayer(args[1]);
						if(Utils.isInteger(args[2]) == true){
							int max = Integer.parseInt(args[2]);
							gtarget.setMaximumApartments(max);
							sender.sendMessage("You have set " + args[1] + "'s maximum apartments to " + max + "!");
						}else{
							sender.sendMessage("You have entered an invalid maximum value!");
						}
					}else if(args[0].equalsIgnoreCase("givemoney")){
						GPlayer gtarget = null;
						if(Utils.isOnline(args[1]) == true){
							gtarget = new GPlayer(Bukkit.getPlayer(args[1]));
						}else{
							gtarget = new GPlayer(args[1]);
						}
						if(Utils.isInteger(args[2]) == true){
							int money = Integer.parseInt(args[2]);
							gtarget.setWalletBalance(gtarget.getWalletBalance() + money);
							sender.sendMessage("You have given " + args[1] + " $" + money + "!");
						}else{
							sender.sendMessage("You have entered an invalid amount of money!");
						}
					}else if(args[0].equalsIgnoreCase("addlevels")){
						GPlayer gtarget = new GPlayer(args[1]);
						if(Bukkit.getPlayer(args[1]) != null && Bukkit.getPlayer(args[1]).isOnline() == true){
							gtarget = new GPlayer(Bukkit.getPlayer(args[1]));
						}
						if(Utils.isInteger(args[2]) == true){
							int level = Integer.parseInt(args[2]);
							gtarget.levelUp(level);
							sender.sendMessage("You have added " + level + " level(s) to " + args[1] + "!");
						}else{
							sender.sendMessage("You have entered an invalid amount of money!");
						}
					}else if(args[0].equalsIgnoreCase("setrank")){
						GPlayer gtarget = new GPlayer(args[1]);
						if(Bukkit.getPlayer(args[1]) != null && Bukkit.getPlayer(args[1]).isOnline() == true){
							gtarget = new GPlayer(Bukkit.getPlayer(args[1]));
						}
						Rank rank = null;
						for(Rank r : Rank.values()){
							if(r.toString().equalsIgnoreCase(args[2])){
								rank = r;
								break;
							}
						}
						if(rank != null){
							gtarget.setRank(rank);
							if(rank.getLadder() > gtarget.getRankLadder()){
								gtarget.setRankLadder(rank.getLadder());
							}
							if(rank == Rank.THUG){
								gtarget.setMaximumApartments(3);
							}else if(rank == Rank.PIMP){
								gtarget.setMaximumApartments(6);
							}else if(rank == Rank.GODFATHER){
								gtarget.setMaximumApartments(10);
							}
							sender.sendMessage("You have set " + args[1] + "'s rank to " + WordUtils.capitalizeFully(rank.toString()) + "!");
							gtarget.sendMessage("You are now a " + gold + WordUtils.capitalizeFully(rank.toString()) + gray + "!");
						}else{
							sender.sendMessage("You have entered an invalid rank!");
						}
					}else{
						sender.sendMessage("Invalid command!");
					}
				}else{
					sender.sendMessage("-=-(*)-=- Console Help -=-(*)-=-");
					sender.sendMessage("/gca setmaxapartments <player> <max> - Set the max amount of apartments the player can own!");
					sender.sendMessage("/gca givemoney <player> <money> - Give the player money!");
					sender.sendMessage("/gca addlevels <player> <levels> - Give the player levels!");
					sender.sendMessage("/gca setrank <player> <rank> - Set the player's rank!");
				}
			}
		}
		return false;
	}
}
