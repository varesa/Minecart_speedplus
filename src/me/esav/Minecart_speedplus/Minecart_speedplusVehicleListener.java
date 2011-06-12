package me.esav.Minecart_speedplus;


import java.util.logging.Logger;


import org.bukkit.entity.Minecart;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleListener;;;

public class Minecart_speedplusVehicleListener extends VehicleListener {

	public static Minecart_speedplus plugin;
	Logger log = Logger.getLogger("Minecraft");

	public Minecart_speedplusVehicleListener(Minecart_speedplus instance) { 

        plugin = instance;
	}

	public void onVehicleCreate(VehicleCreateEvent event) {
		if (event.getVehicle() instanceof Minecart) {

			Minecart cart = (Minecart)event.getVehicle();
			cart.setMaxSpeed(cart.getMaxSpeed()*Minecart_speedplus.speedmultiplier);
			String info = "Minecart "+event.getVehicle().getEntityId()+" has speed set to "+cart.getMaxSpeed();
			log.info(info);

		}
	}

}