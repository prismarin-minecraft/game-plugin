package in.prismar.game.warzone.tombstone;

import in.prismar.game.warzone.WarzoneService;
import in.prismar.library.spigot.hologram.line.HologramLineType;
import lombok.AllArgsConstructor;

import java.util.Iterator;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AllArgsConstructor
public class TombstoneTask implements Runnable {

    private final WarzoneService service;

    @Override
    public void run() {
        for(Tombstone tombstone : service.getTombstones()) {
            if(System.currentTimeMillis() >= tombstone.getDespawnTimestamp() || service.isTombstoneEmpty(tombstone)) {
                tombstone.getHologram().disable();
                service.getTombstones().remove(tombstone);
            } else {
                tombstone.getHologram().updateLine(1, "§8[§c" + service.getFormattedTombstoneTime(tombstone) + "§8]");
            }
        }
    }
}
