package com.SySammy.triggercmds;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Query;
import com.avaje.ebean.QueryIterator;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class tCmds extends JavaPlugin
{
  private static final Logger log = Logger.getLogger("Minecraft");
  public HashMap<Player, String> iNames = new HashMap();
  public HashMap<Location, String> Cmds = new HashMap();
  private tListener playerListener = new tListener(this);
  public PermissionHandler Permissions;
  private boolean UsePermissions;

  public static void main(String[] args)
  {
  }

  public void onDisable()
  {
    PluginDescriptionFile pdfFile = getDescription();
    log.log(Level.OFF, pdfFile.getName() + " version " + pdfFile.getVersion() + " is disabled!");
  }

  public void onEnable()
  {
    PluginDescriptionFile pdfFile = getDescription();
    PluginManager pm = getServer().getPluginManager();
    getCommand("tcmds").setExecutor(new Commands(this));
    pm.registerEvent(Event.Type.PLAYER_INTERACT, this.playerListener, Event.Priority.Normal, this);

    setupDatabase();
    EnableInteractions();
    setupPermissions();
    log.log(Level.INFO, pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
  }

  private void setupDatabase() {
    try {
      getDatabase().find(tReg.class).findRowCount();
    } catch (PersistenceException ex) {
      System.out.println("Installing database for " + getDescription().getName() + " due to first time usage");
      installDDL();
    }
  }

  public List<Class<?>> getDatabaseClasses()
  {
    List list = new ArrayList();
    list.add(tReg.class);
    return list;
  }

  public void EnableInteractions() {
    QueryIterator findIterate = getDatabase().find(tReg.class).where().findIterate();
    while (findIterate.hasNext()) {
      tReg plyReg = (tReg)findIterate.next();
      if (plyReg.getWorld() != null) {
        this.Cmds.put(new Location(getServer().getWorld(plyReg.getWorld()), plyReg.getX(), plyReg.getY(), plyReg.getZ()), plyReg.getCmd());
      }
    }
    findIterate.close();
  }

  private void setupPermissions() {
    Plugin test = getServer().getPluginManager().getPlugin("Permissions");

    if (this.Permissions == null)
      if (test != null) {
        this.UsePermissions = true;
        this.Permissions = ((Permissions)test).getHandler();
        System.out.println("Permissions is enable for BrokenFingers.");
      } else {
        log.info("Permission system not detected, defaulting to OP");
        this.UsePermissions = false;
      }
  }

  public boolean casUseTriggerCmds(Player p)
  {
    if (this.UsePermissions) {
      return this.Permissions.has(p, "triggercmds");
    }
    return p.isOp();
  }
}