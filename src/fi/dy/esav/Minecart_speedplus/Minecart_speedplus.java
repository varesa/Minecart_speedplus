package fi.dy.esav.Minecart_speedplus;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Minecart_speedplus extends JavaPlugin {
	

	Logger log = Logger.getLogger("Minecraft");
	private final Minecart_speedplusVehicleListener VehicleListener = new Minecart_speedplusVehicleListener(this);
	
	static double speedmultiplier = 1.25;
	
	boolean result;
	
	public static double getSpeedMultiplier() {
		return speedmultiplier;
	}
	
	public boolean setSpeedMultiplier(double multiplier) {
		if ( 0 < multiplier & multiplier <= 50) {
			speedmultiplier = multiplier;
			return true;
		}	return false;
	}
	
	public void onEnable(){ 
				
		log.info("Minecart_speed+ has been enabled.");
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvent(Event.Type.VEHICLE_CREATE, VehicleListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.VEHICLE_MOVE, VehicleListener, Event.Priority.Normal, this);
		
		
	} 
	public void onDisable(){ 
	 
		log.info("Minecart_speed+ stopped.");
		
	}
	
	double multiplier;
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		  
		  if(cmd.getName().equalsIgnoreCase("msp")){ // If the player typed /basic then do the following...
			  
			  try {
				  multiplier = Double.parseDouble(args[0]);
			  } catch (Exception e) {
				  sender.sendMessage(ChatColor.DARK_BLUE + "Minecart_speed+: Multiplier should be a number");
				  return false;
			  }
			  
			  result = setSpeedMultiplier(multiplier);
			  if (result) {
				  sender.sendMessage(ChatColor.DARK_BLUE + "Minecart_speed+: Speed multiplier for new carts set to: " + multiplier);
				  return true;
			  } else {
				  sender.sendMessage(ChatColor.DARK_BLUE + "minecart_speed+: Value must be non-zero and under 50");
				  return true;
			  }
			  
			  
		  } //If this has happened the function will break and return true. if this hasn't happened the a value of false will be returned.
		  return false;
		  
	  }
	
}




