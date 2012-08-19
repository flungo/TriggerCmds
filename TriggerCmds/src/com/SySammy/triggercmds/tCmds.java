/**
 *
 * Copyright by Samuel Castro and Fabrizio Lungo, 2011
 * All Rights Reserved
 *
 * NOTES:
 * Please do not redistribute this plugin without our permission
 */
package com.SySammy.triggercmds;

import com.avaje.ebean.QueryIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class tCmds extends JavaPlugin {
	
    public HashMap<Player, String> iNames = new HashMap<Player, String>();
    public HashMap<Location, String> Cmds = new HashMap<Location, String>();
    private tListener playerListener = new tListener(this);
    private boolean UsePermissions = true;
	
	public PluginManager pm;
    public PluginDescriptionFile pdf;
	
	public final Log logger = new Log(this);
	public final Permissions permissions = new tPermissions(this, logger);

    public static void main(String[] args) {
        // TODO code application logic here
    }

    @Override
    public void onDisable() {
		disable();
        logger.logMessage(pdf.getName() + " version " + pdf.getVersion() + " is disabled!");
    }

    @Override
    public void onEnable() {
        pm = getServer().getPluginManager();
    	pdf = getDescription();
		getConfig().options().copyDefaults(true);
		saveConfig();
        getCommand("tcmds").setExecutor(new Commands(this));
        if (getConfig().getBoolean("enabled")) {
			enable();
			logger.logMessage("Enabled.");
		} else {
			logger.logMessage("Disabled by config");
		}
    }
	
	private void enable() {
		pm.registerEvents(this.playerListener, this);
		permissions.setupPermissions();
		setupDatabase();
        EnableInteractions();
	}
	
	private void disable() {
		HandlerList hl = new HandlerList();
		hl.unregister(this);
	}
	
	public void reload() {
		onDisable();
		onEnable();
	}

    private void setupDatabase() {
        try {
            getDatabase().find(tReg.class).findRowCount();
        } catch (PersistenceException ex) {
            System.out.println("Installing database for " + getDescription().getName() + " due to first time usage");
            installDDL();
        }
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(tReg.class);
        return list;
    }

    public void EnableInteractions() {
        QueryIterator<tReg> findIterate = getDatabase().find(tReg.class).where().findIterate();
        while (findIterate.hasNext()) {
            tReg plyReg = findIterate.next();
            if (plyReg.getWorld() != null) {
                Cmds.put(new Location(getServer().getWorld(plyReg.getWorld()), plyReg.getX(), plyReg.getY(), plyReg.getZ()), plyReg.getCmd());
            }
        }
        findIterate.close();
    }

    public boolean casUseTriggerCmds(Player p) {
        if (UsePermissions) {
            return p.hasPermission("triggercmds");
        }
        return p.isOp();
    }
}