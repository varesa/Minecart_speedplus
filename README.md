# Minecart_speed+ v2.0.1

## [Download plugin jar](https://github.com/steel9/Minecart_speedplus/raw/master/bin/Minecart_speedplus_2.0.1.jar)

This is a fork of a plugin called Minecart_speed+ (v1.6.3) by Esa Varemo (https://github.com/varesa/Minecart_speedplus). This fork adds multiple improvements to the original plugin.     
     
Minecart_speed+ is a Spigot plugin that increases the maximum speed limits of minecarts.

Minecart_speedplus should be compatible with Spigot 1.12.2

**If you want to use the speedometer feature, the plugin ActionBarAPI is required (https://www.spigotmc.org/resources/actionbarapi-1-8-1-12-2.1315/). Everything except the speedometer should still work without ActionBarAPI.**

Highlighted new features in steel9 fork:
2.0.0:
- Changes to settings is now saved
- Implemented acceleration and deceleration instead of an instant speed change (when changing speed multiplier with signs)
- Speedometer (experimental, shows set speed according to multiplier, not always actual speed of cart (speed becomes incorrect at higher set speeds than possible, eg. if the multiplier is set to 100, the speedometer shows 28.8\*100 km/h, but the actual speed can't reach that high)). Requires ActionBarAPI to function (though not required if you don't want a speedometer).
- Project changed to target Java 1.8
- Now building with Spigot API (is probably still compatible with Bukkit)

Original author:     
Esa Varemo     
esa@kuivanto.fi     

This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
View it at: http://creativecommons.org/licenses/by-nc-sa/3.0/
