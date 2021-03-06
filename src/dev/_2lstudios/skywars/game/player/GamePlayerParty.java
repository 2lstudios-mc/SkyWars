package dev._2lstudios.skywars.game.player;

import java.util.Collection;
import java.util.HashSet;

import dev._2lstudios.skywars.game.arena.Arena;

public class GamePlayerParty {
  private final GamePlayer owner;
  private final Collection<GamePlayer> invited = new HashSet<>();
  private final Collection<GamePlayer> members = new HashSet<>();

  GamePlayerParty(GamePlayer owner) {
    this.owner = owner;
  }

  public GamePlayer getOwner() {
    return this.owner;
  }

  public void disband() {
    for (GamePlayer gamePlayer : this.members) {
      gamePlayer.setParty(null);
    }

    this.owner.setParty(null);
    this.members.clear();
    this.invited.clear();
  }

  public boolean invite(GamePlayer gamePlayer) {
    if (!this.members.contains(gamePlayer)) {
      return this.invited.add(gamePlayer);
    }

    return false;
  }

  public boolean deinvite(GamePlayer gamePlayer) {
    if (this.members.contains(gamePlayer)) {
      return this.invited.remove(gamePlayer);
    }

    return false;
  }

  public boolean add(GamePlayer gamePlayer) {
    return this.members.add(gamePlayer);
  }

  public boolean remove(GamePlayer gamePlayer) {
    return this.members.remove(gamePlayer);
  }

  public void sendMessage(String message) {
    for (GamePlayer gamePlayer : this.members) {
      gamePlayer.sendMessage(message);
    }

    owner.sendMessage(message);
  }

  public Collection<GamePlayer> getMembers() {
    return this.members;
  }

  public Collection<GamePlayer> getInvited() {
    return this.invited;
  }

  public void updateArena(Arena newArena, GamePlayerMode newMode) {
    for (final GamePlayer gamePlayer : members) {
      if (gamePlayer.getArena() == owner.getArena()) {
        gamePlayer.updateArena(newArena, newMode);
      }
    }
  }
}
