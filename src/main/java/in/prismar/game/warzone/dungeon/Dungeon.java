package in.prismar.game.warzone.dungeon;

import in.prismar.api.warzone.dungeon.DungeonInfo;
import lombok.Getter;

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

    private Map<UUID, DungeonParticipant> participants;

    private long until;

    public Dungeon(String id, String title, String spawnerName, long duration) {
        this.id = id;
        this.title = title;
        this.spawnerName = spawnerName;
        this.duration = duration;
        this.participants = new ConcurrentHashMap<>();
    }

    public void resetUntil() {
        this.until = System.currentTimeMillis() + duration;
    }

    public boolean isRunning() {
        return System.currentTimeMillis() <= until;
    }

    @Override
    public long getTimer() {
        return !isRunning() ? duration : until - System.currentTimeMillis();
    }
}
