package in.prismar.game.bounty;

import in.prismar.api.PrismarinApi;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.Game;
import in.prismar.game.bounty.gui.LayoutInitialization;
import in.prismar.game.bounty.model.Bounty;
import in.prismar.game.bounty.model.BountySupplier;
import in.prismar.game.bounty.repository.BountyRepository;
import in.prismar.game.bounty.repository.FileBountyRepository;
import in.prismar.library.meta.anno.Service;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Getter
public class BountyService {

    private final BountyRepository repository;
    private final ConfigStore configStore;
    private final UserProvider<User> userProvider;

    public BountyService(Game game) {
        this.repository = new FileBountyRepository(game.getDefaultDirectory());
        this.configStore = PrismarinApi.getProvider(ConfigStore.class);
        this.userProvider = PrismarinApi.getProvider(UserProvider.class);

        if(game.getServer().getPluginManager().isPluginEnabled("AdvancedGUI")) {
           LayoutInitialization.init(this, game);
        }


    }

    public Optional<Bounty> getBounty(Player player) {
        return repository.findByIdOptional(player.getUniqueId().toString());
    }

    public Bounty increase(Player target, BountySupplier supplier, double money) {
        Bounty bounty = repository.findById(target.getUniqueId().toString());
        for(BountySupplier current : bounty.getSuppliers()) {
            if(current.getUuid().equals(supplier.getUuid())) {
                current.setMoney(current.getMoney() + money);
                repository.saveAsync(bounty, true);
                return bounty;
            }
        }
        bounty.getSuppliers().add(supplier);
        repository.saveAsync(bounty, true);
        return bounty;
    }

    public int getRank(Bounty bounty) {
        int rank = 0;
        for(Bounty current : getRepository().getBountiesSorted()) {
            rank++;
            if(current.getId().equals(bounty.getId())) {
                return rank;
            }
        }
        return -1;
    }

    public CompletableFuture<Bounty> create(Player target, BountySupplier supplier) {
        Bounty bounty = new Bounty();
        bounty.setId(target.getUniqueId().toString());
        bounty.setName(target.getName());
        bounty.setSuppliers(new ArrayList<>());
        bounty.getSuppliers().add(supplier);
        return repository.createAsync(bounty);
    }

    public double getMinMoney() {
        return Double.parseDouble(configStore.getProperty("bounty.min.money"));
    }
}
