/**
 *
 * Copyright by Samuel Castro and Fabrizio Lungo, 2011
 * All Rights Reserved
 *
 * NOTES:
 * Please do not redistribute this plugin without our permission
 */
package me.flungo.bukkit.triggercmds;

import java.util.HashMap;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.bukkit.entity.Player;

@Entity
@Table(name = "TriggerCmds")
public class tReg {

    @Id
    @GeneratedValue
    private int InteractionId;
    private String PlayerName;
    private String intName;
    private String cmd;
    private String world;
    private double x;
    private double y;
    private double z;
	private int MaxUses;
	private int MaxPlayerUses;
	private HashMap<Player, Integer> PlayerUses = new HashMap<Player, Integer>();
	private int Cooldown;
	private int LastUse;
	private HashMap<Player, Integer> LastPlayerUse = new HashMap<Player, Integer>();

    public int getInteractionId() {
        return InteractionId;
    }

    public void setInteractionId(int InteractionId) {
        this.InteractionId = InteractionId;
    }

    public String getPlayerName() {
        return PlayerName;
    }

    public void setPlayerName(String PlayerName) {
        this.PlayerName = PlayerName;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public String getIntName() {
        return intName;
    }

    public void setIntName(String intName) {
        this.intName = intName;
    }
	
	public int getMaxUses() {
		return MaxUses;
	}

    public void setMaxUses(int MaxUses) {
        this.MaxUses = MaxUses;
    }
	
	public int getMaxPlayerUses() {
		return MaxPlayerUses;
	}

    public void setMaxPlayerUses(int MaxPlayerUses) {
        this.MaxPlayerUses = MaxPlayerUses;
    }
	
	public int getPlayerUses(Player p) {
		return PlayerUses.get(p);
	}
	
	public void setPlayerUses(Player p, int u) {
		this.PlayerUses.put(p, u);
	}
	
	public int getLastUse() {
		return MaxUses;
	}

    public void setCooldown(int Cooldown) {
        this.Cooldown = Cooldown;
    }
	
	public int getCooldown(Player p) {
		return LastPlayerUse.get(p);
	}

    public void setLastUse(int LastUse) {
        this.LastUse = LastUse;
    }
	
	public int getLastPlayerUse(Player p) {
		return LastPlayerUse.get(p);
	}
	
	public void setLastPlayerUse(Player p, int u) {
		this.LastPlayerUse.put(p, u);
	}
}