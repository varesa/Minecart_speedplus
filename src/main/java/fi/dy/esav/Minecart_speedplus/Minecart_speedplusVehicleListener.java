package fi.dy.esav.Minecart_speedplus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.util.Vector;

import com.connorlinfoot.actionbarapi.ActionBarAPI;

public class Minecart_speedplusVehicleListener implements Listener {

	int[] xmodifier = { -1, 0, 1 };
	int[] ymodifier = { -2, -1, 0, 1, 2 };
	int[] zmodifier = { -1, 0, 1 };

	int cartx, carty, cartz;
	int blockx, blocky, blockz;

	Block block;
	int blockid;

	double line1;

	public static Minecart_speedplus plugin;
	Logger log = Logger.getLogger("Minecraft");

	boolean error;

	Vector flyingmod = new Vector(10, 0.01, 10);
	Vector noflyingmod = new Vector(1, 1, 1);
	
	//Keeping lists with players with running threads, to prevent multiple threads from conflicting
		//ListMultimap<Player, Double> playersWithActiveAcceleration = ArrayListMultimap.create(); //(or deceleration)
		Map<Minecart, Sign> registeredMinecartSigns = new HashMap<Minecart, Sign>();
		List<Player> playersWithActiveSpeedometer = new ArrayList<Player>();

	public Minecart_speedplusVehicleListener(Minecart_speedplus instance) {
		plugin = instance;
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}
	
	public static boolean moreThanOnce(List<Player> list, Player search) 
	{
	    int objCount = 0;

	    for (Object thisObj : list) {
	        if (thisObj == search) objCount++;
	    }

	    return objCount > 1;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onVehicleCreate(VehicleCreateEvent event) {
		if (event.getVehicle() instanceof Minecart) {

			Minecart cart = (Minecart) event.getVehicle();
			cart.setMaxSpeed(0.4 * Minecart_speedplus.getSpeedMultiplier());

		}
	}
	
	void activateSpeedometer(Minecart cart, boolean replaceThread) {
		List<Entity> passengers = cart.getPassengers();
		for (int i = 0; i < passengers.size(); i++) {
			final int c = i;
			Player player = (Player) passengers.get(c);
			
			//If speedometer is disabled, or replaceThread is set to false and other speedometer threads are running for the same player, return
			if (!Minecart_speedplus.speedometer || (moreThanOnce(playersWithActiveSpeedometer, player) && !replaceThread)) {
				return;
			}
			
			Thread speedometerThread = new Thread() {
				public void run() {

					boolean run = true;
					playersWithActiveSpeedometer.add(player);
					
					//While this is true, update the speedometer
					while (run && Minecart_speedplus.speedometer
							&& cart.getPassengers().contains(passengers.get(c)) &&
							!moreThanOnce(playersWithActiveSpeedometer, player)
							&& (Minecart_speedplus.speedometerAtStandardSpeed ||
									Double.compare(cart.getMaxSpeed() / 0.4D, Minecart_speedplus.getSpeedMultiplier()) > 0))
					{
						if (Minecart_speedplus.verbose) {
							Bukkit.broadcastMessage("Showing speedometer:");
							Bukkit.broadcastMessage("cart.getMaxSpeed() / 0.4D: " + cart.getMaxSpeed() / 0.4D + " > speedmultiplier: " + Minecart_speedplus.getSpeedMultiplier());
						}
						
						try {
							Bukkit.getScheduler().runTask(Minecart_speedplus.getPlugin(Minecart_speedplus.class), () -> {
								ActionBarAPI.sendActionBar((Player) passengers.get(c), "Current speed: " + round(8 * (cart.getMaxSpeed() / 0.4D) * 3.6, 1) + " km/h");
							});
						}
						catch (NoClassDefFoundError e) {
							run = false;
							playersWithActiveSpeedometer.remove(player);
							return;
						}
						
						try
						{
							Thread.sleep(500);
						}
						catch (InterruptedException e) {
							run = false;
							playersWithActiveSpeedometer.remove(player);
							return;
						}
					}
					
					//If speed is now standard speed, update the speedometer one last time to show the standard speed
					if (run && cart.getPassengers().contains(passengers.get(c)) &&
							!moreThanOnce(playersWithActiveSpeedometer, player)
							&& !(Minecart_speedplus.speedometerAtStandardSpeed ||
									Double.compare(cart.getMaxSpeed() / 0.4D, Minecart_speedplus.getSpeedMultiplier()) > 0)) {
						
						try
						{
							Bukkit.getScheduler().runTask(Minecart_speedplus.getPlugin(Minecart_speedplus.class), () -> {
								ActionBarAPI.sendActionBar((Player) passengers.get(c), "Current speed: " + round(8 * (cart.getMaxSpeed() / 0.4D) * 3.6, 1) + " km/h");
							});
						}
						catch (NoClassDefFoundError e) {
							run = false;
							playersWithActiveSpeedometer.remove(player);
							return;
						}
					}
					
					playersWithActiveSpeedometer.remove(player);
				}
			};
			
			speedometerThread.start();
		}
	}

	private void accelerateCart(Minecart cart, String[] signText) {
		double signMultiplier = Double.parseDouble(signText[1]);
		double speedMultiplier = cart.getMaxSpeed() / 0.4D; //current cart speed multiplier

		if (signMultiplier > speedMultiplier) {
			// accelerate
			double diff = signMultiplier
					- round(speedMultiplier, 1);
			for (int i = 0; i < diff * 10; i++) {

				if (Minecart_speedplus.verbose) {
					Bukkit.broadcastMessage("Minecart_speed+: Acceleration diff * 10: " + (diff * 10));
					Bukkit.broadcastMessage("Minecart_speed+: i variable: " + i);
				}

				speedMultiplier += 0.1;
				cart.setMaxSpeed(0.4D * speedMultiplier);
				if (Minecart_speedplus.verbose)
					Bukkit.broadcastMessage(
							"Minecart_speed+: Accelerating. Current multiplier: "
									+ speedMultiplier);

				//show speedometer and replace (delete) old speedometers for the same player by calling with
				//--> 'true'

				activateSpeedometer(cart, true);
				try {
					//Increase sleep interval to slow down acceleration
					Thread.sleep(500);
				} catch (InterruptedException e) {
					cart.setMaxSpeed(0.4D * signMultiplier);
					break;
				}
			}
		} else if (signMultiplier < speedMultiplier) {
			// decelerate
			double diff = round(speedMultiplier, 1)
					- signMultiplier;
			for (int i = 0; i < diff * 10; i++) {

				if (Minecart_speedplus.verbose) {
					Bukkit.broadcastMessage("Minecart_speed+: Deceleration diff * 10: " + (diff * 10));
					Bukkit.broadcastMessage("Minecart_speed+: i variable: " + i);
				}

				speedMultiplier -= 0.1;
				cart.setMaxSpeed(0.4D * speedMultiplier);
				if (Minecart_speedplus.verbose)
					Bukkit.broadcastMessage(
							"Minecart_speed+: Decelerating. Current multiplier: "
									+ speedMultiplier);

				//show speedometer
				activateSpeedometer(cart, true);
				try {
					//Increase sleep interval to slow down deceleration
					Thread.sleep(500);
				} catch (InterruptedException e) {
					cart.setMaxSpeed(0.4D * signMultiplier);
					break;
				}
			}
		}
		// Set the final speed
		// (multiplier according to
		// sign) to correct eventual
		// errors made by
		// acceleration (and deceleration) function
		cart.setMaxSpeed(0.4D * Double.parseDouble(signText[1]));
		if (Minecart_speedplus.verbose)
			Bukkit.broadcastMessage(
					"Minecart_speed+: Final multiplier: " + Double.parseDouble(signText[1]));

		Block block_ = cart.getWorld().getBlockAt
				(cart.getLocation().getBlockX(), cart.getLocation().getBlockY(), cart.getLocation().getBlockZ());

		//Check if cart is still standing beside the sign
		while ((block_.getBlockData() instanceof Sign || block_.getBlockData() instanceof WallSign)
				&& ((Sign)block_.getState()).getLines()[0].equalsIgnoreCase("[msp]")) {
			try {
				//Wait until cart has passed the sign
				if (Minecart_speedplus.verbose) {
					Bukkit.broadcastMessage("Minecart_speed+: Waiting for minecart to pass the sign...");
				}
				Thread.sleep(500);
			} catch (InterruptedException e) {
				registeredMinecartSigns.remove(cart);
				return;
			}

			//Update variables
			block_ = cart.getWorld().getBlockAt
					(cart.getLocation().getBlockX(), cart.getLocation().getBlockY(), cart.getLocation().getBlockZ());
		}

		//Remove the cart and sign from the list, as the sign is already passed
		//(won't disturb acceleration functions anymore)
		registeredMinecartSigns.remove(cart);
		if (Minecart_speedplus.verbose) {
			Bukkit.broadcastMessage("Minecart_speed+: Cart and sign removed from list");
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onVehicleMove(VehicleMoveEvent event) {

		if (event.getVehicle() instanceof Minecart) {

			Minecart cart = (Minecart) event.getVehicle();
			
			if (Minecart_speedplus.speedometerAtStandardSpeed) {
				activateSpeedometer(cart, false);
			}
			
			for (int xmod : xmodifier) {
				for (int ymod : ymodifier) {
					for (int zmod : zmodifier) {

						cartx = cart.getLocation().getBlockX();
						carty = cart.getLocation().getBlockY();
						cartz = cart.getLocation().getBlockZ();
						blockx = cartx + xmod;
						blocky = carty + ymod;
						blockz = cartz + zmod;
						block = cart.getWorld().getBlockAt(blockx, blocky, blockz);

						if (!(block.getState() instanceof Sign || block.getState() instanceof WallSign)) {
							return;
						}

						Sign sign = (Sign) block.getState();

						if (registeredMinecartSigns.containsKey(cart) && registeredMinecartSigns.get(cart).equals(sign)) {
							//This sign has already been registered by the speed system
							if (Minecart_speedplus.verbose) {
								Bukkit.broadcastMessage("Minecart_speed+: Cancelling speed change, another speed change to the same multiplier is already running");
							}
							return;
						}

						//Add the cart and sign to the list, to prevent the same sign from being registered multiple times while passing it
						registeredMinecartSigns.put(cart, sign);

						String[] text = sign.getLines();

						if (!text[0].equalsIgnoreCase("[msp]")) {
							return;
						}

						if (text[1].equalsIgnoreCase("fly")) {
							cart.setFlyingVelocityMod(flyingmod);
						} else if (text[1].equalsIgnoreCase("nofly")) {
							cart.setFlyingVelocityMod(noflyingmod);
						} else {
							//error = false;
							try {
								line1 = Double.parseDouble(text[1]);
							} catch (Exception e) {
								sign.setLine(2, "  ERROR");
								sign.setLine(3, "WRONG VALUE");
								sign.update();
								//error = true;
								return;
							}

							if (!(0 < line1 & line1 <= 50)) {
								sign.setLine(2, "  ERROR");
								sign.setLine(3, "WRONG VALUE");
								sign.update();
								return;
							}

							// Acceleration/deceleration of cart
							// speed
							// Running as separate threads to
							// not freeze whole plugin while
							// using Thread.sleep
							Thread thread = new Thread() {
								public void run() {
									accelerateCart(cart, text);
								}
							};
							thread.start();
						}
					}
				}
			}
		}
	}
}
