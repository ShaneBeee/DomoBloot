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
    private double oxygen;
    private final String boardTitle = Util.getColString(" " + Util.PREFIX);

    public PlayerData(Player player) {
        this.uuid = player.getUniqueId();
        this.player = player;
        this.board = new Board(player);
        this.board.setTitle(boardTitle);
        reset();
    }

    private PlayerData(UUID uuid, double energy, double fatigue, double oxygen) {
        this.uuid = uuid;
        Player player = Bukkit.getPlayer(uuid);
        assert player != null;
        this.player = player;
        this.board = new Board(player);
        this.board.setTitle(boardTitle);
        this.energy = energy;
        this.fatigue = fatigue;
        this.oxygen = oxygen;
    }

    public void reset() {
        this.energy = 20.0;
        this.fatigue = 0.0;
        this.oxygen = 20.0;
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

    public void setOxygen(double oxygen) {
        this.oxygen = Math.max(0, Math.min(20.0, oxygen));
        updateBoard();
    }

    public void increaseOxygen(double amount) {
        setOxygen(oxygen + amount);
    }

    public double getOxygen() {
        return oxygen;
    }

    public void increaseFatigue(double amount) {
        setFatigue(fatigue + amount);
    }

    public Board getBoard() {
        return board;
    }

    public void updateBoard() {
        board.setLine(10, " ");
        board.setLine(9, "<#35DF92>&lEnergy:");
        board.setLine(8, PlayerUtils.getEnergyString(energy));
        board.setLine(7, " ");
        board.setLine(6, "<#B346EA>&lFatigue:");
        board.setLine(5, PlayerUtils.getFatigueString(fatigue));
        board.setLine(4, " ");
        board.setLine(3, "<#35D7DF>&lOxygen:");
        board.setLine(2, PlayerUtils.getOxygenString(oxygen)); // TODO getAirString
        board.setLine(1, " ");
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("uuid", uuid.toString());
        map.put("energy", energy);
        map.put("fatigue", fatigue);
        map.put("oxygen", oxygen);

        return map;
    }

    public static PlayerData deserialize(Map<String, Object> args) {
        UUID uuid = UUID.fromString((String) args.get("uuid"));
        double energy = ((Number) args.get("energy")).doubleValue();
        double fatigue = ((Number) args.get("fatigue")).doubleValue();
        double oxygen = ((Number) args.get("oxygen")).doubleValue();
        return new PlayerData(uuid, energy, fatigue, oxygen);
    }

}
