package in.prismar.game.warzone.dungeon.task;

import in.prismar.api.PrismarinApi;
import in.prismar.api.scoreboard.ScoreboardProvider;
import in.prismar.game.warzone.dungeon.Dungeon;
import in.prismar.game.warzone.dungeon.DungeonParticipant;
import in.prismar.game.warzone.dungeon.DungeonService;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class DungeonScoreboardTask implements Runnable {

    private final DungeonService service;
    private final ScoreboardProvider scoreboardProvider;

    public DungeonScoreboardTask(DungeonService service) {
        this.service = service;
        this.scoreboardProvider = PrismarinApi.getProvider(ScoreboardProvider.class);
    }

    @Override
    public void run() {
        if(service.getWarzoneService().getWarzoneLocation() == null) {
            return;
        }
        for(Dungeon dungeon : service.getRegistry().getAll()) {
            for(Player player : service.getWarzoneService().getWarzoneLocation().getWorld().getPlayers()) {
                if(!dungeon.getParticipants().containsKey(player.getUniqueId()) && service.getRegionProvider().isIn(player.getLocation(), dungeon.getId())) {
                    Bukkit.getScheduler().runTask(service.getGame(), () -> {
                        scoreboardProvider.recreateSidebar(player);
                    });
                    dungeon.getParticipants().put(player.getUniqueId(), new DungeonParticipant(player));
                }
            }
            if(!dungeon.isRunning() && !dungeon.isTeleported()) {
                String id = dungeon.getId().toLowerCase();
                Location location = service.getRegionProvider().getLocationB(dungeon.getId());
                if(location != null) {
                    Bukkit.getScheduler().runTask(service.getGame(), () -> {
                        for(Player player : location.getWorld().getPlayers()) {
                            if(service.getRegionProvider().isIn(player.getLocation(), id)) {
                                player.teleport(service.getWarzoneService().getWarzoneLocation());
                                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.5f, 1f);
                            }
                        }
                    });
                }
                dungeon.setTeleported(true);
            }
            for(DungeonParticipant participant : dungeon.getParticipants().values()) {
                if(!participant.getPlayer().isOnline()) {
                    dungeon.getParticipants().remove(participant.getPlayer().getUniqueId());
                    continue;
                }
                if(!service.getWarzoneService().isInWarzone(participant.getPlayer())) {
                    dungeon.getParticipants().remove(participant.getPlayer().getUniqueId());
                    Bukkit.getScheduler().runTask(service.getGame(), () -> {
                        scoreboardProvider.recreateSidebar(participant.getPlayer());
                    });
                    continue;
                }
                if(!service.getRegionProvider().isIn(participant.getPlayer().getLocation(), dungeon.getId())) {
                    dungeon.getParticipants().remove(participant.getPlayer().getUniqueId());
                    Bukkit.getScheduler().runTask(service.getGame(), () -> {
                        scoreboardProvider.recreateSidebar(participant.getPlayer());
                    });
                }
            }
        }
    }
}
