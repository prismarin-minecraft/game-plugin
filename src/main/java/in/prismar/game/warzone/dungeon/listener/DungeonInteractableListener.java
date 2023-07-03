package in.prismar.game.warzone.dungeon.listener;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.interactable.event.InteractableInteractEvent;
import in.prismar.game.interactable.model.keylock.KeyLock;
import in.prismar.game.warzone.dungeon.Dungeon;
import in.prismar.game.warzone.dungeon.DungeonService;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.spawning.spawners.MythicSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@AutoListener
public class DungeonInteractableListener implements Listener {

    @Inject
    private DungeonService service;


    @EventHandler
    public void onCall(InteractableInteractEvent event) {
        if(event.getInteractable() instanceof KeyLock lock) {
            if(lock.getDungeon() != null) {
                if(service.getRegistry().existsById(lock.getDungeon())) {
                    Dungeon dungeon = service.getRegistry().getById(lock.getDungeon());
                    MythicSpawner spawner = MythicBukkit.inst().getSpawnerManager().getSpawnerByName(dungeon.getSpawnerName());
                    if(spawner.isOnWarmup()) {
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(PrismarinConstants.PREFIX + "§cThe boss hasn't been spawned");
                        return;
                    }
                }
            }
        }
    }
}
