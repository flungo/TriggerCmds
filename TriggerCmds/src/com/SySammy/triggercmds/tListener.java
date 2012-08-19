/**
 *
 * Copyright by Samuel Castro and Fabrizio Lungo, 2011
 * All Rights Reserved
 *
 * NOTES:
 * Please do not redistribute this plugin without our permission
 */
package com.SySammy.triggercmds;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

class tListener implements Listener {

    tCmds plugin;
    private Methods Methods;

    public tListener(tCmds aThis) {
        this.plugin = aThis;
    }
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		Material block_type = block.getType();
		if (block_type.equals(Material.STONE_BUTTON) || block_type.equals(Material.LEVER) || block_type.equals(Material.STONE_PLATE) || block_type.equals(Material.WOOD_PLATE)) {
			Player p = event.getPlayer();
			Location loc = block.getLocation();
			//Cancel block break if editing (avoids instabreak in creative)
			if (plugin.iNames.containsKey(p)) {
				event.setCancelled(true);
			} else 
			//Delete link if link existed
			if (plugin.Cmds.containsKey(loc)) {
				String owner = Methods.getOwner(loc);
				if (p.getName().equalsIgnoreCase(owner) || p.isOp()) {
					String name = Methods.getName(loc);
					Methods.delLink(loc);
					String msg = "You deleted the trigger for the \"" + name + "\" tcmd";
					if (p.getName().equalsIgnoreCase(owner)) {
						msg = msg + " owned by " + owner + ".";
					} else {
						msg = msg + ".";
					}
					p.sendMessage(msg);
				} else {
					event.setCancelled(true);
					p.sendMessage("This button has a command attached to it. Only the owner or an OP can delete it.");
				}
			}
		}
			
	}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if (event.getClickedBlock().getType().equals(Material.STONE_BUTTON) || event.getClickedBlock().getType().equals(Material.LEVER) || event.getClickedBlock().getType().equals(Material.STONE_PLATE) || event.getClickedBlock().getType().equals(Material.WOOD_PLATE)) {
                Player p = event.getPlayer();
                Location loc = event.getClickedBlock().getLocation();
                Methods = new Methods(plugin);
                if (plugin.iNames.containsKey(p)) {
                    Methods.RegLocation(p, loc);
                    return;
                } else if (plugin.Cmds.containsKey(loc) && !event.getClickedBlock().getType().equals(Material.STONE_PLATE) && !event.getClickedBlock().getType().equals(Material.WOOD_PLATE)) {
                    Methods.ExecuteCmd(p, loc);
                }
            }
        }
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (event.getClickedBlock().getType().equals(Material.STONE_BUTTON) || event.getClickedBlock().getType().equals(Material.LEVER) || event.getClickedBlock().getType().equals(Material.STONE_PLATE) || event.getClickedBlock().getType().equals(Material.WOOD_PLATE)) {
                Player p = event.getPlayer();
                Location loc = event.getClickedBlock().getLocation();
                Methods = new Methods(plugin);
                if (plugin.iNames.containsKey(p)) {
                    Methods.delLink(p);
                    return;
                } else if (plugin.Cmds.containsKey(loc) && !event.getClickedBlock().getType().equals(Material.STONE_PLATE) && !event.getClickedBlock().getType().equals(Material.WOOD_PLATE)) {
                    Methods.ExecuteCmd(p, loc);
                }
            }
        }
        if (event.getAction().equals(Action.PHYSICAL)) {
            if (event.getClickedBlock().getType().equals(Material.STONE_PLATE) || event.getClickedBlock().getType().equals(Material.WOOD_PLATE)) {
                Player p = event.getPlayer();
                Location loc = event.getClickedBlock().getLocation();
                Methods = new Methods(plugin);
                if (!plugin.iNames.containsKey(p) && plugin.Cmds.containsKey(loc)) {
                    Methods.ExecuteCmd(p, loc);
                    return;
                }
            }
        }
    }
}
