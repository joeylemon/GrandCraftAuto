package gca.game.jobs;

import gca.core.Main;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class JobFile {
	
	Main main = Main.getInstance();
	
	private int id;
	public JobFile(int id){
		this.id = id;
	}
	
	/**
	 * Get the job file
	 */
	public File getFile(){
		return new File(main.getDataFolder() + File.separator + "jobs", id + ".yml");
	}
	
	/**
	 * Get the job config
	 */
	public FileConfiguration getConfig(){
		return YamlConfiguration.loadConfiguration(getFile());
	}
	
	/**
	 * Set a value in the job config
	 * 
	 * @param key - The location of the value to set
	 * @param entry - The value to set
	 */
	public void setConfigValue(String key, Object entry){
		FileConfiguration fc = getConfig();
	    fc.set(key, entry);
	    try{
	      fc.save(getFile());
	    }catch (IOException e) {
	      e.printStackTrace();
	    }
	}
	
}
