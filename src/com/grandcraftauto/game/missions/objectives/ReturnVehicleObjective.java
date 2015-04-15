package com.grandcraftauto.game.missions.objectives;

import com.grandcraftauto.game.cars.Car;
import com.grandcraftauto.game.missions.Character;
import com.grandcraftauto.game.missions.Dialogue;
import com.grandcraftauto.game.missions.Objective;

public class ReturnVehicleObjective extends Objective{
	
	private Car car;
	private Character returnTo;
	private String carOwner;
	
	public ReturnVehicleObjective(String desc, Dialogue dialogue, boolean revert, Car car, Character returnTo, String carOwner){
		super(desc, dialogue, revert);
		this.car = car;
		this.returnTo = returnTo;
		this.carOwner = carOwner;
	}
	
	/**
	 * Get the objective's car type
	 * @return The objective's car type
	 */
	public Car getCar(){
		return car;
	}
	
	/**
	 * Get the objective's return to NPC
	 * @return The objective's return to NPC
	 */
	public Character getWhoToReturnTo(){
		return returnTo;
	}
	
	/**
	 * Get the objective's car owner
	 * @return The objective's car owner
	 */
	public String getCarOwner(){
		return carOwner;
	}

}
