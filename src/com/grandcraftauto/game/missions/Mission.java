package com.grandcraftauto.game.missions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import com.grandcraftauto.game.Gang;
import com.grandcraftauto.game.VillagerType;
import com.grandcraftauto.game.cars.Car;
import com.grandcraftauto.game.missions.objectives.ApproachObjective;
import com.grandcraftauto.game.missions.objectives.BuyProstituteObjective;
import com.grandcraftauto.game.missions.objectives.EscapeCopsObjective;
import com.grandcraftauto.game.missions.objectives.HoldUpObjective;
import com.grandcraftauto.game.missions.objectives.KillTargetObjective;
import com.grandcraftauto.game.missions.objectives.ObtainItemsObjective;
import com.grandcraftauto.game.missions.objectives.PlaceBlockObjective;
import com.grandcraftauto.game.missions.objectives.ReachDestinationObjective;
import com.grandcraftauto.game.missions.objectives.ReturnVehicleObjective;
import com.grandcraftauto.game.missions.objectives.RobStationObjective;
import com.grandcraftauto.game.missions.objectives.StealKidneyObjective;
import com.grandcraftauto.game.missions.objectives.StealVehicleObjective;
import com.grandcraftauto.game.weapons.Weapon;
import com.grandcraftauto.game.weapons.WeaponType;
import com.grandcraftauto.utils.Utils;

public enum Mission {
	
	/*
	 * 
	 * - (Side Mission) Create a crew
	 * 
	 * - Find the prostitute that gave Michael herpes and bring her to him. Use handcuffs to force her to follow you
	 * 
	 */
	
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
									), false, Character.LAMAR, Weapon.get(WeaponType.KNIFE).getItemStack(false), null, 0, false, false), 
					new KillTargetObjective("Kill five citizens.", null, false, EntityType.VILLAGER, 5, null, false),
					new ApproachObjective("Speak to Lamar.", 
							new Dialogue(Character.LAMAR,
									"Good job, %n. You mugged some people. Big deal.",
									"You ain't even started. Mugging is the least criminal thing I can think of.",
									"You've got the guts to do some crazy things, though. I can tell.",
									"Come back to me once you've deposited that new cash of yours, and I'll give you a real challenge."
									), false, Character.LAMAR, null, null, 0, false, false)),
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
									), false, Character.LAMAR, null, null, 0, false, false), 
					new ReachDestinationObjective("Get to the Cobra's hideout.", null, false, Gang.COBRA.getHideoutLocation(1)),
					new ObtainItemsObjective("Steal ten cocaine.", null, true, Material.SUGAR, 10, VillagerType.GANG_MEMBER, "Cobra"),
					new ApproachObjective("Speak to Lamar.", 
							new Dialogue(Character.LAMAR,
									"Now you starting to get into the good criminal activities!",
									"You can keep half of the cocaine you stole. I gotta keep some for myself, if you don't mind.",
									"Keep doing what you doing, %n. Soon enough, you be the baddest gangster in Los Craftos."
									), false, Character.LAMAR, null, Material.SUGAR, 5, false, false)),
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
									), false, Character.MICHAEL, null, null, 0, false, false), 
					new ReachDestinationObjective("Find the kidnapper.", null, false, MissionUtils.getMissionLocation("projects")),
					new KillTargetObjective("Kill the kidnapper.", null, true, EntityType.VILLAGER, 1, "Kidnapper", false),
					new ApproachObjective("Speak to Michael.", 
							new Dialogue(Character.MICHAEL,
									"You got the kidnapper? Perfect! No wonder you're name is floating around the city.",
									"Here, take some cash I suppose. You look like you could use it."
									), false, Character.MICHAEL, null, null, 0, false, false)),
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
									), false, Character.LAMAR, null, null, 0, false, false), 
					new ApproachObjective("Speak to Simeon.", 
							new Dialogue(Character.SIMEON,
									"Hello there, %n! It seems Lamar sent you as I requested.",
									"He said you're looking for any job you can get. Conveniently enough, I have several jobs for you.",
									"My business specializes in repossessing vehicles. The more cars we repo, the more cash we make.",
									"I need you to go repossess an Ubermacht Oracle on Liberty Street and come back to me when you're done."
									), false, Character.SIMEON, null, null, 0, false, false),
					new ReachDestinationObjective("Get to Liberty Street.", null, false, MissionUtils.getMissionLocation("libertystreet")),
					new StealVehicleObjective("Steal the vehicle.", null, true, Car.UBERMACHT_ORACLE),
					new ReturnVehicleObjective("Return to Simeon with the vehicle.", null, false, Car.UBERMACHT_ORACLE, Character.SIMEON, null),
					new ApproachObjective("Speak to Simeon.", 
							new Dialogue(Character.SIMEON,
									"Ah, you have the vehicle! Very good!",
									"You seem to be experienced in this field of work. You will definitely come in handy.",
									"Since you seem to not have a car of your own, I will let you keep this one for free.",
									"Also, don't forget to check with me every now and then for more jobs!"
									), false, Character.SIMEON, null, null, 0, true, false)),
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
									), false, Character.SIMEON, null, null, 0, false, false),
					new ReachDestinationObjective("Get to the projects.", null, false, MissionUtils.getMissionLocation("projects")),
					new KillTargetObjective("Kill the hoodlums.", null, true, EntityType.VILLAGER, 7, "Hoodlum", false),
					new ApproachObjective("Speak to Simeon.", 
							new Dialogue(Character.SIMEON,
									"You, my friend, are sent from the heavens. I cannot fathom how incredibly great you are.",
									"As a token of my gratitude, I insist you accept my gift of $150.", 
									"You should probably upgrade that weapon of yours, but you can use the money however you'd like."
									), false, Character.SIMEON, null, null, 0, false, false)),
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
									), false, Character.MICHAEL, null, null, 0, false, false), 
					new ReachDestinationObjective("Find the drug dealer.", null, false, MissionUtils.getMissionLocation("drugdealer1")),
					new HoldUpObjective("Hold the drug dealer up.", null, true, EntityType.VILLAGER, "Drug Dealer", Utils.getCocaineItem(1)),
					new ApproachObjective("Speak to Michael.", 
							new Dialogue(Character.MICHAEL,
									"Perfect, you got the real drugs. I'll make sure not to do business with that guy again.",
									"That shouldn't of been too hard of a task, so here is some extra cash I had in my pocket."
									), false, Character.MICHAEL, null, Material.SUGAR, 1, false, false)),
			6, // ID
			Character.MICHAEL,
			5,
			15,
			Arrays.asList(new MoneyReward(55).asReward())),
	PROSTITUTE_TIME("Prostitute Time", 
			"Bring a prostitute to Michael.", 
			MissionType.REGULAR,
			Arrays.asList(
					new ApproachObjective("Speak to Michael.", 
							new Dialogue(Character.MICHAEL,
									"What's up buddy. Ever since my wife and I started having issues, I've been feeling a little lonely.", 
									"I would ask you to keep me company, but fortunately, I'm straight.",
									"Would you ming getting a prostitute for me? I'll pay you back afterwards."
									), false, Character.MICHAEL, null, null, 0, false, false), 
					new BuyProstituteObjective("Find and buy a prostitute.", null, false),
					new ApproachObjective("Return to Michael.", 
							new Dialogue(Character.MICHAEL,
									"Awesome. I want to do so many things with this woman.",
									"Uh- anyways, thanks for the errand. Here's $60, hopefully that pays for it."
									), false, Character.MICHAEL, null, Material.SUGAR, 1, false, true)),
			9, // ID
			Character.MICHAEL,
			4,
			10,
			Arrays.asList(new MoneyReward(60).asReward())),
	EXPLOSIVE_ENTRANCE("Explosive Entrance", 
			"Plant an explosive for Simeon.", 
			MissionType.REGULAR,
			Arrays.asList(
					new ApproachObjective("Speak to Simeon.", 
							new Dialogue(Character.SIMEON,
									"Hello %n. I've recently had a dispute with a friend of mine.",
									"He's not really a friend. More of a partner I have to do business with.",
									"And as long as his operations are still underway, he will continue to cause me trouble.",
									"I want to bring his building to the ground, but if he sees me near his building, he'll know something's wrong.",
									"I need you to go there and plant this C4. Keep it low-profile, please."
									), false, Character.SIMEON, Utils.getC4Item(), null, 0, false, false),
					new ReachDestinationObjective("Get to the building.", null, false, MissionUtils.getMissionLocation("building1")),
					new PlaceBlockObjective("Plant the explosive.", null, true, MissionUtils.getMissionLocation("building1"), Material.LEVER, VillagerType.GANG_MEMBER, "Guard", 12, true),
					new KillTargetObjective("Kill the guards.", null, true, EntityType.VILLAGER, 0, "Guard", true),
					new ApproachObjective("Speak to Simeon.", 
							new Dialogue(Character.SIMEON,
									"Yes, my friend, yes! He won't be a problem anymore.",
									"You do great work. Take this money as a special gift from me to you."
									), false, Character.SIMEON, null, null, 0, false, false)),
			10, // ID
			Character.SIMEON,
			9,
			40,
			Arrays.asList(new MoneyReward(400).asReward())),
	SNITCHES_GET_STITCHES("Snitches Get Stitches", 
			"Help Michael rid of a snitch.", 
			MissionType.REGULAR,
			Arrays.asList(
					new ApproachObjective("Speak to Michael.", 
							new Dialogue(Character.MICHAEL,
									"How's it going my man! A friend of mine was recently put in the pound for dealing drugs.",
									"One of his customers snitched on him after being interrogated by the LCPD.",
									"This customer of his got off free because the cops didn't find enough evidence to put him in jail too.",
									"I want you to teach him that no one can snitch and get away with it."
									), false, Character.MICHAEL, null, null, 0, false, false),
					new ReachDestinationObjective("Get to the snitch.", null, false, Gang.SNITCH.getHideoutLocation(1)),
					new KillTargetObjective("Kill the snitch.", null, true, EntityType.VILLAGER, 1, "Snitch", false),
					new ApproachObjective("Speak to Michael.", 
							new Dialogue(Character.MICHAEL,
									"Thanks for that buddy. Here, take some of this cash.",
									"Make sure you don't ever snitch on me either, or you'll end up like that guy you just killed."
									), false, Character.MICHAEL, null, null, 0, false, false)),
			11, // ID
			Character.MICHAEL,
			3,
			18,
			Arrays.asList(new MoneyReward(80).asReward())),
	DRUG_BUSTERS("Drug Busters", 
			"Get revenge for Lamar.", 
			MissionType.REGULAR,
			Arrays.asList(
					new ApproachObjective("Speak to Lamar.", 
							new Dialogue(Character.LAMAR, 
									"Hey, what's up dog. Just last week I was jumped by some amateur thugs.",
									"They got lucky, because I was in the process of moving cocaine to a new warehouse.",
									"I need ya to teach them a lesson, since they wanna learn how to be thugs so bad.",
									"Also, while you're doing your thing, get my cocaine back too. That stuff is worth a lot nowadays."
									), false, Character.LAMAR, null, null, 0, false, false), 
					new ReachDestinationObjective("Get to the thugs.", null, false, Gang.THUG.getHideoutLocation(1)),
					new KillTargetObjective("Kill the thugs.", null, true, EntityType.VILLAGER, 0, "Thug", true),
					new ApproachObjective("Speak to Lamar.", 
							new Dialogue(Character.LAMAR,
									"Nice job dude. You got that cocaine too, right?",
									"Good. You're a good worker, man. I'll try to hook you up with some more people soon.",
									"Here's a bit of money, and here's a little bit of cocaine too, for your personal enjoyment."
									), false, Character.LAMAR, null, Material.SUGAR, 4, false, false)),
			12, // ID
			Character.LAMAR,
			2,
			22,
			Arrays.asList(new ItemReward(Utils.getCocaineItem(1)), new MoneyReward(85).asReward())),
	VERDICT_GUILTY("Verdict: Guilty", 
			"Assassinate a judge for Michael.", 
			MissionType.REGULAR,
			Arrays.asList(
					new ApproachObjective("Speak to Michael.", 
							new Dialogue(Character.MICHAEL, 
									"My man %n! You've come back at a great time.",
									"Yesterday, a judge fined me for parking illegally. Go figure, right?",
									"With all the crime in this city, you would think he'd have other things to worry about.",
									"Anyways, I'd like you to show that judge what really happens in this city."
									), false, Character.MICHAEL, null, null, 0, false, false), 
					new ReachDestinationObjective("Get to the judge.", null, false, Gang.BODYGUARD.getHideoutLocation(1)),
					new KillTargetObjective("Kill the bodyguards.", null, true, EntityType.VILLAGER, 0, "Bodyguard", true),
					new ReachDestinationObjective("Find the judge.", null, false, Gang.JUDGE.getHideoutLocation(1)),
					new KillTargetObjective("Kill the judge.", null, true, EntityType.VILLAGER, 0, "Judge", true),
					new ApproachObjective("Speak to Michael.", 
							new Dialogue(Character.MICHAEL,
									"Great job man. I'm surprised you haven't been arrested yet.",
									"Remember, as long as you're available for work, I'll be right here waiting.",
									"And hey, take this cash, you're going to need it for a better weapon than that."
									), false, Character.MICHAEL, null, null, 0, false, false)),
			13, // ID
			Character.MICHAEL,
			4,
			28,
			Arrays.asList(new MoneyReward(65).asReward())),
			
			
			
			
			
	THE_ROBBERY("The Robbery", 
			"Rob a gas station.", 
			MissionType.SIDE_MISSION,
			Arrays.asList(
					new ApproachObjective("Speak to Lamar.", 
							new Dialogue(Character.LAMAR,
									"Yo %n! So you're just looking for some money, huh?", 
									"Well, people around the city love robbing gas stations.",
									"How about you go rob one? It'll boost your rep and get you some extra cash."
									), false, Character.LAMAR, null, null, 0, false, false), 
					new RobStationObjective("Rob a gas station.", null, false, 10)),
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
									"Everyone knows these LCPD cops are the easiest to escape.",
									"If you escape them three times in a row, it'll prove how stupid they are and boost your rep."
									), false, Character.LAMAR, null, null, 0, false, false), 
					new EscapeCopsObjective("Lose the cops three times in a row.", null, false, 3),
					new ApproachObjective("Speak to Lamar.", 
							new Dialogue(Character.LAMAR,
									"Nice job yo, that was perfect. Your reputation is already starting to grow.", 
									"By the way man, I've got this extra AP Pistol from a thug off of the streets.",
									"I dunno if you need it or not, but here it is anyway."
									), false, Character.LAMAR, null, null, 0, false, false)),
			8, // ID
			Character.LAMAR,
			3,
			15,
			Arrays.asList(Weapon.get(WeaponType.AP_PISTOL).asReward())),
	WHERE_MY_KIDNEY("Where's My Kidney", 
			"Steal a kidney.", 
			MissionType.SIDE_MISSION,
			Arrays.asList(
					new ApproachObjective("Speak to Michael.", 
							new Dialogue(Character.MICHAEL,
									"How's it been, %n? I've got another task for you.", 
									"I'm a little strapped on cash, and I only know of one really quick way to make some money.",
									"The Black Market buys kidneys for $400 each. Go out and steal a single kidney for me, and I'll give you some of the profit."
									), false, Character.MICHAEL, null, null, 0, false, false), 
					new StealKidneyObjective("Steal a player's kidney.", null, false, 1),
					new ApproachObjective("Speak to Michael.", 
							new Dialogue(Character.MICHAEL,
									"Nice job dude, thanks. This is easy cash, you know. You could do this whenever you want.", 
									"Oh yeah, here's the $85 I promised. Maybe spend it on some more chloroform rags?"
									), false, Character.MICHAEL, null, Material.FERMENTED_SPIDER_EYE, 1, false, false)),
			14, // ID
			Character.MICHAEL,
			5,
			17,
			Arrays.asList(new MoneyReward(85).asReward()));
	
	private static List<Mission> list = null;
	
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
	
	/**
	 * Get a mission from a name
	 * @param name - The name of the mission
	 * @return The mission from the ID
	 */
	public static final Mission getMissionFromName(String name){
		Mission mission = null;
		for(Mission m : values()){
			if(m.getName().equalsIgnoreCase(name)){
				mission = m;
				break;
			}
		}
		return mission;
	}
	
	/**
	 * Get the total amount of missions
	 */
	public static final int getTotalMissions(){
		int total = 0;
		for(Mission m : values()){
			if(m.getType() == MissionType.REGULAR){
				total++;
			}
		}
		return total;
	}
	
	/**
	 * Get the list of missions sorted from lowest level to highest
	 * @return The list of missions sorted from lowest level to highest
	 */
	@SuppressWarnings("rawtypes")
	public static final List<Mission> list(){
		if(list == null){
			list = new ArrayList<Mission>();
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			for(Mission m : values()){
				map.put(m.getName(), m.getMinimumLevel());
			}
			ValueComparator bvc =  new ValueComparator(map);
			TreeMap<String,Integer> sorted = new TreeMap<String,Integer>(bvc);
			sorted.putAll(map);
			
			Iterator it = sorted.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry pairs = (Map.Entry)it.next();
				list.add(getMissionFromName((String) pairs.getKey()));
				it.remove();
			}
		}
		return list;
	}
}
