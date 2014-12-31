package gca.tasks;

import gca.core.Main;
import gca.game.weapons.Gun;
import gca.utils.Utils;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ShootTask extends BukkitRunnable{
	
	Main main = Main.getInstance();
	int roundsShot = 0;
	
	Player player;
	Gun gun;
	public ShootTask(Player p, Gun g){
		player = p;
		gun = g;
	}
	
	int cooldown = 0;
	
	@Override
	public void run(){
		if((gun.getRoundsPerShot() - roundsShot) > 0){
			Utils.shootBullet(player);
			int maxdura = gun.getMaterial().getMaxDurability();
			int durapershot = maxdura / gun.getClipSize();
			player.getItemInHand().setDurability((short) (player.getItemInHand().getDurability() + durapershot));
			roundsShot++;
		}else{
			if(cooldown < gun.getCooldown() && gun.getCooldown() != 0){
				cooldown += gun.getFiringRate();
			}else{
				if(main.shootCooldown.contains(player.getName())){
					main.shootCooldown.remove(player.getName());
				}
				this.cancel();
			}
		}
	}

}
