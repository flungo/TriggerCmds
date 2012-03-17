package com.SySammy.triggercmds;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Query;
import com.avaje.ebean.QueryIterator;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Methods
{
  tCmds plugin;

  public Methods(tCmds aThis)
  {
    this.plugin = aThis;
  }

  private tReg OpenDataBase(String p, String iName) {
    tReg persistenceClass = (tReg)this.plugin.getDatabase().find(tReg.class).where().ieq("PlayerName", p).ieq("intName", iName).findUnique();
    if (persistenceClass == null) {
      persistenceClass = new tReg();
    }
    return persistenceClass;
  }

  private boolean iNameExist(String p, String iName) {
    tReg persistenceClass = (tReg)this.plugin.getDatabase().find(tReg.class).where().ieq("PlayerName", p).ieq("intName", iName).findUnique();

    return persistenceClass != null;
  }

  private boolean IsRegOn(Player p)
  {
    return this.plugin.iNames.containsKey(p);
  }

  public void RegEditInteraction(Player p, String iName) {
    if (iNameExist(p.getName(), iName)) {
      this.plugin.iNames.put(p, iName);
      p.sendMessage(ChatColor.GREEN + "You are now editing your interaction: " + ChatColor.GOLD + iName);
      return;
    }
    if (this.plugin.iNames.containsKey(p)) {
      p.sendMessage(ChatColor.RED + "You're already editing a interaction.");
      return;
    }
    tReg plyReg = OpenDataBase(p.getName(), iName);
    plyReg.setPlayerName(p.getName());
    plyReg.setIntName(iName);
    this.plugin.getDatabase().save(plyReg);
    this.plugin.iNames.put(p, iName);
    p.sendMessage(ChatColor.GREEN + "Interaction Registered.");
    p.sendMessage(ChatColor.GREEN + "You are now editing: " + ChatColor.GOLD + iName);
  }

  public void RegCmd(Player p, String cmd) {
    if (!IsRegOn(p)) {
      p.sendMessage(ChatColor.RED + "Please start the edit function first.");
      return;
    }
    tReg plyReg = OpenDataBase(p.getName(), (String)this.plugin.iNames.get(p));
    plyReg.setCmd(cmd);
    this.plugin.getDatabase().save(plyReg);

    if (getLoc(p) != null) {
      Location loc = getLoc(p);
      this.plugin.Cmds.put(loc, plyReg.getCmd());
    }
    p.sendMessage(ChatColor.GREEN + "Command saved to the database: " + ChatColor.GOLD + cmd);
  }

  public void RegLocation(Player p, Location loc) {
    if (!IsRegOn(p)) {
      p.sendMessage(ChatColor.RED + "Please start the edit function first.");
      return;
    }
    this.plugin.Cmds.remove(getLoc(p));
    tReg plyReg = OpenDataBase(p.getName(), (String)this.plugin.iNames.get(p));
    if (iNameExist(p.getName(), (String)this.plugin.iNames.get(p))) {
      plyReg.setWorld(loc.getWorld().getName());
      plyReg.setX(loc.getX());
      plyReg.setY(loc.getY());
      plyReg.setZ(loc.getZ());
      this.plugin.getDatabase().save(plyReg);
      this.plugin.Cmds.put(loc, plyReg.getCmd());
      p.sendMessage(ChatColor.GREEN + "Location saved to the database");
    }
  }

  public boolean HasInteration(String p, Location loc) {
    return this.plugin.Cmds.containsKey(loc);
  }

  public String GetCmds(String[] arg) {
    String cmd = "";
    for (int x = 1; x < arg.length; x++) {
      cmd = cmd + " " + arg[x];
    }
    return cmd.trim();
  }

  public void DelRegState(Player p) {
    if (!IsRegOn(p)) {
      p.sendMessage(ChatColor.RED + "No edit function started.");
      return;
    }
    this.plugin.iNames.remove(p);
    p.sendMessage(ChatColor.GREEN + "Iteraction edit closed.");
  }

  public void ExecuteCmd(Player p, Location loc) {
    String get = (String)this.plugin.Cmds.get(loc);
    p.chat(get);
  }

  public Location getLoc(Player p) {
    try {
      tReg plyReg = OpenDataBase(p.getName(), (String)this.plugin.iNames.get(p));
      Location loc = new Location(this.plugin.getServer().getWorld(plyReg.getWorld()), plyReg.getX(), plyReg.getY(), plyReg.getZ());
      return loc; } catch (NullPointerException e) {
    }
    return null;
  }

  public void iList(Player p)
  {
    QueryIterator findIterate = this.plugin.getDatabase().find(tReg.class).where().ieq("PlayerName", p.getName()).findIterate();
    while (findIterate.hasNext()) {
      tReg plyReg = (tReg)findIterate.next();
      p.sendMessage(ChatColor.GREEN + "Trigger Name: " + ChatColor.GOLD + plyReg.getIntName());
      p.sendMessage(ChatColor.GREEN + "Command: " + ChatColor.GOLD + plyReg.getCmd());
      if (plyReg.getWorld() == null)
        p.sendMessage(ChatColor.GREEN + "Location: " + ChatColor.RED + "Not yet placed.");
      else {
        p.sendMessage(ChatColor.GREEN + "Location: " + ChatColor.GOLD + plyReg.getWorld() + ", [" + plyReg.getX() + "," + plyReg.getY() + "," + plyReg.getZ() + "]");
      }
      p.sendMessage("");
    }
    findIterate.close();
  }

  public void delTrigger(Player p) {
    if (!IsRegOn(p)) {
      p.sendMessage(ChatColor.RED + "Please start the edit function first.");
      return;
    }
    tReg plyReg = OpenDataBase(p.getName(), (String)this.plugin.iNames.get(p));
    this.plugin.Cmds.remove(getLoc(p));
    this.plugin.iNames.remove(p);
    this.plugin.getDatabase().delete(tReg.class, Integer.valueOf(plyReg.getInteractionId()));
    p.sendMessage(ChatColor.GREEN + "Trigger deleted.");
  }
}