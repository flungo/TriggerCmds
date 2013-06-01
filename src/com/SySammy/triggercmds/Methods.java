package com.SySammy.triggercmds;

import com.avaje.ebean.QueryIterator;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Methods {
	
	private static final Logger logger = Logger.getLogger(Methods.class.getName());
	
	tCmds plugin;

	public Methods(tCmds aThis) {
		this.plugin = aThis;
	}

	private tReg OpenDataBase(String p, String iName) {
		tReg persistenceClass = (tReg) this.plugin.getDatabase().find(tReg.class).where().ieq("PlayerName", p).ieq("intName", iName).findUnique();
		if (persistenceClass == null) {
			persistenceClass = new tReg();
		}
		return persistenceClass;
	}

	private tReg OpenDataBase(Location loc) {
		String world = loc.getWorld().getName();
		String x = Double.toString(loc.getX());
		String y = Double.toString(loc.getY());
		String z = Double.toString(loc.getZ());
		tReg persistenceClass = (tReg) this.plugin.getDatabase().find(tReg.class).where().ieq("world", world).ieq("x", x).ieq("y", y).ieq("z", z).findUnique();
		if (persistenceClass == null) {
			persistenceClass = new tReg();
		}
		return persistenceClass;
	}

	private boolean iNameExist(String p, String iName) {
		tReg persistenceClass = (tReg) this.plugin.getDatabase().find(tReg.class).where().ieq("PlayerName", p).ieq("intName", iName).findUnique();

		return persistenceClass != null;
	}

	private boolean IsRegOn(Player p) {
		return this.plugin.iNames.containsKey(p);
	}

	public void RegEditInteraction(Player p, String iName) {
		if (IsRegOn(p)) {
			p.sendMessage(ChatColor.GOLD + "[ " + ChatColor.RED + "You're already editing a interaction" + ChatColor.GOLD + " ]");
			return;
		}
		if (iNameExist(p.getName(), iName)) {
			this.plugin.iNames.put(p, iName);
			p.sendMessage(ChatColor.GOLD + "[ " + ChatColor.GREEN + "Interaction Opened" + ChatColor.GOLD + " ]");
			p.sendMessage(ChatColor.GREEN + "You are now editing: " + ChatColor.BLUE + iName);
			return;
		}
		tReg plyReg = OpenDataBase(p.getName(), iName);
		plyReg.setPlayerName(p.getName());
		plyReg.setIntName(iName);
		this.plugin.getDatabase().save(plyReg);
		this.plugin.iNames.put(p, iName);
		p.sendMessage(ChatColor.GOLD + "[ " + ChatColor.GREEN + "Interaction Registered" + ChatColor.GOLD + " ]");
		p.sendMessage(ChatColor.GREEN + "You are now editing: " + ChatColor.BLUE + iName);
	}

	public void RegCmd(Player p, String cmd) {
		if (!IsRegOn(p)) {
			p.sendMessage(ChatColor.GOLD + "[ " + ChatColor.RED + "Please start the edit function first" + ChatColor.GOLD + " ]");
			return;
		}
		tReg plyReg = OpenDataBase(p.getName(), (String) this.plugin.iNames.get(p));
		plyReg.setCmd(cmd);
		this.plugin.getDatabase().save(plyReg);

		if (getLoc(p) != null) {
			Location loc = getLoc(p);
			this.plugin.Cmds.put(loc, plyReg.getCmd());
		}
		p.sendMessage(ChatColor.GOLD + "[ " + ChatColor.GREEN + "Details saved to the database: " + ChatColor.GOLD + " ]");
		p.sendMessage(ChatColor.GREEN + "Command: " + ChatColor.BLUE + cmd);
	}

	public void AddCmd(Player p, String cmd) {
		if (!IsRegOn(p)) {
			p.sendMessage(ChatColor.GOLD + "[ " + ChatColor.RED + "Please start the edit function first" + ChatColor.GOLD + " ]");
			return;
		}
		tReg plyReg = OpenDataBase(p.getName(), (String) this.plugin.iNames.get(p));

		String old_cmd = plyReg.getCmd();
		String new_cmd = cmd;
		if (old_cmd != null) {
			new_cmd = old_cmd + " & " + cmd;
		}

		RegCmd(p, new_cmd);
	}

	public void RegLocation(Player p, Location loc) {
		if (!IsRegOn(p)) {
			p.sendMessage(ChatColor.GOLD + "[ " + ChatColor.RED + "Please start the edit function first" + ChatColor.GOLD + " ]");
			return;
		}
		this.plugin.Cmds.remove(getLoc(p));
		tReg plyReg = OpenDataBase(p.getName(), (String) this.plugin.iNames.get(p));
		if (iNameExist(p.getName(), (String) this.plugin.iNames.get(p))) {
			plyReg.setWorld(loc.getWorld().getName());
			plyReg.setX(loc.getX());
			plyReg.setY(loc.getY());
			plyReg.setZ(loc.getZ());
			this.plugin.getDatabase().save(plyReg);
			this.plugin.Cmds.put(loc, plyReg.getCmd());
			p.sendMessage(ChatColor.GOLD + "[ " + ChatColor.GREEN + "Details saved to the database" + ChatColor.GOLD + " ]");
			p.sendMessage(ChatColor.GREEN + "Location: " + ChatColor.BLUE + loc.getWorld().getName() + "[" + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "]");
		}
		DelRegState(p);
	}

	public boolean HasInteration(String p, Location loc) {
		return this.plugin.Cmds.containsKey(loc);
	}

	public String GetCmds(String p, String[] arg) {
		String cmd = "";
		for (int x = 1; x < arg.length; x++) {
			cmd = cmd + " " + arg[x];
		}
		String replace = "";
		if (cmd.contains("$me:")) {
			replace = cmd.replace("$me:", p + ":");
		}
		if (cmd.contains("$me")) {
			replace = cmd.replace("$me", p);
		} else {
			replace = cmd;
		}
		return replace.trim();
	}

	public void DelRegState(Player p) {
		if (!IsRegOn(p)) {
			p.sendMessage(ChatColor.GOLD + "[ " + ChatColor.RED + "No edit function started" + ChatColor.GOLD + " ]");
			return;
		}
		this.plugin.iNames.remove(p);
		p.sendMessage(ChatColor.GOLD + "[ " + ChatColor.GREEN + "Iteraction edit closed" + ChatColor.GOLD + " ]");
	}

	public void ExecuteCmd(Player p, Location loc) {
		String[] InitCmds = ((String) this.plugin.Cmds.get(loc)).split(" & ");
		for (int x = 0; InitCmds.length > x; x++) {
			String get = InitCmds[x];
			String filtedCmd = "";
			Player sender = null;
			String[] split = get.split("/", 2);
			boolean console = false;
			if (split.length == 1) {
				sender = p;
				filtedCmd = split[0];
			} else if (split[0].equalsIgnoreCase("$ply:")) {
				sender = p;
				filtedCmd = split[1];
			} else if (split[0].equalsIgnoreCase("$con:")) {
				console = true;
				filtedCmd = split[1];
			} else if (split[0].equalsIgnoreCase("$bot:")) {
				sender = p;

				filtedCmd = split[1];
			} else {
				sender = this.plugin.getServer().getPlayer(split[0].replace(":", ""));
				filtedCmd = split[1];
			}
			if (get.contains("$ply")) {
				filtedCmd = filtedCmd.replace("$ply", p.getName());
			}
			if ((sender == null) && (!console)) {
				p.sendMessage(ChatColor.RED + "The owner of that trigger isn't online at the moment.");
				return;
			}
			if (console) {
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), filtedCmd);
			} else {
				sender.chat("/" + filtedCmd);
			}
		}
	}

	public Location getLoc(Player p) {
		tReg plyReg = OpenDataBase(p.getName(), (String) this.plugin.iNames.get(p));
		if (plyReg.getWorld() == null) {
			return null;
		}
		Location loc = new Location(this.plugin.getServer().getWorld(plyReg.getWorld()), plyReg.getX(), plyReg.getY(), plyReg.getZ());
		return loc;
	}

	public void iList(Player p) {
		p.sendMessage(ChatColor.GOLD + "[ " + ChatColor.GREEN + "List of Triggers and Links" + ChatColor.GOLD + " ]");
		QueryIterator findIterate = this.plugin.getDatabase().find(tReg.class).where().ieq("PlayerName", p.getName()).findIterate();
		while (findIterate.hasNext()) {
			tReg plyReg = (tReg) findIterate.next();
			p.sendMessage(ChatColor.GREEN + "Trigger Name: " + ChatColor.BLUE + plyReg.getIntName());
			p.sendMessage(ChatColor.GREEN + "Command: " + ChatColor.BLUE + plyReg.getCmd());
			if (plyReg.getWorld() == null) {
				p.sendMessage(ChatColor.GREEN + "Location: " + ChatColor.BLUE + "Not yet placed.");
			} else {
				p.sendMessage(ChatColor.GREEN + "Location: " + ChatColor.BLUE + plyReg.getWorld() + "[" + plyReg.getX() + "," + plyReg.getY() + "," + plyReg.getZ() + "]");
			}
			p.sendMessage("");
		}
		findIterate.close();
	}

	public void delTrigger(Player p) {
		if (!IsRegOn(p)) {
			p.sendMessage(ChatColor.GOLD + "[ " + ChatColor.RED + "Please start the edit function first" + ChatColor.GOLD + " ]");
			return;
		}
		tReg plyReg = OpenDataBase(p.getName(), (String) this.plugin.iNames.get(p));
		this.plugin.Cmds.remove(getLoc(p));
		this.plugin.iNames.remove(p);
		this.plugin.getDatabase().delete(tReg.class, Integer.valueOf(plyReg.getInteractionId()));
		p.sendMessage(ChatColor.GOLD + "[ " + ChatColor.GREEN + "Trigger deleted from the database" + ChatColor.GOLD + " ]");
	}

	public void delLink(Player p) {
		if (!IsRegOn(p)) {
			p.sendMessage(ChatColor.GOLD + "[ " + ChatColor.RED + "Please start the edit function first" + ChatColor.GOLD + " ]");
			return;
		}
		tReg plyReg = OpenDataBase(p.getName(), (String) this.plugin.iNames.get(p));
		delLink(getLoc(p), plyReg);
		p.sendMessage(ChatColor.GOLD + "[ " + ChatColor.GREEN + "Link deleted, your trigger no longer has a location" + ChatColor.GOLD + " ]");
		DelRegState(p);
	}

	public void delLink(Location loc) {
		tReg locReg = OpenDataBase(loc);
		delLink(loc, locReg);
	}

	public void delLink(Location loc, tReg plyReg) {
		this.plugin.Cmds.remove(loc);
		plyReg.setWorld(" ");
		this.plugin.getDatabase().save(plyReg);
	}

	public String getOwner(Location loc) {
		tReg locReg = OpenDataBase(loc);
		String owner = locReg.getPlayerName();
		return owner;
	}

	public String getName(Location loc) {
		tReg locReg = OpenDataBase(loc);
		String name = locReg.getIntName();
		return name;
	}
}