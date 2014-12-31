package gca.tasks;

import gca.core.GPlayer;
import gca.core.Main;
import gca.utils.ParticleEffect;
import gca.utils.ParticleUtils;
import gca.utils.Utils;

import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.scheduler.BukkitRunnable;

public class ProstituteFollowTask extends BukkitRunnable {
	
	private Main main = Main.getInstance();
	
	private Player player;
	private GPlayer gplayer;
	private Villager villager;
	private int time;
	public ProstituteFollowTask(Player player, Villager villager, int time){
		this.player = player;
		this.gplayer = new GPlayer(player);
		this.villager = villager;
		this.time = time * 2;
	}
	
	private int runtime = 0;
	
	public void run(){
		if((time - runtime) > 0){
			if(villager.getLocation().distance(player.getLocation()) > 4){
				Utils.setNavigation(player.getLocation(), villager);
			}
			ParticleUtils.sendToLocation(ParticleEffect.HEART, villager.getEyeLocation(), 0.4F, 0.4F, 0.4F, 1, 3);
			runtime++;
		}else{
			if(main.prostitute.containsKey(player.getName())){
				main.prostitute.remove(player.getName());
			}
			villager.remove();
			gplayer.sendMessage("Your time with the prostitute has run out!");
			this.cancel();
		}
	}
	
	/**
	 * Get the villager of the task
	 * @return The villager of the task
	 */
	public Villager getVillager(){
		return villager;
	}
	
}
