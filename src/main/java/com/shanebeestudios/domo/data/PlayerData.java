package com.shanebeestudios.domo.data;

import com.shanebeestudios.domo.util.PlayerUtils;
import com.shanebeestudios.domo.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unused")
public class PlayerData implements ConfigurationSerializable {

    private final UUID uuid;
    private final Player player;
    private final Board board;
    private double energy;
    private double fatigue;
    private final String boardPrefix = Util.getColString("  &b&l&nDomo&3&l&nBloot&r  ");

    public PlayerData(Player player) {
        this.uuid = player.getUniqueId();
        this.player = player;
        this.board = new Board(player);
        this.board.setTitle(boardPrefix);
        this.energy = 20.0;
        this.fatigue = 0.0;
    }

    private PlayerData(UUID uuid, double energy, double fatigue) {
        this.uuid = uuid;
        Player player = Bukkit.getPlayer(uuid);
        assert player != null;
        this.player = player;
        this.board = new Board(player);
        this.board.setTitle(boardPrefix);
        this.energy = energy;
        this.fatigue = fatigue;
    }

    public UUID getUuid() {
        return uuid;
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    public boolean isDay() {
        return player.getWorld().getTime() < 12542;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = Math.max(0, Math.min(20.0, energy));
        updateBoard();
    }

    public void increaseEnergy(double amount) {
        setEnergy(energy + amount);
    }

    public double getFatigue() {
        return fatigue;
    }

    public void setFatigue(double fatigue) {
        this.fatigue = Math.max(0, Math.min(10.0, fatigue));
        updateBoard();
    }

    public void increaseFatigue(double amount) {
        setFatigue(fatigue + amount);
    }

    public Board getBoard() {
        return board;
    }

    public void updateBoard() {
        board.setLine(7, " ");
        board.setLine(6, Util.getColString("<#35DF92>&lEnergy:"));
        board.setLine(5, PlayerUtils.getEnergyString(energy));
        board.setLine(4, " ");
        board.setLine(3, Util.getColString("<#B346EA>&lFatigue:"));
        board.setLine(2, PlayerUtils.getFatigueString(fatigue));
        board.setLine(1, " ");
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("uuid", uuid.toString());
        map.put("energy", energy);
        map.put("fatigue", fatigue);

        return map;
    }

    public static PlayerData deserialize(Map<String, Object> args) {
        UUID uuid = UUID.fromString((String) args.get("uuid"));
        double energy = ((Number) args.get("energy")).doubleValue();
        double fatigue = ((Number) args.get("fatigue")).doubleValue();
        return new PlayerData(uuid, energy, fatigue);
    }

}
