package in.prismar.game.warzone.dungeon;

import in.prismar.api.warzone.dungeon.DungeonInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class Dungeon implements DungeonInfo {

    private String id;
    private String title;
    private String spawnerName;
    private long duration;

    private long reduceTimer;
    private String endBossId;

    private Map<UUID, DungeonParticipant> participants;

    private long until;

    @Setter
    private boolean teleported = true;

    public Dungeon(String id, String title, String spawnerName, long duration, long reduceTimer, String endBossId) {
        this.id = id;
        this.title = title;
        this.spawnerName = spawnerName;
        this.duration = duration;
        this.endBossId = endBossId;
        this.reduceTimer = reduceTimer;
        this.participants = new ConcurrentHashMap<>();
    }

    public void reduceTimeTo(long millis) {
        this.until = System.currentTimeMillis() + millis;
    }

    public void resetUntil() {
        this.until = System.currentTimeMillis() + duration;
        this.teleported = false;
    }

    public boolean isRunning() {
        return System.currentTimeMillis() <= until;
    }

    @Override
    public long getTimer() {
        return !isRunning() ? duration : until - System.currentTimeMillis();
    }
}
