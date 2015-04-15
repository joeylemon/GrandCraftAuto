package com.grandcraftauto.tasks;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;
import org.bukkit.scheduler.BukkitRunnable;

import com.grandcraftauto.core.Main;

public class DoorCloseTask extends BukkitRunnable {
	
	private Main main = Main.getInstance();
	
	private Block block;
	public DoorCloseTask(Block block){
		this.block = block;
	}
	
	public int runtime = 0;
	
	public void run(){
		runtime++;
		if(runtime == 3){
			BlockState state = block.getState(); 
            Openable o = (Openable) state.getData();
            o.setOpen(false); 
            state.setData((MaterialData) o);
            state.update();
			if(main.doorclose.containsKey(block.getLocation()) == true){
				main.doorclose.remove(block.getLocation());
			}
			this.cancel();
		}
	}

}
