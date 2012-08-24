/**
 *
 * Copyright by Samuel Castro and Fabrizio Lungo, 2011
 * All Rights Reserved
 *
 * NOTES:
 * Please do not redistribute this plugin without our permission
 */
package me.flungo.bukkit.triggercmds;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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
}
