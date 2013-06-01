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

class tListener
  implements Listener
{
  tCmds plugin;
  private Methods Methods;

  public tListener(tCmds aThis)
  {
    this.plugin = aThis;
  }
  @EventHandler(priority=EventPriority.HIGH)
  public void onBlockBreak(BlockBreakEvent event) {
    Block block = event.getBlock();
    Material block_type = block.getType();
    if ((block_type.equals(Material.STONE_BUTTON)) || (block_type.equals(Material.LEVER)) || (block_type.equals(Material.STONE_PLATE)) || (block_type.equals(Material.WOOD_PLATE))) {
      Player p = event.getPlayer();
      Location loc = block.getLocation();

      if (this.plugin.iNames.containsKey(p)) {
        event.setCancelled(true);
      }
      else if (this.plugin.Cmds.containsKey(loc)) {
        String owner = this.Methods.getOwner(loc);
        if ((p.getName().equalsIgnoreCase(owner)) || (p.isOp())) {
          String name = this.Methods.getName(loc);
          this.Methods.delLink(loc);
          p.sendMessage("You deleted the trigger for the \"" + name + "\" tcmd.");
        } else {
          event.setCancelled(true);
          p.sendMessage("This button has a command attached to it. Only the owner or an admin can delete it.");
        }
      }
    }
  }

  @EventHandler(priority=EventPriority.NORMAL)
  public void onPlayerInteract(PlayerInteractEvent event) {
    if ((event.getAction().equals(Action.LEFT_CLICK_BLOCK)) && (
      (event.getClickedBlock().getType().equals(Material.STONE_BUTTON)) || (event.getClickedBlock().getType().equals(Material.WOOD_BUTTON)) || (event.getClickedBlock().getType().equals(Material.LEVER)) || (event.getClickedBlock().getType().equals(Material.STONE_PLATE)) || (event.getClickedBlock().getType().equals(Material.WOOD_PLATE)))) {
      Player p = event.getPlayer();
      Location loc = event.getClickedBlock().getLocation();
      this.Methods = new Methods(this.plugin);
      if (this.plugin.iNames.containsKey(p)) {
        this.Methods.RegLocation(p, loc);
        return;
      }if ((this.plugin.Cmds.containsKey(loc)) && (!event.getClickedBlock().getType().equals(Material.STONE_PLATE)) && (!event.getClickedBlock().getType().equals(Material.WOOD_PLATE))) {
        this.Methods.ExecuteCmd(p, loc);
      }
    }

    if ((event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) && (
      (event.getClickedBlock().getType().equals(Material.STONE_BUTTON)) || (event.getClickedBlock().getType().equals(Material.WOOD_BUTTON)) || (event.getClickedBlock().getType().equals(Material.LEVER)) || (event.getClickedBlock().getType().equals(Material.STONE_PLATE)) || (event.getClickedBlock().getType().equals(Material.WOOD_PLATE)))) {
      Player p = event.getPlayer();
      Location loc = event.getClickedBlock().getLocation();
      this.Methods = new Methods(this.plugin);
      if (this.plugin.iNames.containsKey(p)) {
        this.Methods.delLink(p);
        return;
      }
	  if ((this.plugin.Cmds.containsKey(loc)) && (!event.getClickedBlock().getType().equals(Material.STONE_PLATE)) && (!event.getClickedBlock().getType().equals(Material.WOOD_PLATE))) {
        this.Methods.ExecuteCmd(p, loc);
      }
    }

    if ((event.getAction().equals(Action.PHYSICAL)) && (
      (event.getClickedBlock().getType().equals(Material.STONE_PLATE)) || (event.getClickedBlock().getType().equals(Material.WOOD_PLATE)))) {
      Player p = event.getPlayer();
      Location loc = event.getClickedBlock().getLocation();
      this.Methods = new Methods(this.plugin);
      if ((!this.plugin.iNames.containsKey(p)) && (this.plugin.Cmds.containsKey(loc))) {
        this.Methods.ExecuteCmd(p, loc);
        return;
      }
    }
  }
}