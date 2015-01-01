package gca.utils;

import gca.core.Main;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

public class EffectUtils {
	
	static Main main = Main.getInstance();
	
	/**
	 * Play a shot sound at the location
	 * @param location - The location to send a shot sound to
	 */
	public static final void playShotSound(Location location){
		playSound(location, Sound.BLAZE_HIT, 1F, 2F, 0);
		playSound(location, Sound.ZOMBIE_WOOD, 1F, 2F, 0);
	}
	
	/**
	 * Play a reload sound at the location
	 * @param location - The location to send a reload sound to
	 */
	public static final void playReloadSound(Location location){
		playSound(location, Sound.SKELETON_IDLE, 1F, 0F, 0);
		playSound(location, Sound.FIRE_IGNITE, 1F, 1F, 14);
		playSound(location, Sound.DOOR_OPEN, 1F, 2F, 16);
		playSound(location, Sound.FIRE_IGNITE, 1F, 1F, 26);
		playSound(location, Sound.HURT_FLESH, 1F, 0F, 27);
		playSound(location, Sound.DOOR_CLOSE, 1F, 2F, 28);
	}
	
	/**
	 * Play a spawn sound at the location
	 * @param location - The location to send a spawn sound to
	 */
	public static final void playSpawnSound(Player player){
		player.playSound(player.getEyeLocation(), Sound.BLAZE_HIT, 1F, 0.85F);
	}
	
	/**
	 * Play a smoke effect at the location
	 * @param loc - The location to send a smoke effect to
	 */
	public static final void playSmokeEffect(Location loc){
		ParticleUtils.sendToLocation(ParticleEffect.LAVA_SPARK, loc.add(0, .5, 0), 0, 0, 0, 0, 1);
	}
	
	/**
	 * Play a trail effect at the location
	 * @param loc - The location to send a trail effect to
	 */
	public static final void playTrailEffect(Location loc){
		ParticleUtils.sendToLocation(ParticleEffect.CLOUD, loc.add(0, .5, 0), 0, 0, 0, 0, 1);
	}
	
	/**
	 * Play an explosion effect at the location
	 * @param loc - The location to send an explosion effect to
	 */
	public static final void playExplodeEffect(Location loc){
		ParticleUtils.sendToLocation(ParticleEffect.LARGE_EXPLODE, loc, 0.8F, 0.8F, 0.8F, 0, 10);
		ParticleUtils.sendToLocation(ParticleEffect.LAVA_SPARK, loc, 0.8F, 0.8F, 0.8F, 0, 50);
		EffectUtils.playSound(loc, Sound.EXPLODE, 1, 1.1F, 0);
	}
	
	/**
	 * Play a lock effect at the location
	 * @param loc - The location to send a lock effect to
	 */
	public static final void playLockEffect(Location loc){
		ParticleUtils.sendToLocation(ParticleEffect.WHITE_SPARKLE, loc, 0.5F, 0.5F, 0.5F, 0, 7);
		playSound(loc, Sound.NOTE_STICKS, 1F, 0.8F, 0);
		playSound(loc, Sound.NOTE_STICKS, 1F, 0.8F, 2);
	}
	
	/**
	 * Play a blood effect on the entity
	 * @param entity - The entity to send a blood effect on
	 */
	@SuppressWarnings("deprecation")
	public static final void playBloodEffect(Entity entity){
		for(Entity e : entity.getNearbyEntities(20, 20, 20)){
			if(e instanceof Player){
				Player p = (Player) e;
				p.playEffect(entity.getLocation().add(0, .75, 0), Effect.STEP_SOUND, 152);
			}
		}
	}
	
	/**
	 * Play a green sparkle effect at the location
	 * @param loc - The location to send a green sparkle effect to
	 */
	public static final void playGreenSparkleEffect(Location loc){
		ParticleUtils.sendToLocation(ParticleEffect.GREEN_SPARKLE, loc.subtract(0, .25, 0), .6F, .6F, .6F, 0F, 15);
	}
	
	/**
	 * Play a sound at the location with the given volume, pitch, and optional delay
	 * @param location - The location to play the sound at
	 * @param sound - The sound to play
	 * @param volume - The volume of the sound
	 * @param pitch - The pitch of the sound
	 * @param delay - The delay of playing the sound
	 */
	public static final void playSound(final Location location, final Sound sound, final float volume, final float pitch, int delay){
		if(delay > 0){
			main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
				public void run(){
					for(Player p : Bukkit.getOnlinePlayers()){
						if(p.getLocation().getWorld().getName().equalsIgnoreCase(Utils.getGCAWorld().getName())){
							p.playSound(location, sound, volume, pitch);
						}
					}
				}
			}, delay);
		}else{
			for(Player p : Bukkit.getOnlinePlayers()){
				if(p.getLocation().getWorld().getName().equalsIgnoreCase(Utils.getGCAWorld().getName())){
					p.playSound(location, sound, volume, pitch);
				}
			}
		}
	}
	
	/**
	 * Create a bullet trail on the player
	 * @param player - The player to create a bullet trail on
	 */
	public static final void createBulletTrail(Location loc){
		BlockIterator blocks = new BlockIterator(loc, 0, 10);
		while(blocks.hasNext() == true){
			playTrailEffect(blocks.next().getLocation().add(0.5, 0, 0.5));
		}
	}
}
