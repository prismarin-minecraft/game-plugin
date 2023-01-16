package in.prismar.game.hardpoint.task;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.game.hardpoint.HardpointFacade;
import in.prismar.game.hardpoint.HardpointTeam;
import in.prismar.game.hardpoint.config.Hardpoint;
import in.prismar.game.hardpoint.session.HardpointSession;
import in.prismar.game.hardpoint.session.HardpointSessionPlayer;
import in.prismar.game.hardpoint.session.HardpointSessionState;
import in.prismar.library.common.random.UniqueRandomizer;
import in.prismar.library.common.text.Progress;
import in.prismar.library.spigot.hologram.Hologram;
import in.prismar.library.spigot.hologram.line.HologramLineType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Consumer;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class HardpointTask implements Runnable {

    private final HardpointFacade facade;
    private final ConfigStore configStore;

    private long ticks;

    public HardpointTask(HardpointFacade facade) {
        this.facade = facade;
        this.configStore = PrismarinApi.getProvider(ConfigStore.class);

        //this.progress = new Progress("§8[§7■§7■§7■§7■§7■§7■§7■§7■§8] §70%", 15, "§7■", "§a■", true);
    }

    @Override
    public void run() {
        if(facade.getConfigFile().getEntity().getPoints().isEmpty()) {
           return;
        }
        if(ticks >= Integer.MAX_VALUE) {
            ticks = 0;
        }

        ticks += 5;
        HardpointSession session = facade.getSession();
        if (session.getPoint() == null) {
            Hardpoint point = getNextPoint();
            long durationInSeconds = Long.valueOf(configStore.getProperty("hardpoint.duration"));
            session.setPointUntil(System.currentTimeMillis() + 1000 * durationInSeconds);
            session.setPoint(point);
            session.setPointState(HardpointSessionState.IDLE);

            point.getLocation().getBlock().setType(Material.BEACON);
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    point.getLocation().clone().add(i, -1, j).getBlock().setType(Material.IRON_BLOCK);
                }
            }
            updateBlocks(session, Material.WHITE_WOOL);

            facade.executeForAll(sessionPlayer -> {
                facade.updateCompass(sessionPlayer.getPlayer());
            });
        }
        if(System.currentTimeMillis() >= session.getPointUntil()) {
            updateBlocks(session, Material.WHITE_WOOL);
            session.getPoint().getLocation().getBlock().setType(Material.WHITE_WOOL);
            session.setPoint(null);
            facade.executeForAll(sessionPlayer -> {
                sessionPlayer.getPlayer().sendMessage(PrismarinConstants.PREFIX + "§7The §bpoint §7has changed!");
                sessionPlayer.getPlayer().playSound(sessionPlayer.getPlayer(), Sound.BLOCK_IRON_TRAPDOOR_CLOSE, 0.4f, 1);
            });
            return;
        }

        if(ticks % 20 == 0 && !session.isAllowedToMove()) {
            long difference = session.getMoveUntil() - System.currentTimeMillis();
            facade.executeForAll(sessionPlayer -> {
                if((difference/1000) <= 0) {
                    sessionPlayer.getPlayer().sendTitle("§6Hardpoint", "§aGame started", 20, 20, 20);
                    sessionPlayer.getPlayer().playSound(sessionPlayer.getPlayer(), Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 1);
                    return;
                }
                sessionPlayer.getPlayer().sendTitle("§6Hardpoint", "§7Game starts in §b" + (difference/1000) + "s", 20, 20, 20);
            });
        }

        HardpointSessionState lastState = session.getPointState();
        HardpointSessionState state = determineState(session);

        if(state == HardpointSessionState.CAPTURED) {
            session.getTeamPoints().put(session.getCapturedTeam(), session.getTeamPoints().get(session.getCapturedTeam()) + 1);
        }

        if (lastState != state) {
            updateBlocks(session, state == HardpointSessionState.CAPTURED ? session.getCapturedTeam().getIcon().getType() : state.getMaterial());
        }

        session.setPointState(state);

        HardpointTeam winner = facade.handleWin(session);
        if(winner != null) {
            facade.executeForAll(sessionPlayer -> {
                facade.respawn(sessionPlayer.getPlayer());
            });
            session.setPointUntil(System.currentTimeMillis());
            session.setMoveUntil(System.currentTimeMillis() + 1000 * Long.valueOf(configStore.getProperty("hardpoint.reset.move.time")));
        }
    }



    public void updateBlocks(HardpointSession session, Material material) {
        for (Location location : session.getPoint().getCircle()) {
            if(location.getBlock().getType() != Material.BEACON) {
                location.getBlock().setType(material);
            }
        }
    }

    public HardpointSessionState determineState(HardpointSession session) {
        double distance = Double.valueOf(configStore.getProperty("hardpoint.distance"));
        Map<HardpointTeam, Integer> sizes = new HashMap<>();
        for (Map.Entry<HardpointTeam, Map<UUID, HardpointSessionPlayer>> entry : session.getPlayers().entrySet()) {
            for (HardpointSessionPlayer sessionPlayer : entry.getValue().values()) {
                if (sessionPlayer.getPlayer().getWorld().getName().equals(session.getPoint().getLocation().getWorld().getName())) {
                    if (sessionPlayer.getPlayer().getLocation().distanceSquared(session.getPoint().getLocation()) <= (distance * distance)) {
                        sizes.put(entry.getKey(), sizes.getOrDefault(entry.getKey(), 0) + 1);
                    }
                }
            }
        }
        HardpointSessionState state = HardpointSessionState.IDLE;
        if (sizes.size() == 1) {
            HardpointTeam team = sizes.keySet().iterator().next();
            session.setCapturedTeam(team);
            state = HardpointSessionState.CAPTURED;
        } else if (sizes.size() >= 2) {
            state = HardpointSessionState.CONTESTED;
        }
        return state;
    }

    public Hardpoint getNextPoint() {
        return UniqueRandomizer.getRandom("hardpoint", facade.getConfigFile().getEntity().getPoints());
    }


}
