package com.grandcraftauto.game.jobs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.grandcraftauto.core.Main;
import com.grandcraftauto.utils.Utils;

public class JobJoiner {
	
	static Main main = Main.getInstance();
	private static List<JobJoiner> jobjoiners = new ArrayList<JobJoiner>();
	
	private Job job;
	private int id;
	public JobJoiner(Job job, int id){
		this.job = job;
		this.id = id;
	}
	
	private Hologram hologram = null;
	
	public void refresh(){
		if(hologram != null){
			hologram.delete();
		}
		hologram = HologramsAPI.createHologram(main, this.getLocation().add(0, 1.5, 0));
		hologram.appendTextLine(ChatColor.DARK_BLUE + job.getName());
		hologram.appendTextLine(ChatColor.GRAY + job.getType());
	}
	
	public Job getJob(){
		return job;
	}
	
	public Location getLocation(){
		return getJobJoinerLocation(id);
	}
	
	public Hologram getHologram(){
		return hologram;
	}
	
	public static final List<JobJoiner> getJobJoiners(){
		return jobjoiners;
	}
	
	public static final List<JobJoiner> getNewJobJoiners(){
		List<JobJoiner> jobjoiners = new ArrayList<JobJoiner>();
		for(int x = 1; x <= getTotalJobJoiners(); x++){
			jobjoiners.add(new JobJoiner(Job.getJob(main.getConfig().getInt("jobjoiners." + x + ".job")), x));
		}
		return jobjoiners;
	}
	
	public static final void refreshJobJoiners(){
		if(jobjoiners.size() == 0){
			jobjoiners.addAll(getNewJobJoiners());
		}
		for(JobJoiner j : jobjoiners){
			j.refresh();
		}
	}
	
	public static final int getTotalJobJoiners(){
		return main.getConfig().getInt("jobjoiners.total");
	}
	
	public static final void setTotalJobJoiners(int value){
		main.getConfig().set("jobjoiners.total", value);
		main.saveConfig();
	}
	
	public static final void addJobJoiner(Job job, Location loc){
		int id = getTotalJobJoiners() + 1;
		main.getConfig().set("jobjoiners." + id + ".job", job.getID());
		main.getConfig().set("jobjoiners." + id + ".loc.x", loc.getX());
		main.getConfig().set("jobjoiners." + id + ".loc.y", loc.getY());
		main.getConfig().set("jobjoiners." + id + ".loc.z", loc.getZ());
		main.getConfig().set("jobjoiners.total", id);
		main.saveConfig();
	}
	
	public static final Location getJobJoinerLocation(int id){
		double x,y,z;
		x = main.getConfig().getDouble("jobjoiners." + id + ".loc.x");
		y = main.getConfig().getDouble("jobjoiners." + id + ".loc.y");
		z = main.getConfig().getDouble("jobjoiners." + id + ".loc.z");
		return new Location(Utils.getGCAWorld(), x, y, z);
	}
}
