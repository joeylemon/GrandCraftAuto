package gca.tasks;

import gca.core.GPlayer;
import gca.core.Main;
import gca.game.inventories.InventoryHandler;
import gca.game.missions.Dialogue;
import gca.game.missions.objectives.ApproachObjective;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MessageTask extends BukkitRunnable{
	
	Main main = Main.getInstance();
	private String gray = ChatColor.GRAY + "";
	private String gold = ChatColor.GOLD + "";
	
	private Player player;
	private GPlayer gplayer;
	private Dialogue dialogue = null;
	private List<String> messages;
	
	public MessageTask(Player player, Dialogue dialogue){
		this.player = player;
		this.gplayer = new GPlayer(player);
		this.dialogue = dialogue;
		this.messages = new ArrayList<String>();
		for(int x = 0; x <= (dialogue.getDialogue().length - 1); x++){
			this.messages.add(dialogue.getDialogue()[x]);
		}
		Main.getInstance().messageTask.add(player.getName());
	}
	
	public MessageTask(Player player, List<String> messages){
		this.player = player;
		this.gplayer = new GPlayer(player);
		this.messages = messages;
		Main.getInstance().messageTask.add(player.getName());
	}
	
	int sentMsgs = 0;
	public void run(){
		if(sentMsgs <= (messages.size() - 1)){
			if(dialogue != null){
				gplayer.sendMessage(gold + dialogue.getCharacter().getName() + ": " + gray + messages.get(sentMsgs).replaceAll("%n", player.getName()));
			}else{
				gplayer.sendMessage(messages.get(sentMsgs).replaceAll("%n", player.getName()));
			}
			player.playSound(player.getLocation(), Sound.ORB_PICKUP, 0.8F, 1.5F);
			sentMsgs++;
		}else{
			if(main.messageTask.contains(player.getName())){
				for(int x = 0; x <= (main.messageTask.size() - 1); x++){
					if(main.messageTask.get(x).equalsIgnoreCase(player.getName())){
						main.messageTask.remove(x);
					}
				}
			}
			if(main.tutorial.containsKey(player.getName())){
				main.tutorial.get(player.getName()).advanceStep();
			}else if(gplayer.hasMission() == true){
				if(gplayer.getObjective() instanceof ApproachObjective){
					ApproachObjective obj = (ApproachObjective) gplayer.getObjective();
					if(obj.getItemToGive() != null){
						InventoryHandler.addItemToAppropriateSlot(player, obj.getItemToGive());
					}
				}
				gplayer.advanceObjective();
			}
			this.cancel();
		}
	}

}
