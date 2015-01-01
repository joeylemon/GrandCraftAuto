package gca.game.apartment;

import gca.game.Creator;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ApartmentCreator extends Creator {
	
	private String gray = ChatColor.GRAY + "";
	private String gold = ChatColor.GOLD + "";
	
	private Apartment apt;
	public ApartmentCreator(Player creator, int creatorAptID){
		super(creator, 7);
		apt = new Apartment(creatorAptID);
	}
	
	public Apartment getApartment(){
		return apt;
	}
	
	public void sendStep(int step){
		if(step == 1){
			creator.sendMessage(gold + "Step " + step + ": " + gray + "Create doors by right clicking the blocks, and left click to go to the next step.");
		}else if(step == 2){
			creator.sendMessage(gold + "Step " + step + ": " + gray + "Create chests by right clicking the blocks, and left click to go to the next step.");
		}else if(step == 3){
			creator.sendMessage(gold + "Step " + step + ": " + gray + "Set the spawn by right clicking at where the spawn should be.");
		}else if(step == 4){
			creator.sendMessage(gold + "Step " + step + ": " + gray + "Set the sign by right clicking it.");
		}else if(step == 5){
			creator.sendMessage(gold + "Step " + step + ": " + gray + "Set the name by typing it into chat.");
		}else if(step == 6){
			creator.sendMessage(gold + "Step " + step + ": " + gray + "Set the price by typing it into chat.");
		}else if(step == 7){
			creator.sendMessage(gold + "Step " + step + ": " + gray + "Set the rent price by typing it into chat.");
		}
	}
	
	// Step 1
	public void createDoor(Location loc){
		apt.addDoorLocation(loc);
	}
	
	// Step 2
	public void createChest(Location loc){
		apt.addChestLocation(loc);
	}
	
	// Step 3
	public void setSpawn(Location loc){
		apt.setSpawn(loc);
	}
	
	// Step 4
	public void setSign(Location loc){
		apt.setSignLocation(loc);
	}
	
	// Step 5
	public void setName(String name){
		apt.setName(name);
	}
	
	// Step 6
	public void setPrice(int price){
		apt.setPrice(price);
	}
	
	// Step 7
	public void setRent(int rent){
		apt.setRent(rent);
	}
}
