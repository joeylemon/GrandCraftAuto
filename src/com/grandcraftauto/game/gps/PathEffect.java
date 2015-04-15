package com.grandcraftauto.game.gps;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.grandcraftauto.core.Main;

public class PathEffect {
	
	Main main = Main.getInstance();
	
	private Player player;
	private List<Hologram> holograms;
	private Location destination;
	public PathEffect(Player player, List<Hologram> holograms, Location destination){
		this.player = player;
		this.holograms = holograms;
		this.destination = destination;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public List<Hologram> getHolograms(){
		return holograms;
	}
	
	public Location getDestination(){
		return destination;
	}
	
	public void clearEffect(){
		for(Hologram h : holograms){
			h.delete();
		}
		if(main.pathEffect.containsKey(player.getName())){
			main.pathEffect.remove(player.getName());
		}
	}
}
