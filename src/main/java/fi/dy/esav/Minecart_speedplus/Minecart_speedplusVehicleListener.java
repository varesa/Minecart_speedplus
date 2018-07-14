package fi.dy.esav.Minecart_speedplus;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.util.Vector;

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

	@EventHandler(priority = EventPriority.NORMAL)
	public void onVehicleCreate(VehicleCreateEvent event) {
		if (event.getVehicle() instanceof Minecart) {

			Minecart cart = (Minecart) event.getVehicle();
			cart.setMaxSpeed(0.4 * Minecart_speedplus.getSpeedMultiplier());

		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onVehicleMove(VehicleMoveEvent event) {

		if (event.getVehicle() instanceof Minecart) {

			Minecart cart = (Minecart) event.getVehicle();
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
						blockid = cart.getWorld().getBlockTypeIdAt(blockx, blocky, blockz);

						if (blockid == Material.WALL_SIGN.getId() || blockid == Material.SIGN_POST.getId()) {
							Sign sign = (Sign) block.getState();
							String[] text = sign.getLines();

							if (text[0].equalsIgnoreCase("[msp]")) {

								if (text[1].equalsIgnoreCase("fly")) {
									cart.setFlyingVelocityMod(flyingmod);

								} else if (text[1].equalsIgnoreCase("nofly")) {

									cart.setFlyingVelocityMod(noflyingmod);

								} else {

									error = false;
									try {

										line1 = Double.parseDouble(text[1]);

									} catch (Exception e) {

										sign.setLine(2, "  ERROR");
										sign.setLine(3, "WRONG VALUE");
										sign.update();
										error = true;

									}
									if (!error) {

										if (0 < line1 & line1 <= 50) {
											// Acceleration/deceleration of cart
											// speed
											// Running as separate threads to
											// not freeze whole plugin while
											// using Thread.sleep
											Thread thread = new Thread() {
												public void run() {
													double signMultiplier = Double.parseDouble(text[1]);
													double speedMultiplier = cart.getMaxSpeed() / 0.4D;

													if (signMultiplier > cart.getMaxSpeed() / 0.4D) {
														// accelerate
														double diff = signMultiplier
																- round(cart.getMaxSpeed() / 0.4D, 1);
														for (double i = 0; i < diff; i += 0.1) {
															speedMultiplier += 0.1;
															cart.setMaxSpeed(0.4D * speedMultiplier);
															if (Minecart_speedplus.verbose)
																Bukkit.broadcastMessage(
																		"Minecart_speed+: Accelerating. Current multiplier: "
																				+ speedMultiplier);
															try {
																Thread.sleep(500);
															} catch (InterruptedException e) {
																cart.setMaxSpeed(0.4D * signMultiplier);
																break;
															}
														}
													} else if (signMultiplier < cart.getMaxSpeed() / 0.4D) {
														// decelerate
														double diff = round(cart.getMaxSpeed() / 0.4D, 1)
																- signMultiplier;
														for (double i = 0; i < diff; i += 0.1) {
															speedMultiplier -= 0.1;
															cart.setMaxSpeed(0.4D * speedMultiplier);
															if (Minecart_speedplus.verbose)
																Bukkit.broadcastMessage(
																		"Minecart_speed+: Decelerating. Current multiplier: "
																				+ speedMultiplier);
															try {
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
													// acceleration function
													cart.setMaxSpeed(0.4D * Double.parseDouble(text[1]));
													if (Minecart_speedplus.verbose)
														Bukkit.broadcastMessage(
																"Final multiplier: " + Double.parseDouble(text[1]));
												}
											};
											thread.start();

											// cart.setMaxSpeed(0.4D *
											// Double.parseDouble(text[1]));

										} else {

											sign.setLine(2, "  ERROR");
											sign.setLine(3, "WRONG VALUE");
											sign.update();
										}
									}
								}
							}

						}

					}
				}
			}

		}
	}

}
