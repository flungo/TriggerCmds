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
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class tCmds extends JavaPlugin
{
  private static final Logger log = Logger.getLogger("Minecraft");
  public HashMap<Player, String> iNames = new HashMap();
  public HashMap<Location, String> Cmds = new HashMap();
  private tListener playerListener = new tListener(this);
  private boolean UsePermissions = true;

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
    pm.registerEvents(this.playerListener, this);

    setupDatabase();
    EnableInteractions();
    log.log(Level.INFO, pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
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

  public boolean casUseTriggerCmds(Player p) {
    if (this.UsePermissions) {
      return p.hasPermission("triggercmds");
    }
    return p.isOp();
  }
}