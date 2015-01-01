package com.grandcraftauto.game.jobs;

import java.util.ArrayList;
import java.util.List;

public abstract class Job {
	
	static List<Job> jobs = new ArrayList<Job>();
	
	private int id;
	private JobFile file;
	public Job(int id){
		this.id = id;
		this.file = new JobFile(id);
	}
	
	/**
	 * Get the job's ID
	 * @return The job's ID
	 */
	public int getID(){
		return id;
	}
	
	/**
	 * Get the job's file
	 * @return The job's file
	 */
	public JobFile getFile(){
		return file;
	}
	
	/**
	 * Get the job's name
	 * @return The job's name
	 */
	public String getName(){
		return file.getConfig().getString("name");
	}
	
	/**
	 * Set the job's name
	 * @param value - The job's name
	 */
	public void setName(String value){
		file.setConfigValue("name", value);
	}
	
	/**
	 * Get the minimum players allowed in the job
	 * @return The minimum players allowed in the job
	 */
	public abstract int getMinimumPlayers();
	
	/**
	 * Get the maximum players allowed in the job
	 * @return The maximum players allowed in the job
	 */
	public abstract int getMaximumPlayers();
	
	/**
	 * Get the job's type
	 * @return The job's type
	 */
	public abstract String getType();
	
	/**
	 * Check if the job has teams
	 * @return True if the job has teams, false if not
	 */
	public abstract boolean hasTeams();
	
	/**
	 * Initialize the missions list for usage
	 */
	public static final void initializeList(){
		jobs.add(new FreeForAll(1));
	}
	
	/**
	 * Get the jobs list
	 * @return The jobs list
	 */
	public static final List<Job> list(){
		return jobs;
	}
	
	/**
	 * Get a job in the list with the id
	 * @param id - The id
	 * @return The job in the list with the id
	 */
	public static final Job getJob(int id){
		Job job = null;
		for(Job j : list()){
			if(j.getID() == id){
				job = j;
				break;
			}
		}
		return job;
	}
}
