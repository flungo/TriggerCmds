package com.SySammy.triggercmds;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TriggerCmds")
public class tReg
{

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

  public int getInteractionId()
  {
    return this.InteractionId;
  }

  public void setInteractionId(int InteractionId) {
    this.InteractionId = InteractionId;
  }

  public String getPlayerName() {
    return this.PlayerName;
  }

  public void setPlayerName(String PlayerName) {
    this.PlayerName = PlayerName;
  }

  public String getCmd() {
    return this.cmd;
  }

  public void setCmd(String cmd) {
    this.cmd = cmd;
  }

  public String getWorld() {
    return this.world;
  }

  public void setWorld(String world) {
    this.world = world;
  }

  public double getX() {
    return this.x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return this.y;
  }

  public void setY(double y) {
    this.y = y;
  }

  public double getZ() {
    return this.z;
  }

  public void setZ(double z) {
    this.z = z;
  }

  public String getIntName() {
    return this.intName;
  }

  public void setIntName(String intName) {
    this.intName = intName;
  }
}