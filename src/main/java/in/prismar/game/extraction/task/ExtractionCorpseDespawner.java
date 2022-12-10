package in.prismar.game.extraction.task;

import dev.sergiferry.playernpc.api.NPCLib;
import in.prismar.game.extraction.ExtractionFacade;
import in.prismar.game.extraction.corpse.Corpse;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;

import java.util.Iterator;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AllArgsConstructor
public class ExtractionCorpseDespawner implements Runnable{

    private final ExtractionFacade facade;

    @Override
    public void run() {
        if(facade.getCorpses().isEmpty()) {
            return;
        }
        Iterator<Corpse> iterator = facade.getCorpses().iterator();
        while (iterator.hasNext()) {
            Corpse corpse = iterator.next();
            if(corpse.getInventory().isEmpty() || System.currentTimeMillis() >= corpse.getTimeToLive()) {
                Bukkit.getScheduler().runTask(facade.getGame(), () -> {
                    NPCLib.getInstance().removeNPC(corpse.getNpc());
                });
                iterator.remove();
            }
        }
    }
}
