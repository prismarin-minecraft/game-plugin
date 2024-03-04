package in.prismar.game.warzone.combatlog.listener;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.api.region.RegionProvider;
import in.prismar.game.warzone.combatlog.npc.TemporaryNpcService;
import in.prismar.game.warzone.combatlog.CombatLogService;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;


/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerQuitListener implements Listener {

    @Inject
    private CombatLogService service;

    @Inject
    private TemporaryNpcService temporaryNpcService;

    private RegionProvider regionProvider;

    private final ConfigStore configStore;

    public PlayerQuitListener() {
        this.configStore = PrismarinApi.getProvider(ConfigStore.class);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(player.hasPermission(PrismarinConstants.PERMISSION_PREFIX + "combatlog.bypass")) {
            return;
        }
        if(getRegionProvider().isInRegionWithFlag(player.getLocation(), "pvp")) {
            return;
        }
        if(service.isInCombatLog(player)) {
            player.setHealth(0);
        } else {
            if(player.isDead()) {
                return;
            }
            temporaryNpcService.spawn(player);
        }
    }

    public RegionProvider getRegionProvider() {
        if(regionProvider == null) {
            regionProvider = PrismarinApi.getProvider(RegionProvider.class);
        }
        return regionProvider;
    }
}
