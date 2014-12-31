package gca.game.cars;

import gca.core.Main;
import gca.game.Skill;
import gca.game.jobs.JobInstance;
import gca.game.jobs.JobState;
import gca.game.jobs.Race;
import gca.game.player.GPlayer;
import gca.utils.Utils;

import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.useful.uCarsAPI.CarSpeedModifier;

public class SpeedModifier implements CarSpeedModifier{

	Main main = Main.getInstance();
	
	@Override
	public Vector getModifiedSpeed(Minecart car, Vector travelVector,
			double currentMultiplier) {
		if(car.getPassenger() instanceof Player){
			Player player = (Player) car.getPassenger();
			GPlayer gplayer = new GPlayer(player);
			boolean cancel = false;
			if(gplayer.hasJob() == true){
				JobInstance job = gplayer.getJobInstance();
				if(job.getJob() instanceof Race && job.getState() == JobState.PREGAME){
					cancel = true;
				}
			}
			if(cancel == false){
				if(Utils.isRoadBlock(player.getWorld().getBlockAt(player.getLocation())) == true){
					Vector speed = travelVector;
					if(main.currentcar.containsKey(player.getName())){
						speed = travelVector.multiply(main.currentcar.get(player.getName()).getSpeed());
					}
					double driving = 1;
					driving += (gplayer.getSkillLevel(Skill.DRIVING) * .0015);
					return speed.multiply(driving);
				}else{
					return travelVector.multiply(.375);
				}
			}else{
				return travelVector.multiply(0);
			}
		}else{
			return travelVector;
		}
	}

}
