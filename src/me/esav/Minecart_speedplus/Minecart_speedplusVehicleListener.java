package me.esav.Minecart_speedplus;


import java.util.logging.Logger;


import org.bukkit.entity.Minecart;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleListener;
import org.bukkit.util.Vector;

public class Minecart_speedplusVehicleListener extends VehicleListener {

	public static Minecart_speedplus plugin;
	Logger log = Logger.getLogger("Minecraft");

	public Minecart_speedplusVehicleListener(Minecart_speedplus instance) { 

        plugin = instance;
	}

	public void onVehicleCreate(VehicleCreateEvent event) {
		if (event.getVehicle() instanceof Minecart) {

			Minecart cart = (Minecart)event.getVehicle();
			cart.setMaxSpeed(0.4*Minecart_speedplus.speedmultiplier);


		}
	}

}