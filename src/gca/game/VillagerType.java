package gca.game;

import org.bukkit.entity.Villager.Profession;

public enum VillagerType {
	
	REGULAR(Profession.FARMER),
	COP(Profession.LIBRARIAN),
	GANG_MEMBER(Profession.BUTCHER),
	PROSTITUTE(Profession.PRIEST);
	
	private Profession profession;
	VillagerType(Profession prof){
		profession = prof;
	}
	
	/**
	 * Get the profession of this type
	 * @return The profession of this type
	 */
	public Profession getProfession(){
		return profession;
	}
	
	/**
	 * Get a villager type from a profession
	 * @param prof - The profession
	 * @return The villager type from the profession
	 */
	public static VillagerType fromProfession(Profession prof){
		VillagerType type = VillagerType.REGULAR;
		for(VillagerType t : VillagerType.values()){
			if(t.getProfession() == prof){
				type = t;
				break;
			}
		}
		return type;
	}
}
