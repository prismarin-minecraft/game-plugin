package in.prismar.game.warzone.combatlog.npc;

import dev.sergiferry.playernpc.api.NPCLib;
import in.prismar.api.PrismarinApi;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.game.Game;
import in.prismar.game.warzone.WarzoneService;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.Service;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Service
public class TemporaryNpcService {

    private static TemporaryNpcService instance;

    @Getter
    private Map<UUID, TemporaryNpc> npcs = new HashMap<>();

    private ConfigStore configStore;

    @Inject
    private WarzoneService warzoneService;

    @Getter
    private TemporaryNpcLoggedOffFile npcLoggedOffFile;

    private final Game game;


    public TemporaryNpcService(Game game) {
        instance = this;
        this.game = game;
        this.configStore = PrismarinApi.getProvider(ConfigStore.class);
        this.npcLoggedOffFile = new TemporaryNpcLoggedOffFile(game.getDefaultDirectory());
        Bukkit.getScheduler().runTaskTimerAsynchronously(game, () -> {
            List<UUID> all = new ArrayList<>(npcs.keySet());
            final long timer = getTimer();
            for(UUID uuid : all) {
                if(isOverTimer(uuid)) {
                    remove(uuid);
                } else {
                    TemporaryNpc npc = npcs.get(uuid);
                    long difference = System.currentTimeMillis() - npc.getCreatedAt();
                    npc.updateText(String.format(npc.getText(), ((timer-difference)/1000) + "s"));
                }
            }
        }, 20, 20);

        NPCLib.getInstance().registerPlugin(game);
    }

    public void addLoggedOff(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(game, () -> {
            npcLoggedOffFile.getEntity().add(player.getUniqueId());
            npcLoggedOffFile.save();
        });
    }

    public void removeLoggedOf(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(game, () -> {
            npcLoggedOffFile.getEntity().remove(player.getUniqueId());
            npcLoggedOffFile.save();
        });
    }

    public void damage(TemporaryNpc npc, double damage) {
        if (npc.damage(damage)) {
            List<ItemStack> stacks = new ArrayList<>();
            for(ItemStack stack : npc.getPlayer().getInventory()) {
                if(stack != null) {
                    if(stack.getType() != Material.AIR) {
                        stacks.add(stack);
                    }
                }
            }
            npc.getPlayer().getInventory().clear();
            warzoneService.createTombstone(npc.getPlayer(), stacks);
            remove(npc.getPlayer().getUniqueId());
            addLoggedOff(npc.getPlayer());

        }
    }

    public void spawn(Player player) {
        TemporaryNpc npc = new TemporaryNpc(game, player);
        npcs.put(player.getUniqueId(), npc);
    }

    public void remove(UUID uuid) {
        TemporaryNpc npc = npcs.get(uuid);
        Bukkit.getScheduler().runTask(game, () -> {
            npc.despawn();
            npcs.remove(uuid);
        });

    }

    public boolean isOverTimer(UUID uuid) {
        TemporaryNpc npc = npcs.get(uuid);
        long difference = System.currentTimeMillis() - npc.getCreatedAt();
        if(difference >= getTimer()) {
            return true;
        }
        return false;
    }

    public TemporaryNpc getNpc(UUID uuid) {
        return npcs.get(uuid);
    }

    public boolean hasNpc(UUID uuid) {
        return npcs.containsKey(uuid);
    }

    private long getTimer() {
        return this.configStore.getLongProperty("warzone.temporary.npc.timer") * 1000;
    }

    public static TemporaryNpcService getInstance() {
        return instance;
    }
}
