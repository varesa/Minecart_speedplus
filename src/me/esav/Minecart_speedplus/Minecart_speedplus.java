package me.esav.Minecart_speedplus;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Minecart_speedplus extends JavaPlugin {

	Logger log = Logger.getLogger("Minecraft");
	private final Minecart_speedplusVehicleListener VehicleListener = new Minecart_speedplusVehicleListener(this);
	
	static double speedmultiplier = 1.25;
	
	public void onEnable(){ 
				
		log.info("Minecart_speed+ has been enabled.");
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvent(Event.Type.VEHICLE_CREATE, VehicleListener, Event.Priority.Normal, this);	
		
		
	} 
	public void onDisable(){ 
	 
		log.info("Minecart_speed+ stopped.");
		
	}
	
	  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		  
		  if(cmd.getName().equalsIgnoreCase("msp")){ // If the player typed /basic then do the following...
			  
			  try {
				  speedmultiplier = Double.parseDouble(args[0]);
			  } catch (Exception e) {
				  System.out.println("improper argument:"+e.getMessage());
				  return false;
			  }
			  
			  if ( 0 < speedmultiplier & speedmultiplier < 50) {
				  return true;
			  } else {
				  return false;
			  }
			  
			  
		  } //If this has happened the function will break and return true. if this hasn't happened the a value of false will be returned.
		  return false;
		  
	  }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		while (true) { }
	}

	
}




