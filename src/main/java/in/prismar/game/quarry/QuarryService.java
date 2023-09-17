package in.prismar.game.quarry;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.api.meta.Provider;
import in.prismar.game.Game;
import in.prismar.game.quarry.frame.QuarryInputFrame;
import in.prismar.game.quarry.frame.QuarryOutputFrame;
import in.prismar.game.quarry.model.Quarry;
import in.prismar.game.quarry.repository.FileQuarryRepository;
import in.prismar.game.quarry.repository.QuarryRepository;
import in.prismar.game.quarry.task.QuarryTask;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.spigot.item.container.ItemContainer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Service
@Getter
public class QuarryService {

    private final Game game;
    private final QuarryRepository repository;

    private ConfigStore configStore;

    public QuarryService(Game game) {
        this.game = game;
        this.repository = new FileQuarryRepository(game.getDefaultDirectory());
        this.configStore = PrismarinApi.getProvider(ConfigStore.class);
        for(Quarry quarry : repository.findAll()) {
            acquireFrames(quarry);
        }

        Bukkit.getScheduler().runTaskTimerAsynchronously(game, new QuarryTask(this), 20, 20);
    }

    public Quarry create(String id, ItemStack output, int producePerSecond, String displayName) {
        Quarry quarry = new Quarry();
        quarry.setId(id);
        quarry.setInputLocation(Bukkit.getWorlds().get(0).getSpawnLocation());
        quarry.setOutputLocation(Bukkit.getWorlds().get(0).getSpawnLocation());
        quarry.setOutput(new ItemContainer(output));
        quarry.setDisplayName(displayName);
        quarry.setProducePerSecond(producePerSecond);
        acquireFrames(quarry);
        return repository.create(quarry);
    }

    public void fillQuarry(Quarry quarry, int amount) {
        quarry.setInputAmount(quarry.getInputAmount() + amount);
        quarry.setCurrentFuelConsumptionSeconds(getNextFuelConsumption());
        save(quarry);
    }

    public int getNextFuelConsumption() {
        return Integer.valueOf(configStore.getProperty("quarry.fuel.consumption.max.seconds"));
    }

    public void acquireFrames(Quarry quarry) {
        quarry.setInputFrame(new QuarryInputFrame(this, quarry));
        quarry.setOutputFrame(new QuarryOutputFrame(this, quarry));
    }

    public void openInput(Player player, Quarry quarry) {
        quarry.getInputFrame().openInventory(player, Sound.BLOCK_CHEST_OPEN, 0.6f);
    }

    public void openOutput(Player player, Quarry quarry) {
        quarry.getOutputFrame().openInventory(player, Sound.BLOCK_CHEST_OPEN, 0.6f);
    }

    public void announceQuarry(Quarry quarry) {
        final String arrow = PrismarinConstants.ARROW_RIGHT + " §7";
        Bukkit.broadcastMessage(PrismarinConstants.BORDER);
        Bukkit.broadcastMessage("§c ");
        Bukkit.broadcastMessage(arrow + quarry.getDisplayName() + " §7has been enabled");
        Bukkit.broadcastMessage("§c ");
        Bukkit.broadcastMessage(PrismarinConstants.BORDER);
    }

    public void save(Quarry quarry) {
        repository.saveAsync(quarry, true);
    }
}
