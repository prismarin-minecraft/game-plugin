package in.prismar.game.airdrop;

import in.prismar.api.PrismarinApi;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.game.Game;
import in.prismar.game.airdrop.loot.AirDropLootTable;
import in.prismar.library.common.callback.Callback;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.meta.anno.Service;
import lombok.Getter;
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

    private List<AirDrop> airDrops;
    private AirDropLootTable lootTable;
    private ConfigStore configStore;

    public AirDropRegistry(Game game) {
        this.airDrops = new ArrayList<>();
        this.lootTable = new AirDropLootTable(game.getDefaultDirectory());
        this.configStore = PrismarinApi.getProvider(ConfigStore.class);
    }

    public Optional<AirDrop> findDropByStandLocation(Location location) {
        for (AirDrop airDrop : airDrops) {
            if(airDrop.getArmorStand().getLocation().distanceSquared(location) <= 1) {
                return Optional.of(airDrop);
            }
        }
        return Optional.empty();
    }

    public AirDrop callAirDrop(Location location){
        final double y = Double.valueOf(configStore.getProperty("airdrop.location.y"));
        final long exp = Long.valueOf(configStore.getProperty("airdrop.expiration"));
        final int rows = Integer.valueOf(configStore.getProperty("airdrop.loot.inventory.rows"));
        final String[] amount = configStore.getProperty("airdrop.loot.amount").split("-");
        AirDrop airDrop = new AirDrop(lootTable.generateRandomInventory("§cAirdrop", rows,
                MathUtil.random(Integer.valueOf(amount[0]), Integer.valueOf(amount[1]))), location.clone().add(0, y, 0),
                exp);
        airDrop.setRemoveCallback(() -> airDrops.remove(airDrop));
        airDrops.add(airDrop);
        return airDrop;
    }
}
