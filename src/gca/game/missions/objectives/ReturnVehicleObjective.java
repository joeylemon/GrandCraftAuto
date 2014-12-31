package gca.game.missions.objectives;

import gca.game.cars.Car;
import gca.game.missions.Character;
import gca.game.missions.Dialogue;
import gca.game.missions.Objective;

public class ReturnVehicleObjective extends Objective{
	
	private Car car;
	private Character returnTo;
	private String carOwner;
	
	public ReturnVehicleObjective(String objDesc, Dialogue objDialogue, Car objCar, Character objReturnTo, String objCarOwner){
		super(objDesc, objDialogue);
		car = objCar;
		returnTo = objReturnTo;
		carOwner = objCarOwner;
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
