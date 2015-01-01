package gca.game;

public class InvokedGroup {
	
	private VillagerType type;
	private String gangName;
	public InvokedGroup(VillagerType type, String gangName){
		this.type = type;
		this.gangName = gangName;
	}
	
	public VillagerType getVillagerType(){
		return type;
	}
	
	public String getGangName(){
		return gangName;
	}
}
