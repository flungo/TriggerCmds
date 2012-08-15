/**
 *
 * Copyright by Samuel Castro, 2011
 * All Rights Reserved
 *
 * NOTES:
 * Please do not redistribute this plugin without my permission
 */
package com.SySammy.triggercmds;

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
        String[] args = strings;
        String cmd = cmnd.getName().toLowerCase();
        if (cmd.equals("tcmds")) {
            return BrokenMethod(cs, args);
        }
        return true;
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
            } else if (arg[0].equalsIgnoreCase("list")) {
                Methods = new Methods(plugin);
                Methods.iList(p);
                return true;
            } else if (arg[0].equalsIgnoreCase("edit")) {
                Methods = new Methods(plugin);
                Methods.RegEditInteraction(p, arg[1].toLowerCase());
                return true;
            } else if (arg[0].equalsIgnoreCase("cmd") || arg[0].equalsIgnoreCase("set")) {
                Methods = new Methods(plugin);
				String GetCmds = Methods.GetCmds(p.getName(), arg);
				Methods.RegCmd(p, GetCmds);
				return true;
			} else if (arg[0].equalsIgnoreCase("add")) {
                Methods = new Methods(plugin);
				String AddCmds = Methods.GetCmds(p.getName(), arg);
				Methods.AddCmd(p, AddCmds);
				return true;
            } else if (arg[0].equalsIgnoreCase("reload")) {
                plugin.reload();
                return true;
            } else if (arg[0].equalsIgnoreCase("del")) {
                Methods = new Methods(plugin);
                Methods.delTrigger(p);
                return true;
            } else if (arg[0].equalsIgnoreCase("help")) {
                p.sendMessage(ChatColor.GOLD + "[ " + ChatColor.GREEN + "TriggerCmds Help Page" + ChatColor.GOLD + " ]");
                p.sendMessage(ChatColor.GOLD +"LeftClick" + ChatColor.WHITE + " - Reg Trigger Link ");
                p.sendMessage(ChatColor.GOLD +"RightClick" + ChatColor.WHITE + " - Del Trigger Link");
                return false;
            }
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
