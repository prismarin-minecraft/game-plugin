package in.prismar.game.airdrop;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.game.Game;
import in.prismar.game.airdrop.event.AirdropCallEvent;
import in.prismar.game.airdrop.event.AirdropRemoveEvent;
import in.prismar.game.airdrop.loot.AirDropLootTable;
import in.prismar.library.common.event.EventBus;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.spigot.text.CenteredMessage;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Service
public class AirDropRegistry {

    private final List<AirDrop> airDrops;
    private final AirDropLootTable lootTable;
    private final ConfigStore configStore;

    private final EventBus eventBus;

    public AirDropRegistry(Game game) {
        this.eventBus = new EventBus();
        this.airDrops = new ArrayList<>();
        this.lootTable = new AirDropLootTable(game.getDefaultDirectory());
        this.configStore = PrismarinApi.getProvider(ConfigStore.class);
    }

    public Optional<AirDrop> findDropByStandLocation(Location location) {
        for (AirDrop airDrop : airDrops) {
            if(!airDrop.getArmorStand().getLocation().getWorld().getName().equals(location.getWorld().getName())) {
                continue;
            }
            Location sameLevelLoc = airDrop.getArmorStand().getLocation().clone();
            sameLevelLoc.setY(location.getY());
            if(sameLevelLoc.distanceSquared(location) <= 1) {
                return Optional.of(airDrop);
            }
        }
        return Optional.empty();
    }

    public void despawnAll() {
        for(AirDrop airDrop : airDrops) {
            airDrop.getArmorStand().remove();
        }
    }

    public AirDrop callAirDrop(Location location){
        final double y = Double.valueOf(configStore.getProperty("airdrop.location.y"));
        final long exp = Long.valueOf(configStore.getProperty("airdrop.expiration"));
        final int rows = Integer.valueOf(configStore.getProperty("airdrop.loot.inventory.rows"));
        final String[] amount = configStore.getProperty("airdrop.loot.amount").split("-");
        AirDrop airDrop = new AirDrop(lootTable.generateRandomInventory("§cAirdrop", rows,
                MathUtil.random(Integer.valueOf(amount[0]), Integer.valueOf(amount[1]))), location.clone().add(0, y, 0),
                exp);
        airDrop.setRemoveCallback(() -> {
            airDrops.remove(airDrop);
            eventBus.publish(new AirdropRemoveEvent(airDrop));
        });
        airDrops.add(airDrop);

        Bukkit.broadcastMessage(CenteredMessage.createCentredMessage(PrismarinConstants.BORDER));
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(CenteredMessage.createCentredMessage("§e§lAirdrop in Warzone"));
        Bukkit.broadcastMessage(CenteredMessage.createCentredMessage("§7Incoming at§8: §7X§8: §e" +
                location.getBlockX() + " §7Z§8: §e" + location.getBlockZ()));
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(CenteredMessage.createCentredMessage(PrismarinConstants.BORDER));

        eventBus.publish(new AirdropCallEvent(airDrop));
        return airDrop;
    }
}
