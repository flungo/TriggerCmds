/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SySammy.triggercmds;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Fabrizio
 */
public class tPermissions extends Permissions {
	tPermissions(JavaPlugin instance, Log logger) {
		super(instance, logger);
	}
	
	public boolean canCreate(Player p) {
		return hasPermission(p, "create");
	}
	
	public boolean canUse(Player p) {
		return hasPermission(p, "use");
	}
	
	public boolean canListOwn(Player p) {
		return hasPermission(p, "list.own");
	}
	
	public boolean canListAll(Player p) {
		return hasPermission(p, "list.all");
	}
	
	public boolean canEditOwn(Player p) {
		return hasPermission(p, "edit.own");
	}
	
	public boolean canEditAll(Player p) {
		return hasPermission(p, "edit.all");
	}
	
	public boolean canSetOwn(Player p) {
		return hasPermission(p, "set.own");
	}
	
	public boolean canSetAll(Player p) {
		return hasPermission(p, "set.all");
	}
	
	public boolean canAddOwn(Player p) {
		return hasPermission(p, "add.own");
	}
	
	public boolean canAddAll(Player p) {
		return hasPermission(p, "add.all");
	}
	
	public boolean canDelOwn(Player p) {
		return hasPermission(p, "del.own");
	}
	
	public boolean canDelAll(Player p) {
		return hasPermission(p, "del.all");
	}
	
	public boolean canReload(Player p) {
		return hasPermission(p, "reload");
	}
}
