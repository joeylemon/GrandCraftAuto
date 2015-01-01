package com.grandcraftauto.game.leaderboards;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.grandcraftauto.core.Main;
import com.grandcraftauto.game.player.GPlayer;
import com.grandcraftauto.utils.Utils;

public class LeaderboardUtils {

	static Main main = Main.getInstance();
	private static String gray = ChatColor.GRAY + "";
	private static String gold = ChatColor.GOLD + "";

	public static HashMap<LeaderboardType, Long> refresh = new HashMap<LeaderboardType, Long>();
	public static HashMap<LeaderboardType, Leaderboard> leaderboards = new HashMap<LeaderboardType, Leaderboard>();
	public static List<LeaderboardType> sorting = new ArrayList<LeaderboardType>();

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public static final void sendLeaderboard(final Player player, final LeaderboardType type, final int page){
		final GPlayer gplayer = new GPlayer(player);
		long lastRefresh = 0;
		if(refresh.containsKey(type)){
			lastRefresh = refresh.get(type);
		}
		if((System.currentTimeMillis() - lastRefresh) > 0){
			/*
			 * Refresh leaderboard
			 */
			if(!sorting.contains(type)){
				sorting.add(type);
				main.getServer().getScheduler().scheduleAsyncDelayedTask(main, new Runnable(){
					public void run(){
						if(leaderboards.containsKey(type)){
							leaderboards.remove(type);
						}
						
						gplayer.sendMessage("Please wait while the leaderboard is sorted...");
						long begin = System.currentTimeMillis();
						
						File folder = new File(main.getDataFolder() + File.separator + "players");
						HashMap<String,Integer> leaderboard = new HashMap<String,Integer>();
						ValueComparator bvc =  new ValueComparator(leaderboard);
						TreeMap<String,Integer> sorted = new TreeMap<String,Integer>(bvc);
						int players = 0;
						for(File f : folder.listFiles()){
							players++;
							GPlayer gp = new GPlayer(f);
							String name = gp.getName();
							if(name != null && Utils.isOnline(name) == true){
								gp = new GPlayer(Bukkit.getPlayer(name));
							}
							if(type == LeaderboardType.KILLS){
								leaderboard.put(gp.getName(), gp.getKills());
							}else if(type == LeaderboardType.LEVEL){
								leaderboard.put(gp.getName(), gp.getLevel());
							}else if(type == LeaderboardType.PLAYTIME){
								leaderboard.put(gp.getName(), gp.getPlaytime());
							}
						}
						sorted.putAll(leaderboard);
						
						Leaderboard board = new Leaderboard(type, new ArrayList<String>());

						Iterator it = sorted.entrySet().iterator();
						int current = 1;
						while (it.hasNext()) {
							if(current <= 42){
								Map.Entry pairs = (Map.Entry)it.next();
								board.addEntry(pairs.getKey() + "|" + pairs.getValue());
								it.remove();
								current++;
							}else{
								break;
							}
						}
						
						leaderboards.put(type, board);
						refresh.remove(type);
						refresh.put(type, System.currentTimeMillis() + 1200000);
						
						long finish = System.currentTimeMillis();
						long time = finish - begin;
						double sec = Utils.round((time / 1000.0F), 5);
						gplayer.sendMessage("Successfully sorted " + gold + players + gray + " players in " + gold + sec + gray + "s!");
						if(sorting.contains(type)){
							sorting.remove(type);
						}
						
						sendSavedLeaderboard(player, type, board, page);
					}
				}, 1);
			}else{
				gplayer.sendMessage("This leaderboard is currently being refreshed.");
			}
		}else{
			sendSavedLeaderboard(player, type, leaderboards.get(type), page);
		}
	}
	
	public static final void sendSavedLeaderboard(Player player, LeaderboardType type, Leaderboard board, int page){
		GPlayer gplayer = new GPlayer(player);
		String name = WordUtils.capitalizeFully(type.toString());
		List<String> set = board.getSet();
		int size = set.size();
		
		if(size > 0){
			int sizePerPage=7;
			if(size < sizePerPage){
				sizePerPage = size;
			}
			double total = (Utils.roundUp(size, sizePerPage)) / sizePerPage;
			int totalpages = (int)total;
			if(page > totalpages){
				page = totalpages;
			}
			gplayer.sendMessageHeader("Top " + name + " - Page " + page + "/" + totalpages);
			page--;
			
			int from = Math.max(0,page*sizePerPage);
			int to = Math.min(size,(page+1)*sizePerPage);
			
			page++;
			List<String> result = set.subList(from,to);
			int current = from + 1;
			for(String s : result){
				String entry[] = s.split("\\|");
				String key = entry[0];
				int value = Integer.parseInt(entry[1]);
				if(type != LeaderboardType.PLAYTIME){
					gplayer.sendMessage(gray + current + ". " + gold + key + gray + " - " + name + ": " + gold + value);
				}else{
					String time = Utils.convertSeconds(value);
					gplayer.sendMessage(gray + current + ". " + gold + key + gray + " - " + name + ": " + gold + time);
				}
				current++;
			}
			if(result.size() >= sizePerPage && page < totalpages){
				player.sendMessage(" ");
				gplayer.sendMessage("Type " + gold + "/top " + type.toString().toLowerCase() + " " + (page + 1) + gray + " to go to the next page!");
			}
		}else{
			gplayer.sendError("There are no entries in this leaderboard.");
		}
		/*
		gplayer.sendMessageHeader("Top " + WordUtils.capitalizeFully(type.toString()));
		int current = 1;
		for(String s : board.getSet()){
			String entry[] = s.split("\\|");
			String key = entry[0];
			int value = Integer.parseInt(entry[1]);
			if(type != LeaderboardType.PLAYTIME){
				gplayer.sendMessage(gray + current + ". " + gold + key + gray + " - " + WordUtils.capitalizeFully(type.toString()) + ": " + gold + value);
			}else{
				String time = Utils.convertSeconds(value);
				gplayer.sendMessage(gray + current + ". " + gold + key + gray + " - " + WordUtils.capitalizeFully(type.toString()) + ": " + gold + time);
			}
			current++;
		}
		*/
	}

}
