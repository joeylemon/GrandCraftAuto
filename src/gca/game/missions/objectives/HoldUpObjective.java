package gca.game.missions.objectives;

import gca.game.missions.Dialogue;
import gca.game.missions.Objective;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class HoldUpObjective extends Objective {

	private EntityType targetType;
	private String targetName;
	private ItemStack itemToGet;
	public HoldUpObjective(String description, Dialogue dialogue, EntityType targetType, String targetName, ItemStack itemToGet){
		super(description, dialogue);
		this.targetType = targetType;
		this.targetName = targetName;
		this.itemToGet = itemToGet;
	}
	
	public EntityType getTargetType(){
		return targetType;
	}
	
	public String getTargetName(){
		return targetName;
	}
	
	public ItemStack itemToGet(){
		return itemToGet;
	}
}
