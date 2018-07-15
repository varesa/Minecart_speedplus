package fi.dy.esav.Minecart_speedplus;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Minecart_speedplus extends JavaPlugin {
	public static boolean verbose = false;
	
	public static boolean speedometer = true;
	public static boolean speedometerAtStandardSpeed = false;
	
	public static FileConfiguration config;

	Logger log = Logger.getLogger("Minecraft");
	private final Minecart_speedplusVehicleListener VehicleListener = new Minecart_speedplusVehicleListener(this);
	private final Minecart_speedplusSignListener SignListener = new Minecart_speedplusSignListener(this);
	
	static double speedmultiplier = 1.25;
	
	boolean result;
	
	public static double getSpeedMultiplier() {
		return speedmultiplier;
	}
	
	public boolean setSpeedMultiplier(double multiplier) {
		if ( 0 < multiplier & multiplier <= 50) {
			speedmultiplier = multiplier;
			return true;
		}
		return false;
	}
	
	public void onEnable(){ 		
		log.info(getDescription().getName() + " version " + getDescription().getVersion() + " started.");

		config = this.getConfig();
		config.addDefault("defaultSpeedMultiplier", speedmultiplier);
		config.addDefault("verbose", false);
		config.addDefault("enableSpeedometer", true);
		config.addDefault("showSpeedometerAtStandardSpeed", true);
		config.options().copyDefaults(true);
		saveConfig();

		verbose = config.getBoolean("verbose");
		speedmultiplier = config.getDouble("defaultSpeedMultiplier");
		speedometer = config.getBoolean("enableSpeedometer");
		speedometerAtStandardSpeed = config.getBoolean("showSpeedometerAtStandardSpeed");

		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(VehicleListener, this);
		pm.registerEvents(SignListener, this);
	} 
	
	public void onDisable(){ 
		log.info(this.getDescription().getName() + " version "
				+ this.getDescription().getVersion() + " stopped.");
	}
	
	double multiplier;
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){	  
		  if (cmd.getName().equalsIgnoreCase("msp")) {
			if ((sender instanceof Player)) {
				Player player = (Player) sender;
				if (!player.hasPermission("msp.cmd")) {
					player.sendMessage("You don't have the right to do that");
					return true;
				}
			}
			
			if (args.length == 0) {
				sender.sendMessage("Type '/help Minecart_speedplus' for available commands");
				return true;
			}
			switch (args[0].toUpperCase()) {
				case "SPEEDMULTIPLIER":
					try {
						multiplier = Double.parseDouble(args[1]);
					} catch (Exception e) {
						sender.sendMessage("Minecart_speed+: ERROR: Multiplier should be a number");
						return false;
					}
	
					result = setSpeedMultiplier(multiplier);
					if (result) {
						sender.sendMessage("Minecart_speed+: Speed multiplier for new carts set to: "
								+ multiplier);
						config.set("defaultSpeedMultiplier", multiplier);
						saveConfig();
						return true;
					}
					else if (args.length == 1) {
						sender.sendMessage("Current value of 'defaultSpeedMultiplier': " + config.getBoolean("defaultSpeedMultiplier"));
						break;
					}
					else
					{
						sender.sendMessage("Minecart_speed+: ERROR: Value must be non-zero and under 50");
					}
					return true;
				
				case "VERBOSE":
					boolean verbose;
					if (args.length > 1 && (args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("false"))) {
						verbose = Boolean.valueOf(args[1]);
					}
					else if (args.length == 1) {
						sender.sendMessage("Current value of 'verbose': " + config.getBoolean("verbose"));
						break;
					}
					else {
						sender.sendMessage("Minecart_speed+: ERROR: Boolean should either be 'true' or 'false'");
						break;
					}
					
					Minecart_speedplus.verbose = verbose;
					config.set("verbose", verbose);
					saveConfig();
					sender.sendMessage("Minecart_speed+: Verbose boolean set to '" + verbose + "'");
					return true;
					
				case "SPEEDOMETER":
					boolean speedometer;
					if (args.length > 1 && (args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("false"))) {
						speedometer = Boolean.valueOf(args[1]);
					}
					else if (args.length == 1) {
						sender.sendMessage("Current value of 'enableSpeedometer': " + config.getBoolean("enableSpeedometer"));
						break;
					}
					else {
						sender.sendMessage("Minecart_speed+: ERROR: Boolean should either be 'true' or 'false'");
						break;
					}
					
					Minecart_speedplus.speedometer = speedometer;
					config.set("speedometer", speedometer);
					saveConfig();
					sender.sendMessage("Minecart_speed+: Speedometer set to '" + speedometer + "'");
					
					return true;
					
				case "SPEEDOMETERATSTANDARDSPEED":
					boolean speedometerAtStandardSpeed;
					if (args.length > 1 && (args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("false"))) {
						speedometerAtStandardSpeed = Boolean.valueOf(args[1]);
					}
					else if (args.length == 1) {
						sender.sendMessage("Current value of 'showSpeedometerAtStandardSpeed': " + config.getBoolean("showSpeedometerAtStandardSpeed"));
						break;
					}
					else {
						sender.sendMessage("Minecart_speed+: ERROR: Boolean should either be 'true' or 'false'");
						break;
					}
					
					Minecart_speedplus.speedometerAtStandardSpeed = speedometerAtStandardSpeed;
					config.set("showSpeedometerAtStandardSpeed", speedometerAtStandardSpeed);
					saveConfig();
					sender.sendMessage("Minecart_speed+: Speedometer at standard speed set to '" + speedometerAtStandardSpeed + "'");
					
					return true;
					
				default:
					sender.sendMessage("Minecart_speed+: ERROR: Invalid command. Type '/help Minecart_speedplus' for available commands");
					return true;
			}

		} //If this has happened the function will break and return true. if this hasn't happened the a value of false will be returned.

		return false;
	  }
}