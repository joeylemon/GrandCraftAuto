package gca.game.missions.objectives;

import gca.game.cars.Car;
import gca.game.missions.Dialogue;
import gca.game.missions.Objective;

public class StealVehicleObjective extends Objective{
	
	private Car car;
	
	public StealVehicleObjective(String desc, Dialogue dialogue, Car car){
		super(desc, dialogue);
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
