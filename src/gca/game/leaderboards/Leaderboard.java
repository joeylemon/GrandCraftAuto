package gca.game.leaderboards;

import java.util.List;

public class Leaderboard {
	
	private LeaderboardType type;
	private List<String> set;
	public Leaderboard(LeaderboardType type, List<String> set){
		this.type = type;
		this.set = set;
	}
	
	public LeaderboardType getType(){
		return type;
	}
	
	public List<String> getSet(){
		return set;
	}
	
	public void addEntry(String entry){
		set.add(entry);
	}
}
