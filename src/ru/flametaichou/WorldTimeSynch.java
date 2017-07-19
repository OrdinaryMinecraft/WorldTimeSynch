package ru.flametaichou;

import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;


public class WorldTimeSynch extends JavaPlugin {
	
	Logger log = getLogger(); 
	String prefix = "[WorldTimeSynch] ";
	String logPrefix = "[LOG] ";
	String version = "0.1";
	protected FileConfiguration config;
	int confMinutes;
	String confWorldName;
	boolean confLog;
	
	public void loadConfiguration(){
		
	    this.getConfig().addDefault("period", 1);
	    this.getConfig().addDefault("worldname", "world-spawn");
	    this.getConfig().addDefault("log", true);
	    this.getConfig().options().copyDefaults(true); 
	    this.saveConfig();
	}
	
	public void reloadCfg(){
		
		this.reloadConfig();
		confMinutes = this.getConfig().getInt("period");
		confWorldName = this.getConfig().getString("worldname");
		confLog = this.getConfig().getBoolean("log");
	}
	
	public void onEnable(){ 
		
		log.info("Просыпаеццо...");
		loadConfiguration();
		reloadCfg();
		BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                log.info("Проверяю нет ли различий во времени.");
                Server server = getServer();
                long time1 = server.getWorld("world").getFullTime();
                long time2 = server.getWorld(confWorldName).getFullTime();
                if (time1 != time2) {
                	server.getWorld(confWorldName).setTime(time1);
                	if (confLog) log.info("Найдены различия во времени с основным миром, меняю время в " + confWorldName);
                }
                else {
                	log.info("Различий во времени не обнаружено!");
                }
    
            }
        }, 0L, 1200L*confMinutes);
        
		//PluginManager pm = this.getServer().getPluginManager();
		
	}

	public void onDisable(PluginDisableEvent event) { 
		
		log.info("Засыпает...");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		
		if(cmd.getName().equalsIgnoreCase("wts")){
			
			if(args.length != 0) {
				
				switch (args[0]) {
					case "reload":
						return true;
					case "help":
						sender.sendMessage(this.prefix + "Доступные команды:");
						sender.sendMessage("/wts reload" + " - перезагрузить плагин");
						sender.sendMessage("/wts help" + " - вывести справку");
						return true;
					}
                } else return false;
					
		}
		return false; 
	}
}
