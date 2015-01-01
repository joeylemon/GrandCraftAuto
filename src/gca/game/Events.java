package gca.game;

import gca.core.Main;
import gca.game.apartment.Apartment;
import gca.game.apartment.ApartmentCreator;
import gca.game.cars.Car;
import gca.game.crew.Crew;
import gca.game.crew.CrewMember;
import gca.game.districts.DistrictCreator;
import gca.game.garage.Garage;
import gca.game.garage.GarageCreator;
import gca.game.garage.GarageInstance;
import gca.game.gps.Destination;
import gca.game.gps.GPS;
import gca.game.inventories.Bank;
import gca.game.inventories.BankInventory;
import gca.game.inventories.Bounties;
import gca.game.inventories.InventoryHandler;
import gca.game.inventories.Slot;
import gca.game.inventories.Store;
import gca.game.jobs.FreeForAll;
import gca.game.jobs.FreeForAllCreator;
import gca.game.jobs.Job;
import gca.game.jobs.JobInstance;
import gca.game.jobs.JobJoiner;
import gca.game.jobs.JobState;
import gca.game.jobs.Race;
import gca.game.jobs.RaceCreator;
import gca.game.jobs.TeamDeathmatch;
import gca.game.jobs.TeamDeathmatchCreator;
import gca.game.jobs.ValueType;
import gca.game.missions.Character;
import gca.game.missions.Mission;
import gca.game.missions.MissionType;
import gca.game.missions.SideMission;
import gca.game.missions.SideMissionType;
import gca.game.missions.objectives.ApproachObjective;
import gca.game.missions.objectives.HoldUpObjective;
import gca.game.missions.objectives.KillTargetObjective;
import gca.game.missions.objectives.ObtainItemsObjective;
import gca.game.missions.objectives.ReachDestinationObjective;
import gca.game.missions.objectives.ReturnVehicleObjective;
import gca.game.missions.objectives.RobStationObjective;
import gca.game.missions.objectives.StealVehicleObjective;
import gca.game.player.GPlayer;
import gca.game.weapons.Grenade;
import gca.game.weapons.Gun;
import gca.game.weapons.MeleeWeapon;
import gca.game.weapons.Weapon;
import gca.tasks.KidneyHarvestTask;
import gca.tasks.KnockOutTask;
import gca.tasks.LockpickTask;
import gca.tasks.ProstituteFollowTask;
import gca.tasks.ReloadTask;
import gca.tasks.RobTask;
import gca.tasks.TeleportTask;
import gca.utils.EffectUtils;
import gca.utils.ObjectType;
import gca.utils.TextUtils;
import gca.utils.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import com.gmail.filoghost.holograms.api.Hologram;
import com.gmail.filoghost.holograms.api.HolographicDisplaysAPI;
import com.gmail.filoghost.holograms.object.HologramBase;
import com.gmail.filoghost.holograms.utils.VisibilityManager;

public class Events implements Listener{
	
	Main main = Main.getInstance();
	private String gray = ChatColor.GRAY + "";
	private String gold = ChatColor.GOLD + "";
	
	@EventHandler
	public void asyncPlayerPreLogin(AsyncPlayerPreLoginEvent event){
		if(Utils.isServerWhitelisted() == true){
			if(Utils.isPlayerWhitelisted(event.getName()) == false){
				String kickmsg = ChatColor.DARK_RED + "Unfortunately, you didn't sign up for the Grand Craft Auto beta testing :(" + "\n" + "However, it is possible to make a last-minute signup at our forums!";
				event.disallow(Result.KICK_WHITELIST, kickmsg);
				event.setKickMessage(kickmsg);
			}
		}
	}
	
	@EventHandler
	public void playerJoin(PlayerJoinEvent event){
		final Player player = event.getPlayer();
		final GPlayer gplayer = new GPlayer(player);
		
		event.setJoinMessage(ChatColor.DARK_GREEN + "+ " + gray + player.getName());
		
		gplayer.refreshScoreboard();
		gplayer.sendTabTitle(gray + "Welcome to " + gold + "Grand Craft Auto" + gray + "!", ChatColor.DARK_RED + "" + ChatColor.ITALIC + "The" + ChatColor.GOLD + ChatColor.ITALIC + "Legend" + 
		ChatColor.YELLOW + ChatColor.ITALIC + "Craft");
		
		player.getInventory().setItem(Slot.GPS.getSlot(), GPS.getCompass());
		player.getInventory().setItem(Slot.PHONE.getSlot(), Utils.renameItem(new ItemStack(Material.PAPER), ChatColor.YELLOW + "iFruit Cell Phone"));
		if(gplayer.hasACar() == true && gplayer.getCurrentCar() != null){
			player.getInventory().setItem(Slot.CAR.getSlot(), gplayer.getCurrentCar().getItemStack());
		}
		if(gplayer.hasCrew() == true){
			gplayer.getCrew().giveCrewArmor(player);
		}else{
			gplayer.clearArmor();
		}
		gplayer.updateTabListName();
		gplayer.setName(player.getName());
		if(gplayer.getRank() == null){
			gplayer.setRank(Rank.DEFAULT);
		}
		
		if(main.playtime.containsKey(player.getName())){
			main.playtime.remove(player.getName());
		}
		main.playtime.put(player.getName(), gplayer.getPlaytime());
		
		main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
			public void run(){
				gplayer.refreshNametag();
			}
		}, 20);
		
		if(gplayer.hasJoinedBefore() == true){
			/*
			 * Spawn task
			 */
			gplayer.spawn();
		}else{
			/*
			 * Tutorial
			 */
			gplayer.setMaximumApartments(1);
			gplayer.setLevel(1);
			gplayer.setAllowsApartmentForCrew(true);
			gplayer.setWalletBalance(0);
			gplayer.setBankBalance(0);
			gplayer.setHasJoinedBefore(true);
			main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
				public void run(){
					Tutorial tutorial = new Tutorial(player);
					main.tutorial.put(player.getName(), tutorial);
					tutorial.advanceStep();
				}
			}, 20);
		}
	}
	
	@EventHandler
	public void playerToggleFlight(PlayerToggleFlightEvent event){
		Player player = event.getPlayer();
		if(main.spawning.contains(player.getName()) || main.tutorial.containsKey(player.getName())){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void playerQuit(PlayerQuitEvent event){
		GPlayer gplayer = new GPlayer(event.getPlayer());
		event.setQuitMessage(ChatColor.DARK_RED + "- " + gray + event.getPlayer().getName());
		gplayer.logout();
	}
	
	@EventHandler
	public void playerKick(PlayerKickEvent event){
		GPlayer gplayer = new GPlayer(event.getPlayer());
		event.setLeaveMessage(ChatColor.DARK_RED + "- " + gray + event.getPlayer().getName());
		gplayer.logout();
	}
	
	@EventHandler
	public void playerCommandPreprocessEvent(PlayerCommandPreprocessEvent event){
		if(Utils.isCommandBlocked(event.getMessage()) == true){
			GPlayer gplayer = new GPlayer(event.getPlayer());
			event.setCancelled(true);
			gplayer.sendError("You are not allowed to perform that command!");
		}
	}
	
	@EventHandler
	public void asyncPlayerChatEvent(AsyncPlayerChatEvent event){
		Player player = event.getPlayer();
		GPlayer gplayer = new GPlayer(player);
		if(!main.creating.containsKey(player.getName())){
			if(main.messageTask.contains(player.getName())){
				event.setCancelled(true);
			}else if(main.knockout.contains(player.getName())){
				event.setCancelled(true);
			}else{
				for(Player p : event.getRecipients()){
					if(main.messageTask.contains(p.getName())){
						event.getRecipients().remove(p.getName());
					}else if(main.knockout.contains(p.getName())){
						event.getRecipients().remove(p.getName());
					}
				}
				if(!main.crewChat.contains(player.getName())){
					if(player.hasPermission("chat.color")){
						event.setMessage(event.getMessage().replaceAll("&", "§"));
					}
					String symbol = "";
					if(gplayer.getLevelSymbol().length() > 0){
						symbol = gplayer.getLevelSymbol() + " ";
					}
					String format = gold + "[" + gplayer.getLevel() + "] " + symbol + gplayer.getRank().getChatColor() + player.getName() + " " + gold + TextUtils.getArrow() + gray + " " + 
							event.getMessage();
					if(gplayer.getRank() == Rank.THUG){
						format = gold + "[" + gold + "T" + gold + "] " + format;
					}else if(gplayer.getRank() == Rank.PIMP){
						format = gold + "[" + gold + "P" + gold + "] " + format;
					}else if(gplayer.getRank() == Rank.GODFATHER){
						format = gold + "[" + gold + "G" + gold + "] " + format;
					}
					event.setFormat(format);
				}else{
					event.setCancelled(true);
					gplayer.getCrew().broadcastRawMessage(gold + ChatColor.ITALIC + "Crew Chat " + ChatColor.RESET + gray + TextUtils.getArrow() + gold + " " + player.getName() + gold + ": " + 
					gray + event.getMessage());
				}
			}
		}else{
			if(main.creating.containsKey(player.getName())){
				Creator c = main.creating.get(player.getName());
				if(c instanceof ApartmentCreator){
					ApartmentCreator creator = (ApartmentCreator) main.creating.get(player.getName());
					if(creator.getStep() >= 5){
						event.setCancelled(true);
						if(creator.getStep() == 5){
							creator.setName(event.getMessage());
							gplayer.sendMessage("You have set the name to " + gold + event.getMessage() + gray + "!");
							creator.advanceStep();
						}else if(creator.getStep() == 6){
							if(Utils.isInteger(event.getMessage())){
								int price = Integer.parseInt(event.getMessage());
								creator.setPrice(price);
								gplayer.sendMessage("You have set the price to " + gold + "$" + price + gray + "!");
								creator.advanceStep();
							}
						}else if(creator.getStep() == 7){
							if(Utils.isInteger(event.getMessage())){
								int rent = Integer.parseInt(event.getMessage());
								creator.setRent(rent);
								gplayer.sendMessage("You have set the rent price to " + gold + "$" + rent + gray + "!");
								main.creating.remove(player.getName());
								gplayer.sendMessage("You have successfully created a new apartment!");
							}
						}
					}
				}else if(c instanceof RaceCreator){
					RaceCreator creator = (RaceCreator) main.creating.get(player.getName());
					if(creator.getStep() >= 3){
						event.setCancelled(true);
						if(creator.getStep() == 3){
							if(Utils.isInteger(event.getMessage())){
								int value = Integer.parseInt(event.getMessage());
								creator.setLaps(value);
								gplayer.sendMessage("You have set the laps to " + gold + value + gray + "!");
								creator.advanceStep();
							}
						}else if(creator.getStep() == 4){
							Car car = null;
							for(Car v : Car.values()){
								if(v.toString().equalsIgnoreCase(event.getMessage())){
									car = v;
									break;
								}
							}
							if(car != null){
								creator.setCar(car);
								gplayer.sendMessage("You have set the car to " + gold + car.getName() + gray + "!");
								creator.advanceStep();
							}else{
								gplayer.sendError("You have entered an invalid car!");
							}
						}else if(creator.getStep() == 5){
							creator.setName(event.getMessage());
							gplayer.sendMessage("You have set the name to " + gold + event.getMessage() + gray + "!");
							main.creating.remove(player.getName());
							gplayer.sendMessage("You have successfully created a new race!");
						}
					}
				}else if(c instanceof FreeForAllCreator){
					FreeForAllCreator creator = (FreeForAllCreator) main.creating.get(player.getName());
					if(creator.getStep() >= 2){
						event.setCancelled(true);
						if(creator.getStep() == 2){
							if(Utils.isInteger(event.getMessage())){
								int value = Integer.parseInt(event.getMessage());
								creator.setKillsRequired(value);
								gplayer.sendMessage("You have set the kills required to " + gold + value + gray + "!");
								creator.advanceStep();
							}
						}else if(creator.getStep() == 3){
							creator.setName(event.getMessage());
							gplayer.sendMessage("You have set the name to " + gold + event.getMessage() + gray + "!");
							main.creating.remove(player.getName());
							gplayer.sendMessage("You have successfully created a new free for all!");
						}
					}
				}else if(c instanceof TeamDeathmatchCreator){
					TeamDeathmatchCreator creator = (TeamDeathmatchCreator) main.creating.get(player.getName());
					if(creator.getStep() >= 3){
						event.setCancelled(true);
						if(creator.getStep() == 3){
							if(Utils.isInteger(event.getMessage())){
								int value = Integer.parseInt(event.getMessage());
								creator.setKillsRequired(value);
								gplayer.sendMessage("You have set the kills required to " + gold + value + gray + "!");
								creator.advanceStep();
							}
						}else if(creator.getStep() == 4){
							creator.setName(event.getMessage());
							gplayer.sendMessage("You have set the name to " + gold + event.getMessage() + gray + "!");
							main.creating.remove(player.getName());
							gplayer.sendMessage("You have successfully created a new team deathmatch!");
						}
					}
				}else if(c instanceof DistrictCreator){
					DistrictCreator creator = (DistrictCreator) main.creating.get(player.getName());
					if(creator.getStep() == 3){
						event.setCancelled(true);
						creator.setName(event.getMessage());
						creator.finish();
						gplayer.sendMessage("You have set the name to " + gold + event.getMessage() + gray + "!");
						main.creating.remove(player.getName());
						gplayer.sendMessage("You have successfully created a new district!");
					}
				}
			}
		}
	}
	
	@EventHandler
	public void blockBreak(BlockBreakEvent event){
		if(event.getPlayer().isOp() == false){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void blockBreak(BlockPlaceEvent event){
		if(event.getPlayer().isOp() == false){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void creatureSpawn(CreatureSpawnEvent event){
		if(event.getSpawnReason() != SpawnReason.CUSTOM){
			event.setCancelled(true);
			if(Utils.getAliveCitizens() < Utils.getMaxAllowedCitizens()){
				Utils.spawnRandomVillager();
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void projectileHit(ProjectileHitEvent event){
	    BlockIterator iterator = new BlockIterator(event.getEntity().getWorld(), event.getEntity().getLocation().toVector(), event.getEntity().getVelocity().normalize(), 0, 4);
	    Block hitBlock = null;

	    while(iterator.hasNext()) {
	        hitBlock = iterator.next();
	        if(hitBlock.getTypeId()!=0){
	            break;
	        }
	    }
		
		if(hitBlock != null){
			for(Entity e : event.getEntity().getNearbyEntities(20, 20, 20)){
				if(e instanceof Player){
					Player p = (Player) e;
					p.playEffect(hitBlock.getLocation(), Effect.STEP_SOUND, hitBlock.getTypeId());
				}
			}
		}
	}
	
	@EventHandler
	public void playerItemConsume(PlayerItemConsumeEvent event){
		Player player = event.getPlayer();
		GPlayer gplayer = new GPlayer(player);
		if(event.getItem().hasItemMeta() == true && event.getItem().getItemMeta().hasDisplayName() == true){
			String name = ChatColor.stripColor(event.getItem().getItemMeta().getDisplayName());
			if(name.contains("Beer")){
				event.setCancelled(true);
				player.setItemInHand(null);
				double newhealth = player.getHealth() + 1.5;
				if(newhealth <= 20){
					player.setHealth(newhealth);
				}else{
					player.setHealth(20);
				}
				if(player.hasPotionEffect(PotionEffectType.SPEED)){
					player.removePotionEffect(PotionEffectType.SPEED);
				}
				player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 1));
				if(player.hasPotionEffect(PotionEffectType.CONFUSION)){
					player.removePotionEffect(PotionEffectType.CONFUSION);
				}
				player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 300, 0));
				
				double consume = 0;
				if(main.consumption.containsKey(player.getName())){
					consume = main.consumption.get(player.getName());
					main.consumption.remove(player.getName());
				}
				double consumed = consume + 4;
				main.consumption.put(player.getName(), consumed);
				if(consumed >= Utils.getMaxConsumptionLevel()){
					if(consumed <= 35){
						if(player.hasPotionEffect(PotionEffectType.POISON)){
							player.removePotionEffect(PotionEffectType.POISON);
						}
						player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 300, (int)consumed - Utils.getMaxConsumptionLevel()));
						gplayer.sendMessage(ChatColor.ITALIC + Utils.getRandomSickStatement());
					}else{
						double newhearts = player.getHealth() - 6;
						if(newhearts <= 0){
							gplayer.death();
						}else{
							player.setHealth(newhearts);
						}
					}
				}
			}else if(name.contains("Soda")){
				event.setCancelled(true);
				player.setItemInHand(null);
				double newhealth = player.getHealth() + 1;
				if(newhealth <= 20){
					player.setHealth(newhealth);
				}else{
					player.setHealth(20);
				}
			}else if(name.contains("Wine")){
				event.setCancelled(true);
				player.setItemInHand(null);
				double newhealth = player.getHealth() + 1.2;
				if(newhealth <= 20){
					player.setHealth(newhealth);
				}else{
					player.setHealth(20);
				}
			}else if(name.contains("Whiskey")){
				event.setCancelled(true);
				player.setItemInHand(null);
				double newhealth = player.getHealth() + 2;
				if(newhealth <= 20){
					player.setHealth(newhealth);
				}else{
					player.setHealth(20);
				}
				if(player.hasPotionEffect(PotionEffectType.ABSORPTION)){
					player.removePotionEffect(PotionEffectType.ABSORPTION);
				}
				player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 360, 1));
				if(player.hasPotionEffect(PotionEffectType.SLOW)){
					player.removePotionEffect(PotionEffectType.SLOW);
				}
				player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 260, 0));
				if(player.hasPotionEffect(PotionEffectType.CONFUSION)){
					player.removePotionEffect(PotionEffectType.CONFUSION);
				}
				player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 260, 0));
				
				double consume = 0;
				if(main.consumption.containsKey(player.getName())){
					consume = main.consumption.get(player.getName());
					main.consumption.remove(player.getName());
				}
				double consumed = consume + 7;
				main.consumption.put(player.getName(), consumed);
				if(consumed >= Utils.getMaxConsumptionLevel()){
					if(consumed <= 35){
						if(player.hasPotionEffect(PotionEffectType.POISON)){
							player.removePotionEffect(PotionEffectType.POISON);
						}
						player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 300, (int)consumed - Utils.getMaxConsumptionLevel()));
						gplayer.sendMessage(ChatColor.ITALIC + Utils.getRandomSickStatement());
					}else{
						double newhearts = player.getHealth() - 6;
						if(newhearts <= 0){
							gplayer.death();
						}else{
							player.setHealth(newhearts);
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.MONITOR)
	public void playerInteract(PlayerInteractEvent event){
		final Player player = event.getPlayer();
		GPlayer gplayer = new GPlayer(player);
		ItemStack item = player.getItemInHand();
		if(!main.creating.containsKey(player.getName())){
			if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
				if(item != null){
					/*
					 * Shoot weapon
					 */
					if(gplayer.hasWeaponInHand() == true){
						if(gplayer.getWeaponInHand() instanceof Gun || gplayer.getWeaponInHand() instanceof Grenade){
							event.setCancelled(true);
							gplayer.shootGun();
						}
					}else if(item.getType() == Material.SUGAR){
						/*
						 * Cocaine
						 */
						if(player.hasPotionEffect(PotionEffectType.BLINDNESS)){
							player.removePotionEffect(PotionEffectType.BLINDNESS);
						}
						if(player.hasPotionEffect(PotionEffectType.SPEED)){
							player.removePotionEffect(PotionEffectType.SPEED);
						}
						if(player.hasPotionEffect(PotionEffectType.CONFUSION)){
							player.removePotionEffect(PotionEffectType.CONFUSION);
						}
						player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 300, 0));
						player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 300, 2));
						player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 300, 0));
						if(item.getAmount() > 1){
							item.setAmount(item.getAmount() - 1);
						}else{
							player.setItemInHand(null);
						}
						gplayer.addMentalState(0.5);
						if(gplayer.hasCopsNearby() == true){
							if(!main.invoked.containsKey(player.getName())){
								gplayer.setHasInvoked(VillagerType.COP, null);
							}
						}
						double consume = 0;
						if(main.consumption.containsKey(player.getName())){
							consume = main.consumption.get(player.getName());
							main.consumption.remove(player.getName());
						}
						double consumed = consume + 3.5;
						main.consumption.put(player.getName(), consumed);
						if(consumed >= Utils.getMaxConsumptionLevel()){
							if(consumed <= 35){
								if(player.hasPotionEffect(PotionEffectType.POISON)){
									player.removePotionEffect(PotionEffectType.POISON);
								}
								player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 300, (int)consumed - Utils.getMaxConsumptionLevel()));
								gplayer.sendMessage(ChatColor.ITALIC + Utils.getRandomSickStatement());
							}else{
								double newhearts = player.getHealth() - 6;
								if(newhearts <= 0){
									gplayer.death();
								}else{
									player.setHealth(newhearts);
								}
							}
						}
					}else if(item.getType() == Material.COMPASS){
						event.setCancelled(true);
						player.openInventory(GPS.getInventory(player));
					}else if(item.getType() == Material.PAPER){
						if(item.hasItemMeta() == true && item.getItemMeta().hasDisplayName() == true){
							if(item.getItemMeta().getDisplayName().contains("Phone")){
								event.setCancelled(true);
								player.openInventory(Utils.getPhoneInventory());
							}
						}
					}
				}
			}
			if(event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
				if(gplayer.hasWeaponInHand() == true){
					if(player.isSneaking() == false){
						/*
						 * Reload weapon
						 */
						if(!main.reloadCooldown.contains(player.getName()) && !main.reloading.containsKey(player.getName())){
							if(gplayer.getWeaponInHand() instanceof Gun){
								Gun gun = (Gun) gplayer.getWeaponInHand();
								if(player.getInventory().contains(gun.getAmmoType().getItem().getType())){
									if(gplayer.isZoomed() == true){
										gplayer.setZoomed(false);
									}
									ReloadTask task = new ReloadTask(player, gun);
									task.runTaskTimer(main, 0, gun.getReloadRate());
									main.reloading.put(player.getName(), task);
									main.reloadCooldown.add(player.getName());
									main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
										public void run(){
											main.reloadCooldown.remove(player.getName());
										}
									}, 100);
								}
							}
						}
					}else if(player.isSneaking() == true){
						/*
						 * Zoom weapon
						 */
						if(gplayer.isZoomed() == true){
							gplayer.setZoomed(false);
						}else if(gplayer.isZoomed() == false){
							gplayer.setZoomed(true);
						}
					}
				}
			}
			if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
				if(event.getClickedBlock() != null){
					if(event.getClickedBlock().getType() == Material.WALL_SIGN){
						Sign sign = (Sign) event.getClickedBlock().getState();
						if(sign.getLine(0).contains("Apartment")){
							/*
							 * Purchase apartment
							 */
							if(gplayer.getTotalApartments() < gplayer.getMaximumApartments()){
								Apartment apt = null;
								for(Apartment a : Apartment.list()){
									if(a.getSignLocation().distance(sign.getLocation()) < 1){
										apt = a;
										break;
									}
								}
								if(apt != null){
									if(gplayer.ownsApartment(apt) == false){
										if(gplayer.getWalletBalance() >= apt.getPrice()){
											gplayer.setWalletBalance(gplayer.getWalletBalance() - apt.getPrice());
											gplayer.addApartment(apt);
											gplayer.sendMessage("You now have the keys to " + gold + apt.getName() + gray + "!");
										}else{
											gplayer.sendMessage("You need $" + gold + (apt.getPrice() - gplayer.getWalletBalance()) + gray + " more to purchase this apartment!");
										}
									}else{
										gplayer.sendError("You already own this apartment!");
									}
								}else{
									gplayer.sendError("This apartment does not exist!");
								}
							}else{
								gplayer.sendError("You may not purchase another apartment!");
							}
						}
					}else{
						if(event.getClickedBlock().getType() == Material.CHEST){
							if(event.getClickedBlock().getType() == Material.CHEST){
								Chest chest = (Chest) event.getClickedBlock().getState();
								if(gplayer.hasApartment() == true){
									boolean allow = false;
									boolean breakInitial = false;
									for(Apartment a : gplayer.getApartments()){
										if(breakInitial == false){
											for(Location l : a.getChestLocations()){
												if(l.getBlock().getLocation().getX() == event.getClickedBlock().getLocation().getX() 
														&& l.getBlock().getLocation().getY() == event.getClickedBlock().getLocation().getY()
														&& l.getBlock().getLocation().getZ() == event.getClickedBlock().getLocation().getZ()){
													allow = true;
													breakInitial = true;
													break;
												}
											}	
										}else{
											break;
										}
									}
									if(allow == false){
										event.setCancelled(true);
									}else{
										event.setCancelled(true);
										player.openInventory(gplayer.getChestInventory(chest));
									}
								}else{
									event.setCancelled(true);
								}
							}else{
								event.setCancelled(true);
							}
						}else if(event.getClickedBlock().getType() == Material.WOODEN_DOOR){
							if(player.isOp() == false){
								if(gplayer.hasApartment() == true || gplayer.crewHasApartment() == true){
									boolean allow = false;
									if(gplayer.hasApartment() == true){
										boolean breakFirst = false;
										for(Apartment a : gplayer.getApartments()){
											if(breakFirst == false){
												for(Location l : a.getDoorLocations()){
													if(l.getBlock().getLocation().getX() == event.getClickedBlock().getLocation().getX() 
															&& l.getBlock().getLocation().getY() == event.getClickedBlock().getLocation().getY()
															&& l.getBlock().getLocation().getZ() == event.getClickedBlock().getLocation().getZ()){
														allow = true;
														breakFirst = true;
														break;
													}
												}
											}else{
												break;
											}
										}
									}else if(gplayer.crewHasApartment() == true){
										boolean breakFirst = false;
										boolean breakSecond = false;
										for(Apartment a : gplayer.getCrewApartments()){
											if(breakFirst == false){
												for(Location l : a.getDoorLocations()){
													if(breakSecond == false){
														if(l.getBlock().getLocation().getX() == event.getClickedBlock().getLocation().getX() 
																&& l.getBlock().getLocation().getY() == event.getClickedBlock().getLocation().getY()
																&& l.getBlock().getLocation().getZ() == event.getClickedBlock().getLocation().getZ()){
															for(CrewMember m : gplayer.getCrew().getMembers()){
																if(m.getPlayer().hasApartment() == true){
																	if(m.getPlayer().ownsApartment(a) == true){
																		if(m.getPlayer().allowsApartmentForCrew() == true){
																			allow = true;
																			breakFirst = true;
																			breakSecond = true;
																			break;
																		}else{
																			allow = false;
																			breakFirst = true;
																			breakSecond = true;
																			break;
																		}
																	}
																}
															}
														}
													}else{
														break;
													}
												}
											}else{
												break;
											}
										}
									}
									if(allow == false){
										event.setCancelled(true);
									}
								}else{
									event.setCancelled(true);
								}
							}
						}else if(event.getClickedBlock().getType() == Material.TRAP_DOOR){
							event.setCancelled(true);
						}
					}
				}
			}
		}else{
			if(main.creating.containsKey(player.getName())){
				Creator c = main.creating.get(player.getName());
				if(c instanceof ApartmentCreator){
					ApartmentCreator creator = (ApartmentCreator) main.creating.get(player.getName());
					if(creator.getStep() == 1){
						if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
							if(event.getClickedBlock() != null){
								if(event.getClickedBlock().getType() == Material.WOODEN_DOOR){
									event.setCancelled(true);
									creator.createDoor(event.getClickedBlock().getLocation());
									gplayer.sendMessage("You have added a door!");
								}
							}
						}
					}else if(creator.getStep() == 2){
						if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
							if(event.getClickedBlock() != null){
								if(event.getClickedBlock().getType() == Material.CHEST){
									event.setCancelled(true);
									creator.createChest(event.getClickedBlock().getLocation());
									gplayer.sendMessage("You have added a chest!");
								}
							}
						}
					}else if(creator.getStep() == 3){
						if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)){
							creator.setSpawn(player.getLocation());
							gplayer.sendMessage("You have set the spawn!");
							creator.advanceStep();
						}
					}else if(creator.getStep() == 4){
						if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
							if(event.getClickedBlock() != null){
								if(event.getClickedBlock().getState() instanceof Sign){
									event.setCancelled(true);
									creator.setSign(event.getClickedBlock().getLocation());
									gplayer.sendMessage("You have set the sign!");
									creator.advanceStep();
								}
							}
						}
					}
					if(event.getAction().equals(Action.LEFT_CLICK_AIR)){
						if(event.getClickedBlock() == null){
							if(creator.getStep() <= 4){
								creator.advanceStep();
							}
						}
					}
				}else if(c instanceof GarageCreator){
					GarageCreator creator = (GarageCreator) main.creating.get(player.getName());
					if(creator.getStep() == 1){
						if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)){
							creator.setLocation(player.getLocation());
							gplayer.sendMessage("You have set the garage location!");
							creator.advanceStep();
						}
					}else if(creator.getStep() == 2){
						if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
							if(event.getClickedBlock() != null){
								event.setCancelled(true);
								creator.addCarSpawn(player.getTargetBlock(null, 7).getLocation());
								gplayer.sendMessage("You have added a car spawn!");
							}
						}
					}
					if(event.getAction().equals(Action.LEFT_CLICK_AIR)){
						if(event.getClickedBlock() == null){
							if(creator.getStep() == 2){
								Garage.setTotalGarages(Garage.getTotalGarages() + 1);
								main.creating.remove(player.getName());
								gplayer.sendMessage("You have created a garage!");
							}
						}
					}
				}else if(c instanceof RaceCreator){
					RaceCreator creator = (RaceCreator) main.creating.get(player.getName());
					if(creator.getStep() == 1){
						if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)){
							creator.addStartingLocation(player.getLocation());
							gplayer.sendMessage("You have added a starting location!");
						}
					}else if(creator.getStep() == 2){
						if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)){
							creator.addCheckpoint(player.getLocation());
							gplayer.sendMessage("You have added a checkpoint!");
						}
					}
					if(event.getAction().equals(Action.LEFT_CLICK_AIR)){
						if(event.getClickedBlock() == null){
							if(creator.getStep() <= 2){
								creator.advanceStep();
							}
						}
					}
				}else if(c instanceof FreeForAllCreator){
					FreeForAllCreator creator = (FreeForAllCreator) main.creating.get(player.getName());
					if(creator.getStep() == 1){
						if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)){
							creator.addStartingLocation(player.getLocation());
							gplayer.sendMessage("You have added a starting location!");
						}
					}
					if(event.getAction().equals(Action.LEFT_CLICK_AIR)){
						if(event.getClickedBlock() == null){
							if(creator.getStep() <= 2){
								creator.advanceStep();
							}
						}
					}
				}else if(c instanceof TeamDeathmatchCreator){
					TeamDeathmatchCreator creator = (TeamDeathmatchCreator) main.creating.get(player.getName());
					if(creator.getStep() == 1){
						if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)){
							creator.addStartingLocation(1, player.getLocation());
							gplayer.sendMessage("You have added a Team 1 starting location!");
						}
					}else if(creator.getStep() == 2){
						if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)){
							creator.addStartingLocation(2, player.getLocation());
							gplayer.sendMessage("You have added a Team 2 starting location!");
						}
					}
					if(event.getAction().equals(Action.LEFT_CLICK_AIR)){
						if(event.getClickedBlock() == null){
							if(creator.getStep() <= 2){
								creator.advanceStep();
							}
						}
					}
				}else if(c instanceof DistrictCreator){
					DistrictCreator creator = (DistrictCreator) main.creating.get(player.getName());
					if(creator.getStep() == 1){
						if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getClickedBlock() != null){
							creator.setFirstPosition(event.getClickedBlock().getLocation());
							gplayer.sendMessage("You have set the first position!");
							creator.advanceStep();
						}
					}else if(creator.getStep() == 2){
						if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getClickedBlock() != null){
							creator.setSecondPosition(event.getClickedBlock().getLocation());
							gplayer.sendMessage("You have set the second position!");
							creator.advanceStep();
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void inventoryClose(InventoryCloseEvent event){
		if(event.getPlayer() instanceof Player){
			Player player = (Player) event.getPlayer();
			GPlayer gplayer = new GPlayer(player);
			if(gplayer.hasApartment() == true){
				if(event.getInventory().getTitle().contains("Apartment Chest")){
					if(event.getInventory().getHolder() instanceof Chest){
						Chest chest = (Chest) event.getInventory().getHolder();
						gplayer.updateChestInventory(chest, event.getInventory());
					}
				}
			}
			if(event.getInventory().getTitle().contains("Cash")){
				gplayer.refreshScoreboard();
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void playerMove(PlayerMoveEvent event){
		Player player = event.getPlayer();
		GPlayer gplayer = new GPlayer(player);
		if(player.isOp() == false && gplayer.hasJob() == false && (event.getTo().getX() > Utils.getWorldBorder() || event.getTo().getX() < (Utils.getWorldBorder() * -1) || 
				event.getTo().getZ() > Utils.getWorldBorder() || event.getTo().getZ() < (Utils.getWorldBorder() * -1))){
			player.teleport(event.getFrom());
			gplayer.sendError("You have reached the city's border!");
		}
		if(main.spawning.contains(player.getName()) || main.tutorial.containsKey(player.getName()) || main.knockout.contains(player.getName())){
			player.teleport(event.getFrom());
		}else if(main.teleporting.containsKey(player.getName())){
			if(event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ()){
				TeleportTask task = main.teleporting.get(player.getName());
				new GPlayer(task.getToTeleportTo()).sendMessage(gold + player.getName() + gray + " has cancelled the teleportation!");
				gplayer.sendError("You moved while teleporting!");
				task.cancelTask();
			}
		}else{
			if(gplayer.hasMission() == true){
				if(gplayer.getObjective() instanceof ReachDestinationObjective){
					ReachDestinationObjective obj = (ReachDestinationObjective) gplayer.getObjective();
					if(player.getLocation().distance(obj.getDestination()) <= 20){
						gplayer.advanceObjective();
					}
				}
			}
			if(gplayer.hasJob() == false){
				Job job = null;
				for(JobJoiner j : JobJoiner.getJobJoiners()){
					if(j.getHologram().getLocation().distance(player.getLocation()) < 3){
						job = j.getJob();
						break;
					}
				}
				if(job != null){
					JobInstance jobinstance = null;
					for(JobInstance j : main.jobs){
						if(j.getJob().getName().equalsIgnoreCase(job.getName()) && j.getPlayers().size() < j.getJob().getMaximumPlayers() && j.getState() == JobState.WAITING){
							jobinstance = j;
							break;
						}
					}
					if(jobinstance == null){
						jobinstance = new JobInstance(job);
						main.jobs.add(jobinstance);
					}
					jobinstance.addPlayer(player);
					gplayer.sendMessage("You have joined " + gold + job.getName() + gray + "! (Quit with " + gold + "/job quit" + gray + ")");
				}
			}else{
				if(event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ()){
					if(gplayer.getJobInstance().getState() == JobState.PREGAME){
						if(gplayer.getJobInstance().getJob() instanceof FreeForAll || gplayer.getJobInstance().getJob() instanceof TeamDeathmatch){
							player.teleport(event.getFrom());
						}
					}
				}
			}
			if(Utils.isGarage(player.getLocation()) == true){
				if(!main.inGarage.containsKey(player.getName())){
					if(player.isInsideVehicle() == false){
						Garage garage = null;
						for(Garage g : Garage.list()){
							if(g.getLocation().distance(player.getLocation()) <= 15){
								garage = g;
							}
						}
						if(garage != null){
							if(gplayer.hasACar() == true){
								String arrow = gold + TextUtils.getArrow() + " ";
								int spawn = 0;
								List<Entity> cars = new ArrayList<Entity>();
								List<Hologram> holograms = new ArrayList<Hologram>();
								for(Car c : gplayer.getCars()){
									if(spawn < garage.getCarSpawns().size()){
										Hologram hologram = HolographicDisplaysAPI.createHologram(main, garage.getCarSpawns().get(spawn).add(0.5, 2.5, 0.5), 
												gold + c.getName(), 
												arrow + "Speed: " + gray + c.getBarSpeed() + "    ");
										Minecart minecart = (Minecart) Utils.getGCAWorld().spawnEntity(garage.getCarSpawns().get(spawn).add(0.5, 1.5, 0.5), EntityType.MINECART);
										Utils.setMetadata(minecart, "car", c.getName());
										Utils.setMetadata(minecart, "isGarageCar", true);
										for(Player p : Bukkit.getOnlinePlayers()){
											if(!p.getName().equalsIgnoreCase(player.getName())){
												Utils.getEntityHider().hideEntity(p, minecart);
											}
										}
										cars.add(minecart);
										holograms.add(hologram);
										spawn++;
									}else{
										break;
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
								main.inGarage.put(player.getName(), new GarageInstance(garage, cars, holograms));
							}
						}
					}
				}
			}else{
				if(main.inGarage.containsKey(player.getName())){
					main.inGarage.get(player.getName()).clearInstance();
					main.inGarage.remove(player.getName());
				}
			}
			if(gplayer.hasDestination() == true){
				if(player.getLocation().distance(gplayer.getDestination()) < 7){
					gplayer.removeDestination();
					gplayer.sendNotification("GPS", "You have reached your destination!");
				}
			}
		}
	}
	
	@EventHandler
	public void vehicleEnter(VehicleEnterEvent event){
		if(event.getEntered() instanceof Player){
			Player player = (Player) event.getEntered();
			GPlayer gplayer = new GPlayer(player);
			if(event.getVehicle().hasMetadata("car") == true){
				Car car = null;
				for(Car c : Car.values()){
					if(c.getName().contains((String)Utils.getMetadata(event.getVehicle(), "car", ObjectType.STRING))){
						car = c;
						break;
					}
				}
				if(car != null){
					/*
					 * Garage select car
					 */
					if(main.inGarage.containsKey(player.getName())){
						GarageInstance instance = main.inGarage.get(player.getName());
						if(gplayer.ownsCar(car) == true){
							event.setCancelled(true);
							gplayer.setCurrentCar(car);
							instance.clearInstance();
							Minecart cart = (Minecart) Utils.getGCAWorld().spawnEntity(event.getVehicle().getLocation(), EntityType.MINECART);
							Utils.setMetadata(cart, "car", car.getName());
							Utils.setMetadata(cart, "owner", player.getName());
							gplayer.sendNotification("Garage", "You have selected the " + gold + car.getName() + gray + "!");
							if(main.car.containsKey(player.getName())){
								main.car.get(player.getName()).remove();
								main.car.remove(player.getName());
							}
							main.car.put(player.getName(), cart);
							main.inGarage.remove(player.getName());
							cart.setPassenger(player);
						}
					}else{
						if(gplayer.hasMission() == true){
							/*
							 * Repossess objective
							 */
							if(gplayer.getObjective() instanceof StealVehicleObjective){
								StealVehicleObjective obj = (StealVehicleObjective) gplayer.getObjective();
								if(car == obj.getCar()){
									gplayer.advanceObjective();
									return;
								}
							}
						}
						if(event.getVehicle() instanceof Minecart){
							Minecart cart = (Minecart) event.getVehicle();
							if(Car.isLocked(event.getVehicle()) == true){
								event.setCancelled(true);
								if(player.getItemInHand().getType() != Material.TRIPWIRE_HOOK){
									gplayer.sendError("This vehicle is locked!");
								}else{
									boolean isOwner = false;
									if(Car.getOwner(cart) != null){
										if(Car.getOwner(cart).equalsIgnoreCase(player.getName())){
											isOwner = true;
										}
									}
									if(isOwner == false){
										if(!main.lockpicking.containsKey(player.getName())){
											LockpickTask task = new LockpickTask(player, cart);
											task.runTaskTimer(main, 60, 60);
											main.lockpicking.put(player.getName(), task);
											gplayer.sendMessage("You begin lockpicking the vehicle...");
										}
									}else{
										gplayer.sendError("You may not lockpick your own vehicle!");
									}
								}
								return;
							}
						}
						if(gplayer.hasJob() == true && gplayer.getJobInstance().getJob() instanceof Race){
							return;
						}
						if(main.currentcar.containsKey(player.getName())){
							main.currentcar.remove(player.getName());
						}
						main.currentcar.put(player.getName(), car);
						gplayer.sendMessage("You have entered a " + gold + car.getName() + gray + "!");
					}
				}
			}else{
				if(main.inGarage.containsKey(player.getName())){
					main.inGarage.get(player.getName()).clearInstance();
					main.inGarage.remove(player.getName());
				}
			}
		}else{
			event.setCancelled(true);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void vehicleMove(VehicleMoveEvent event){
		if(event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ()){
			if(event.getVehicle().hasMetadata("isGarageCar") == true || Car.isLocked(event.getVehicle()) == true){
				event.getVehicle().teleport(event.getFrom());
			}
			if(event.getVehicle().getPassenger() != null && event.getVehicle().getPassenger() instanceof Player){
				Player player = (Player) event.getVehicle().getPassenger();
				GPlayer gplayer = new GPlayer(player);
				if(gplayer.hasJob() == true && gplayer.getJobInstance().getJob() instanceof Race){
					JobInstance jobinstance = gplayer.getJobInstance();
					Race job = (Race) jobinstance.getJob();
					if(jobinstance.getState() == JobState.STARTED){
						int checkpoint = jobinstance.getPlayerValue(player, ValueType.CHECKPOINTS) + 1;
						Location loc = job.getCheckpoint(checkpoint).add(0, 1, 0);
						if(player.getLocation().distance(job.getCheckpoint(checkpoint)) < 5){
							if(checkpoint < job.getTotalCheckpoints()){
								jobinstance.setPlayerValue(player, ValueType.CHECKPOINTS, checkpoint);
								gplayer.sendNotification("Race", "You have reached a checkpoint! (" + gold + checkpoint + "/" + job.getTotalCheckpoints() + gray + ")");
								jobinstance.refreshPlayerScoreboards();
								player.sendBlockChange(loc, 0, (byte) 0);
								player.sendBlockChange(job.getCheckpoint(checkpoint + 1).add(0, 1, 0), 124, (byte) 0);
							}else{
								jobinstance.setPlayerValue(player, ValueType.CHECKPOINTS, 0);
								int newlaps = jobinstance.getPlayerValue(player, ValueType.LAPS) + 1;
								if(newlaps < job.getLaps()){
									jobinstance.setPlayerValue(player, ValueType.LAPS, newlaps);
									gplayer.sendNotification("Race", "You have finished a lap! (" + gold + newlaps + "/" + job.getLaps() + gray + ")");
									jobinstance.refreshPlayerScoreboards();
									player.sendBlockChange(loc, 0, (byte) 0);
									player.sendBlockChange(job.getCheckpoint(1).add(0, 1, 0), 124, (byte) 0);
								}else{
									player.sendBlockChange(loc, Utils.getGCAWorld().getBlockAt(loc).getTypeId(), Utils.getGCAWorld().getBlockAt(loc).getData());
									jobinstance.end(player.getName());
								}
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void vehicleExit(VehicleExitEvent event){
		if(event.getExited() instanceof Player){
			Player player = (Player) event.getExited();
			GPlayer gplayer = new GPlayer(player);
			if(gplayer.hasJob() == true){
				if(gplayer.getJobInstance().getJob() instanceof Race){
					event.setCancelled(true);
				}else{
					if(main.currentcar.containsKey(player.getName())){
						main.currentcar.remove(player.getName());
					}
				}
			}else{
				if(main.currentcar.containsKey(player.getName())){
					main.currentcar.remove(player.getName());
				}
			}
		}
	}
	
	@EventHandler
	public void playerPickupItem(PlayerPickupItemEvent event){
		Player player = event.getPlayer();
		GPlayer gplayer = new GPlayer(player);
		if(event.getItem().hasMetadata("value") == false){
			if(InventoryHandler.addItemToAppropriateSlot(player, event.getItem().getItemStack()) == true){
				event.setCancelled(true);
				event.getItem().remove();
				if(gplayer.hasMission() == true){
					if(gplayer.getObjective() instanceof ObtainItemsObjective){
						ObtainItemsObjective obj = (ObtainItemsObjective) gplayer.getObjective();
						if(event.getItem().getItemStack().getType() == obj.getItemType()){
							if(!main.invoked.containsKey(player.getName())){
								gplayer.setHasInvoked(obj.getToInvoke(), obj.getGangToInvoke());
							}else{
								main.invoked.get(player.getName()).setTimer(0);
							}
							obj.setAmountObtained(player.getName(), obj.getAmountObtained(player.getName()) + event.getItem().getItemStack().getAmount());
							if(obj.getAmountObtained(player.getName()) >= obj.getAmountToObtain()){
								gplayer.advanceObjective();
							}
						}
					}
				}
			}else{
				event.setCancelled(true);
			}
		}else{
			event.setCancelled(true);
			int value = 0;
			if(event.getItem().hasMetadata("value") == true){
				value = (int) Utils.getMetadata(event.getItem(), "value", ObjectType.INT);
			}
			gplayer.setWalletBalance(gplayer.getWalletBalance() + value);
			player.sendMessage(gray + "+ " + gold + "$" + value);
			gplayer.refreshScoreboard();
			event.getItem().remove();
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void inventoryClick(final InventoryClickEvent event){
		if(event.getWhoClicked() instanceof Player){
			final Player player = (Player) event.getWhoClicked();
			GPlayer gplayer = new GPlayer(player);
			InventoryType type = event.getInventory().getType();
			boolean armor = false;
			/*
			 * Raw slots 1-4 are the crafting slots in player inventory
			 */
			if(event.getCurrentItem() != null){
				if(event.getCurrentItem().getType() == Material.LEATHER_HELMET || event.getCurrentItem().getType() == Material.LEATHER_CHESTPLATE || 
						event.getCurrentItem().getType() == Material.LEATHER_LEGGINGS || event.getCurrentItem().getType() == Material.LEATHER_BOOTS){
					if(type != InventoryType.CHEST){
						armor = true;
					}
				}
				if(event.getCurrentItem().hasItemMeta() == true && event.getCurrentItem().getItemMeta().hasDisplayName() == true){
					String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
					if(name.contains("Phone") || name.contains("GPS") || (gplayer.getCurrentCar() != null && name.contains(gplayer.getCurrentCar().getName()))){
						event.setCancelled(true);
						return;
					}
				}
			}
			if(armor == false){
				if(type == InventoryType.CHEST){
					if(event.getInventory().getTitle() != null){
						String title = ChatColor.stripColor(event.getInventory().getTitle());
						Store store = null;
						for(Store s : Store.list()){
							if(title.contains(s.getName())){
								store = s;
								break;
							}
						}
						if(store != null){
							event.setCancelled(true);
							if(event.getCurrentItem() != null){
								if(event.getCurrentItem().hasItemMeta() == true && event.getCurrentItem().getItemMeta().hasDisplayName() == true){
									String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
									if(!title.contains("Categories")){
										if(name.contains("Go Back")){
											player.openInventory(store.getPage(1));
										}else{
											store.purchaseItem(player, event.getCurrentItem());
										}
									}else{
										if(name.contains("Shotguns")){
											player.openInventory(store.getPage(5));
										}else if(name.contains("Guns")){
											player.openInventory(store.getPage(2));
										}else if(name.contains("Melee")){
											player.openInventory(store.getPage(3));
										}else if(name.contains("Ammunition")){
											player.openInventory(store.getPage(4));
										}else if(name.contains("Explosives")){
											player.openInventory(store.getPage(6));
										}
									}
								}
							}
						}else{
							if(!event.getInventory().getTitle().contains("Apartment Chest")){
								if(event.getInventory().getTitle().contains("Select Mission")){
									if(event.getCurrentItem() != null){
										if(event.getCurrentItem().hasItemMeta() == true){
											if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Regular")){
												event.setCancelled(true);
												List<Character> givers = new ArrayList<Character>();
												List<Mission> availableMissions = new ArrayList<Mission>();
												for(Mission m : Mission.values()){
													if(m.getType() == MissionType.REGULAR){
														if(gplayer.hasCompletedMission(m.getID()) == false){
															if(!givers.contains(m.getGiver())){
																if(gplayer.getLevel() >= m.getMinimumLevel()){
																	availableMissions.add(m);
																	givers.add(m.getGiver());
																}
															}
														}
													}
												}
												Mission mission = null;
												if(availableMissions.size() > 0){
													for(Mission m : availableMissions){
														if(event.getInventory().getTitle().contains(m.getGiver().getName())){
															mission = m;
															break;
														}
													}
												}
												if(mission != null){
													gplayer.setMission(mission.getID());
													gplayer.sendMessageHeader("New Mission");
													gplayer.sendMissionOverview(false);
													player.closeInventory();
												}
											}else if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Daily")){
												event.setCancelled(true);
												List<Character> givers = new ArrayList<Character>();
												List<Mission> availableMissions = new ArrayList<Mission>();
												for(Mission m : Mission.values()){
													if(m.getType() == MissionType.SIDE_MISSION){
														if(SideMission.getSideMission(SideMissionType.DAILY).getID() == m.getID()){
															if(gplayer.canCompleteSideMission(SideMissionType.DAILY) == true){
																if(gplayer.getLevel() >= m.getMinimumLevel()){
																	availableMissions.add(m);
																	givers.add(m.getGiver());
																}
															}
														}
													}
												}
												Mission mission = null;
												if(availableMissions.size() > 0){
													for(Mission m : availableMissions){
														if(event.getInventory().getTitle().contains(m.getGiver().getName())){
															mission = m;
															break;
														}
													}
												}
												if(mission != null){
													gplayer.setMission(mission.getID());
													gplayer.sendMessageHeader("New Mission");
													gplayer.sendMissionOverview(false);
													player.closeInventory();
												}
											}
										}
									}
								}else if(event.getInventory().getTitle().contains("GPS")){
									event.setCancelled(true);
									if(event.getCurrentItem() != null){
										if(event.getCurrentItem().hasItemMeta() == true && event.getCurrentItem().getItemMeta().hasDisplayName() == true){
											String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
											if(event.getInventory().getTitle().contains("Apartments")){
												Apartment apt = null;
												for(Apartment a : gplayer.getApartments()){
													if(a.getName().equalsIgnoreCase(name)){
														apt = a;
														break;
													}
												}
												if(apt != null){
													Location loc = apt.getSpawn();
													player.setCompassTarget(loc);
													gplayer.setDestination(name, loc);
													player.closeInventory();
												}
											}else{
												if(!name.contains("Cancel") && !name.contains("Apartments") && !name.contains("Mission Objective")){
													Destination dest = null;
													for(Destination d : Destination.values()){
														if(name.toLowerCase().contains(d.toString().toLowerCase().replaceAll("_", " "))){
															dest = d;
															break;
														}
													}
													if(dest != null){
														Location loc = GPS.getNearest(player, dest);
														if(player.getLocation().distance(loc) >= 7){
															player.setCompassTarget(loc);
															gplayer.setDestination(name, loc.subtract(0, 1, 0));
															player.closeInventory();
														}else{
															gplayer.sendError("You are already at your destination!");
															player.closeInventory();
														}
													}
												}else{
													if(name.contains("Cancel")){
														if(gplayer.hasDestination() == true){
															gplayer.removeDestination();
															player.closeInventory();
															gplayer.sendNotification("GPS", "Your current route has been cancelled.");
														}else{
															gplayer.sendError("You currently don't have a route!");
														}
													}else if(name.contains("Apartments")){
														player.openInventory(GPS.getApartmentInventory(player));
													}else if(name.contains("Mission Objective")){
														ReachDestinationObjective obj = (ReachDestinationObjective) gplayer.getObjective();
														player.setCompassTarget(obj.getDestination());
														gplayer.setDestination(name, obj.getDestination());
														player.closeInventory();
													}
												}
											}
										}
									}
								}else if(event.getInventory().getTitle().contains("Withdraw or Deposit")){
									event.setCancelled(true);
									if(event.getCurrentItem() != null){
										if(event.getCurrentItem().hasItemMeta() == true && event.getCurrentItem().getItemMeta().hasDisplayName() == true){
											String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
											if(name.contains("Withdraw")){
												player.openInventory(Bank.getInventory(gplayer, BankInventory.WITHDRAW));
											}else if(name.contains("Deposit")){
												player.openInventory(Bank.getInventory(gplayer, BankInventory.DEPOSIT));
											}
										}
									}
								}else if(event.getInventory().getTitle().contains("Withdraw Cash") || event.getInventory().getTitle().contains("Deposit Cash")){
									event.setCancelled(true);
									if(event.getCurrentItem() != null){
										if(event.getCurrentItem().hasItemMeta() == true && event.getCurrentItem().getItemMeta().hasDisplayName() == true){
											String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).replaceAll("\\$", "");
											if(Utils.isInteger(name) == true){
												int amount = Integer.parseInt(name);
												if(event.getInventory().getTitle().contains("Withdraw Cash")){
													if(gplayer.getBankBalance() >= amount){
														gplayer.setBankBalance(gplayer.getBankBalance() - amount);
														gplayer.setWalletBalance(gplayer.getWalletBalance() + amount);
														gplayer.sendMessage("You have withdrawn " + gold + "$" + amount + gray + " from your bank!");
													}else{
														gplayer.sendError("You cannot withdraw that much cash!");
													}
												}else if(event.getInventory().getTitle().contains("Deposit Cash")){
													if(gplayer.getWalletBalance() >= amount){
														gplayer.setWalletBalance(gplayer.getWalletBalance() - amount);
														gplayer.setBankBalance(gplayer.getBankBalance() + amount);
														gplayer.sendMessage("You have deposited " + gold + "$" + amount + gray + " into your bank!");
													}else{
														gplayer.sendError("You cannot deposit that much cash!");
													}
												}
											}else{
												if(name.contains("Go Back")){
													player.openInventory(Bank.getInventory(gplayer, BankInventory.WITHDRAW_DEPOSIT));
												}
											}
										}
									}
								}else if(event.getInventory().getTitle().contains("Which color to change")){
									event.setCancelled(true);
									if(event.getCurrentItem() != null){
										if(event.getCurrentItem().hasItemMeta() == true && event.getCurrentItem().getItemMeta().hasDisplayName() == true){
											String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
											if(name.contains("Primary")){
												player.openInventory(Crew.getColorsInventory(2));
											}else if(name.contains("Secondary")){
												player.openInventory(Crew.getColorsInventory(3));
											}
										}
									}
								}else if(event.getInventory().getTitle().contains("Select the")){
									event.setCancelled(true);
									if(event.getCurrentItem() != null){
										if(event.getCurrentItem().getType() == Material.WOOL){
											if(gplayer.hasCrew() == true){
												int data = event.getCurrentItem().getData().getData();
												if(event.getInventory().getTitle().contains("Select the primary color")){
													gplayer.getCrew().setPrimaryColor(data);
													gplayer.getCrew().updateCrewArmor();
													player.closeInventory();
													gplayer.sendNotification("Crew", "You have set the crew's primary color to " + gold + Utils.getColorNameFromWool(data).toLowerCase() + gray + "!");
												}else if(event.getInventory().getTitle().contains("Select the secondary color")){
													gplayer.getCrew().setSecondaryColor(data);
													gplayer.getCrew().updateCrewArmor();
													player.closeInventory();
													gplayer.sendNotification("Crew", "You have set the crew's secondary color to " + gold + Utils.getColorNameFromWool(data).toLowerCase() + gray + "!");
												}
											}
										}
									}
								}else if(event.getInventory().getTitle().contains("Phone")){
									event.setCancelled(true);
									if(event.getCurrentItem() != null){
										if(event.getCurrentItem().hasItemMeta() == true && event.getCurrentItem().getItemMeta().hasDisplayName() == true){
											String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
											if(name.contains("Skills")){
												player.openInventory(Skill.getInventory(player));
											}else if(name.contains("Bounty")){
												player.openInventory(Bounties.getInventory(player));
											}
										}
									}
								}else if(event.getInventory().getTitle().contains("Thug Points")){
									event.setCancelled(true);
									if(event.getCurrentItem() != null){
										if(event.getCurrentItem().hasItemMeta() == true && event.getCurrentItem().getItemMeta().hasDisplayName() == true){
											String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).toLowerCase();
											Skill skill = null;
											for(Skill s : Skill.values()){
												if(name.equalsIgnoreCase(s.toString().toLowerCase())){
													skill = s;
													break;
												}
											}
											if(skill != null){
												if(gplayer.getSkillLevel(skill) >= 50){
													gplayer.sendError("This skill is maxed out, therefore you may not upgrade it!");
													return;
												}
												if(gplayer.getThugPoints() <= 0){
													gplayer.sendError("You don't have enough thug points to upgrade this skill!");
													return;
												}
												gplayer.addSkillLevels(skill, 1);
												gplayer.removeThugPoints(1);
												gplayer.sendMessage("You have upgraded your " + name + " skill!");
												player.openInventory(Skill.getInventory(player));
											}
										}
									}
								}else if(event.getInventory().getTitle().contains("Place a bounty")){
									event.setCancelled(true);
									if(event.getCurrentItem() != null){
										if(event.getCurrentItem().getType() == Material.SKULL_ITEM){
											if(event.getCurrentItem().hasItemMeta() == true && event.getCurrentItem().getItemMeta().hasDisplayName() == true){
												String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
												player.openInventory(Bounties.getSetInventory(name));
											}
										}
									}
								}else if(event.getInventory().getTitle().contains("Bounty - ")){
									event.setCancelled(true);
									if(event.getCurrentItem() != null){
										if(event.getCurrentItem().getType() == Material.GOLD_NUGGET){
											if(event.getCurrentItem().hasItemMeta() == true && event.getCurrentItem().getItemMeta().hasDisplayName() == true){
												String bountyPlayer = ChatColor.stripColor(event.getInventory().getTitle()).replaceAll("Bounty - ", "");
												String item = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).replaceAll("\\$", "");
												if(Utils.isInteger(item) == true){
													int amount = Integer.parseInt(item);
													if(gplayer.getWalletBalance() >= amount){
														gplayer.setWalletBalance(gplayer.getWalletBalance() - amount);
														gplayer.refreshScoreboard();
														int currentBounty = 0;
														if(main.bounties.containsKey(bountyPlayer)){
															currentBounty = main.bounties.get(bountyPlayer);
															main.bounties.remove(bountyPlayer);
														}
														currentBounty += amount;
														main.bounties.put(bountyPlayer, currentBounty);
														Utils.broadcastMessage(gold + bountyPlayer + gray + " now has a " + gold + "$" + currentBounty + gray + " bounty!");
														player.closeInventory();
													}else{
														gplayer.sendError("You don't have enough money for this!");
													}
												}
											}
										}
									}
								}else if(event.getInventory().getTitle().contains("How long")){
									event.setCancelled(true);
									if(event.getCurrentItem() != null){
										if(event.getCurrentItem().getType() == Material.GOLD_NUGGET){
											if(event.getCurrentItem().hasItemMeta() == true && event.getCurrentItem().getItemMeta().hasDisplayName() == true){
												String item = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
												String strTime = gold + "30 " + gray + "seconds!";
												int time = 30;
												int price = 15;
												if(item.contains("1 minute")){
													strTime = gold + "1 " + gray + "minute!";
													time = 60;
													price = 30;
												}else if(item.contains("2 minutes")){
													strTime = gold + "2 " + gray + "minutes!";
													time = 120;
													price = 60;
												}
												if(gplayer.getWalletBalance() >= price){
													Villager villager = null;
													for(Entity e : Utils.getGCAWorld().getEntities()){
														if(e.getEntityId() == event.getInventory().getMaxStackSize() && e.getType() == EntityType.VILLAGER){
															villager = (Villager) e;
														}
													}
													if(villager != null){
														player.closeInventory();
														gplayer.setWalletBalance(gplayer.getWalletBalance() - price);
														ProstituteFollowTask task = new ProstituteFollowTask(player, villager, time);
														task.runTaskTimer(main, 0, 10);
														main.prostitute.put(player.getName(), task);
														Utils.setMetadata(villager, "prostitute", true);
														gplayer.sendMessage("You have purchased this prostitute for " + strTime);
														gplayer.refreshScoreboard();
													}
												}else{
													gplayer.sendError("You do not have enough money for this!");
												}
											}
										}
									}
								}else{
									event.setCancelled(true);
								}
							}else{
								if(event.getClick() == ClickType.LEFT || event.getClick() == ClickType.RIGHT){
									if(event.getCursor() != null && event.getCursor().getType() != Material.AIR){
										if(event.getRawSlot() >= 27){
											try{
												if((event.getCurrentItem().getAmount() >= InventoryHandler.getItemMaxStackSize(event.getCurrentItem()) && Weapon.isWeapon(event.getCurrentItem().getType()) == false) 
														|| !InventoryHandler.getAllowedMaterialsForSlot(event.getSlot()).contains(event.getCursor().getType())){
													event.setCancelled(true);
													gplayer.sendError("That item may not be placed in that slot!");
													main.cancelDrop.add(player.getName());
													main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
														public void run(){
															player.closeInventory();
														}
													}, 1);
												}
											}catch (Exception ex){
												event.setCancelled(true);
											}
										}
									}
								}
								if(event.getClick() == ClickType.SHIFT_RIGHT || event.getClick() == ClickType.SHIFT_LEFT){
									if(event.getCurrentItem() != null){
										if(event.getRawSlot() <= 27){
											if(InventoryHandler.addItemToAppropriateSlot(player, event.getCurrentItem()) == true){
												if(event.getRawSlot() <= 27){
													event.getInventory().setItem(event.getRawSlot(), null);
												}else{
													player.getInventory().setItem(InventoryHandler.convertRawSlot(event.getRawSlot()), null);	
												}
											}else{
												event.setCancelled(true);
												gplayer.sendError("That item may not be placed in that slot!");
												player.closeInventory();
											}
										}else{
											event.getInventory().addItem(event.getCurrentItem());
											player.getInventory().setItem(InventoryHandler.convertRawSlot(event.getRawSlot()), null);
										}
									}
								}
							}
						}
					}
				}
				if(type == InventoryType.CRAFTING){
					if(event.getClick() == ClickType.LEFT || event.getClick() == ClickType.RIGHT){
						if(event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.AIR){
							if(event.getCursor() != null && event.getCursor().getType() != Material.AIR){
								try{
									if(!InventoryHandler.getAllowedMaterialsForSlot(event.getSlot()).contains(event.getCursor().getType())){
										event.setCancelled(true);
										gplayer.sendError("That item may not be placed in that slot!");
										main.cancelDrop.add(player.getName());
										player.closeInventory();
									}
								}catch (Exception ex){
									event.setCancelled(true);
								}
							}
						}else if(event.getCurrentItem() != null){
							if(event.getCursor() != null && event.getCursor().getType() != Material.AIR){
								if((event.getCurrentItem().getAmount() >= InventoryHandler.getItemMaxStackSize(event.getCurrentItem()) && Weapon.isWeapon(event.getCurrentItem().getType()) == false) || 
										!InventoryHandler.getAllowedMaterialsForSlot(event.getSlot()).contains(event.getCursor().getType())){
									event.setCancelled(true);
									gplayer.sendError("That item may not be placed in that slot!");
									main.cancelDrop.add(player.getName());
									player.closeInventory();
								}
							}
						}
					}
					if(event.getClick() == ClickType.SHIFT_RIGHT || event.getClick() == ClickType.SHIFT_LEFT){
						if(event.getCurrentItem() != null){
							boolean allow = false;
							int slot = 0;
							for(int i : InventoryHandler.getOpenSlots(player)){
								if(InventoryHandler.getAllowedMaterialsForSlot(i).contains(event.getCurrentItem().getType())){
									allow = true;
									slot = i;
									break;
								}
							}
							if(allow == true){
								event.setCancelled(true);
								player.getInventory().setItem(slot, event.getCurrentItem());
								player.getInventory().setItem(event.getSlot(), null);
							}else{
								event.setCancelled(true);
								gplayer.sendError("That item may not be placed in that slot!");
								player.closeInventory();
							}
						}
					}
				}
			}else{
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void playerDropItem(PlayerDropItemEvent event){
		Player player = event.getPlayer();
		GPlayer gplayer = new GPlayer(player);
		
		if(gplayer.hasMission() == true){
			if(gplayer.getObjective() instanceof ObtainItemsObjective){
				ObtainItemsObjective obj = (ObtainItemsObjective) gplayer.getObjective();
				if(event.getItemDrop().getItemStack().getType() == obj.getItemType()){
					event.setCancelled(true);
					gplayer.sendError("You may not drop this item during your current mission!");
					return;
				}
			}
		}
		
		if(event.getItemDrop().getItemStack().getType() == Material.MINECART || event.getItemDrop().getItemStack().getType() == Material.COMPASS || 
				event.getItemDrop().getItemStack().getType() == Material.PAPER || main.cancelDrop.contains(player.getName())){
			if(InventoryHandler.addItemToAppropriateSlot(player, event.getItemDrop().getItemStack()) == true){
				event.getItemDrop().remove();
			}
			if(main.cancelDrop.contains(player.getName())){
				main.cancelDrop.remove(player.getName());
			}
		}
	}
	
	@EventHandler
	public void vehicleEntityCollision(VehicleEntityCollisionEvent event){
		if(event.getEntity() instanceof Villager || event.getEntity() instanceof Player){
			final LivingEntity entity = (LivingEntity) event.getEntity();
			if(!main.collisionCooldown.contains(entity.getEntityId())){
				if(event.getVehicle().getPassenger() != null && event.getVehicle().getPassenger() instanceof Player){
					Player damager = (Player) event.getVehicle().getPassenger();
					entity.setVelocity(new Vector((event.getVehicle().getVelocity().getX() * 1.6), 0.45, (event.getVehicle().getVelocity().getZ() * 1.6)));
					if(event.getEntity() instanceof Player){
						Player player = (Player) event.getEntity();
						if(main.playerkiller.containsKey(player.getName())){
							main.playerkiller.remove(player.getName());
						}
						main.playerkiller.put(player.getName(), damager);
						double damage = 1.5;
						if((player.getHealth() - damage) > 0){
							player.damage(damage);
						}else{
							GPlayer gplayer = new GPlayer(player);
							gplayer.death();
						}
					}else{
						entity.damage(2);
					}
				}
				main.collisionCooldown.add(entity.getEntityId());
				main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
					public void run(){
						for(int x = 0; x <= (main.collisionCooldown.size() - 1); x++){
							if(main.collisionCooldown.get(x) == entity.getEntityId()){
								main.collisionCooldown.remove(x);
								break;
							}
						}
					}
				}, 30);
			}else{
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void entityDamageByEntity(EntityDamageByEntityEvent event){
		Entity dmgr = null;
		if(event.getDamager() instanceof Egg){
			Egg egg = (Egg) event.getDamager();
			if(egg.getShooter() instanceof Player){
				/*
				 * Player shoots entity
				 */
				dmgr = (Player) egg.getShooter();
			}else if(egg.getShooter() instanceof Villager){
				/*
				 * Villager shoots entity
				 */
				dmgr = (Villager) egg.getShooter();
			}
		}else if(event.getDamager() instanceof Player){
			/*
			 * Player hits entity
			 */
			dmgr = event.getDamager();
		}
		if(dmgr != null){
			if(dmgr instanceof Player){
				Player damager = (Player) dmgr;
				GPlayer gdamager = new GPlayer(damager);
				if(event.getEntity() instanceof LivingEntity){
					LivingEntity entity = (LivingEntity) event.getEntity();
					boolean allow = true;
					if(entity instanceof Player){
						Player player = (Player) entity;
						GPlayer gplayer = new GPlayer(player);
						if(gplayer.hasCrew() == true && gdamager.hasCrew() == true){
							if(gplayer.getCrew().getName().equalsIgnoreCase(gdamager.getCrew().getName())){
								allow = false;
							}
						}
						if(gplayer.hasJob() == true){
							JobInstance jobinstance = gplayer.getJobInstance();
							if(jobinstance.getState() == JobState.PREGAME){
								allow = false;
							}
							if(jobinstance.getJob().hasTeams() == true && jobinstance.getPlayerTeam(player) == jobinstance.getPlayerTeam(damager)){
								allow = false;
							}
						}
					}else if(entity instanceof Villager){
						Villager villager = (Villager) entity;
						if(villager.getCustomName() != null && villager.getCustomName().contains("Drug")){
							if(gdamager.hasMission() == true && gdamager.getObjective() instanceof HoldUpObjective){
								if(!main.robbing.containsKey(damager.getName())){
									RobTask task = new RobTask(damager, villager, villager.getCustomName(), 10);
									task.runTaskTimer(main, 0, 20);
									main.robbing.put(damager.getName(), task);
									gdamager.sendMessage(gold + ChatColor.stripColor(villager.getCustomName()) + ": " + gray + "Yo bro, settle down. I'll get you the drugs.");
								}
							}
						}
					}
					if(allow == true){
						EffectUtils.playBloodEffect(entity);
						/*
						 * Damager is a player
						 */
						if(gdamager.hasWeaponInHand() == true){
							/*
							 * Damager has weapon in hand
							 */
							Weapon weapon = gdamager.getWeaponInHand();
							double damage = weapon.getDamage() + event.getDamage();
							if(weapon instanceof Gun){
								damage += (gdamager.getSkillLevel(Skill.SHOOTING) * .006);
							}else if(weapon instanceof MeleeWeapon){
								damage += (gdamager.getSkillLevel(Skill.STRENGTH) * .01);
							}
							double newHealth = entity.getHealth() - damage;
							if(newHealth > 1){
								entity.damage(damage);
								if(entity instanceof Player){
									Player player = (Player) entity;
									if(main.playerkiller.containsKey(player.getName())){
										main.playerkiller.remove(player.getName());
									}
									main.playerkiller.put(player.getName(), damager);
								}
							}else{
								/*
								 * Kill the damaged entity due to lack of health
								 */
								if(entity instanceof Player){
									Player player = (Player) entity;
									GPlayer gplayer = new GPlayer(player);
									if(main.playerkiller.containsKey(player.getName())){
										main.playerkiller.remove(player.getName());
									}
									main.playerkiller.put(player.getName(), damager);
									gplayer.death();
								}else{
									entity.setHealth(0);
								}
							}
						}else{
							/*
							 * Damager has no weapon in hand
							 */
							double damage = 1 + event.getDamage();
							double newHealth = entity.getHealth() - damage;
							if(newHealth > 1){
								entity.damage(damage);
								if(entity instanceof Player){
									Player player = (Player) entity;
									if(main.playerkiller.containsKey(player.getName())){
										main.playerkiller.remove(player.getName());
									}
									main.playerkiller.put(player.getName(), damager);
								}
							}else{
								/*
								 * Kill the damaged entity due to lack of health
								 */
								if(entity instanceof Player){
									Player player = (Player) entity;
									GPlayer gplayer = new GPlayer(player);
									if(main.playerkiller.containsKey(player.getName())){
										main.playerkiller.remove(player.getName());
									}
									main.playerkiller.put(player.getName(), damager);
									gplayer.death();
								}else{
									entity.setHealth(0);
								}
							}
						}
						if(entity instanceof Player){
							if(!main.invoked.containsKey(damager.getName())){
								if(gdamager.hasCopsNearby() == true){
									if(gdamager.hasJob() == true && gdamager.getJobInstance().getJob() instanceof FreeForAll && gdamager.getJobInstance().getState() == JobState.STARTED){
										return;
									}
									/*
									 * Damaged entity is a player, warn cops if nearby
									 */
									gdamager.setHasInvoked(VillagerType.COP, null);
								}
							}
						}
						
						if(entity instanceof Villager){
							/*
							 * Damaged entity is a villager
							 */
							Villager villager = (Villager) entity;
							gdamager.attackedVillager(villager);
						}
					}else{
						event.setCancelled(true);
					}
				}
			}else if(dmgr instanceof Villager){
				/*
				 * Damager is a villager (gang member)
				 */
				Villager villager = (Villager) dmgr;
				if(event.getEntity() instanceof Player){
					Player player = (Player) event.getEntity();
					GPlayer gplayer = new GPlayer(player);
					double newHealth = player.getHealth() - 3;
					if(newHealth > 1){
						player.damage(3);
					}else{
						event.setCancelled(true);
						if(main.playerkiller.containsKey(player.getName())){
							main.playerkiller.remove(player.getName());
						}
						main.playerkiller.put(player.getName(), villager);
						gplayer.death();
					}
				}
			}
		}
	}
	
	@EventHandler
	public void entityDamage(EntityDamageEvent event){
		if(event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();
			GPlayer gplayer = new GPlayer(player);
			double newHealth = player.getHealth() - event.getDamage();
			if(newHealth <= 0){
				event.setCancelled(true);
				gplayer.death();
			}
		}
	}
	
	@EventHandler
	public void entityDeath(EntityDeathEvent event){
		LivingEntity entity = (LivingEntity) event.getEntity();
		if(entity.getKiller() != null){
			if(entity.getKiller() instanceof Player){
				Player killer = (Player) entity.getKiller();
				GPlayer gkiller = new GPlayer(killer);
				
				if(!main.killCooldown.contains(killer.getName())){
					/*
					 * Increase KillTargetObjective killed amount
					 */
					if(gkiller.hasMission() == true){
						if(gkiller.getObjective() instanceof KillTargetObjective){
							KillTargetObjective obj = (KillTargetObjective) gkiller.getObjective();
							boolean allow = true;
							if(entity.getType() != obj.getTarget()){
								allow = false;
							}
							if(obj.getTargetName() != null){
								if(entity.getCustomName() != null){
									if(!ChatColor.stripColor(entity.getCustomName()).equalsIgnoreCase(obj.getTargetName())){
										allow = false;
									}
								}
							}
							if(allow == true){
								obj.setAmountKilled(killer.getName(), obj.getAmountKilled(killer.getName()) + 1);
								if(obj.getAmountKilled(killer.getName()) >= obj.getAmountToKill()){
									gkiller.advanceObjective();
								}
							}
						}
					}
					
					/*
					 * Drop money for players to pick up
					 */
					if(entity instanceof Villager){
						Villager villager = (Villager) entity;
						Utils.dropMoney(entity.getLocation());
						gkiller.addMentalState(0.25);
						if(main.prostitute.containsKey(killer.getName())){
							ProstituteFollowTask task = main.prostitute.get(killer.getName());
							if(task.getVillager().getEntityId() == villager.getEntityId()){
								main.prostitute.remove(killer.getName());
								main.killedProstitute.add(killer.getName());
								task.cancel();
							}
						}
					}
				}
				
				main.killCooldown.add(killer.getName());
			}
		}
	}
	
	@EventHandler
	public void playerInteractEntityEvent(PlayerInteractEntityEvent event){
		Player player = event.getPlayer();
		GPlayer gplayer = new GPlayer(player);
		if(event.getRightClicked() instanceof Player){
			if(Utils.isNPC((Player)event.getRightClicked()) == true){
				Player npc = (Player) event.getRightClicked();
				String name = ChatColor.stripColor(npc.getName());
				boolean isJobGiver = false;
				for(Character c : Character.values()){
					if(c.getName().equalsIgnoreCase(name)){
						isJobGiver = true;
						break;
					}
				}
				boolean isStoreVendor = false;
				Store store = null;
				for(Store s : Store.list()){
					if(s.getVendor().equalsIgnoreCase(name)){
						isStoreVendor = true;
						store = s;
						break;
					}
				}
				if(isJobGiver == true){
					if(gplayer.hasMission() == true){
						Mission mission = gplayer.getMission();
						int objID = gplayer.getCurrentObjectiveID();
						if(objID != gplayer.getMission().getObjectives().size()){
							if(!main.invoked.containsKey(player.getName())){
								if(gplayer.getObjective() instanceof ApproachObjective){
									ApproachObjective obj = (ApproachObjective) gplayer.getObjective();
									if(obj.getToApproach().getName().equalsIgnoreCase(name)){
										if(obj.getItemToReturn() != null){
											if(gplayer.getAmountOfMaterialInInventory(obj.getItemToReturn()) >= obj.getAmountOfItemToReturn()){
												gplayer.removeMaterialFromInventory(obj.getItemToReturn(), obj.getAmountOfItemToReturn());
												gplayer.sendDialogueMessage(mission.getObjectives().get(objID).getDialogue(), true);
											}else{
												gplayer.sendMessage(gold + name + ": " + gray + "Get what I asked you to get, then come back to me!");
											}
										}else{
											gplayer.sendDialogueMessage(mission.getObjectives().get(objID).getDialogue(), true);
										}
									}
								}else if(gplayer.getObjective() instanceof ReturnVehicleObjective){
									ReturnVehicleObjective obj = (ReturnVehicleObjective) gplayer.getObjective();
									if(obj.getWhoToReturnTo().getName().equalsIgnoreCase(name)){
										if(player.getVehicle() != null){
											if(player.getVehicle().hasMetadata("car") == true){
												Car car = null;
												for(Car c : Car.values()){
													if(c.getName().contains((String)Utils.getMetadata(player.getVehicle(), "car", ObjectType.STRING))){
														car = c;
														break;
													}
												}
												if(car != null){
													if(car == obj.getCar()){
														boolean isOwner = true;
														if(Car.getOwner(player.getVehicle()) != null && obj.getCarOwner() != null){
															if(!Car.getOwner(player.getVehicle()).equalsIgnoreCase(obj.getCarOwner())){
																isOwner = false;
															}
														}
														if(isOwner == true){
															player.getVehicle().remove();
															player.teleport(player.getLocation().add(0, 1, 0));
															gplayer.advanceObjective();
														}else{
															gplayer.sendMessage(gold + name + ": " + gray + "This is not the vehicle I asked for!");
														}
													}else{
														gplayer.sendMessage(gold + name + ": " + gray + "This is not the vehicle I asked for!");
													}
												}else{
													gplayer.sendMessage(gold + name + ": " + gray + "This is not the vehicle I asked for!");
												}
											}
										}else{
											gplayer.sendMessage(gold + name + ": " + gray + "You need to be inside the vehicle to return it to me!");
										}
									}
								}else{
									if(gplayer.getMission().getGiver().getName().equalsIgnoreCase(name)){
										gplayer.sendMessage(gold + name + ": " + gray + "Do what I told you to do, then come back to me!");
									}
								}
							}else{
								gplayer.sendMessage(gold + name + ": " + gray + "Lose the heat before we talk, man!");
							}
						}
					}else{
						List<Character> givers = new ArrayList<Character>();
						List<Mission> availableMissions = new ArrayList<Mission>();
						for(Mission m : Mission.values()){
							if(m.getGiver().getName().equalsIgnoreCase(name)){
								if(m.getType() == MissionType.REGULAR){
									if(gplayer.hasCompletedMission(m.getID()) == false){
										if(!givers.contains(m.getGiver())){
											if(gplayer.getLevel() >= m.getMinimumLevel()){
												availableMissions.add(m);
												givers.add(m.getGiver());
											}
										}
									}
								}else if(m.getType() == MissionType.SIDE_MISSION){
									if(SideMission.getSideMission(SideMissionType.DAILY) != null && SideMission.getSideMission(SideMissionType.DAILY).getID() == m.getID()){
										if(gplayer.canCompleteSideMission(SideMissionType.DAILY) == true){
											if(gplayer.getLevel() >= m.getMinimumLevel()){
												availableMissions.add(m);
												givers.add(m.getGiver());
											}
										}
									}
								}
							}
						}
						boolean isRegularMission = false;
						boolean isSideMission = false;
						Mission mission = null;
						if(availableMissions.size() > 0){
							for(Mission m : availableMissions){
								if(m.getType() == MissionType.SIDE_MISSION){
									isSideMission = true;
								}else if(m.getType() == MissionType.REGULAR){
									isRegularMission = true;
								}
								mission = m;
							}
						}
						if(mission != null){
							if(isRegularMission == true && isSideMission == true){
								Inventory inv = Bukkit.createInventory(null, 9, gray + "Select Mission - " + name);
								inv.setItem(3, Utils.renameItem(new ItemStack(Material.WOOL, 1, (short) 5), gold + "Regular Mission"));
								inv.setItem(5, Utils.renameItem(new ItemStack(Material.WOOL, 1, (short) 1), gold + "Daily Mission"));
								player.openInventory(inv);
							}else{
								gplayer.setMission(mission.getID());
								gplayer.sendMessageHeader("New Mission");
								gplayer.sendMissionOverview(false);
							}
						}else{
							gplayer.sendMessage(gold + name + ": " + gray + "I don't have any missions for you right now, but try coming back when you're a higher level.");
						}
					}
				}else if(isStoreVendor == true){
					if(!name.contains("Gas Station")){
						boolean open = true;
						if(name.contains("Black Market")){
							ItemStack item = player.getItemInHand();
							if(item != null && item.hasItemMeta() == true && item.getItemMeta().hasDisplayName() == true){
								if(item.getItemMeta().getDisplayName().contains("Kidney")){
									open = false;
									player.setItemInHand(null);
									int price = Utils.getKidneySellPrice();
									gplayer.setWalletBalance(gplayer.getWalletBalance() + price);
									gplayer.sendMessage("You have earned " + gold + "$" + price + gray + " for selling a kidney!");
									gplayer.refreshScoreboard();
								}
							}
						}
						if(open == true){
							player.openInventory(store.getPage(1));
						}
					}else{
						if(!main.robbing.containsKey(player.getName()) && !main.robCooldown.contains(player.getName())){
							if(gplayer.hasWeaponInHand() == true){
								int time = 10;
								if(gplayer.hasMission() == true){
									if(gplayer.getObjective() instanceof RobStationObjective){
										RobStationObjective obj = (RobStationObjective) gplayer.getObjective();
										time = obj.getRobbingPeriod();
									}
								}
								RobTask task = new RobTask(player, npc, npc.getName(), time);
								task.runTaskTimer(main, 0, 20);
								main.robbing.put(player.getName(), task);
								gplayer.sendMessage(gold + name + ": " + gray + "I'll get the money! Hold on!");
								if(Utils.randInt(1, 8) == 1){
									gplayer.setHasInvoked(VillagerType.COP, null);
								}
							}else{
								player.openInventory(store.getPage(1));
							}
						}else{
							if(main.robCooldown.contains(player.getName())){
								gplayer.sendError("This station is closed due to recent robberies.");
							}
						}
					}
				}else if(npc.getName().contains("Bank")){
					player.openInventory(Bank.getInventory(gplayer, BankInventory.WITHDRAW_DEPOSIT));
				}
			}else{
				ItemStack item = player.getItemInHand();
				Player target = (Player) event.getRightClicked();
				GPlayer gtarget = new GPlayer(target);
				if(item != null && item.hasItemMeta() == true && item.getItemMeta().hasDisplayName() == true){
					if(item.getItemMeta().getDisplayName().contains("Chloroform")){
						if(main.knockout.contains(target.getName()) == false){
							KnockOutTask task = null;
							if(main.knockingout.containsKey(player.getName())){
								task = main.knockingout.get(player.getName());
							}else{
								task = new KnockOutTask(player, target);
								task.runTaskTimer(main, 0, 10);
								main.knockingout.put(player.getName(), task);
								gtarget.sendMessage(gold + player.getName() + gray + " has covered your mouth with a chloroform rag!");
								gplayer.sendMessage("You have begun knocking " + gold + target.getName() + gray + " out!");
							}
							task.addClick();
						}else{
							gplayer.sendError("This player is already knocked out!");
						}
					}else if(item.getItemMeta().getDisplayName().contains("Knife")){
						if(main.knockout.contains(target.getName())){
							if(target.hasMetadata("kidneyStolen") == false){
								if(main.harvesting.contains(target.getName()) == false){
									KidneyHarvestTask task = new KidneyHarvestTask(player, target);
									task.runTaskTimer(main, 0, 20);
									main.harvesting.add(target.getName());
									gplayer.sendMessage("You have begun cutting out " + gold + target.getName() + "'s " + gray + "kidney.");
								}else{
									gplayer.sendError("This player's kidney is currently being taken!");
								}
							}else{
								gplayer.sendError("This player's kidney has already been stolen!");
							}
						}
					}
				}
			}
		}else if(event.getRightClicked() instanceof Minecart){
			Minecart cart = (Minecart) event.getRightClicked();
			if(player.isSneaking() == true){
				event.setCancelled(true);
				if(cart.hasMetadata("owner") == true){
					String owner = (String) Utils.getMetadata(cart, "owner", ObjectType.STRING);
					if(owner.equalsIgnoreCase(player.getName())){
						if(Car.isLocked(cart) == true){
							Utils.setMetadata(cart, "locked", false);
							gplayer.sendMessage("You have unlocked your vehicle!");
							EffectUtils.playLockEffect(cart.getLocation());
						}else{
							Utils.setMetadata(cart, "locked", true);
							gplayer.sendMessage("You have locked your vehicle!");
							EffectUtils.playLockEffect(cart.getLocation());
						}
					}else{
						if(Car.isLocked(cart) == true){
							gplayer.sendError("You may not unlock a vehicle which you do not own!");
						}else{
							gplayer.sendError("You may not lock a vehicle which you do not own!");
						}
					}
				}else{
					if(Car.isLocked(cart) == true){
						gplayer.sendError("You may not unlock a vehicle which you do not own!");
					}else{
						gplayer.sendError("You may not lock a vehicle which you do not own!");
					}
				}
			}
		}else if(event.getRightClicked() instanceof Villager){
			Villager villager = (Villager) event.getRightClicked();
			event.setCancelled(true);
			if(gplayer.hasWeaponInHand() == true){
				if(villager.getCustomName() != null){
					if(villager.getCustomName().contains("Drug")){
						if(gplayer.hasWeaponInHand() == true && gplayer.hasMission() == true && gplayer.getObjective() instanceof HoldUpObjective){
							if(!main.robbing.containsKey(player.getName())){
								RobTask task = new RobTask(player, villager, villager.getCustomName(), 10);
								task.runTaskTimer(main, 0, 20);
								main.robbing.put(player.getName(), task);
								gplayer.sendMessage(gold + ChatColor.stripColor(villager.getCustomName()) + ": " + gray + "Yo bro, settle down. I'll get you the drugs.");
							}
						}else{
							gplayer.sendMessage(gold + ChatColor.stripColor(villager.getCustomName()) + ": " + gray + "I don't sell drugs to just anybody, dude.");
						}
					}
				}else{
					if(gplayer.getWeaponInHand() instanceof Gun){
						gplayer.shootGun();
					}
				}
			}else{
				if(villager.getCustomName() != null){
					if(villager.getCustomName().contains("Prostitute")){
						if(villager.hasMetadata("prostitute") == false){
							if(main.killedProstitute.contains(player.getName()) == false){
								if(main.prostitute.containsKey(player.getName()) == false){
									player.openInventory(Utils.getProstituteInventory(villager));
								}else{
									gplayer.sendMessage(villager.getCustomName() + ": " + gray + "Sorry sir, I'm not allowed to do threesomes.");
								}
							}else{
								gplayer.sendMessage(villager.getCustomName() + ": " + gray + "I don't do business with murderers.");
							}
						}
					}
				}
			}
		}
	}
}
