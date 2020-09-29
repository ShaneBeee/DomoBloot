package com.shanebeestudios.tl.data;

import com.shanebeestudios.tl.util.PlayerUtils;
import com.shanebeestudios.tl.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unused")
public class PlayerData implements ConfigurationSerializable {

    private final UUID uuid;
    private final Board board;
    private double energy;
    private final String boardPrefix = Util.getColString("  &b&l&nTen&3&l&nLives&r  ");

    public PlayerData(Player player) {
        this.uuid = player.getUniqueId();
        this.board = new Board(player);
        this.board.setTitle(boardPrefix);
        this.energy = 20.0;
    }

    private PlayerData(UUID uuid, double energy) {
        this.uuid = uuid;
        Player player = Bukkit.getPlayer(uuid);
        assert player != null;
        this.board = new Board(player);
        this.board.setTitle(boardPrefix);
        this.energy = energy;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Nullable
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
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

    public Board getBoard() {
        return board;
    }

    public void updateBoard() {
        board.setLine(4, " ");
        board.setLine(3, Util.getHexString("#35DF92", "&lEnergy:"));
        board.setLine(2, PlayerUtils.getEnergyString(energy));
        board.setLine(1, " ");
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("uuid", uuid.toString());
        map.put("energy", energy);

        return map;
    }

    public static PlayerData deserialize(Map<String, Object> args) {
        UUID uuid = UUID.fromString((String) args.get("uuid"));
        double energy = ((Number) args.get("energy")).doubleValue();
        return new PlayerData(uuid, energy);
    }

}
