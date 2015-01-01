package gca.tasks;

import gca.core.Main;
import gca.game.weapons.Gun;
import gca.game.weapons.Shotgun;
import gca.utils.EffectUtils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ReloadTask extends BukkitRunnable{
	
	private Main main = Main.getInstance();
	
	private Player player;
	private Gun gun;
	private ItemStack item;
	private int maxdura;
	private int reloadpersecond;
	public ReloadTask(Player p, Gun g){
		gun = g;
		player = p;
		item = p.getItemInHand();
		maxdura = g.getMaterial().getMaxDurability();
		if(g instanceof Shotgun){
			reloadpersecond = (maxdura / g.getClipSize());
		}else{
			reloadpersecond = (maxdura / g.getClipSize()) * (g.getRoundsPerShot());
		}
		EffectUtils.playReloadSound(p.getEyeLocation());
	}
	
	@SuppressWarnings("deprecation")
	public void run(){
		if(player.getInventory().contains(gun.getAmmoType().getItem().getType())){
			if(item.getDurability() != 0){
				if((item.getDurability() - reloadpersecond) > 0){
					item.setDurability((short) (item.getDurability() - reloadpersecond));
				}else{
					item.setDurability((short) 0);
				}
				for(int x = 0; x <= 35; x++){
					if(player.getInventory().getItem(x) != null){
						ItemStack slot = player.getInventory().getItem(x);
						if(slot.getType() == gun.getAmmoType().getItem().getType() && slot.getData().getData() == gun.getAmmoType().getItem().getData().getData()){
							if(slot.getAmount() > 1){
								slot.setAmount(slot.getAmount() - 1);
							}else{
								player.getInventory().setItem(x, null);
							}
							break;
						}
					}
				}
			}else{
				if(main.reloading.containsKey(player.getName())){
					main.reloading.remove(player.getName());
				}
				this.cancel();
			}
		}else{
			if(main.reloading.containsKey(player.getName())){
				main.reloading.remove(player.getName());
			}
			this.cancel();
		}
	}
}
