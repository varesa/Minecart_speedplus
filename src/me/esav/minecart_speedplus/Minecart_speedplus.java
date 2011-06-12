package me.esav.minecart_speedplus;

import java.util.logging.Logger;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Minecart_speedplus extends JavaPlugin {

	Logger log = Logger.getLogger("Minecraft");
	private final Minecart_speedplusVehicleListener VehicleListener = new Minecart_speedplusVehicleListener(this);
	
	public void onEnable(){ 
				
		log.info("Your plugin has been enabled.");
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvent(Event.Type.VEHICLE_CREATE, VehicleListener, Event.Priority.Normal, this);	
		
		
	} 
	public void onDisable(){ 
	 
		log.info("Your plugin has been disabled.");
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		while (true) { }
	}

	
}




