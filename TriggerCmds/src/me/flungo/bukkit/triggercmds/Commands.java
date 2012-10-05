/**
 *
 * Copyright by Samuel Castro and Fabrizio Lungo, 2011
 * All Rights Reserved
 *
 * NOTES:
 * Please do not redistribute this plugin without our permission
 */
package me.flungo.bukkit.triggercmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    tCmds plugin;
    private Methods Methods;

    public Commands(tCmds aThis) {
        this.plugin = aThis;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        if (plugin.getConfig().getBoolean("enabled")) {
			String[] args = strings;
			String cmd = cmnd.getName().toLowerCase();
			if (cmd.equals("tcmds")) {
				return BrokenMethod(cs, args);
			}
			return true;
		} else {
			String msg = "Trigger Commands is diabled";
			if (cs instanceof Player) {
				((Player)cs).sendMessage(msg);
			} else {
				plugin.logger.logMessage(msg);
			}
			return true;
		}
    }

    private boolean BrokenMethod(CommandSender cs, String[] arg) {
        Player p = (Player) cs;
        try {
            if (!plugin.casUseTriggerCmds(p)) {
                p.sendMessage(ChatColor.RED + "You don't have permisson to use that command");
                return true;
            }
            if (arg.length <= 0) {
                p.sendMessage(ChatColor.GOLD + "[ " + ChatColor.GREEN + "TriggerCmds Help Page" + ChatColor.GOLD + " ]");
                p.sendMessage(ChatColor.GOLD +"LeftClick" + ChatColor.WHITE + " - Reg Trigger Link ");
                p.sendMessage(ChatColor.GOLD +"RightClick" + ChatColor.WHITE + " - Del Trigger Link");
                return false;
            }
            Methods = new Methods(plugin);
            if (arg[0].equalsIgnoreCase("list")) {
                Methods.iList(p);
                return true;
            } else if (arg[0].equalsIgnoreCase("edit")) {
                Methods.RegEditInteraction(p, arg[1].toLowerCase());
                return true;
            } else if (arg[0].equalsIgnoreCase("set")) {
                if (arg[1].equalsIgnoreCase("cmd")) {
                    String GetCmds = Methods.GetCmds(p.getName(), arg);
                    Methods.RegCmd(p, GetCmds);
                    return true;
                } else if (arg[1].equalsIgnoreCase("uses")) {
                    Methods.RegUses(p, arg[2]);
                    return true;
                } else if (arg[1].equalsIgnoreCase("unlink")) {
                    Methods.RegUnlink(p, arg[2]);
                    return true;
                } else if (arg[1].equalsIgnoreCase("puses")) { //Player uses
                    Methods.RegPUses(p, arg[2]);
                    return true;
                } else if (arg[1].equalsIgnoreCase("cooldown")) {
                    Methods.RegCooldown(p, arg[2]);
                    return true;
                } else if (arg[1].equalsIgnoreCase("pcooldown")) {
                    Methods.RegPlayerCooldown(p, arg[2]);
                    return true;
                } else if (arg[1].equalsIgnoreCase("unlinkonbreak")) {
                    Methods.RegUnlinkOnBreak(p, arg[2]);
                    return true;
                } else if (arg[1].equalsIgnoreCase("breakonunlink")) {
                    Methods.RegBreakOnUnlink(p, arg[2]);
                    return true;
                }
            } else if (arg[0].equalsIgnoreCase("add")) {
                if (arg[1].equalsIgnoreCase("cmd")) {
                    String AddCmds = Methods.GetCmds(p.getName(), arg);
                    Methods.AddCmd(p, AddCmds);
                    return true;
                }
            } //Add reset command
            else if (arg[0].equalsIgnoreCase("reload")) {
                plugin.reload();
                return true;
            } else if (arg[0].equalsIgnoreCase("del")) {
                Methods.delTrigger(p);
                return true;
            }
            //"help" command will return the same as a bad command:
            p.sendMessage(ChatColor.GOLD + "[ " + ChatColor.GREEN + "TriggerCmds Help Page" + ChatColor.GOLD + " ]");
            p.sendMessage(ChatColor.GOLD +"LeftClick" + ChatColor.WHITE + " - Reg Trigger Link ");
            p.sendMessage(ChatColor.GOLD +"RightClick" + ChatColor.WHITE + " - Del Trigger Link");
            return false;
        } catch (Exception e) {
            p.sendMessage(ChatColor.RED + "Input error, please try again.");
            p.sendMessage(ChatColor.GOLD + "[ " + ChatColor.GREEN + "TriggerCmds Help Page" + ChatColor.GOLD + " ]");
            return false;
        }
    }
}
