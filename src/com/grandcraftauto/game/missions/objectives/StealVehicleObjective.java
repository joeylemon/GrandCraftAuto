package com.grandcraftauto.game.missions.objectives;

import com.grandcraftauto.game.cars.Car;
import com.grandcraftauto.game.missions.Dialogue;
import com.grandcraftauto.game.missions.Objective;

public class StealVehicleObjective extends Objective{
	
	private Car car;
	
	public StealVehicleObjective(String desc, Dialogue dialogue, boolean revert, Car car){
		super(desc, dialogue, revert);
		this.car = car;
	}
	
	/**
	 * Get the objective's car type
	 * @return The objective's car type
	 */
	public Car getCar(){
		return car;
	}
}
