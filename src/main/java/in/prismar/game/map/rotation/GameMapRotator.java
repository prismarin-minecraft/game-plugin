package in.prismar.game.map.rotation;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.api.placeholder.PlaceholderStore;
import in.prismar.game.map.GameMapFacade;
import in.prismar.game.map.model.GameMap;
import in.prismar.game.map.model.GameMapPowerUp;
import in.prismar.game.map.powerup.PowerUp;
import in.prismar.library.spigot.hologram.Hologram;
import in.prismar.library.spigot.hologram.line.HologramLineType;
import in.prismar.library.spigot.text.InteractiveTextBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Setter
public class GameMapRotator implements Runnable {

    private final GameMapFacade facade;

    private final ConfigStore store;

    private final PlaceholderStore placeholderStore;

    private Map<String, Set<UUID>> voting;

    private GameMap currentMap;

    private long nextRotate;

    private long nextRotateVote;

    public GameMapRotator(GameMapFacade facade) {
        this.facade = facade;
        this.voting = new HashMap<>();
        this.store = PrismarinApi.getProvider(ConfigStore.class);
        this.placeholderStore = PrismarinApi.getProvider(PlaceholderStore.class);
        if(facade.getRepository().findAll().size() >= 1) {
            this.currentMap = facade.getRandomMap();
            registerCurrentMapPlaceholder();
        }

        nextRotateTimer();

    }

    public void callVote() {
        this.nextRotateVote = System.currentTimeMillis();
        this.nextRotate = System.currentTimeMillis() + Long.valueOf(store.getProperty("map.rotation.vote.time"));
        registerPlaceholders();
    }

    private void nextRotateTimer() {
        this.nextRotate = System.currentTimeMillis() + Long.valueOf(store.getProperty("map.rotation.time"));
        this.nextRotateVote = nextRotate - Long.valueOf(store.getProperty("map.rotation.vote.time"));

        registerPlaceholders();
    }

    private void registerPlaceholders() {
        placeholderStore.setPlaceholder("map.rotation.time", nextRotate);
        placeholderStore.setPlaceholder("map.rotation.vote.time", nextRotateVote);
    }

    private void registerCurrentMapPlaceholder() {
        placeholderStore.setPlaceholder("map.rotation.current", currentMap.getIcon().getItem().getItemMeta().getDisplayName());
    }
    public void createVoteSurvey() {
        for(GameMap map : facade.getRepository().findAll()) {
            this.voting.put(map.getId(), new HashSet<>());
        }
        displayVoteSurvey();
    }

    public void displayVoteSurvey() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage("§8╔═══════════════════════╗");
            player.sendMessage(" ");
            player.sendMessage("§8» §7Vote for the next map");
            player.sendMessage(" ");
            for(GameMap map : facade.getRepository().findAll()) {
                InteractiveTextBuilder textBuilder = new InteractiveTextBuilder();
                textBuilder.addText("§8» §b" + map.getIcon().getItem().getItemMeta().getDisplayName() + " §8(§7" + voting.get(map.getId()).size() + "§8)" , "/map vote " + map.getId(),
                        "§7Vote for this map");
                player.spigot().sendMessage(textBuilder.build());
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.65f, 1);
            }

            player.sendMessage(" ");
            player.sendMessage("§8╚═══════════════════════╝");
        }
    }

    private Hologram spawnPowerUpHologram(Location location, PowerUp powerUp) {
        Hologram hologram = new Hologram(location);
        hologram.addLine(HologramLineType.TEXT, powerUp.getDisplayName(), true);
        hologram.addLine(HologramLineType.ITEM_HEAD, new ItemStack(powerUp.getMaterial()), true);
        hologram.enable();
        return hologram;
    }

    @Override
    public void run() {
        long now = System.currentTimeMillis();
        if(currentMap != null) {
            for(GameMapPowerUp mapPowerUp : currentMap.getPowerUps()) {
                PowerUp powerUp = facade.getPowerUpRegistry().getById(mapPowerUp.getId());
                if(now >= mapPowerUp.getRespawnTime() && mapPowerUp.getHologram() == null) {
                    mapPowerUp.setHologram(spawnPowerUpHologram(mapPowerUp.getLocation(), powerUp));
                } else {
                    if(mapPowerUp.getHologram() != null) {
                        for(Player player : currentMap.getPlayers().values()) {
                            double distance = player.getLocation().distanceSquared(mapPowerUp.getLocation());
                            if(distance <= 3) {
                                powerUp.onPickUp(player);
                                mapPowerUp.getHologram().disable();
                                mapPowerUp.setHologram(null);
                                mapPowerUp.setRespawnTime(System.currentTimeMillis() + powerUp.getRespawnTime());
                                break;
                            }
                            if(distance <= 30) {
                                final Location location = mapPowerUp.getLocation().clone().add(0, 0.5, 0);
                                mapPowerUp.getLocation().getWorld().spawnParticle(Particle.PORTAL, location, 1);
                                mapPowerUp.getLocation().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 1);
                            }
                        }
                    }
                }
            }
        }

        if(facade.getRepository().findAll().size() >= 1) {
            if(now >= nextRotate) {
                GameMap winner = findVoteWinner();
                if(!winner.getId().equalsIgnoreCase(currentMap.getId())) {
                    for(Player player : currentMap.getPlayers().values()) {
                        winner.getPlayers().put(player.getUniqueId(), player);
                    }
                    currentMap.getPlayers().clear();
                }

                this.currentMap = winner;
                for(Player player : currentMap.getPlayers().values()) {
                    facade.respawn(player);
                }
                registerCurrentMapPlaceholder();
                Bukkit.broadcastMessage(PrismarinConstants.PREFIX + "§7Map changed to " + winner.getIcon().getItem().getItemMeta().getDisplayName());
                nextRotateTimer();
                voting.clear();
                return;
            }
            if(now >= nextRotateVote) {
                if(this.voting.isEmpty()) {
                    createVoteSurvey();
                }

            }
        }

    }

    public GameMap findVoteWinner() {
        List<VoteEntry> entries = new ArrayList<>();
        for(Map.Entry<String, Set<UUID>> entry : voting.entrySet()) {
            entries.add(new VoteEntry(entry.getKey(), entry.getValue().size()));
        }
        entries.sort((o1, o2) -> Integer.compare(o2.getCount(), o1.getCount()));

        VoteEntry winnerEntry = entries.get(0);
        return facade.getRepository().findById(winnerEntry.getId());
    }

    public boolean canVoteFor(GameMap map) {
        return voting.containsKey(map.getId());
    }

    public void voteForMap(GameMap map, UUID uuid) {
        if(!voting.containsKey(map.getId())) {
            voting.put(map.getId(), new HashSet<>());
        }
        voting.get(map.getId()).add(uuid);
    }

    @Data
    @AllArgsConstructor
    public class VoteEntry {
        private String id;
        private int count;
    }

}
