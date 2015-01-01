package gca.game.missions;

import gca.game.Gang;
import gca.game.VillagerType;
import gca.game.cars.Car;
import gca.game.missions.objectives.ApproachObjective;
import gca.game.missions.objectives.EscapeCopsObjective;
import gca.game.missions.objectives.HoldUpObjective;
import gca.game.missions.objectives.KillTargetObjective;
import gca.game.missions.objectives.ObtainItemsObjective;
import gca.game.missions.objectives.ReachDestinationObjective;
import gca.game.missions.objectives.ReturnVehicleObjective;
import gca.game.missions.objectives.RobStationObjective;
import gca.game.missions.objectives.StealVehicleObjective;
import gca.game.weapons.Weapon;
import gca.game.weapons.WeaponType;
import gca.utils.Utils;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public enum Mission {
	
	WELCOME_TO_LOS_CRAFTOS("Welcome to Los Craftos", 
				"Speak to Lamar for the first time.", 
				MissionType.REGULAR,
				Arrays.asList(
						new ApproachObjective("Speak to Lamar.", 
								new Dialogue(Character.LAMAR, 
										"What's up my man %n!",
										"You new around Los Craftos, I hear.",
										"Everyone in this city, including you, just wants to get they name out there. Most of 'em don't know how to do that.",
										"If you really want to be known around the city, you gotta do criminal things.",
										"Take this knife so you can go mug a few people, then come back to me."
										), Character.LAMAR, Weapon.get(WeaponType.KNIFE).getItemStack(false), null, 0, false), 
						new KillTargetObjective("Kill five citizens.", null, EntityType.VILLAGER, 5, null),
						new ApproachObjective("Speak to Lamar.", 
								new Dialogue(Character.LAMAR,
										"Good job, %n. You mugged some people. Big deal.",
										"You ain't even started. Mugging is the least criminal thing I can think of.",
										"You've got the guts to do some crazy things, though. I can tell.",
										"Come back to me once you've deposited that mugged cash of yours, and I'll give you a real challenge."
										), Character.LAMAR, null, null, 0, false)),
				1, // ID
				Character.LAMAR,
				1,
				10,
				null),
	THE_ROPES("The Ropes", 
			"Infiltrate a gang hideout for Lamar.", 
			MissionType.REGULAR,
			Arrays.asList(
					new ApproachObjective("Speak to Lamar.", 
							new Dialogue(Character.LAMAR, 
									"So you decided to come back, huh? Good job.",
									"This new task ain't too hard as long as you got the guts to do it.",
									"I need you to infiltrate the gang that call themselves \"Cobras\".",
									"They ain't too crazy. All you gotta do is enter their gang hideout and steal some drugs.",
									"There should be bags of cocaine scattered throughout, just pick 'em up and come back to me."
									), Character.LAMAR, null, null, 0, false), 
					new ReachDestinationObjective("Get to the Cobra's hideout.", null, Gang.COBRA.getHideoutLocation(1)),
					new ObtainItemsObjective("Steal ten cocaine.", null, Material.SUGAR, 10, VillagerType.GANG_MEMBER, "Cobra"),
					new ApproachObjective("Speak to Lamar.", 
							new Dialogue(Character.LAMAR,
									"Now you starting to get into the good criminal activities!",
									"You can keep half of the cocaine you stole. I gotta keep some for myself, if you don't mind.",
									"Keep doing what you doing, %n. Soon enough, you be the baddest gangster in Los Craftos."
									), Character.LAMAR, null, Material.SUGAR, 5, false)),
			2, // ID
			Character.LAMAR,
			2,
			15,
			null),
	THE_TEENAPPER("The Teenapper", 
			"Kill Tracey's kidnapper.", 
			MissionType.REGULAR,
			Arrays.asList(
					new ApproachObjective("Speak to Michael.",
							new Dialogue(Character.MICHAEL,
									"Hey there, %n. I have been hearing your name come up pretty often lately. I'm glad you've come to see me.",
									"Last week, my daughter, Tracey, was kidnapped. I was able to rescue her on my own, but the kidnapper escaped.",
									"In case you are not aware, I am a very busy man. I don't have much time nowadays to kill anyone unless they are very important.", 
									"So, are you up to killing the kidnapper? Last I heard, he was around the projects hanging with the hoodlums."
									), Character.MICHAEL, null, null, 0, false), 
					new ReachDestinationObjective("Find the kidnapper.", null, MissionUtils.getMissionLocation("projects")),
					new KillTargetObjective("Kill the kidnapper.", null, EntityType.VILLAGER, 1, "Kidnapper"),
					new ApproachObjective("Speak to Michael.", 
							new Dialogue(Character.MICHAEL,
									"You got the kidnapper? Perfect! No wonder you're name is floating around the city.",
									"Here, take some cash I suppose. You look like you could use it."
									), Character.MICHAEL, null, null, 0, false)),
			3, // ID
			Character.MICHAEL,
			3,
			15,
			Arrays.asList(new MoneyReward(70).asReward())),
	SIMEON_YETARIAN("Simeon Yetarian", 
			"Meet Simeon for the first time.", 
			MissionType.REGULAR,
			Arrays.asList(
					new ApproachObjective("Speak to Lamar.", 
							new Dialogue(Character.LAMAR,
									"What's up homie? I just talked to my good bud Simeon, he said he could use a dude like you.",
									"Simeon is a straight business guy. He don't mess around.",
									"How 'bout you go talk to him? Trust me, he pays good."
									), Character.LAMAR, null, null, 0, false), 
					new ApproachObjective("Speak to Simeon.", 
							new Dialogue(Character.SIMEON,
									"Hello there, %n! It seems Lamar sent you as I requested.",
									"He said you're looking for any job you can get. Conveniently enough, I have several jobs for you.",
									"My business specializes in repossessing vehicles. The more cars we repo, the more cash we make.",
									"I need you to go repossess an Ubermacht Oracle on Liberty Street and come back to me when you're done."
									), Character.SIMEON, null, null, 0, false),
					new ReachDestinationObjective("Get to Liberty Street.", null, MissionUtils.getMissionLocation("libertystreet")),
					new StealVehicleObjective("Steal the vehicle.", null, Car.UBERMACHT_ORACLE),
					new ReturnVehicleObjective("Return to Simeon with the vehicle.", null, Car.UBERMACHT_ORACLE, Character.SIMEON, null),
					new ApproachObjective("Speak to Simeon.", 
							new Dialogue(Character.SIMEON,
									"Ah, you have the vehicle! Very good!",
									"You seem to be experienced in this field of work. You will definitely come in handy.",
									"Since you seem to not have a car of your own, I will let you keep this one for free.",
									"Also, don't forget to check with me every now and then for more jobs!"
									), Character.SIMEON, null, null, 0, true)),
			4, // ID
			Character.LAMAR,
			3,
			18,
			Arrays.asList(Car.UBERMACHT_ORACLE.asReward())),
	AVENGE_HER("Avenge Her", 
			"Avenge Simeon's sister's death.", 
			MissionType.REGULAR,
			Arrays.asList(
					new ApproachObjective("Speak to Simeon.", 
							new Dialogue(Character.SIMEON,
									"Salutations, %n. I've been recently informed that my sister has been murdered while in the projects.",
									"Those hoodlums won't get away with it. However, as you have probably assumed, I am not experienced with a gun.",
									"I request that you go to the hood, kill the people who need to be killed, and I'll reward you greatly."
									), Character.SIMEON, null, null, 0, false),
					new ReachDestinationObjective("Get to the projects.", null, MissionUtils.getMissionLocation("projects")),
					new KillTargetObjective("Kill the hoodlums.", null, EntityType.VILLAGER, 7, "Hoodlum"),
					new ApproachObjective("Speak to Simeon.", 
							new Dialogue(Character.SIMEON,
									"You, my friend, are sent from the heavens. I cannot fathom how incredibly great you are.",
									"As a token of my gratitude, I insist you accept my gift of $150.", 
									"You should probably upgrade that weapon of yours, but you can use the money however you'd like."
									), Character.SIMEON, null, null, 0, false)),
			5, // ID
			Character.SIMEON,
			7,
			30,
			Arrays.asList(new MoneyReward(175).asReward())),
	PLASTIC_DRUGS("Plastic Drugs", 
			"Hold up a drug dealer for Michael.", 
			MissionType.REGULAR,
			Arrays.asList(
					new ApproachObjective("Speak to Michael.", 
							new Dialogue(Character.MICHAEL,
									"Hey, how's it going man. I have a little problem, and I will pay you to fix it.", 
									"The dealer I buy drugs from gave me fake cocaine. He messed with the wrong person, %n.",
									"However, since I am feeling nice today, all I want you to do is hold him up and have him give you real drugs."
									), Character.MICHAEL, null, null, 0, false), 
					new ReachDestinationObjective("Find the drug dealer.", null, MissionUtils.getMissionLocation("drugdealer1")),
					new HoldUpObjective("Hold the drug dealer up.", null, EntityType.VILLAGER, "Drug Dealer", Utils.getCocaineItem(1)),
					new ApproachObjective("Speak to Michael.", 
							new Dialogue(Character.MICHAEL,
									"Perfect, you got the real drugs. I'll make sure not to do business with that guy again.",
									"That shouldn't of been too hard of a task, so here is some extra cash I had in my pocket."
									), Character.MICHAEL, null, Material.SUGAR, 1, false)),
			6, // ID
			Character.MICHAEL,
			5,
			15,
			Arrays.asList(new MoneyReward(55).asReward())),
	THE_ROBBERY("The Robbery", 
			"Rob a gas station.", 
			MissionType.SIDE_MISSION,
			Arrays.asList(
					new ApproachObjective("Speak to Lamar.", 
							new Dialogue(Character.LAMAR,
									"Yo %n! So you're just looking for some money, huh?", 
									"Well, people around the city love robbing gas stations.",
									"How about you go rob one? It'll boost your rep and get you some extra cash."
									), Character.LAMAR, null, null, 0, false), 
					new RobStationObjective("Rob a gas station.", null, 10)),
			7, // ID
			Character.LAMAR,
			1,
			10,
			null),
	THREE_TIMES_LUCKY("Three Times Lucky", 
			"Escape the cops three times.", 
			MissionType.SIDE_MISSION,
			Arrays.asList(
					new ApproachObjective("Speak to Lamar.", 
							new Dialogue(Character.LAMAR,
									"Hey dude, I think your reputation is starting to get a little low.", 
									"Everyone knows these LSPD cops are the easiest to escape.",
									"If you escape them three times in a row, it'll prove how stupid they are and boost your rep."
									), Character.LAMAR, null, null, 0, false), 
					new EscapeCopsObjective("Lose the cops three times in a row.", null, 3),
					new ApproachObjective("Speak to Lamar.", 
							new Dialogue(Character.LAMAR,
									"Nice job yo, that was perfect. Your reputation is already starting to grow.", 
									"By the way man, I've got this extra AP Pistol from a thug off of the streets.",
									"I dunno if you need it or not, but here it is anyway."
									), Character.LAMAR, null, null, 0, false)),
			8, // ID
			Character.LAMAR,
			3,
			15,
			Arrays.asList(Weapon.get(WeaponType.AP_PISTOL).asReward()));
	
	private String name;
	private String desc;
	private List<Objective> objectives;
	private int id;
	private Character giver;
	private int minimumLevel;
	private int rpReward;
	private List<Reward> rewards;
	private MissionType type;
	Mission(String name, String desc, MissionType type, List<Objective> objectives, int id, Character giver, int minimumLevel, int rpReward, List<Reward> rewards){
		this.name = name;
		this.desc = desc;
		this.type = type;
		this.objectives = objectives;
		this.id = id;
		this.giver = giver;
		this.minimumLevel = minimumLevel;
		this.rpReward = rpReward;
		this.rewards = rewards;
	}
	
	/**
	 * Get the mission's name
	 * @return The mission's name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Get the mission's description
	 * @return The mission's description
	 */
	public String getDescription(){
		return desc;
	}
	
	/**
	 * Get the mission's type
	 * @return The mission's type
	 */
	public MissionType getType(){
		return type;
	}
	
	/**
	 * Get the mission's objective list
	 * @return The mission's objective list
	 */
	public List<Objective> getObjectives(){
		return objectives;
	}
	
	/**
	 * Get the mission's ID
	 * @return The mission's ID
	 */
	public int getID(){
		return id;
	}
	
	/**
	 * Get the mission's giver
	 * @return The mission's giver
	 */
	public Character getGiver(){
		return giver;
	}
	
	/**
	 * Get the mission's minimum level
	 * @return The mission's minimum level
	 */
	public int getMinimumLevel(){
		return minimumLevel;
	}
	
	/**
	 * Get the mission's rp reward
	 * @return The mission's rp reward
	 */
	public int getRPReward(){
		return rpReward;
	}
	
	/**
	 * Get the mission's rewards
	 * @return The mission's rewards
	 */
	public List<Reward> getRewards(){
		return rewards;
	}
	
	/**
	 * Get a mission from an ID
	 * @param id - The ID of the mission
	 * @return The mission from the ID
	 */
	public static final Mission getMissionFromID(int id){
		Mission mission = null;
		for(Mission m : values()){
			if(m.getID() == id){
				mission = m;
				break;
			}
		}
		return mission;
	}
}
