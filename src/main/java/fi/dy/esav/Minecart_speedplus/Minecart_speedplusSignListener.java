package fi.dy.esav.Minecart_speedplus;

import org.bukkit.block.Sign;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;

public class Minecart_speedplusSignListener extends BlockListener {
	
	Minecart_speedplus plugin;
	
	public Minecart_speedplusSignListener(Minecart_speedplus instance) {

		plugin = instance;
	}
	
	public void onSignChange (SignChangeEvent e) {
		e.getPlayer().sendMessage("created sign");
		e.getPlayer().sendMessage("1st line: " + e.getLine(0));
		if(e.getLine(0).equalsIgnoreCase("[msp]")){
			e.getPlayer().sendMessage("got msp sign");
			
			if(e.getLine(1).equalsIgnoreCase("fly") || e.getLine(1).equalsIgnoreCase("nofly")){
				e.getPlayer().sendMessage("it is a fly sign");
				if(!(e.getPlayer().hasPermission("msp.signs.fly"))) {
					e.getPlayer().sendMessage("no perms to fly");
					e.setLine(0, "NO PERMS");
				} else {
					e.getPlayer().sendMessage("you can fly");
				}
			} else {
				boolean error = false;
				double speed = -1;
				
				try {
					speed = Double.parseDouble(e.getLine(1));
				} catch (Exception ex) {
					error = true;
				}
				
				if (error || 50 < speed || speed < 0) {
					e.setLine(1, "WRONG VALUE");
				}
				
				if(!(e.getPlayer().hasPermission("msp.signs.speed"))) {
					e.setLine(0, "NO PERMS");
				}
				
				e.getPlayer().sendMessage("error: " + error + ", speed: " + speed);
			}
			
			
		}
		
	}



}
