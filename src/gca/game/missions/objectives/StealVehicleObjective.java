package gca.game.missions.objectives;

import gca.game.cars.Car;
import gca.game.missions.Dialogue;
import gca.game.missions.Objective;

public class StealVehicleObjective extends Objective{
	
	private Car car;
	
	public StealVehicleObjective(String objDesc, Dialogue objDialogue, Car objCar){
		super(objDesc, objDialogue);
		car = objCar;
	}
	
	/**
	 * Get the objective's car type
	 * @return The objective's car type
	 */
	public Car getCar(){
		return car;
	}
}
